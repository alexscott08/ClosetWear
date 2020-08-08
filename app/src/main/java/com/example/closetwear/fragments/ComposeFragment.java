package com.example.closetwear.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.closetwear.Navigation;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;

public class ComposeFragment extends Fragment {
    public static final String TAG = "ComposeActivity";
    private File photoFile;
    public String photoFileName = "photo.png";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private int itemSelected = 0;
    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;
    private Bitmap selectedImage;
    private OutputStream os;
    private String type;
    private Button newItemBtn;
    private Button newFitBtn;
    private OutfitPost newOutfit = new OutfitPost();
    private File save;

    public ComposeFragment() {

    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_compose, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setOnClickListeners();
    }

    private void bindViews(View view) {
        newItemBtn = view.findViewById(R.id.newItemBtn);
        newFitBtn = view.findViewById(R.id.newFitBtn);
    }

    private void setOnClickListeners() {
        newItemBtn.setOnClickListener(view -> {
            // AlertDialog to ask user to select between taking picture or opening photos gallery
            type = "item";
            createDialog(view);
        });

        newFitBtn.setOnClickListener(view -> {
            // AlertDialog to ask user to select between taking picture or opening photos gallery
            type = "fit";
            createDialog(view);
        });
    }

    private void createDialog(View view) {
        String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_photo);
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select one")
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        itemSelected = selectedIndex;
                    }
                })
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    if (itemSelected == 0) {
                        launchCamera();
                    } else {
                        // Launch photo gallery
                        onPickPhoto(view);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getOutputMediaFile();
        if (filesDir == null) {
            Log.d(TAG, "Error creating media file, check storage permissions");
            makeToast("Error creating media file, check storage permissions");
            return null;
        }
        try {
            os = new FileOutputStream(filesDir);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing bitmap", e);
            makeToast("Couldn't convert file to ready format");
        }
        return filesDir;
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
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
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.ClosetWear", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        }
    }

    // Returns the File for a photo stored on disk given the fileName (for camera intent)
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    // Converts gallery selection from Uri to Bitmap
    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
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
                    selectedImage = takenImage;
                    nextStep();
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
                nextStep();
            }
        }
    }

    // Adds item picture to parse server in background
    private void saveParseFile(final File file, final View view) {
        final ParseFile img = new ParseFile(file);
        img.saveInBackground((SaveCallback) e -> {
            if (e != null) {
                Log.e(TAG, "Error saving image: ", e);
                makeToast("Error saving image.");
            }
            Log.i(TAG, "Image saved");
            newOutfit.put("image", img);
            saveOutfit(view);
        });
    }

    private void saveOutfit(final View view) {
        MaterialAlertDialogBuilder fitTitleDialog = new MaterialAlertDialogBuilder(getContext()).setTitle("Title the fit");
        final EditText editText = new EditText(getContext());
        editText.setHint("Title");

        fitTitleDialog.setView(editText);

        fitTitleDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                newOutfit.setTitle(editText.getText().toString());
                newOutfit.setUser(ParseUser.getCurrentUser());
                newOutfit.saveInBackground(e -> {
                    if (e != null) {
                        Log.e(TAG, "Error while saving", e);
                        makeToast("Error while saving fit!");
                    }
                    Snackbar.make(view, "New outfit added to closet!", Snackbar.LENGTH_LONG)
                            .setAction("Cancel", view1 -> newOutfit.deleteInBackground()).show();
                    Log.i(TAG, "New outfit added to closet!");
                    Navigation.goNewOutfitActivity(getActivity(), newOutfit);
                });
            }
        });

        fitTitleDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        fitTitleDialog.show();

    }

    private void nextStep() {
        if (type != null) {
            if (selectedImage != null) {
                save = persistImage(selectedImage, photoFileName);
                if (type.equals("item")) {
                    Navigation.goNewItemActivity(getActivity(), save);
                } else {
                    // Sets user for new OutfitPost and navigates to next view
                    saveParseFile(save, getView());
                }
            } else {
                makeToast("Where's the picture?");
            }
        } else {
           makeToast("Is this a new item or fit?");
        }
    }

    // Helper function to create toasts for user
    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}