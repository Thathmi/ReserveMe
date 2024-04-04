package lk.jiat.reserveme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.jiat.reserveme.model.Booking;

public class BokingAdaptor extends RecyclerView.Adapter<BokingAdaptor.MyViewHolder>{
    //    private int selectedPos = RecyclerView.NO_POSITION;
    static String hEmail;
    static String hPrice;
    static Context context;
    ArrayList<Booking> hotelArrayList;

    public BokingAdaptor(Context context, ArrayList<Booking> hotelArrayList) {
        this.context = context;
        this.hotelArrayList = hotelArrayList;
    }


    @NonNull
    @Override
    public BokingAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.booking_list_layout, parent, false);

        return new BokingAdaptor.MyViewHolder(v);


    }


    @Override
    public void onBindViewHolder(@NonNull BokingAdaptor.MyViewHolder holder, int position) {

//        Hotel hotel = hotelArrayList.get(position);
Booking booking = hotelArrayList.get(position);
        holder.getTextViewVH1().setText(hotelArrayList.get(position).getHotelName());
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

            textViewVH1 = itemView.findViewById(R.id.textView100);
            textViewVH2 = itemView.findViewById(R.id.textView101);
            textViewVH3 = itemView.findViewById(R.id.textView102);
            textViewVH4 = itemView.findViewById(R.id.textView103);
            textViewVH5 = itemView.findViewById(R.id.textView104);
            textViewVH6 = itemView.findViewById(R.id.textView105);
//            Toast.makeText(context, textViewVH1.getText().toString(), Toast.LENGTH_SHORT).show();

            textViewVH1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, HotelSingleViewActivity.class));

                }
            });
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
