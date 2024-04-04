package lk.jiat.reserveme;

//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.widget.DatePicker;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

//    public interface DatePickerListner {
//        void onDateSet(DatePicker datePicker, int year, int month, int day);
//    }
public interface DatePickerListener {
    void onDateSet(DatePicker datePicker, int year, int month, int day);
}
//    private DatePickerListner
    private DatePickerListener mListener;
//    DatePickerListner mListner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        try {
//            mListner = (DatePickerListner) context;
//        } catch (Exception e) {
//            throw new ClassCastException(getActivity().toString() + "ad datepick listenr");
//        }
        try {
            mListener = (DatePickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DatePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int date = cal.get(Calendar.DATE);
//        return new DatePickerDialog(getActivity(), this, year, month, date);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);

        // Set the minimum selectable date as today's date
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//        mListner.onDateSet(datePicker, year, month, dayOfMonth);
        if (mListener != null) {
            mListener.onDateSet(datePicker, year, month, dayOfMonth);
        }
    }
}
