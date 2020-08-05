package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.parse.*;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText username;
    private EditText password;
    private Button loginBtn;
    private Button signUpBtn;
    private SignInButton googleSignInBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // If a user is already signed in, skip Login and go to MainActivity
        if (ParseUser.getCurrentUser() != null) {
            Navigation.goMainActivity(LoginActivity.this);
        }
        bindViews();
        setOnClickListeners();
    }

    private void bindViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        googleSignInBtn = findViewById(R.id.googleSignInBtn);
    }

    private void setOnClickListeners() {
        // Listener to log in/sign up with Google account
        googleSignInBtn.setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.googleSignInBtn:
                    googleSignIn();
                    break;
                default:
                    break;
            }
        });

        // Listener to log in user
        loginBtn.setOnClickListener(view -> {
            Log.i(TAG, "onClick login button");
            loginUser(LoginActivity.this.username.getText().toString(),
                    LoginActivity.this.password.getText().toString());
        });

        // Listener to create new account and log in user
        signUpBtn.setOnClickListener(view -> {
            Log.i(TAG, "onClick signup button");
            Navigation.goSignupActivity(LoginActivity.this);
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        // Navigates to main activity if login is successful
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                makeToast("Issue with login!");
                return;
            }
            Navigation.goMainActivity(LoginActivity.this);
            makeToast("Hello!");
        });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, determines if user account already exists in app
            checkIfUserExists(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            makeToast("Issue with Google Sign-In. Please try again.");
        }
    }

    private void checkIfUserExists(GoogleSignInAccount account) {
        // Email associated with Google account
        String email = account.getEmail();
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.findInBackground((users, e) -> {
            if (e != null) {
                Log.e(TAG, "Problem  with querying users: ", e);
                return;
            }
            // If user already exists start that user's session, else navigate to signup to create account
            for (ParseUser user : users) {
                // Emails are guaranteed to be unique throughout accounts
                if (user.getUsername().equals(email)) {
                    Log.i(TAG, "found google user");
                    loginUser(user.getUsername(), account.getId());
                    return;
                }

            }
            try {
                createGoogleUser(account);
            } catch (IOException ex) {
                Log.e(TAG, "Error in createGoogleUser(): " + ex);
            }
        });
    }

    private void createGoogleUser(GoogleSignInAccount account) throws IOException {
        Log.i(TAG, "Creating new Google User");
        ParseUser newUser = new ParseUser();
        newUser.setEmail(account.getEmail());
        newUser.setUsername(account.getEmail());
        newUser.put("name", account.getDisplayName());
        newUser.setPassword(account.getId());
        newUser.signUpInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Couldn't sign up new user");
            }
            loginUser(newUser.getUsername(), account.getId());
        });
    }

    // Sends out toast to user with message parameter
    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}