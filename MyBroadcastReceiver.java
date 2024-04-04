package lk.jiat.reserveme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.example.NOTIFICATION_ACTION")) {
            String message = intent.getStringExtra("message");
            Toast.makeText(context, "Received Broadcast: " + message, Toast.LENGTH_SHORT).show();
            Log.e("MyBroadcastReceiver", "Received Broadcast: " + message);
        }
    }
}

