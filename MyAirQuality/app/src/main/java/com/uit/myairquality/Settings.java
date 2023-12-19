package com.uit.myairquality;



import static android.content.Intent.getIntent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class Settings extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        frameLayout = findViewById(R.id.frame);

        bottomNavigationView = findViewById(R.id.Bottom_Nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID=item.getItemId();
                if(itemID==R.id.nav_home){
                    Intent intent = getIntent();
                    loadFragment(new HomeFragment(), false);
                }
                else if (itemID==R.id.nav_dashboard){
                    loadFragment(new DashboardFragment(), false);
                }
                else if (itemID==R.id.nav_settings){
                    loadFragment(new SettingsFragment(), false);
                }
                else {
                    loadFragment(new ProfileFragment(), false);
                }


                return true;
            }
        });
        loadFragment(new HomeFragment(), true);

    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialized){
            fragmentTransaction.add(R.id.frame,fragment);
        } else {
            fragmentTransaction.replace(R.id.frame,fragment);
        }

        fragmentTransaction.commit();
    }
//
//        drawerLayout = findViewById(R.id.drawerLayout);
//        menu = findViewById(R.id.menu);
//        home = findViewById(R.id.home);
//        share = findViewById(R.id.share);
//        settings = findViewById(R.id.settings);
//        exit = findViewById(R.id.exit);
//        info = findViewById(R.id.info);
//
//
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDrawer(drawerLayout);
//
//            }
//        });
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(Settings.this, navigation.class);
//
//            }
//        });
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recreate();
//            }
//        });
//
//        /*ishare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(Settings.this, ShareActivity.class);
//            }
//        });
//        info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(SettingsActivity.this, AboutActivity.class);
//            }
//        });
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(SettingsActivity.this,"Logout", Toast.LENGTH_SHORT).show();
//            }
//        });*/
//    }
//    public static void  openDrawer (DrawerLayout drawerLayout){
//        drawerLayout.openDrawer(GravityCompat.START);
//
//    }
//    public static void closeDrawer (DrawerLayout drawerLayout){
//        drawerLayout.isDrawerOpen(GravityCompat.START);
//        drawerLayout.closeDrawer(GravityCompat.START);
//    }
//
//    public static void  redirectActivity (Activity activity, Class secondActivity){
//        Intent intent = new Intent(activity,secondActivity);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
//        activity.finish();
//    }
//    protected void onPause(){
//        super.onPause();
//        closeDrawer(drawerLayout);
//    }
}