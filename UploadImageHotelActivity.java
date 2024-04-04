package lk.jiat.reserveme;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import io.github.muddz.styleabletoast.StyleableToast;
import lk.jiat.reserveme.imagegrid.DataClass;

public class UploadImageHotelActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button uploadbtn;
    private ImageView uploadImg;
    String email;
    ProgressBar progressBar;
    private Uri imageUri;

    static String imgPath;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_hotel);

        uploadbtn = findViewById(R.id.button12);
        uploadImg = findViewById(R.id.uploadImg);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadImg.setImageURI(imageUri);
                        } else {
                            StyleableToast.makeText(UploadImageHotelActivity.this, "no image selected", Toast.LENGTH_LONG, R.style.invalid).show();
                        }
                    }
                }
        );

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadToFIrebase(imageUri);
                } else {
                    StyleableToast.makeText(UploadImageHotelActivity.this, "please select image", Toast.LENGTH_LONG, R.style.invalid).show();
                }
            }
        });

//        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent("android.media.action.STILL_IMAGE_CAMERA");
//                startActivity(i);
//            }
//        });
    }

//    outside oncreate

    public void uploadToFIrebase(Uri uri) {
        imgPath = String.valueOf(System.currentTimeMillis());
        email = EmailHolder.getSellerEmail();
//        final StorageReference imageReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        StorageReference imageReference = storageReference.child("hotel_images/" + email + "_image.jpg");

        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DataClass dataClass = new DataClass(uri.toString());
//                        String key = databaseReference.push().getKey();
//                        databaseReference.child(key).setValue(dataClass);
                        progressBar.setVisibility(View.INVISIBLE);
                        StyleableToast.makeText(UploadImageHotelActivity.this, "Image uploaded successfully", Toast.LENGTH_LONG, R.style.success).show();
//                        update hotel
                        db.collection("hotel")
                                .whereEqualTo("email", email)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                documentSnapshot.getReference().update("image", imgPath)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(UploadImageHotelActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            StyleableToast.makeText(UploadImageHotelActivity.this, "No matching user found", Toast.LENGTH_LONG, R.style.invalid).show();
                                        }
                                    }
                                });

                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                StyleableToast.makeText(UploadImageHotelActivity.this, "ailed", Toast.LENGTH_LONG, R.style.invalid).show();

            }
        });

    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}