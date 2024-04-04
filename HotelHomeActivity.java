package lk.jiat.reserveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class HotelHomeActivity extends AppCompatActivity {
    private boolean loggedIn = false;
    AnimatedBottomBar bottomBar;
    public static final String TAG = MainActivity.class.getName();
    ListenerRegistration listenerRegistration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email = EmailHolder.getSellerEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_home);

        loggedIn = true;
        bottomBar = findViewById(R.id.bottom_bar);

//        set default fragment
        replace(new HotelMainHomeFragment());
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {

                if (tab1.getId() == R.id.home) {
                    replace(new HotelMainHomeFragment());
                } else if (tab1.getId() == R.id.booking) {
                    replace(new HotelBookingsFragment());
                } else if (tab1.getId() == R.id.images) {
                    replace(new HotelImageFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (loggedIn) {

        } else {
            // Allow normal back button behavior for other cases
            super.onBackPressed();
        }
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout1, fragment);
        transaction.commit();
    }
}