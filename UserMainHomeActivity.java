package lk.jiat.reserveme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class UserMainHomeActivity extends AppCompatActivity {
    AnimatedBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_home);

        bottomBar = findViewById(R.id.bottom_bar1);

//        set default fragment
        replace(new UserBookingsFragment());
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {

                if (tab1.getId() == R.id.uhome) {
                    Intent i2 = new Intent(UserMainHomeActivity.this, UserHomeActivity.class);
                    startActivity(i2);
                } else if (tab1.getId() == R.id.ubooking) {
                    replace(new UserBookingsFragment());
                }else if (tab1.getId() == R.id.profile) {
                    replace(new UserProfileFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout2, fragment);
        transaction.commit();
    }

}