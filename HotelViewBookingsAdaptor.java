package lk.jiat.reserveme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.jiat.reserveme.model.Booking;

public class HotelViewBookingsAdaptor extends RecyclerView.Adapter<HotelViewBookingsAdaptor.MyViewHolder>{

    static String hEmail;
    static String hPrice;
    static Context context;
    ArrayList<Booking> hotelArrayList;

    public HotelViewBookingsAdaptor(Context context, ArrayList<Booking> hotelArrayList) {
        this.context = context;
        this.hotelArrayList = hotelArrayList;
    }


    @NonNull
    @Override
    public HotelViewBookingsAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.hotel_booking_view, parent, false);

        return new HotelViewBookingsAdaptor.MyViewHolder(v);


    }


    @Override
    public void onBindViewHolder(@NonNull HotelViewBookingsAdaptor.MyViewHolder holder, int position) {

//        Hotel hotel = hotelArrayList.get(position);
        Booking booking = hotelArrayList.get(position);
        holder.getTextViewVH1().setText(hotelArrayList.get(position).getUserEmail());
        holder.getTextViewVH2().setText(hotelArrayList.get(position).getCheckIn());
        holder.getTextViewVH3().setText(hotelArrayList.get(position).getCheckOut());
        holder.getTextViewVH4().setText(hotelArrayList.get(position).getGuests());
        holder.getTextViewVH5().setText(hotelArrayList.get(position).getRooms());
        holder.getTextViewVH6().setText(hotelArrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return hotelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewVH1;
        private final TextView textViewVH2;
        private final TextView textViewVH3;
        private final TextView textViewVH4;
        private final TextView textViewVH5;
        private final TextView textViewVH6;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewVH1 = itemView.findViewById(R.id.textView95);
            textViewVH2 = itemView.findViewById(R.id.textView96);
            textViewVH3 = itemView.findViewById(R.id.textView97);
            textViewVH4 = itemView.findViewById(R.id.textView98);
            textViewVH5 = itemView.findViewById(R.id.textView106);
            textViewVH6 = itemView.findViewById(R.id.textView107);
//            Toast.makeText(context, textViewVH1.getText().toString(), Toast.LENGTH_SHORT).show();


        }


        public TextView getTextViewVH1() {
            return textViewVH1;
        }

        public TextView getTextViewVH2() {
            return textViewVH2;
        }

        public TextView getTextViewVH3() {
            return textViewVH3;
        }

        public TextView getTextViewVH4() {
            return textViewVH4;
        }
        public TextView getTextViewVH5() {
            return textViewVH5;
        }
        public TextView getTextViewVH6() {
            return textViewVH6;
        }
    }

}
