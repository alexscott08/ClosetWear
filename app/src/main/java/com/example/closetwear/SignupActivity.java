package com.example.closetwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.*;

import java.io.*;

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
    public String photoFileName = "photo.png";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private int itemSelected = 0;
    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;
    private Bitmap selectedImage;
    private OutputStream os;
    private File galleryImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        bindViews();
        bindData();
        setOnClickListeners();
    }

    private void bindViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signUpBtn = findViewById(R.id.signUpBtn);
        profilePicBtn = findViewById(R.id.profilePicBtn);
        profileImg = findViewById(R.id.profileImg);
    }

    private void bindData() {
        GlideApp.with(this)
                .load(R.drawable.ic_profileicon)
                .transform(new CircleCrop())
                .into(profileImg);
    }

    private void setOnClickListeners() {
        // Listener to call camera and set profile pic
        profilePicBtn.setOnClickListener(view -> {
            // AlertDialog to ask user to select between taking picture or opening photos gallery
            String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_photo);
            new AlertDialog.Builder(SignupActivity.this)
                    .setTitle("Select one")
                    .setSingleChoiceItems(singleChoiceItems, itemSelected, (dialogInterface,
                                                                            selectedIndex) -> itemSelected = selectedIndex)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        if (itemSelected == 0) {
                            launchCamera();
                        } else {
                            // Launch photo gallery
                            onPickPhoto(profileImg);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        // Listener to create new account and log in user
        signUpBtn.setOnClickListener(view -> {
            Log.i(TAG, "onClick signup button");
            String username = SignupActivity.this.username.getText().toString();
            String password = SignupActivity.this.password.getText().toString();
            String name = SignupActivity.this.name.getText().toString();
            String email = SignupActivity.this.email.getText().toString();
            if (username == null || password == null || name == null) {
                makeToast("Required fields cannot be empty!");
            } else {
                createUser(username, password, name, email);
            }
        });
    }
    // If 1st dialog option is selected, opens camera view
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

    // Returns the File for a photo stored on disk given the fileName (for camera intent)
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

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    // Converts gallery selection from Uri to Bitmap
    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    // Sets profileImg view depending on if picture was just taken or from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (itemSelected == 0) {
            // If photo was taken with camera
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    // by this point we have the camera photo on disk
                    Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    // Load the taken image into a preview
                    profileImg.setImageBitmap(takenImage);
                } else { // Result was a failure
                    makeToast("Picture wasn't taken!");
                }
            }
        } else {
            // If photo was selected from gallery
            if ((data != null) && requestCode == PICK_PHOTO_CODE) {
                Uri photoUri = data.getData();

                // Load the image located at photoUri into selectedImage
                selectedImage = loadFromUri(photoUri);
                // Load the selected image into a preview
                profileImg.setImageBitmap(selectedImage);
            }
        }
    }

    // Creates new ParseUser instance and sets username, password, email, and profile picture
    private void createUser(final String username, final String password, String name, String email) {
        Log.i(TAG, "Attempting to sign up user " + username);
        // Navigates to main activity if signup and login is successful
        // First logs out current user (if someone is currently logged in)
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();

        final ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.put("name", name);
        if (email != null) {
            newUser.setEmail(email);
        }
        // Saves file to server before making call to signup user
        if (photoFile != null) {
            saveParseFile(photoFile, newUser);
        } else if (selectedImage != null) {
            persistImage(selectedImage, photoFileName);
            saveParseFile(galleryImg, newUser);
        }
        // Signs up user and navigates to main activity
        signUpUser(newUser);
    }

    // Adds profile picture to parse server in background
    private void saveParseFile(final File file, final ParseUser newUser) {
        final ParseFile pFile = new ParseFile(file);
        pFile.saveInBackground((SaveCallback) e -> newUser.put("profilePic", pFile));
    }

    // Signs up user in background and navigates to login activity
    private void signUpUser(ParseUser newUser) {
        newUser.signUpInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Issue with signup", e);
                makeToast("Issue with signup!");
                return;
            }
            Navigation.goLoginActivity(SignupActivity.this);
            makeToast("Success!");
        });
    }

    // Converts bitmap to file instance
    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = getFilesDir();
        galleryImg = new File(filesDir, photoFileName);
        try {
            os = new FileOutputStream(galleryImg);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing bitmap", e);
        }
    }

    // Sends out toast to user with message parameter
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}