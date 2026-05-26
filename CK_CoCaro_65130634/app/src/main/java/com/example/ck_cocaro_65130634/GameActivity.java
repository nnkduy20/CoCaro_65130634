package com.example.ck_cocaro_65130634;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    GridLayout gridLayout;

    Button[][] buttons = new Button[15][15];

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

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {

                Button btn = new Button(this);

                GridLayout.LayoutParams params =
                        new GridLayout.LayoutParams(
                                GridLayout.spec(i, 1f),
                                GridLayout.spec(j, 1f)
                        );

                params.width = 0;
                params.height = 0;

                btn.setLayoutParams(params);

                // ✔ BORDER Ô
                btn.setBackgroundResource(R.drawable.cell_border);

                // ✔ STYLE CHUẨN
                btn.setText("");
                btn.setTextSize(20f);
                btn.setGravity(android.view.Gravity.CENTER);
                btn.setAllCaps(false);

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

                            String winner = playerX ? "X" : "O";

                            Toast.makeText(this,
                                    winner + " thắng!",
                                    Toast.LENGTH_LONG).show();

                            return;
                        }

                        playerX = !playerX;
                    }
                });

                buttons[i][j] = btn;
                gridLayout.addView(btn);
            }
        }
    }

    // =========================
    // CHECK WIN
    // =========================
    private boolean checkWin(int row, int col) {

        String current = buttons[row][col].getText().toString();

        return checkDir(row, col, 1, 0, current) ||   // dọc
                checkDir(row, col, 0, 1, current) ||   // ngang
                checkDir(row, col, 1, 1, current) ||   // chéo \
                checkDir(row, col, 1, -1, current);    // chéo /
    }

    private boolean checkDir(int row, int col,
                             int dx, int dy,
                             String current) {

        int count = 1;

        int r = row + dx;
        int c = col + dy;

        while (r >= 0 && r < 15 &&
                c >= 0 && c < 15 &&
                buttons[r][c].getText().toString().equals(current)) {

            count++;
            r += dx;
            c += dy;
        }

        r = row - dx;
        c = col - dy;

        while (r >= 0 && r < 15 &&
                c >= 0 && c < 15 &&
                buttons[r][c].getText().toString().equals(current)) {

            count++;
            r -= dx;
            c -= dy;
        }

        return count >= 5;
    }

    // =========================
    // RESET GAME
    // =========================
    public void resetGame() {

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {

                buttons[i][j].setText("");
                buttons[i][j].setTextColor(Color.BLACK);
            }
        }

        playerX = true;
        gameOver = false;
    }
}