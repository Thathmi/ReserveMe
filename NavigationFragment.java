package lk.jiat.reserveme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NavigationFragment extends Fragment {

    public NavigationFragment() {
        // Required empty public constructor
    }

    public static NavigationFragment newInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();

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
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);


//        go to home
//        fragment.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i2 = new Intent(getActivity(),UserHomeActivity.class);
//                startActivity(i2);
//            }
//        });
//change fragments
//        fragment.findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                remove sign in
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction().
//                        remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2))
//                        .commit();
//
////                add sign up
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .add(R.id.fragmentContainerView2,UserSignUpFragment.class,null)
//                        .commit();
//
//                Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}