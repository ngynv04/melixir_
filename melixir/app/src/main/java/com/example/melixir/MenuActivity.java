    package com.example.melixir;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.MenuItem;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.ActionBarDrawerToggle;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.core.view.GravityCompat;
    import androidx.drawerlayout.widget.DrawerLayout;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;

    import com.example.melixir.fragment.BrandFragment;
    import com.example.melixir.fragment.HomeFragment;
    import com.example.melixir.fragment.InfoFragment;
    import com.example.melixir.fragment.ProductFragment;
    import com.google.android.material.navigation.NavigationView;

    public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
        DrawerLayout drawerLayout;
        private static final int FRAGMENT_HOME = 0;
        private static final int FRAGMENT_PRODUCT = 1;
        private static final int FRAGMENT_BRAND = 2;
        private static final int FRAGMENT_LOGIN = 3;
        private static final int FRAGMENT_INFO = 4;

        private int mCurrentFragment = FRAGMENT_HOME;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);

            drawerLayout = findViewById(R.id.drawerLayour);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.navigatonView);
            navigationView.setNavigationItemSelectedListener(this);

            // Mặc định hiển thị HomeFragment khi khởi động
            replaceFragment(new HomeFragment());
            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
       }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                if (mCurrentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = FRAGMENT_HOME;

//                    // Start HomeActivity
//                    startActivity(new Intent(MenuActivity.this, HomeActivity.class));
//                    // Finish MenuActivity to prevent going back to it when pressing back button
//                    finish();
                }
            } else if (id == R.id.nav_product) {
                if (mCurrentFragment != FRAGMENT_PRODUCT) {
                    replaceFragment(new ProductFragment());
                    mCurrentFragment = FRAGMENT_PRODUCT;
//                    // Start ProductActivity (Replace ProductActivity with your actual activity class)
//                    startActivity(new Intent(MenuActivity.this, ProductActivity.class));
//                    // Finish MenuActivity to prevent going back to it when pressing back button
//                    finish();
                }
            } else if (id == R.id.nav_brand) {
                if (mCurrentFragment != FRAGMENT_BRAND) {
                    replaceFragment(new BrandFragment());
                    mCurrentFragment = FRAGMENT_BRAND;

                }
            }  else if (id == R.id.nav_profile) {
                if (mCurrentFragment != FRAGMENT_INFO) {
                    replaceFragment(new InfoFragment());
                    mCurrentFragment = FRAGMENT_INFO;

                }
            }
         drawerLayout.closeDrawer(GravityCompat.START);
            // Chuyển sang HomeActivity khi chọn mục Home trong NavigationView
            if (id == R.id.nav_home) {
                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
            }

            return true;
        }

        @Override
        public void onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        private void replaceFragment(Fragment fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();
        }
    }