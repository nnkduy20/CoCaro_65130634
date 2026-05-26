package com.example.ck_cocaro_65130634;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    GridLayout gridLayout;

    Button[][] buttons = new Button[20][20];

    boolean playerX = true;
    boolean gameOver = false;

    int gameMode = 0;

    ArrayList<String> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gridLayout = findViewById(R.id.gridLayout);

        createBoard();

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetGame());

        Button btnPVP = findViewById(R.id.btnPVP);
        Button btnPVE = findViewById(R.id.btnPVE);
        Button btnHistory = findViewById(R.id.btnHistory);

        btnPVP.setOnClickListener(v -> {
            gameMode = 1;
            resetGame();
            Toast.makeText(this, "PvP Mode", Toast.LENGTH_SHORT).show();
        });

        btnPVE.setOnClickListener(v -> {
            gameMode = 2;
            resetGame();
            Toast.makeText(this, "PvE Mode", Toast.LENGTH_SHORT).show();
        });

        // 🔥 HISTORY POPUP
        btnHistory.setOnClickListener(v -> {
            showHistoryDialog();
        });
    }

    // =========================
    // BOARD
    // =========================
    private void createBoard() {

        gridLayout.removeAllViews();

        gridLayout.post(() -> {

            int w = gridLayout.getWidth();
            int h = gridLayout.getHeight();

            int cell = Math.min(w, h) / 20;

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {

                    Button btn = new Button(this);

                    GridLayout.LayoutParams params =
                            new GridLayout.LayoutParams(
                                    GridLayout.spec(i, 1f),
                                    GridLayout.spec(j, 1f)
                            );

                    params.width = cell;
                    params.height = cell;

                    btn.setLayoutParams(params);

                    btn.setBackgroundResource(R.drawable.cell_border);

                    btn.setText("");
                    btn.setPadding(0, 0, 0, 0);
                    btn.setGravity(Gravity.CENTER);

                    final int row = i;
                    final int col = j;

                    btn.setOnClickListener(v -> {

                        if (gameOver) return;
                        if (!btn.getText().toString().isEmpty()) return;
                        if (gameMode == 0) {
                            Toast.makeText(this, "Chọn chế độ!", Toast.LENGTH_SHORT).show();
                            return;
                        }

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
                            history.add("Winner: " + winner);

                            showWinDialog(winner);
                            return;
                        }

                        playerX = !playerX;

                        if (gameMode == 2 && !playerX && !gameOver) {
                            aiMove();
                        }
                    });

                    buttons[i][j] = btn;
                    gridLayout.addView(btn);
                }
            }
        });
    }

    // =========================
    // AI (GIỮ NGUYÊN)
    // =========================
    private void aiMove() {

        if (gameOver) return;

        int bestScore = -1;
        int bestR = -1;
        int bestC = -1;

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {

                if (buttons[i][j].getText().toString().isEmpty()) {

                    int score = evaluate(i, j, "X") * 10 +
                            evaluate(i, j, "O");

                    if (score > bestScore) {
                        bestScore = score;
                        bestR = i;
                        bestC = j;
                    }
                }
            }
        }

        if (bestR != -1) {

            buttons[bestR][bestC].setText("O");
            buttons[bestR][bestC].setTextColor(Color.RED);

            if (checkWin(bestR, bestC)) {
                gameOver = true;
                history.add("Winner: O");

                showWinDialog("O");
            }

            playerX = true;
        }
    }

    // =========================
    // WIN POPUP
    // =========================
    private void showWinDialog(String winner) {

        Dialog dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.dialog_win);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        android.widget.TextView tvWinner = dialog.findViewById(R.id.tvWinner);
        Button btnReplay = dialog.findViewById(R.id.btnReplay);

        tvWinner.setText("🔥 WINNER: " + winner);

        btnReplay.setOnClickListener(v -> {
            resetGame();
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    // =========================
    // ⭐ HISTORY POPUP (THÊM MỚI)
    // =========================
    private void showHistoryDialog() {

        Dialog dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.dialog_history);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        android.widget.TextView tvHistory = dialog.findViewById(R.id.tvHistory);
        Button btnClose = dialog.findViewById(R.id.btnCloseHistory);

        StringBuilder sb = new StringBuilder();

        if (history.isEmpty()) {
            sb.append("📜 CHƯA CÓ VÁN NÀO");
        } else {
            sb.append("📜 LỊCH SỬ GAME\n\n");

            for (int i = 0; i < history.size(); i++) {
                sb.append("▶ ").append(i + 1)
                        .append(": ")
                        .append(history.get(i))
                        .append("\n");
            }
        }

        tvHistory.setText(sb.toString());

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);
        dialog.show();
    }

    // =========================
    // SCORE + WIN CHECK (GIỮ NGUYÊN)
    // =========================
    private int evaluate(int r, int c, String p) {
        return count(r, c, 1, 0, p) +
                count(r, c, 0, 1, p) +
                count(r, c, 1, 1, p) +
                count(r, c, 1, -1, p);
    }

    private int count(int r, int c, int dx, int dy, String p) {

        int count = 0;
        int i = r + dx;
        int j = c + dy;

        while (i >= 0 && i < 20 &&
                j >= 0 && j < 20 &&
                p.equals(buttons[i][j].getText().toString())) {

            count++;
            i += dx;
            j += dy;
        }

        return count;
    }

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

    private void resetGame() {

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setTextColor(Color.BLACK);
            }
        }

        playerX = true;
        gameOver = false;
    }
}