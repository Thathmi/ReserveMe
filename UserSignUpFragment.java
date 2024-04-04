package lk.jiat.reserveme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import lk.jiat.reserveme.model.Hotel;
import lk.jiat.reserveme.model.User;


public class UserSignUpFragment extends Fragment {
    ProgressBar progressBar;
    ListenerRegistration listenerRegistration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserSignUpFragment() {
        // Required empty public constructor
    }

    public static UserSignUpFragment newInstance(String param1, String param2) {
        UserSignUpFragment fragment = new UserSignUpFragment();

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
        return inflater.inflate(R.layout.fragment_user_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        CollectionReference UsersCollection = db.collection("user");
        progressBar = fragment.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        EditText fname1 = fragment.findViewById(R.id.editTextText);
        EditText lname1 = fragment.findViewById(R.id.editTextText3);
        EditText email1 = fragment.findViewById(R.id.editTextTextEmailAddress);
        EditText mobile1 = fragment.findViewById(R.id.editTextPhone);
        EditText passwordEditText = fragment.findViewById(R.id.editTextTextPassword2);

//              ADD DATA TO DB

        Button signUpButton = fragment.findViewById(R.id.si);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = email1.getText().toString();
                String fname = fname1.getText().toString();
                String lname = lname1.getText().toString();
                String mobile = mobile1.getText().toString();
                String password = passwordEditText.getText().toString();



                if (email.equals(" ")) {
                    StyleableToast.makeText(getActivity(), "Please enter email", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (fname.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter Firstname", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (lname.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter Lastname", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (mobile.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter mobile", Toast.LENGTH_LONG, R.style.invalid).show();
                } else if (password.equals("")) {
                    StyleableToast.makeText(getActivity(), "Please enter password", Toast.LENGTH_LONG, R.style.invalid).show();
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

                                        User user = new User(fname, lname, email, mobile, password);
                                        db.collection("user").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                fname1.setText(null);
                                                lname1.setText(null);
                                                email1.setText(null);
                                                mobile1.setText(null);
                                                passwordEditText.setText(null);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                signUpButton.setEnabled(true);
                                                StyleableToast.makeText(getActivity(), "Registration successful. Please log in", Toast.LENGTH_SHORT, R.style.success).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                                Log.i(TAG, e.toString());
                                                progressBar.setVisibility(View.INVISIBLE);
                                                signUpButton.setEnabled(true);
                                            }
                                        });
                                    }
                                } else {
                                    StyleableToast.makeText(getActivity(), "Error checking email", Toast.LENGTH_SHORT, R.style.invalid).show();
                                }
                            });
                }
            }
        });
        fragment.findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {

//            CHANGE FRAGMENTS

            @Override
            public void onClick(View v) {
//                remove sign up
                getActivity().getSupportFragmentManager()
                        .beginTransaction().
                        remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2)).commit();

//                add sign in
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainerView2, UserSignInFragment.class, null)
                        .commit();
            }
        });
    }

}