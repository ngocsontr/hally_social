package com.hally.influencerai.adapters.holders;

import android.view.View;

import com.hally.influencerai.main.base.BaseActivity;
import com.hally.influencerai.managers.listeners.OnPostChangedListener;
import com.hally.influencerai.model.FollowingPost;
import com.hally.influencerai.model.Post;
import com.hally.influencerai.utils.LogUtil;

/**
 * Created by HallyTran on 22.05.18.
 */
public class FollowPostViewHolder extends PostViewHolder {


    public FollowPostViewHolder(View view, OnClickListener onClickListener, BaseActivity activity) {
        super(view, onClickListener, activity);
    }

    public FollowPostViewHolder(View view, OnClickListener onClickListener, BaseActivity activity, boolean isAuthorNeeded) {
        super(view, onClickListener, activity, isAuthorNeeded);
    }

    public void bindData(FollowingPost followingPost) {
        postManager.getSinglePostValue(followingPost.getPostId(), new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                bindData(obj);
            }

            @Override
            public void onError(String errorText) {
                LogUtil.logError(TAG, "bindData", new RuntimeException(errorText));
            }
        });
    }

}
