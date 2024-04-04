package lk.jiat.reserveme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.jiat.reserveme.model.Booking;

public class BookingListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Booking> hotelArrayList;
    BokingAdaptor bokingAdaptor;

    String email = EmailHolder.getUserEmail();

    public static final String TAG = MainActivity.class.getName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        recyclerView = findViewById(R.id.bookingRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hotelArrayList = new ArrayList<Booking>();
        bokingAdaptor = new BokingAdaptor(BookingListActivity.this, hotelArrayList);
        recyclerView.setAdapter(bokingAdaptor);

        EventChangeListner();

//        back

        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(BookingListActivity.this,UserHomeActivity.class );
                startActivity(i2);
            }
        });
    }

    private void EventChangeListner() {

        db.collection("booking").whereEqualTo("userEmail", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(BookingListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                hotelArrayList.add(dc.getDocument().toObject(Booking.class));
                            }
                            bokingAdaptor.notifyDataSetChanged();
                        }
                    }
                });
    }
}