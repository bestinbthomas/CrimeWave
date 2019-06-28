package com.example.crimewave;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;

import com.example.crimewave.Force.ForcesBasic;
import com.example.crimewave.Force.GetForceAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , OpenFragment{

    public final String TAG = getClass().getSimpleName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();

        new CommonApiAdapter().getLastUpdatedCall().enqueue(new Callback<lastUpdated>() {
            @Override
            public void onResponse(Call<lastUpdated> call, Response<lastUpdated> response) {
                Log.d(TAG, "onResponse: for last update");
                navigationView.getMenu().findItem(R.id.lastupdate).setTitle("Last Updated : "+response.body().date);
            }

            @Override
            public void onFailure(Call<lastUpdated> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            openSeeForces();
        }
    }

    private void openSeeForces(){
        ForceFragment forceFragment = new ForceFragment();
        forceFragment.setOpenFragment(MainActivity.this);
        fragmentManager.beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container,forceFragment)
                .commit();
        navigationView.setCheckedItem(R.id.forcepage);

    }
    private void openSearchCrimes(){

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setOpenFragment(MainActivity.this);
        fragmentManager.beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container,crimeFragment)
                .commit();
        navigationView.setCheckedItem(R.id.crimeatlocation);

    }
    private void openFavorites(){

        FavoriteCrimesFragment favoriteCrimesFragment = new FavoriteCrimesFragment();
        favoriteCrimesFragment.setOpenFragment(MainActivity.this);
        fragmentManager.beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container,favoriteCrimesFragment)
                .commit();
        navigationView.setCheckedItem(R.id.favorites);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.forcepage:
                openSeeForces();
                break;
            case R.id.crimeatlocation:
                openSearchCrimes();
                break;
            case R.id.favorites:
                openFavorites();
                break;
            case R.id.website:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.police.uk")));
                break;
        }
        drawerLayout.closeDrawer(Gravity.START,true);
        return true;
    }

    @Override
    public void LaunchFragment(int Fragref) {
        switch (Fragref){
            case SEE_FORCES:
                openSeeForces();
                break;
            case SEARCH_CRIMES:
                openSearchCrimes();
                break;
            case FAVORITE:
                openFavorites();
                break;
        }
    }
}
