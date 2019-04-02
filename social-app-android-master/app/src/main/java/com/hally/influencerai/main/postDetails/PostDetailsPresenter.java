/*
 * Copyright 2018
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.hally.influencerai.main.postDetails;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.managers.CommentManager;
import com.hally.influencerai.managers.PostManager;
import com.hally.influencerai.managers.ProfileManager;
import com.hally.influencerai.managers.listeners.OnDataChangedListener;
import com.hally.influencerai.managers.listeners.OnObjectChangedListenerSimple;
import com.hally.influencerai.managers.listeners.OnPostChangedListener;
import com.hally.influencerai.managers.listeners.OnTaskCompleteListener;
import com.hally.influencerai.model.Comment;
import com.hally.influencerai.model.Post;
import com.hally.influencerai.model.Profile;

import java.util.List;

/**
 * Created by HallyTran on 03.05.18.
 */

class PostDetailsPresenter extends BasePresenter<PostDetailsView> {
    private static final int TIME_OUT_LOADING_COMMENTS = 30000;

    private PostManager postManager;
    private ProfileManager profileManager;
    private CommentManager commentManager;
    private Post post;
    private boolean isPostExist;
    private boolean postRemovingProcess = false;

    private boolean attemptToLoadComments = false;

    PostDetailsPresenter(Activity activity) {
        super(activity);

        postManager = PostManager.getInstance(context.getApplicationContext());
        profileManager = ProfileManager.getInstance(context.getApplicationContext());
        commentManager = CommentManager.getInstance(context.getApplicationContext());
    }

    public void loadPost(String postId) {
        postManager.getPost(context, postId, new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                ifViewAttached(new ViewAction<PostDetailsView>() {
                    @Override
                    public void run(@NonNull PostDetailsView view) {
                        if (obj != null) {
                            post = obj;
                            isPostExist = true;
                            view.initLikeController(post);
                            fillInUI(post);
                            view.updateCounters(post);
                            initLikeButtonState();
                            updateOptionMenuVisibility();
                        } else if (!postRemovingProcess) {
                            isPostExist = false;
                            view.onPostRemoved();
                            view.showNotCancelableWarningDialog(context.getString(R.string.error_post_was_removed));
                        }
                    }
                });
            }

            @Override
            public void onError(String errorText) {
                ifViewAttached(new ViewAction<PostDetailsView>() {
                    @Override
                    public void run(@NonNull PostDetailsView view) {
                        view.showNotCancelableWarningDialog(errorText);
                    }
                });
            }
        });
    }

    private void initLikeButtonState() {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null && post != null) {
//            postManager.hasCurrentUserLike(context, post.getId(), firebaseUser.getUid(), new OnObjectExistListener<Like>() {
//                @Override
//                public void onDataChanged(boolean exist) {
//                    PostDetailsPresenter.this.ifViewAttached(new ViewAction<PostDetailsView>() {
//                        @Override
//                        public void run(@NonNull PostDetailsView view) {
//                            view.initLikeButtonState(exist);
//                        }
//                    });
//                }
//            });
//        }
    }

    private void fillInUI(@NonNull Post post) {
        ifViewAttached(new ViewAction<PostDetailsView>() {
            @Override
            public void run(@NonNull PostDetailsView view) {
                view.setTitle(post.getTitle());
                view.setDescription(post.getDescription());
                view.loadPostDetailImage(post.getImageTitle());

                PostDetailsPresenter.this.loadAuthorProfile();
            }
        });
    }

    private void loadAuthorProfile() {
        if (post != null && post.getAuthorId() != null) {
            profileManager.getProfileSingleValue(post.getAuthorId(), new OnObjectChangedListenerSimple<Profile>() {
                @Override
                public void onObjectChanged(Profile profile) {
                    ifViewAttached(new ViewAction<PostDetailsView>() {
                        @Override
                        public void run(@NonNull PostDetailsView view) {
                            if (profile.getPhotoUrl() != null) {
                                view.loadAuthorPhoto(profile.getPhotoUrl());
                            }

                            view.setAuthorName(profile.getUsername());
                        }
                    });
                }
            });
        }
    }

    public void onAuthorClick(View authorView) {
        if (post != null) {
            ifViewAttached(new ViewAction<PostDetailsView>() {
                @Override
                public void run(@NonNull PostDetailsView view) {
                    view.openProfileActivity(post.getAuthorId(), authorView);
                }
            });
        }
    }


    public void onSendButtonClick() {
        if (checkInternetConnection() && checkAuthorization()) {
            sendComment();
        }
    }

    public boolean hasAccessToModifyPost() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        return currentUser != null && post != null && post.getAuthorId().equals(currentUser.getUid());
        return false;
    }

    public boolean hasAccessToEditComment(String commentAuthorId) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        return currentUser != null && commentAuthorId.equals(currentUser.getUid());
        return false;
    }

    public void updateComment(String newText, String commentId) {
        ifViewAttached(BaseView::showProgress);
        if (post != null) {
            commentManager.updateComment(commentId, newText, post.getId(), new OnTaskCompleteListener() {
                @Override
                public void onTaskComplete(boolean success) {
                    PostDetailsPresenter.this.ifViewAttached(new ViewAction<PostDetailsView>() {
                        @Override
                        public void run(@NonNull PostDetailsView view) {
                            view.hideProgress();
                            view.showSnackBar(R.string.message_comment_was_edited);
                        }
                    });
                }
            });
        }
    }

    private void openComplainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_complain)
                .setMessage(R.string.complain_text)
                .setNegativeButton(R.string.button_title_cancel, null)
                .setPositiveButton(R.string.add_complain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PostDetailsPresenter.this.addComplain();
                    }
                });

        builder.create().show();
    }

    private void addComplain() {
//        postManager.addComplain(post);
        ifViewAttached(new ViewAction<PostDetailsView>() {
            @Override
            public void run(@NonNull PostDetailsView view) {
                view.showComplainMenuAction(false);
                view.showSnackBar(R.string.complain_sent);
            }
        });
    }

    public void doComplainAction() {
        if (checkAuthorization()) {
            openComplainDialog();
        }
    }

    public void attemptToRemovePost() {
        if (hasAccessToModifyPost() && checkInternetConnection()) {
            if (!postRemovingProcess) {
                openConfirmDeletingDialog();
            }
        }
    }

    private void openConfirmDeletingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_deletion_post)
                .setNegativeButton(R.string.button_title_cancel, null)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PostDetailsPresenter.this.removePost();
                    }
                });

        builder.create().show();
    }

    private void removePost() {
        postRemovingProcess = true;
        ifViewAttached(new ViewAction<PostDetailsView>() {
            @Override
            public void run(@NonNull PostDetailsView view) {
                view.showProgress(R.string.removing);
            }
        });
        postManager.removePost(post, new OnTaskCompleteListener() {
            @Override
            public void onTaskComplete(boolean success) {
                PostDetailsPresenter.this.ifViewAttached(new ViewAction<PostDetailsView>() {
                    @Override
                    public void run(@NonNull PostDetailsView view) {
                        if (success) {
                            view.onPostRemoved();
                            view.finish();
                        } else {
                            postRemovingProcess = false;
                            view.showSnackBar(R.string.error_fail_remove_post);
                        }

                        view.hideProgress();
                    }
                });
            }
        });


    }

    private void sendComment() {
        if (post == null) {
            return;
        }

        ifViewAttached(new ViewAction<PostDetailsView>() {
            @Override
            public void run(@NonNull PostDetailsView view) {
                String commentText = view.getCommentText();

                if (commentText.length() > 0 && isPostExist) {
                    PostDetailsPresenter.this.createOrUpdateComment(commentText);
                    view.clearCommentField();
                }
            }
        });
    }

    private void createOrUpdateComment(String commentText) {
        commentManager.createOrUpdateComment(commentText, post.getId(), new OnTaskCompleteListener() {
            @Override
            public void onTaskComplete(boolean success) {
                PostDetailsPresenter.this.ifViewAttached(new ViewAction<PostDetailsView>() {
                    @Override
                    public void run(@NonNull PostDetailsView view) {
                        if (success) {
                            if (post != null && post.getCommentsCount() > 0) {
                                view.scrollToFirstComment();
                            }
                        }
                    }
                });
            }
        });
    }

    public void removeComment(String commentId) {
        ifViewAttached(BaseView::showProgress);
        commentManager.removeComment(commentId, post.getId(), new OnTaskCompleteListener() {
            @Override
            public void onTaskComplete(boolean success) {
                PostDetailsPresenter.this.ifViewAttached(new ViewAction<PostDetailsView>() {
                    @Override
                    public void run(@NonNull PostDetailsView view) {
                        view.hideProgress();
                        view.showSnackBar(R.string.message_comment_was_removed);
                    }
                });
            }
        });
    }

    public void editPostAction() {
        if (hasAccessToModifyPost() && checkInternetConnection()) {
            ifViewAttached(new ViewAction<PostDetailsView>() {
                @Override
                public void run(@NonNull PostDetailsView view) {
                    view.openEditPostActivity(post);
                }
            });
        }
    }

    public void updateOptionMenuVisibility() {
        ifViewAttached(new ViewAction<PostDetailsView>() {
            @Override
            public void run(@NonNull PostDetailsView view) {
                if (post != null) {
                    view.showEditMenuAction(PostDetailsPresenter.this.hasAccessToModifyPost());
                    view.showDeleteMenuAction(PostDetailsPresenter.this.hasAccessToModifyPost());
                    view.showComplainMenuAction(!post.isHasComplain());
                }
            }
        });
    }

    public boolean isPostExist() {
        return isPostExist;
    }

    public Post getPost() {
        return post;
    }

    public void onPostImageClick() {
        ifViewAttached(new ViewAction<PostDetailsView>() {
            @Override
            public void run(@NonNull PostDetailsView view) {
                if (post != null && post.getTitle() != null) {
                    view.openImageDetailScreen(post.getImageTitle());
                }
            }
        });
    }

    public void getCommentsList(Context activityContext, String postId) {
        attemptToLoadComments = true;
        runHidingCommentProgressByTimeOut();

        commentManager.getCommentsList(activityContext, postId, new OnDataChangedListener<Comment>() {
            @Override
            public void onListChanged(List<Comment> list) {
                attemptToLoadComments = false;
                PostDetailsPresenter.this.ifViewAttached(new ViewAction<PostDetailsView>() {
                    @Override
                    public void run(@NonNull PostDetailsView view) {
                        view.onCommentsListChanged(list);
                        view.showCommentProgress(false);
                        view.showCommentsRecyclerView(true);
                        view.showCommentsWarning(false);
                    }
                });
            }
        });
    }

    private void runHidingCommentProgressByTimeOut() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (attemptToLoadComments) {
                    PostDetailsPresenter.this.ifViewAttached(new ViewAction<PostDetailsView>() {
                        @Override
                        public void run(@NonNull PostDetailsView view) {
                            view.showCommentProgress(false);
                            view.showCommentsWarning(true);
                        }
                    });
                }
            }
        }, TIME_OUT_LOADING_COMMENTS);
    }

    public void updateCommentsVisibility(long commentsCount) {
        ifViewAttached(new ViewAction<PostDetailsView>() {
            @Override
            public void run(@NonNull PostDetailsView view) {
                if (commentsCount == 0) {
                    view.showCommentsLabel(false);
                    view.showCommentProgress(false);
                } else {
                    view.showCommentsLabel(true);
                }
            }
        });
    }
}
