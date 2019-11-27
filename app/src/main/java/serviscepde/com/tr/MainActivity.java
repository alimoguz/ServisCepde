package serviscepde.com.tr;

import android.app.Activity;
import androidx.annotation.NonNull;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import serviscepde.com.tr.Fragment.AddFragment;
import serviscepde.com.tr.Fragment.HomeFragment;
import serviscepde.com.tr.Fragment.NotificationFragment;
import serviscepde.com.tr.Fragment.ProfileFragment;
import serviscepde.com.tr.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    public static  RelativeLayout relHeader;
    FrameLayout fragMain;
    public static BottomNavigationView bottomNav;

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    AddFragment addFragment;
    NotificationFragment notificationFragment;
    ProfileFragment profileFragment;
    public static Activity act;

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragMain = findViewById(R.id.fragMain);
        bottomNav = findViewById(R.id.bottomNav);
        relHeader = findViewById(R.id.relHeader);

        relHeader.setVisibility(View.VISIBLE);
        bottomNav.setVisibility(View.VISIBLE);

        loadFragment(new HomeFragment());

        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {


                case R.id.nav_home:

                    homeFragment = new HomeFragment();
                    loadFragment(homeFragment);
                    return true;

                case R.id.nav_search:


                    searchFragment = new SearchFragment();
                    loadFragment(searchFragment);
                    return true;

                case R.id.nav_add:

                    addFragment = new AddFragment();
                    loadFragment(addFragment);
                    return true;

                case R.id.nav_notification:

                    notificationFragment = new NotificationFragment();
                    loadFragment(notificationFragment);
                    return true;

                case R.id.nav_user:

                    profileFragment = new ProfileFragment();
                    loadFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {      //fragmentlarımızı çağırdığımız fonksiyon

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragMain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}
