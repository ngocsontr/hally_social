package com.hally.influencerai.helper.facebookSignIn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hally.influencerai.R;
import com.hally.influencerai.enums.UserType;
import com.hally.influencerai.model.SocialUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public class FacebookHelper {
    private Context mContext;
    private FacebookResponse mListener;
    private String mFieldString;
    private CallbackManager mCallBackManager;

    /**
     * Public constructor.
     *
     * @param fieldString      name of the fields required. (e.g. id,name,email,gender,birthday,picture,cover)
     *                         See {@link 'https://developers.facebook.com/docs/graph-api/reference/user'} for more info on user node.
     * @param context          instance of the caller activity
     * @param responseListener {@link FacebookResponse} listener to get call back response.
     */
    public FacebookHelper(@NonNull String fieldString, @NonNull Activity context, @NonNull FacebookResponse responseListener) {
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        mContext = context;
        //noinspection ConstantConditions
        if (responseListener == null)
            throw new IllegalArgumentException("FacebookResponse listener cannot be null.");

        //noinspection ConstantConditions
        if (fieldString == null) throw new IllegalArgumentException("field string cannot be null.");

        mListener = responseListener;
        mFieldString = fieldString;
        mCallBackManager = CallbackManager.Factory.create();

        //get access token
        FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mListener.onFacebookSignInSuccess();

                //get the user profile
                getUserProfile(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                mListener.onFacebookSignInFail(e);
            }
        };
        LoginManager.getInstance().registerCallback(mCallBackManager, mCallBack);
    }

    /**
     * Get user facebook profile.
     *
     * @param loginResult login result with user credentials.
     */
    private void getUserProfile(LoginResult loginResult) {
        Log.i("AccessToken: ", loginResult.getAccessToken() + "");
        // App code
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.i("response: ", response + "");
                        try {
                            mListener.onFacebookProfileReceived(parseResponse(loginResult.getAccessToken(), object));
                        } catch (Exception e) {
                            e.printStackTrace();

                            mListener.onFacebookSignInFail(e);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", mFieldString);
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Get the {@link CallbackManager} for managing callbacks.
     *
     * @return {@link CallbackManager}
     */
    @NonNull
    @CheckResult
    public CallbackManager getCallbackManager() {
        return mCallBackManager;
    }

    /**
     * Parse the response received into {@link SocialUser} object.
     *
     * @param accessToken
     * @param object      response received.
     * @return {@link SocialUser} with required fields.
     * @throws JSONException
     */
    private SocialUser parseResponse(AccessToken accessToken, JSONObject object) throws JSONException {
        SocialUser user = new SocialUser();

        user.setUserType(UserType.FACEBOOK);
        user.setAccessToken(accessToken.getToken());
        if (object.has("id")) user.setId(object.getString("id"));
        if (object.has("email")) user.setEmail(object.getString("email"));
        if (object.has("name")) user.setUsername(object.getString("name"));
        if (object.has("gender"))
            user.setGender(object.getString("gender"));
        if (object.has("about")) user.setAbout(object.getString("about"));
        if (object.has("bio")) user.setDescription(object.getString("bio"));
        if (object.has("link")) user.setWebsite(object.getString("link"));
        if (object.has("location"))
            user.setLocation(object.getJSONObject("location").getString("name"));
        if (object.has("cover"))
            user.setCoverPicUrl(object.getJSONObject("cover").getString("source"));
//        if (object.has("picture"))
        user.setAvatar(String.format(mContext.getString(R.string.facebook_large_image_url_pattern),
                user.getId()));
        return user;
    }

    /**
     * Perform facebook sign in.<p>
     * NOTE: If you are signing from the fragment than you should call {@link #performSignIn(Fragment)}.<p>
     * This method should generally call when user clicks on "Sign in with Facebook" button.
     *
     * @param activity instance of the caller activity.
     */
    public void performSignIn(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
    }

    /**
     * Perform facebook login. This method should be called when you are signing in from
     * fragment.<p>
     * This method should generally call when user clicks on "Sign in with Facebook" button.
     *
     * @param fragment caller fragment.
     */
    public void performSignIn(Fragment fragment) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile", "email"));
    }

    /**
     * This method handles onActivityResult callbacks from fragment or activity.
     *
     * @param requestCode request code received.
     * @param resultCode  result code received.
     * @param data        Data intent.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void performSignOut() {
        LoginManager.getInstance().logOut();
        mListener.onFacebookSignOut();
    }
}
