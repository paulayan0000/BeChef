package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.paula.android.bechef.R;
import com.paula.android.bechef.user.UserManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.paula.android.bechef.utils.Constants;

import androidx.annotation.Nullable;

public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private SignInButton mSignInButtonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignInButtonGoogle = findViewById(R.id.signinbutton_google);
        ((TextView) mSignInButtonGoogle.getChildAt(0)).setText(R.string.signInGoogle);
        mSignInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = UserManager.getInstance().getGoogleSignInClient(mContext);
                startActivityForResult(signInIntent, Constants.SIGN_IN_REQUSET_CODE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(UserManager.getInstance().checkLastSignedInAccount(mContext));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SIGN_IN_REQUSET_CODE) {
            updateUI(UserManager.getInstance().checkSignedInAccountFromIntent(data, mContext));
        }
    }

    @Override
    protected void onDestroy() {
        setResult(Constants.LOGIN_EXIT);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(Constants.LOGIN_EXIT);
        super.onBackPressed();
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            mSignInButtonGoogle.setVisibility(View.GONE);
            Toast.makeText(this, account.getDisplayName() + " 登入中...", Toast.LENGTH_SHORT).show();
            setResult(Constants.LOGIN_SUCCESS);
            finish();
        } else {
            mSignInButtonGoogle.setVisibility(View.VISIBLE);
        }
    }
}
