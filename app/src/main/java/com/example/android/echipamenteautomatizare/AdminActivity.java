package com.example.android.echipamenteautomatizare;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.echipamenteautomatizare.Fragments.CardsFragment;
import com.example.android.echipamenteautomatizare.Fragments.CpusFragment;
import com.example.android.echipamenteautomatizare.Fragments.IOOnboardFragment;
import com.example.android.echipamenteautomatizare.Fragments.ManufacturersFragment;
import com.example.android.echipamenteautomatizare.Fragments.ProtocolsFragment;

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setTitle("Echipamente Automatizare");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        setupDrawerContent((NavigationView) findViewById(R.id.nav_view));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new CpusFragment()).commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_cpus:
                fragmentClass = CpusFragment.class;
                break;
            case R.id.nav_io_onboard:
                fragmentClass = IOOnboardFragment.class;
                break;
            case R.id.nav_cards:
                fragmentClass = CardsFragment.class;
                break;
            case R.id.nav_protocols:
                fragmentClass = ProtocolsFragment.class;
                break;
            case R.id.nav_manufacturers:
                fragmentClass = ManufacturersFragment.class;
                break;
            default:
                fragmentClass = CpusFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
