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

    public static final int CPU_FRAGMENT = 0;
    public static final int IOONBOARD_FRAGMENT = 1;
    public static final int CARDS_FRAGMENT = 2;
    public static final int PROTOCOLS_FRAGMENT = 3;
    public static final int MANUFACTURERS_FRAGMENT = 4;

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

        int componentType = getIntent().getIntExtra("OpenFragment", CPU_FRAGMENT);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        setupDrawerContent((NavigationView) findViewById(R.id.nav_view));
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        switch (componentType){
            case CPU_FRAGMENT:
                fragment = new CpusFragment();
                break;
            case CARDS_FRAGMENT:
                fragment = new CardsFragment();
                break;
            case IOONBOARD_FRAGMENT:
                fragment = new IOOnboardFragment();
                break;
            case MANUFACTURERS_FRAGMENT:
                fragment = new ManufacturersFragment();
                break;
            case PROTOCOLS_FRAGMENT:
                fragment = new ProtocolsFragment();
                break;
            default:
                fragment = new CpusFragment();
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
