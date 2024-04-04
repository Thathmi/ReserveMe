package lk.jiat.reserveme;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaysHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaysHomeFragment extends Fragment implements DatePickerFragment.DatePickerListener{
    private TextView dateView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StaysHomeFragment() {
        // Required empty public constructor
    }

    public static StaysHomeFragment newInstance(String param1, String param2) {
        StaysHomeFragment fragment = new StaysHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stays_home, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fragment.findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
//
            @Override
            public void onClick(View v) {
                dateView=fragment.findViewById(R.id.textView4);
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.setCancelable(false);
//                        dialogFragment.show(getSupportFragmentManager(),"Date picker");
                Toast.makeText(fragment.getContext(), "ok",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DATE,day);
        String date = DateFormat.getDateInstance().format(cal.getTime());
        dateView.setText(date);
    }
}