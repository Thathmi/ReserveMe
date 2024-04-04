package lk.jiat.reserveme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class roomDialog extends AppCompatDialogFragment implements NumberPicker.OnValueChangeListener {
    //    static int adults;
    static int child;
    static int room;
    private NumberPicker numberPicker1;
    private roomDialogListner rd;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater i = LayoutInflater.from(getActivity());
        RelativeLayout l = (RelativeLayout) i.inflate(R.layout.layout_dialog, null);

        NumberPicker numberPicker = l.findViewById(R.id.np);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setOnValueChangedListener(this);

        numberPicker.setOnValueChangedListener((numberPicker2, oldValue, newValue) -> {
            room = newValue;
        });

        builder.setView(l)
                .setTitle("Select Rooms")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        int adults = numberPicker.getMaxValue();
                        rd.applyText(room);
                    }
                });
        return builder.create();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            rd = (roomDialogListner) context;


        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement dioalog listner");
        }
    }

    int newVal;

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        room = newVal;
    }


    public interface roomDialogListner {
        void applyText(int value1);
    }
}
