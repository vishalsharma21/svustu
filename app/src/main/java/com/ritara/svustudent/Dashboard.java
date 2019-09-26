package com.ritara.svustudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.ritara.svustudent.fragments.BooksFragment;
import com.ritara.svustudent.fragments.CampusFragment;
import com.ritara.svustudent.fragments.InternsFragment;
import com.ritara.svustudent.fragments.JobsFragment;
import com.ritara.svustudent.fragments.NewsFragment;
import com.ritara.svustudent.fragments.PrepsFragment;
import com.ritara.svustudent.fragments.SkillsFragment;
import com.ritara.svustudent.fragments.VidsFragment;
import com.ritara.svustudent.ui.home.HomeFragment;
import com.ritara.svustudent.ui.profile.ProfileFragment;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

public class Dashboard extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences_SVU sharedPreferences_svu;
    private TabLayout.Tab tablayout;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private TextView txtToolHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(this);
        sharedPreferences_svu.setTrainingDone(true);
        txtToolHeader = (TextView) toolbar.findViewById(R.id.txtToolHeader);


        ImageView imgToolRight = (ImageView) toolbar.findViewById(R.id.imgToolRight);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        TextView txtHeaderNav = navigationView.getHeaderView(0).findViewById(R.id.textHeaderName);
        txtHeaderNav.setText(sharedPreferences_svu.get_Username());

        NavigationView navigationViewRight = findViewById(R.id.nav_view2);
        navigationViewRight.setItemIconTintList(null);
        View headerview = navigationViewRight.getHeaderView(0);

        headerview.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new HomeFragment() , "Home");

            }
        });

        headerview.findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new HomeFragment() , "Home");

            }
        });

        headerview.findViewById(R.id.campus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new CampusFragment() , "Campus");
            }
        });

        headerview.findViewById(R.id.books).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new BooksFragment() , "Books");
            }
        });

        headerview.findViewById(R.id.videos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new VidsFragment() , "Videos");
            }
        });

        headerview.findViewById(R.id.skills).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new SkillsFragment() , "Skills");
            }
        });

        headerview.findViewById(R.id.preps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new PrepsFragment() , "Preps");

            }
        });

        headerview.findViewById(R.id.interns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new InternsFragment() , "Interns");

            }
        });

        headerview.findViewById(R.id.jobs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new JobsFragment() , "Jobs");

            }
        });

        headerview.findViewById(R.id.news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new NewsFragment() , "News");

            }
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_books, R.id.nav_video,
                R.id.nav_test, R.id.nav_cal, R.id.nav_my_orders,
                R.id.account,R.id.nav_shoping_card,R.id.help,
                R.id.notifications)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        bottomNav.setOnClickListener(null);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getTitle().toString()){

                    case "Home" :
                        changeFragment(new HomeFragment() , "Home");
                        break;
                    case "Library":
                        changeFragment(new HomeFragment() , "Home");
                        break;
                    case "Profile":
                        changeFragment(new ProfileFragment() , "Profile");
                        break;
                    case "Notifications":
                        startActivity(new Intent(Dashboard.this, Payments.class));
                        break;
                    case "Experts":
                        changeFragment(new CircularView() , "Experts");

                        default:
                            break;

                }

                return true;
            }
        });

        final DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawer_layout);

        imgToolRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer1.isDrawerOpen(GravityCompat.END)) {
                    drawer1.closeDrawer(GravityCompat.END);
                } else {
                    drawer1.openDrawer(GravityCompat.END);
                }
            }
        });

        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view2);
        navigationView2.setNavigationItemSelectedListener(Dashboard.this);

        changeFragment(new HomeFragment() , "Home");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void changeFragment(Fragment fragment, String title) {
        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment, "" + title).commit();
        transaction.addToBackStack(title);
        toolbar.setTitle(title);
        txtToolHeader.setText(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        String text = "home";
        if (id == R.id.nav_books) {
            text = "gallery";
        } else if (id == R.id.nav_video) {
            text = "slideshow";
        }else if (id == R.id.nav_cal) {
            text = "share";
        } else if (id == R.id.nav_my_orders) {
            text = "send";
            startActivity(new Intent(Dashboard.this, Login.class));
            finish();
        } else if (id == R.id.nav_profile) {
            text = "home";
//            startActivity(new Intent(Dashboard.this, CircularView.class));
        }

        Toast.makeText(this, "You have chosen " + text, Toast.LENGTH_LONG).show();



        return true;
    }
}
