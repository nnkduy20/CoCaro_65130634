package com.example.ck_cocaro_65130634;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    GridLayout gridLayout;

    // ✔ ĐỔI 20x20
    Button[][] buttons = new Button[20][20];

    boolean playerX = true;
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gridLayout = findViewById(R.id.gridLayout);

        createBoard();

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetGame());
    }

    private void createBoard() {

        gridLayout.removeAllViews();

        gridLayout.post(() -> {

            int w = gridLayout.getWidth();
            int h = gridLayout.getHeight();

            int size = Math.min(w, h);
            int cell = size / 20; // ✔ CHIA 20x20

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {

                    Button btn = new Button(this);

                    GridLayout.LayoutParams params =
                            new GridLayout.LayoutParams();

                    params.width = cell;
                    params.height = cell;

                    btn.setLayoutParams(params);

                    btn.setBackgroundResource(R.drawable.cell_border);

                    btn.setText("");
                    btn.setTextSize(16f);
                    btn.setGravity(Gravity.CENTER);
                    btn.setPadding(0, 0, 0, 0);

                    final int row = i;
                    final int col = j;

                    btn.setOnClickListener(v -> {

                        if (gameOver) return;

                        if (btn.getText().toString().isEmpty()) {

                            if (playerX) {
                                btn.setText("X");
                                btn.setTextColor(Color.BLUE);
                            } else {
                                btn.setText("O");
                                btn.setTextColor(Color.RED);
                            }

                            if (checkWin(row, col)) {
                                gameOver = true;
                            }

                            playerX = !playerX;
                        }
                    });

                    buttons[i][j] = btn;
                    gridLayout.addView(btn);
                }
            }
        });
    }

    // =========================
    // CHECK WIN (20x20 FIX)
    // =========================
    private boolean checkWin(int row, int col) {

        String current = buttons[row][col].getText().toString();

        return checkDir(row, col, 1, 0, current) ||
                checkDir(row, col, 0, 1, current) ||
                checkDir(row, col, 1, 1, current) ||
                checkDir(row, col, 1, -1, current);
    }

    private boolean checkDir(int row, int col,
                             int dx, int dy,
                             String current) {

        int count = 1;

        int r = row + dx;
        int c = col + dy;

        while (r >= 0 && r < 20 &&
                c >= 0 && c < 20 &&
                buttons[r][c].getText().toString().equals(current)) {
            count++;
            r += dx;
            c += dy;
        }

        r = row - dx;
        c = col - dy;

        while (r >= 0 && r < 20 &&
                c >= 0 && c < 20 &&
                buttons[r][c].getText().toString().equals(current)) {
            count++;
            r -= dx;
            c -= dy;
        }

        return count >= 5;
    }

    // =========================
    // RESET
    // =========================
    private void resetGame() {

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                buttons[i][j].setText("");
            }
        }

        playerX = true;
        gameOver = false;
    }
}