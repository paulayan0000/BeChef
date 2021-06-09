package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.SignInButton;
import com.paula.android.bechef.user.UserManager;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.R;

public class LoginActivity extends BaseActivity {
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
                Intent signInIntent = UserManager.getInstance().getGoogleSignInClient();
                startActivityForResult(signInIntent, Constants.SIGN_IN_REQUSET_CODE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(UserManager.getInstance().checkLastSignedInAccount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SIGN_IN_REQUSET_CODE) {
            try {
                updateUI(UserManager.getInstance().checkSignedInAccountFromIntent(data));
            } catch (ApiException e) {
                if (e.getStatusCode() == Constants.SIGN_IN_FAILED) {
                    Toast.makeText(this, getString(R.string.login_error_msg),
                            Toast.LENGTH_LONG).show();
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Constants.LOGIN_EXIT);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            mSignInButtonGoogle.setVisibility(View.GONE);
            String accountName = account.getDisplayName() == null ? "" : account.getDisplayName();
            Toast.makeText(this, accountName + getString(R.string.login_success_msg), Toast.LENGTH_SHORT).show();
            setResult(Constants.LOGIN_SUCCESS);
            finish();
        } else {
            mSignInButtonGoogle.setVisibility(View.VISIBLE);
        }
    }
}