package lk.jiat.reserveme;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import lk.jiat.reserveme.model.Hotel;


public class HotelSignUpFragment extends Fragment {
    ProgressBar progressBar;
    ListenerRegistration listenerRegistration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HotelSignUpFragment() {
        // Required empty public constructor
    }

    public static HotelSignUpFragment newInstance(String param1, String param2) {
        HotelSignUpFragment fragment = new HotelSignUpFragment();

        return fragment;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Password validation
    private boolean isValidPassword(String password) {

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

        return password.matches(passwordPattern);
    }

    private boolean isValidMobile(String mobile) {

        String mobilePattern = "^07\\d{8}$";

        return mobile.matches(mobilePattern);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public static final String TAG = MainActivity.class.getName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        CollectionReference UsersCollection = db.collection("hotel");
        progressBar = fragment.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        String cities[] = new String[]{"Colombo", "Mount Lavinia", "Kesbewa", "Moratuwa", "Maharagama", "Ratnapura", "Kandy", "Negombo", "Sri Jayewardenepura Kotte", "Kalmunai", "Trincomalee", "Galle", "Jaffna", "Athurugiriya", "Weligama", "Matara", "Kolonnawa", "Gampaha", "Badulla", "Kalutara", "Bentota", "Matale",
                "Mannar",
                "Pothuhera",
                "Kurunegala",
                "Mabole",
                "Hatton",
                "Hambantota",
                "Oruwala"};

        Spinner tv = fragment.findViewById(R.id.spinner);
        ArrayAdapter<String> a = new ArrayAdapter<>(fragment.getContext(), android.R.layout.simple_dropdown_item_1line, cities);
        tv.setAdapter(a);

//  ---------  SIGN UP  ---------------

        EditText name1 = fragment.findViewById(R.id.editTextText);
        EditText address1 = fragment.findViewById(R.id.editTextText3);
        EditText email1 = fragment.findViewById(R.id.editTextTextEmailAddress);
        EditText mobile1 = fragment.findViewById(R.id.editTextPhone);
        EditText passwordEditText = fragment.findViewById(R.id.editTextTextPassword2);
        Spinner city1 = fragment.findViewById(R.id.spinner);

        Button signUpButton = fragment.findViewById(R.id.button17);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email1.getText().toString();
                String name = name1.getText().toString();
                String address = address1.getText().toString();
                String mobile = mobile1.getText().toString();
                String password = passwordEditText.getText().toString();
                String city = city1.getSelectedItem().toString();

//              add data

                if (email.equals(" ")) {
                    StyleableToast.makeText(getActivity(), "Please enter email!", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (name.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter Firstname!", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (address.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter address!", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (mobile.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter mobile!", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (password.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter password!", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (!isValidEmail(email)) {
                    StyleableToast.makeText(getActivity(), "Email is not valid", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (!isValidPassword(password)) {
                    StyleableToast.makeText(getActivity(), "Password must be one upper case one lower case and letters numbers and symbols", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (!isValidMobile(mobile)) {
                    StyleableToast.makeText(getActivity(), "Please enter valid mobile", Toast.LENGTH_LONG, R.style.invalid).show();
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    signUpButton.setEnabled(false);


                    UsersCollection.whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult() != null && !task.getResult().isEmpty()) {
                                        StyleableToast.makeText(getActivity(), "Email already exists", Toast.LENGTH_SHORT, R.style.invalid).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        signUpButton.setEnabled(true);
                                    } else {

                                        Hotel hotel = new Hotel(name, password, email, mobile, address, city);
                                        db.collection("hotel").add(hotel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                signUpButton.setEnabled(true);
                                                StyleableToast.makeText(getActivity(), "Registration successful. Please log in", Toast.LENGTH_SHORT, R.style.success).show();
                                                name1.setText(null);
                                                address1.setText(null);
                                                email1.setText(null);
                                                mobile1.setText(null);
                                                passwordEditText.setText(null);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                signUpButton.setEnabled(true);
                                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                                Log.i(TAG, e.toString());
                                            }
                                        });

                                    }
                                } else {
                                    StyleableToast.makeText(getActivity(), "Error checking email", Toast.LENGTH_SHORT, R.style.invalid).show();
                                }
                            });
//                    -----------------
                }
            }
        });
// --------  CHANGING FRAGMENTS  --------------

        fragment.findViewById(R.id.textView2).
                setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                remove sign up
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().
                                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView4)).commit();

//                add sign in
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragmentContainerView4, HotelSignInFragment.class, null)
                                .commit();
                    }
                });
    }

}