package com.example.ck_cocaro_65130634;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);

        // THÊM: kiểm tra null (tránh crash nếu layout sai id)
        if (btnPlay == null) {
            Toast.makeText(this,
                    "Không tìm thấy nút PLAY!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        btnPlay.setOnClickListener(v -> {

            // THÊM: hiệu ứng click nhẹ (UX tốt hơn)
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(80)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(80)
                            .start())
                    .start();

            // THÊM: thông báo khi vào game (giống game thật)
            Toast.makeText(this,
                    "Bắt đầu Cyber XO!",
                    Toast.LENGTH_SHORT).show();

            // CHUYỂN MÀN HÌNH GAME
            Intent intent =
                    new Intent(MainActivity.this,
                            GameActivity.class);

            startActivity(intent);
        });
    }

    // THÊM: xử lý khi quay lại app (UI ổn định hơn)
    @Override
    protected void onResume() {
        super.onResume();

        // reset trạng thái nếu cần (không bắt buộc)
    }
}