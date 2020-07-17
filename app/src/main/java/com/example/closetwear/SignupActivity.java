package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";
    private EditText username;
    private EditText password;
    private EditText name;
    private EditText email;
    private Button signUpBtn;
    private Button profilePicBtn;
    private ImageView profileImg;
    private File photoFile;
    public String photoFileName = "N/A";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private int itemSelected = 0;
    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signUpBtn = findViewById(R.id.signUpBtn);
        profilePicBtn = findViewById(R.id.profilePicBtn);
        profileImg = findViewById(R.id.profileImg);

        // Listener to call camera and set profile pic
        profilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array);
                new AlertDialog.Builder(SignupActivity.this)
                        .setTitle("Select one")
                        .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                itemSelected = selectedIndex;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (itemSelected == 0) {
                                    launchCamera();
                                } else {
                                    // Launch photo gallery
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

        });
        // Listener to create new account and log in user
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick signup button");
                String username = SignupActivity.this.username.getText().toString();
                String password = SignupActivity.this.password.getText().toString();
                String name = SignupActivity.this.name.getText().toString();
                String email = SignupActivity.this.email.getText().toString();
                if (username == null || password == null || name == null) {
                    Toast.makeText(SignupActivity.this, "Required fields cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(username, password, name, email);
                }
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider.ClosetWear", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (itemSelected == 0) {
            // If photo was taken with camera
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    // by this point we have the camera photo on disk
                    Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview
                    profileImg.setImageBitmap(takenImage);
                } else { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Launch photo gallery
        }

    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void signUpUser(String username, String password, String name, String email) {
        Log.i(TAG, "Attempting to sign up user " + username);
        // Navigates to main activity if signup and login is successful
        // First logs out current user (if someone is currently logged in)
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();

        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        if (email != null) {
            newUser.setEmail(email);
        }
        newUser.put("name", name);
        newUser.signUpInBackground();
        // After setting username and password, signs in user
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: better error handling
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(SignupActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(SignupActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }
}