package com.joemoss.firebasetest.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joemoss.firebasetest.Models.CurrentUser;
import com.joemoss.firebasetest.Models.JourneyImageModel;
import com.joemoss.firebasetest.Models.JourneyModel;
import com.joemoss.firebasetest.R;
import com.joemoss.firebasetest.profileviews.EditProfileViewActivity;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NewJourneyViewActivity extends AppCompatActivity {

    public final String APP_TAG = "Journey Tracker";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1002;
    private File newJourneyImageFile;
    private ImageView newJourneyImageThumbnail;
    private FloatingActionButton newJourneyImageButton;
    private FloatingActionButton postNewJourneyButton;
    private EditText newJourneyMainText;
    private EditText newJourneyTitle;
    private EditText newJourneyTags;
    private View newJourneyImageView;
    private FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private JourneyModel newJourney;
    private List<JourneyImageModel> newJourneyImages;
    private int imageCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journey_view);

        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        imageCounter = 1;
        newJourneyImages = new ArrayList<>();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Journey");

        LayoutInflater mInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = findViewById(R.id.new_journey_linear_view);
        newJourneyImageView = mInflater.inflate(R.layout.new_journey_image_view, null);

        //Gets DP value for padding on newJourneyImageView
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int)(10*scale + .05f);

        newJourneyImageView.setPadding(0,0,0,dpAsPixels);
        linearLayout.addView(newJourneyImageView);

        newJourneyImageThumbnail = newJourneyImageView.findViewById(R.id.new_journey_image_thumbnail);
        newJourneyImageButton = findViewById(R.id.new_journey_image_fab);
        postNewJourneyButton = findViewById(R.id.postNewJourneyFab);
        newJourneyMainText = findViewById(R.id.newJourneyMainText);
        newJourneyTitle = findViewById(R.id.newJourneyTitleText);
        newJourneyTags = findViewById(R.id.newJourneyTags);


        newJourneyImageView.findViewById(R.id.new_journey_image_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
            }
        });

        postNewJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSubmissionConfirmDialog();
            }
        });




    }

    private void createSubmissionConfirmDialog(){
        AlertDialog.Builder confirmAlert = new AlertDialog.Builder(this);
        confirmAlert.setMessage("Submit Journey?")
                .setTitle("Journey Tracker")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadJourney();
                    }
                })
                .setCancelable(true)
                .create()
                .show();
    }

    //Initialize new Journey model and upload to the Firestore Database. On Success, update the User's document with the post ID
    private void uploadJourney(){

        final String uid = fAuth.getCurrentUser().getUid();
       // newJourneyImages.add(new JourneyImageModel("users/posts/"))
        EditText imageText = newJourneyImageView.findViewById(R.id.new_journey_image_text);
        EditText imageTitle = newJourneyImageView.findViewById(R.id.new_journey_image_title);
        JourneyImageModel jIModel = new JourneyImageModel("", imageText.getText().toString(),imageTitle.getText().toString());
        newJourneyImages.add(jIModel);

        newJourney = new JourneyModel(newJourneyTitle.getText().toString(), uid, fAuth.getCurrentUser().getDisplayName(),
                "users/"+uid+"/profilePic.jpg", newJourneyMainText.getText().toString(),
                "", "", newJourneyTags.getText().toString(), newJourneyImages);

        firestore.collection("posts").add(newJourney)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        updateUserDocument(documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    //Update the "posts" array of the userdocument with the ID of the new Post
    private void updateUserDocument(final String journeyDocRef){
        String uid = fAuth.getCurrentUser().getUid();
        WriteBatch batch = firestore.batch();

        DocumentReference userRef =  firestore.collection("users").document(uid);
        batch.update(userRef, "posts", FieldValue.arrayUnion("posts/"+journeyDocRef));

        newJourney.getJourneyImages().get(0).setPictureRef("users/"+uid+"/posts/"+journeyDocRef+"image"+(imageCounter)+".jpg");
        DocumentReference postRef = firestore.collection("posts").document(journeyDocRef);
        batch.update(postRef, "journeyImages", newJourney.getJourneyImages());

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                uploadJourneyImages(journeyDocRef);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

//        firestore.collection("users").document(CurrentUser.getInstance().currentUser.getUid())
//                .update("posts", FieldValue.arrayUnion(docRef))
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        uploadJourneyImages(docRef);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });

    }

    private void uploadJourneyImages(final String docRef){
        StorageReference storageRef = storage.getReference().child("users/"+fAuth.getCurrentUser().getUid()+"/posts/"+docRef+"/image/"+imageCounter+".jpg");

        Bitmap takenImage = BitmapFactory.decodeFile(newJourneyImageFile.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        takenImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                updateUserJourneyEntry(docRef);
                Toast.makeText(NewJourneyViewActivity.this, "Journey Posted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void updateUserJourneyEntry(String docRef){
//        JourneyImageModel jIModel = new JourneyImageModel("", newJourneyImageView.findViewById(R.id.new_journey_image_text))
//
//        firestore.collection("post").document(docRef).update("journeyImages", FieldValue.arrayUnion())
//    }

    //Returns the URI for the photo taken
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
        this.newJourneyImageFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(NewJourneyViewActivity.this, "com.codepath.fileprovider", newJourneyImageFile);
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
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(newJourneyImageFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView editProfPic = (ImageView) findViewById(R.id.edit_profile_picture);
                Glide.with(getApplicationContext())
                        .load(takenImage)
                        .circleCrop()
                        .into(newJourneyImageThumbnail);

                        newJourneyImageThumbnail.setVisibility(View.VISIBLE);
                        newJourneyImageButton.hide();

                        newJourneyImageThumbnail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onLaunchCamera(v);
                            }
                        });



            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
