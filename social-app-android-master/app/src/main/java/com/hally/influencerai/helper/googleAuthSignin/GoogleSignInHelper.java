package com.hally.influencerai.helper.googleAuthSignin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hally.influencerai.Constants;
import com.hally.influencerai.R;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.LogUtil;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 * This is the helper class for Google sign in using Google Auth.
 * You can find detail steps here: {@link 'https://developers.google.com/identity/sign-in/android/start-integrating#add-config'}
 */
public class GoogleSignInHelper implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 100;
    private FragmentActivity mContext;
    private GoogleAuthResponse mListener;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Public constructor
     *
     * @param context        instance of caller activity
     * @param serverClientId The client ID of the server that will verify the integrity of the token. If you don't have clientId pass null.
     *                       For more detail visit {@link 'https://developers.google.com/identity/sign-in/android/backend-auth'}
     */
    public GoogleSignInHelper(FragmentActivity context, @Nullable String serverClientId, @NonNull GoogleAuthResponse listener) {
        mContext = context;
        mListener = listener;

        //noinspection ConstantConditions
        if (listener == null) {
            throw new RuntimeException("GoogleAuthResponse listener cannot be null.");
        }

        //build api client
        buildGoogleApiClient(buildSignInOptions(serverClientId));
    }

    private GoogleSignInOptions buildSignInOptions(@Nullable String serverClientId) {
        GoogleSignInOptions.Builder gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId();
        if (serverClientId != null) gso.requestIdToken(serverClientId);
        return gso.build();
    }

    private void buildGoogleApiClient(@NonNull GoogleSignInOptions gso) {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(mContext, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void performSignIn(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void performSignIn(Fragment activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                mListener.onGoogleAuthSignIn(parseToGoogleUser(acct));
            } else {
                mListener.onGoogleAuthSignInFailed();
            }
        }
    }

    private User parseToGoogleUser(GoogleSignInAccount account) {
        User user = new User();
        user.setSocialType(Constants.UserType.YOUTUBE);
        user.setSocialId(account.getId());
        user.setUsername(account.getDisplayName());
        user.setFullName(account.getFamilyName() + account.getGivenName());
        user.setSnsAccessToken(account.getIdToken());
        user.setEmail(account.getEmail());
        user.setAvatar(String.format(mContext.getString(R.string.google_large_image_url_pattern),
                account.getPhotoUrl(), Constants.Profile.MAX_AVATAR_SIZE));
        return user;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtil.logDebug("GoogleSignInHelper", "onConnectionFailed:" + connectionResult);
        mListener.onGoogleAuthSignInFailed();
    }

    public void performSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        mListener.onGoogleAuthSignOut(status.isSuccess());
                    }
                });
    }
}
