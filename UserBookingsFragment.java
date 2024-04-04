package lk.jiat.reserveme;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.jiat.reserveme.model.Booking;

public class UserBookingsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Booking> hotelArrayList;
    BokingAdaptor bokingAdaptor;

    String email =  EmailHolder.getUserEmail();

    public static final String TAG = MainActivity.class.getName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserBookingsFragment() {

    }

    public static UserBookingsFragment newInstance(String param1, String param2) {
        UserBookingsFragment fragment = new UserBookingsFragment();
        Bundle args = new Bundle();
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

        return inflater.inflate(R.layout.fragment_user_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.bfrc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hotelArrayList = new ArrayList<Booking>();
        bokingAdaptor = new BokingAdaptor(getActivity(), hotelArrayList);
        recyclerView.setAdapter(bokingAdaptor);

        EventChangeListner();
    }

    private void EventChangeListner() {

        db.collection("booking").whereEqualTo("userEmail", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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