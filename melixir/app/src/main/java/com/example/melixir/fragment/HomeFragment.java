package com.example.melixir.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.melixir.R;

public class HomeFragment extends Fragment {
    ViewFlipper viewFlipper; // Khai báo ViewFlipper nếu cần

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout của fragment_home
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các thành phần giao diện và thực hiện các thao tác cần thiết
        // Ví dụ: viewFlipper = view.findViewById(R.id.viewFlipper);

        // Thực hiện các thao tác khởi tạo hoặc hiển thị dữ liệu



        return view;
    }
}
