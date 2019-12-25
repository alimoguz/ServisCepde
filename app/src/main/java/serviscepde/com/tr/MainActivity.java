package serviscepde.com.tr;

import android.app.Activity;
import androidx.annotation.NonNull;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import serviscepde.com.tr.Fragment.AddFragment;
import serviscepde.com.tr.Fragment.HomeFragment;
import serviscepde.com.tr.Fragment.NotificationFragment;
import serviscepde.com.tr.Fragment.ProfileFragment;
import serviscepde.com.tr.Fragment.SearchFragment;
import serviscepde.com.tr.Fragment.İletisimFragment;

public class MainActivity extends AppCompatActivity {

    public static  RelativeLayout relHeader;
    FrameLayout fragMain;
    public static BottomNavigationView bottomNav;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private AddFragment addFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;
    private İletisimFragment iletisimFragment;
    public static Activity act;

    private ImageView imgIconIletisim;

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;

    @Override
    public void onBackPressed() {

        FragmentManager manager = getSupportFragmentManager();

        if(manager.getBackStackEntryCount() > 1)
        {
            manager.popBackStackImmediate();
        }
        else
        {
            Bundle quit = new Bundle();
            quit.putString("quit" , "Yes");
            Intent quitIntent = new Intent(this , SplashActivity.class);
            quitIntent.putExtras(quit);
            startActivity(quitIntent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;

        hideSoftKeyboard();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragMain = findViewById(R.id.fragMain);
        bottomNav = findViewById(R.id.bottomNav);
        relHeader = findViewById(R.id.relHeader);
        imgIconIletisim = findViewById(R.id.imgIconIletisim);

        relHeader.setVisibility(View.VISIBLE);
        bottomNav.setVisibility(View.VISIBLE);

        loadFragment(new HomeFragment());

        iletisimFragment = new İletisimFragment();

        imgIconIletisim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(iletisimFragment);
            }
        });

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

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }



}
