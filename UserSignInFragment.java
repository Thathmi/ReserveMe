package lk.jiat.reserveme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;


public class UserSignInFragment extends Fragment {
    ListenerRegistration listenerRegistration;
    private static final String TAG = MainActivity.class.getName();
    //    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference UsersCollection = db.collection("user");
    static String uEmail;

    public UserSignInFragment() {
        // Required empty public constructor
    }

    public static UserSignInFragment newInstance(String param1, String param2) {
        UserSignInFragment fragment = new UserSignInFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        EditText emailEditText = fragment.findViewById(R.id.editTextText);
        EditText passwordEditText = fragment.findViewById(R.id.editTextTextPassword2);

        // Check if the user is already logged in via SharedPreferences and redirect to home page
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", null);
        String savedPassword = sharedPreferences.getString("password", null);

        if (savedEmail != null && savedPassword != null) {

            EmailHolder.setUserEmail(savedEmail);
            uEmail = savedEmail;

            Intent intent = new Intent(requireActivity(), UserHomeActivity.class);
            startActivity(intent);
            requireActivity().finish();

        } else {
            fragment.findViewById(R.id.si).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    if (email.equals(" ")) {
                        StyleableToast.makeText(getActivity(), "Please enter email!", Toast.LENGTH_LONG, R.style.invalid).show();
                    } else if (password.equals("")) {
                        StyleableToast.makeText(getActivity(), "Please enter password!", Toast.LENGTH_LONG, R.style.invalid).show();
                    } else {

                        UsersCollection.whereEqualTo("email", email)
                                .whereEqualTo("password", password)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null && !task.getResult().isEmpty()) {

                                                DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);

                                                //setting email to static and passing to the email holder class
                                                String userEmail = userDoc.getString("email");
                                                EmailHolder.setUserEmail(userEmail);

                                                // Save user details in SharedPreferences
                                                saveUserDetails(email, password);

                                                Intent intent = new Intent(getActivity(), UserHomeActivity.class);
//                                                Log.d("SignInFragment", "Intent content: " + intent.getExtras());

                                                startActivity(intent);
                                            } else {
                                                emailEditText.setText(null);
                                                passwordEditText.setText(null);
                                                StyleableToast.makeText(getActivity(), "Invalid details!", Toast.LENGTH_LONG, R.style.invalid).show();
                                            }
                                        } else {
                                            StyleableToast.makeText(getActivity(), "error", Toast.LENGTH_LONG, R.style.invalid).show();
                                        }
                                    }
                                });
                    }
                }

            });
        }
//        go to home


//change fragments
        fragment.findViewById(R.id.textView8).

                setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                remove sign in
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().
                                remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2))
                                .commit();

//                add sign up
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragmentContainerView2, UserSignUpFragment.class, null)
                                .commit();
                    }
                });
    }

    private void saveUserDetails(String email1, String password1) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email1);
        editor.putString("password", password1);
        editor.apply();
    }
}