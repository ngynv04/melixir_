    package com.example.melixir;
    import androidx.appcompat.app.AlertDialog;
    import androidx.fragment.app.Fragment;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.MenuItem;
    import android.view.View;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.ActionBarDrawerToggle;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.core.view.GravityCompat;
    import androidx.drawerlayout.widget.DrawerLayout;
    import androidx.fragment.app.FragmentTransaction;

    import com.example.melixir.fragment.BrandFragment;
    import com.example.melixir.fragment.HomeFragment;
    import com.example.melixir.fragment.InfoFragment;
    import com.example.melixir.fragment.LoginFragment;
    import com.example.melixir.fragment.RegisterFragment;
    import com.example.melixir.utils.Utils;
    import com.google.android.material.navigation.NavigationView;

    import java.util.ArrayList;

    public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
        DrawerLayout drawerLayout;
        private static final int FRAGMENT_HOME = 0;
        private static final int FRAGMENT_PRODUCT = 1;
        private static final int FRAGMENT_BRAND = 2;
        private static final int FRAGMENT_LOGIN = 3;
        private static final int ACTIVITY_THONGTIN = 4;

        private int mCurrentFragment = FRAGMENT_HOME;
        SharedPreferences sp;
        SharedPreferences.Editor editor;


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

            //CHECK GIOR HANGF
            if (Utils.manggiohang == null) {
                Utils.manggiohang = new ArrayList<>();
            }
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
//                    replaceFragment(new ProductFragment());
//                    mCurrentFragment = FRAGMENT_PRODUCT;
                    // Start ProductActivity (Replace ProductActivity with your actual activity class)
                    startActivity(new Intent(MenuActivity.this, ProductActivity.class));
                    // Finish MenuActivity to prevent going back to it when pressing back button
                    finish();
                }
            } else if (id == R.id.nav_brand) {
                if (mCurrentFragment != FRAGMENT_BRAND) {
                    replaceFragment(new BrandFragment());
                    mCurrentFragment = FRAGMENT_BRAND;

                }
            }  else if (id == R.id.nav_profile) {
                if (mCurrentFragment != ACTIVITY_THONGTIN) {
                    startActivity(new Intent(MenuActivity.this, ThongtinActivity.class));
                    finish();
                }

            }

//         drawerLayout.closeDrawer(GravityCompat.START);
//            // Chuyển sang HomeActivity khi chọn mục Home trong NavigationView
//            if (id == R.id.nav_home) {
//                startActivity(new Intent(MenuActivity.this, HomeActivity.class));
//            }

         drawerLayout.closeDrawer(GravityCompat.START);

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

        public void logout(MenuItem item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc muốn đăng xuất tài khoản?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    replaceFragment(new LoginFragment());
                    SharedPreferences preferences = getSharedPreferences("userFile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    finish();
                    replaceFragment(new LoginFragment());

                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }