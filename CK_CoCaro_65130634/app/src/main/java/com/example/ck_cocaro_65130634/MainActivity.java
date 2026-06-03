package com.example.ck_cocaro_65130634;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // ✔ CHECK AN TOÀN
        if (btnPlay == null) return;

        btnPlay.setOnClickListener(v -> {

            // hiệu ứng giống game
            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(80)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(80)
                            .start())
                    .start();

            // thông báo vào game
            Toast.makeText(this,
                    "Đang vào trận đấu...",
                    Toast.LENGTH_SHORT).show();

            //  delay nhẹ giống game thật
            v.postDelayed(() -> {

                Intent intent = new Intent(
                        MainActivity.this,
                        GameActivity.class
                );

                startActivity(intent);

                // chuyển màn mượt hơn
                overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );

            }, 200);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // reset UI nếu cần
        if (btnPlay != null) {
            btnPlay.setScaleX(1f);
            btnPlay.setScaleY(1f);
        }
    }
}