    package com.example.melixir;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;

    import com.example.melixir.fragment.BrandFragment;
    import com.example.melixir.fragment.ForgotPassFragment;
    import com.example.melixir.fragment.InfoFragment;
    import com.example.melixir.fragment.LoginFragment;
    import com.example.melixir.fragment.RegisterFragment;

    public class MainActivity extends AppCompatActivity implements CallBackFragment {
        FragmentManager fragmentManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Hiển thị fragment ProductFragment khi Activity được tạo
            loadFragment(new LoginFragment());
        }


        private void loadFragment(Fragment fmNew) {
            FragmentTransaction fmTran = getSupportFragmentManager().beginTransaction();
            fmTran.replace(R.id.fragmentContainer, fmNew);
            fmTran.addToBackStack(null);
            fmTran.commit();
        }

        // Callback khi người dùng click vào nút đăng ký

        @Override
        public void onRegisterClicked() {
            loadFragment(new RegisterFragment());
        }

        // Callback khi người dùng click vào nút đăng nhập
        public void onLoginClicked() {
            loadFragment(new LoginFragment());
        }

        public void onResetPass() {
            loadFragment(new ForgotPassFragment());
        }

        public void onInfoClicked() {
            loadFragment(new InfoFragment());
        }

        public void onLoginSuccess() {// Chuyển từ MainActivity sang MenuActivity khi đăng nhập thành công
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish(); // Optional: finish MainActivity to prevent going back to it when pressing back button}

        }
        public void onBuy(){
            Intent intent=new Intent(this, PaymentActivity.class);
            startActivity(intent);
        }
    }


