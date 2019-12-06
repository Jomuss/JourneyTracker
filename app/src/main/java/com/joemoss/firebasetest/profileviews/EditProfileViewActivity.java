package com.joemoss.firebasetest.profileviews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joemoss.firebasetest.Models.CurrentUser;
import com.joemoss.firebasetest.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class EditProfileViewActivity extends AppCompatActivity {

    public final String APP_TAG = "Journey Tracker";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File profPicFile;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    ImageView editProfPicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_view);

        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        editProfPicView = findViewById(R.id.edit_profile_picture);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView editProfPicView = findViewById(R.id.edit_profile_picture);
        editProfPicView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(profPicFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView editProfPic = (ImageView) findViewById(R.id.edit_profile_picture);
                Glide.with(getApplicationContext())
                        .load(takenImage)
                        .circleCrop()
                        .into(editProfPicView);
                uploadPhoto(takenImage);

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        CurrentUser.getInstance().profileDataChanged(true);
    }

    @Override
    public void onBackPressed() {

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == android.R.id.home) {
//            if(CurrentUser.getInstance().getProfileDataChangedValue()) {
//                setResult(RESULT_OK);
//                this.finish();
//            }
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    //Launches Camera App and
    private void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        String photoFileName = "profilePicture.jpg";
        this.profPicFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(EditProfileViewActivity.this, "com.codepath.fileprovider", profPicFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void uploadPhoto(Bitmap profPic){
        //Get reference to logged in users UID
        String uid = CurrentUser.getInstance().currentUser.getUid();

        //Create Firebase Storage References
        StorageReference storageRef = storage.getReference();
        StorageReference profPicRef = storageRef.child("users/"+uid+"/profilePic.jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profPic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profPicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileViewActivity.this, "Your profile picture could not be uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }


}
