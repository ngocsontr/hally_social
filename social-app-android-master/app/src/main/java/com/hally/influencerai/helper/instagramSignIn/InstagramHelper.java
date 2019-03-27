package com.hally.influencerai.helper.instagramSignIn;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hally.influencerai.Constants;
import com.hally.influencerai.model.User;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HallyTran on 3/21/2019.
 * transon97uet@gmail.com
 */
public class InstagramHelper {

    private InstagramDialog mDialog;//Dialog open to display login screen of instagram
    private User mUser;//Save users data
    private InstagramResponse mListener;

    private ProgressDialog mProgress;
    private String mAccessToken;//You can store access token in preferences to maintain session
    private String mClientId;
    private String mClientSecret;
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;

    /**
     * Callback url, as set in 'Manage OAuth Costumers' page
     * (https://developer.github.com/)
     */
    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";
    private static final String TAG = "InstagramAPI";

    /**
     * public constructor
     *
     * @param clientId        generated from https://www.instagram.com/developer/
     * @param clientSecretKey generated from https://www.instagram.com/developer/
     * @param callbackUrl     generated from https://www.instagram.com/developer/
     * @param context         context of your activity
     * @param listeners       listener context of your activity
     */

    @SuppressWarnings("ConstantConditions")
    public InstagramHelper(@NonNull String clientId,
                           @NonNull String clientSecretKey,
                           @NonNull String callbackUrl,
                           @NonNull Context context,
                           @NonNull InstagramResponse listeners) {

        //Validating inputs
        if (clientId == null)
            throw new IllegalArgumentException("Instagram client id cannot be null.");
        if (clientSecretKey == null)
            throw new IllegalArgumentException("Instagram client secret key cannot be null.");
        if (callbackUrl == null)
            throw new IllegalArgumentException("Instagram callback url cannot be null.");
        if (listeners == null)
            throw new IllegalArgumentException("Implement InstagramResponse listener.");

        mListener = listeners;
        mClientId = clientId;
        mClientSecret = clientSecretKey;
        mCallbackUrl = callbackUrl;
        String authUrl = AUTH_URL + "?client_id=" + mClientId + "&redirect_uri="
                + mCallbackUrl + "&response_type=code&display=touch&scope=likes+comments+relationships";

        mDialog = new InstagramDialog(context, authUrl, new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {//authentication completed
                Log.i(TAG, "/authentication completed: " + code);
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {//error while signing in
                mListener.onInstagramSignInFail("Authorization failed");
            }

            @Override
            public void onDismiss() {
                mListener.onInstagramDismiss();
            }
        });
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    /**
     * call this funtion to perform sign in with instagram
     */
    public void performSignIn() {
        if (mAccessToken != null) {
            mAccessToken = null;
        } else {
            mDialog.show();
        }
    }

    /**
     * @param response is response from instagram login url
     */
    private void getAccessToken(final String response) {
        mProgress.setMessage("Getting access token ...");
        mProgress.show();

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;
                try {
                    URL url = new URL(TOKEN_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write("client_id=" + mClientId +
                            "&client_secret=" + mClientSecret +
                            "&grant_type=authorization_code" +
                            "&redirect_uri=" + mCallbackUrl +
                            "&code=" + response);
                    writer.flush();

                    String response = streamToString(urlConnection.getInputStream());
                    Log.i(TAG, "response " + response);

                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

                    mAccessToken = jsonObj.getString("access_token");

                    mUser = new User();
                    mUser.setSocialType(Constants.UserType.INSTAGRAM);
                    mUser.setSnsAccessToken(jsonObj.getString("access_token"));
                    mUser.setUsername(jsonObj.getJSONObject("user").getString("username"));
                    mUser.setDescription(jsonObj.getJSONObject("user").getString("bio"));
                    mUser.setWebsite(jsonObj.getJSONObject("user").getString("website"));
                    mUser.setAvatar(jsonObj.getJSONObject("user").getString("profile_picture"));
                    mUser.setFullName(jsonObj.getJSONObject("user").getString("full_name"));
                    mUser.setSocialId(jsonObj.getJSONObject("user").getString("id"));

                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mProgress.dismiss();

                if (what == WHAT_ERROR) {
                    //if exception occur
                    mListener.onInstagramSignInFail("Instagram Failed to get access token");
                } else {
                    mListener.onInstagramSignInSuccess(mUser);
                }
            }
        }.start();
    }

    /**
     * @param is input stream response from instagram login
     * @return response from inputstream
     * @throws IOException if exception occur
     */
    private String streamToString(InputStream is) throws IOException {
        String str = "";
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    if (reader != null) reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            str = sb.toString();
        }
        return str;
    }
}
