package lk.jiat.reserveme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.jiat.reserveme.model.Hotel;

public class HotelListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Hotel> hotelArrayList;
    MyAdaptor myAdaptor;

    String city = UserHomeActivity.city;
    public static final String TAG = MainActivity.class.getName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        TextView top = findViewById(R.id.textView11);
        top.setText(city);

        recyclerView = findViewById(R.id.recyleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hotelArrayList = new ArrayList<Hotel>();
        myAdaptor = new MyAdaptor(HotelListActivity.this, hotelArrayList);

        recyclerView.setAdapter(myAdaptor);

        EventChangeListner();

    }

//    database
    private void EventChangeListner() {

        db.collection("hotel").whereEqualTo("city", city)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(HotelListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                hotelArrayList.add(dc.getDocument().toObject(Hotel.class));
                            }
                            myAdaptor.notifyDataSetChanged();
                        }
                    }
                });
    }

}


