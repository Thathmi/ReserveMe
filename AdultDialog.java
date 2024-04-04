package lk.jiat.reserveme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AdultDialog extends AppCompatDialogFragment implements NumberPicker.OnValueChangeListener {
    static int adults;

    private NumberPicker numberPicker1;
    private AdultDialog.AdultDialogListner ad;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater i = LayoutInflater.from(getActivity());
        RelativeLayout l = (RelativeLayout) i.inflate(R.layout.adult_dialog, null);

        NumberPicker numberPicker = l.findViewById(R.id.np1);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setOnValueChangedListener(this);

        numberPicker.setOnValueChangedListener((numberPicker3, oldValue, newValue) -> {
//            Log.e("picker selected", ""+newValue);
            adults = newValue;
        });

        builder.setView(l)
                .setTitle("Select Guests")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        int adults = numberPicker.getMaxValue();
                        ad.applyTextadult(adults);
                    }
                });
        return builder.create();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            ad = (AdultDialog.AdultDialogListner) context;


        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement dioalog listner");
        }
    }

    int newVal;

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        adults = newVal;
    }



    public interface AdultDialogListner {
        void applyTextadult(int adults);
    }
}
