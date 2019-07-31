package com.example.sampleapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener,RecyclerViewAdapter.SearchFragmentListener, RecyclerViewFragment.RecyclerFragmentListener {
    static private final int REQUEST_IMAGE_CAPTURE = 101;
//    private AnalyzedWordFragment analyzedWordFragment;
    private RecyclerViewFragment recyclerViewFragment;
    private SearchFragment searchFragment;

    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //analyzedWordFragment = new AnalyzedWordFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_camera:
                            selectedFragment = new CameraFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        default:
                            selectedFragment = new HomeFragment();
                    }
                    manager.beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };
    public void retakePhoto(View v) {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onWordsSent(ArrayList inputArray) {
        recyclerViewFragment = new RecyclerViewFragment();
        manager.beginTransaction().replace(R.id.fragment_container,recyclerViewFragment).addToBackStack(null).commit();
        recyclerViewFragment.initIngredientList(inputArray);
    }

    @Override
    public void searchIngredient(String s) {
        searchFragment = new SearchFragment();
        manager.beginTransaction().replace(R.id.fragment_container,searchFragment).addToBackStack(null).commit();
        searchFragment.searchIngredient(s);
    }


    @Override
    public void onTextsSentForEdit(ArrayList inputArray) {
        EditFragment editFragment = new EditFragment();
        manager.beginTransaction().replace(R.id.fragment_container,editFragment).addToBackStack(null).commit();
        editFragment.getIngredientList(inputArray);
    }
}
