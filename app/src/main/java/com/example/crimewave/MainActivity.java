package com.example.crimewave;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.crimewave.Force.ForcesBasic;
import com.example.crimewave.Force.GetForceAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public final String TAG = getClass().getSimpleName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ForceFragment()).commit();
            navigationView.setCheckedItem(R.id.forcepage);
        }
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ForceFragment()).commit();
                break;
            case R.id.crimeatlocation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CrimeFragment()).commit();
                break;
            case R.id.website:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.police.uk")));
                break;
        }
        drawerLayout.closeDrawer(Gravity.START,true);
        return true;
    }
}
