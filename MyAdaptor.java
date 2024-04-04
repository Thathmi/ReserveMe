package lk.jiat.reserveme;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.muddz.styleabletoast.StyleableToast;
import lk.jiat.reserveme.model.Hotel;

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.MyViewHolder> {

    //    private int selectedPos = RecyclerView.NO_POSITION;
    static String hEmail;
    static String hPrice;
    static String hemail;
    static String hemail1;
    static Context context;
    ArrayList<Hotel> hotelArrayList;

    public MyAdaptor(Context context, ArrayList<Hotel> hotelArrayList) {
        this.context = context;
        this.hotelArrayList = hotelArrayList;
    }


    @NonNull
    @Override
    public MyAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.hotel_view_layout, parent, false);

        return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull MyAdaptor.MyViewHolder holder, int position) {

        Hotel hotel = hotelArrayList.get(position);

        holder.getTextViewVH1().setText(hotelArrayList.get(position).getName());
        holder.getTextViewVH2().setText(hotelArrayList.get(position).getRate());
        holder.getTextViewVH4().setText(hotelArrayList.get(position).getEmail());
        holder.getTextViewVH3().setText(hotelArrayList.get(position).getPrice());
//        holder.itemView.setSelected(selectedPos == position);

        if (holder.imageViewVH != null) {
            hemail1 = hotel.getEmail().toString();

//            -----------------------------------------

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("hotel_images/" + hemail1 + "_image.jpg");

            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(holder.imageViewVH);
            }).addOnFailureListener(exception -> {
                // Handle any errors
                Log.e("Firebase", "Failed to fetch image: " + exception.getMessage());

            });

//            ----------------------------------------
        }
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
        private final ImageView imageViewVH;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewVH = itemView.findViewById(R.id.imageView20);
            textViewVH1 = itemView.findViewById(R.id.textView18);
            textViewVH2 = itemView.findViewById(R.id.textView19);
            textViewVH3 = itemView.findViewById(R.id.textView20);
            textViewVH4 = itemView.findViewById(R.id.textView21);

            textViewVH1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    hEmail = textViewVH1.getText().toString();
                    hPrice = textViewVH3.getText().toString();
                    hemail = textViewVH4.getText().toString();
//                    context.startActivity(new Intent(context, HotelSingleViewActivity.class));

                    // Check if hPrice is null or empty
                    if (hPrice != null && !hPrice.isEmpty()) {
                        context.startActivity(new Intent(context, HotelSingleViewActivity.class));
                    } else {
                        // Show a message or handle the case when hPrice is null
                        StyleableToast.makeText(context, "Cannot select this hotel", Toast.LENGTH_LONG, R.style.success).show();

                        // Disable click action if hPrice is null or empty
                        textViewVH1.setClickable(false);
                        textViewVH1.setFocusable(false);
                        textViewVH1.setEnabled(false);
                    }
                }
            });

        }

        public ImageView getImageViewVH() {
            return imageViewVH;
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
    }
}
