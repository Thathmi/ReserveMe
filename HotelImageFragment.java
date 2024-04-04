package lk.jiat.reserveme;

import static android.app.Activity.RESULT_OK;

//import static io.grpc.Context.LazyStorage.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.muddz.styleabletoast.StyleableToast;
import lk.jiat.reserveme.imagegrid.DataClass;
import lk.jiat.reserveme.imagegrid.MyImageAdaptor;
import lk.jiat.reserveme.model.User;


public class HotelImageFragment extends Fragment {
    public static final String TAG = MainActivity.class.getName();
    private String email = EmailHolder.getSellerEmail();
    RecyclerView recyclerView;
    ArrayList<DataClass> dataList;
    MyImageAdaptor adaptor;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("hotel_images/a/1701697494727");
    private Button uploadbtn, selectimg;
    private ImageView imageView;
    ListenerRegistration listenerRegistration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBar;
    String path;
    StorageReference storageReference;
    Uri image;
    Button fab;
    private String imgPath;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

        @Override

        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                image = result.getData().getData();
                uploadbtn.setEnabled(true);
                Glide.with(getActivity()).load(image).into(imageView);

            } else {
                StyleableToast.makeText(getActivity(), "please select an image", Toast.LENGTH_LONG, R.style.invalid).show();
            }
        }
    });

    public HotelImageFragment() {
    }

    public static HotelImageFragment newInstance(String param1, String param2) {
        HotelImageFragment fragment = new HotelImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.button11);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getActivity(), UploadImageHotelActivity.class);
                startActivity(i2);

            }
        });

//        retreiv
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        dataList = new ArrayList<>();
        adaptor = new MyImageAdaptor(dataList, getActivity());
        recyclerView.setAdapter(adaptor);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        ImageView imageview = view.findViewById(R.id.imageView28);
        StorageReference imageref = storageReference.child("hotel_images/" + email+ "_image.jpg");
        imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageview);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to fetch image: " + e.getMessage());
            }
        });

    }

}