package com.example.finalproject_texi_milan;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Bottom_nav extends AppCompatActivity {

    BottomNavigationView bnv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        bnv = findViewById(R.id.bottom_navigation);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.nav_home){

                    LoadFrag(new home_f(),false);
                } else if (id==R.id.nav_map) {
                    LoadFrag(new map_f(),false);
                }
                else if(id==R.id.nav_cab){
                    LoadFrag(new payment_f(),false);
                }
                else if(id==R.id.nav_profile){
                    LoadFrag(new profile_f(),false);
                }
                else{
                    LoadFrag(new home_f(),true);
                }
                return true;
            }
        });

        bnv.setSelectedItemId(R.id.nav_home);
    }

    public void LoadFrag(Fragment fragment,boolean flag)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(flag)
            ft.add(R.id.frame_container,fragment);
        else
            ft.replace(R.id.frame_container,fragment);

        ft.commit();
    }

}