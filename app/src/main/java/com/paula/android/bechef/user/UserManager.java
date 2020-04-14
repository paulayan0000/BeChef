package com.paula.android.bechef.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.paula.android.bechef.utils.Constants;

public class UserManager {
    private static final UserManager ourInstance = new UserManager();
    private static final String LOG_TAG = UserManager.class.getSimpleName();
    private static GoogleSignInAccount mGoogleSignInAccount;

    public static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {

    }

    public Intent getGoogleSignInClient(Context context) {
        Log.d(LOG_TAG, "get google sign-in client");
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(context, gso).getSignInIntent();
    }

    public GoogleSignInAccount checkLastSignedInAccount(Context context) {
        Log.d(LOG_TAG, "check last signed-in account");
        if (mGoogleSignInAccount == null) mGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
        logUserInfo((Activity) context);
        return mGoogleSignInAccount;
    }

    public GoogleSignInAccount checkSignedInAccountFromIntent(Intent data, Context context) {
        Log.d(LOG_TAG,"check sign-in account from intent");
        if (mGoogleSignInAccount == null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                mGoogleSignInAccount = task.getResult(ApiException.class);
            } catch (ApiException e) {
                Log.w(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
        logUserInfo((Activity) context);
        return mGoogleSignInAccount;
    }

    private void logUserInfo(Activity activity) {
        if (mGoogleSignInAccount != null) {
            Log.d(LOG_TAG, "user display name: " + mGoogleSignInAccount.getDisplayName() + "\n"
                    + "user given name: " + mGoogleSignInAccount.getGivenName() + "\n"
                    + "user family name: " + mGoogleSignInAccount.getFamilyName() + "\n"
                    + "user email: " + mGoogleSignInAccount.getEmail() + "\n"
                    + "user photo url: " + mGoogleSignInAccount.getPhotoUrl() + "\n"
                    + "user token: " + mGoogleSignInAccount.getIdToken() + "\n"
                    + "user id: " + mGoogleSignInAccount.getId());
            activity.setResult(Constants.LOGIN_SUCCESS);
        } else {
            Log.d(LOG_TAG, "No any signed in Google account!");
        }
    }

    public void clearGoogleSignInAccount() {
        mGoogleSignInAccount = null;
        Log.d(LOG_TAG, "Clear mGoogleSignInAccount!");
    }

    public boolean isLoginStatus() {
        return mGoogleSignInAccount != null;
    }
}
