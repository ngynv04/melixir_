package com.example.melixir;

// Giao diện CallBackFragment định nghĩa các phương thức để xử lý sự kiện click từ Fragment
public interface CallBackFragment {
    void onRegisterClicked();

    void onLoginClicked();

    void onLoginSuccess();
    void onResetPass();
    void onBuy();
}
