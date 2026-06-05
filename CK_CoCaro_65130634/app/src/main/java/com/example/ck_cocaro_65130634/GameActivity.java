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

import androidx.appcompat.app.AlertDialog;

public class GameActivity extends AppCompatActivity {

    GridLayout gridLayout;

    Button[][] buttons = new Button[20][20];

    boolean playerX = true;
    boolean gameOver = false;

    int gameMode = 0;
    int aiLevel = 2;

    ArrayList<String> history = new ArrayList<>();
    String player1Name = "Player X";
    String player2Name = "Player O";

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
            showPlayerNameDialog();
        });

        btnPVE.setOnClickListener(v -> {
            showDifficultyDialog();
        });

        //  HISTORY POPUP
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

                            String winner;

                            if (gameMode == 2) {
                                winner = playerX ? "NGƯỜI CHƠI" : "MÁY";
                            } else {
                                winner = playerX ? player1Name : player2Name;
                            }

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
    private void showPlayerNameDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_player_name);
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent);

            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        android.widget.EditText edtPlayer1 =
                dialog.findViewById(R.id.edtPlayer1);

        android.widget.EditText edtPlayer2 =
                dialog.findViewById(R.id.edtPlayer2);

        Button btnStart =
                dialog.findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {

            String p1 =
                    edtPlayer1.getText().toString().trim();

            String p2 =
                    edtPlayer2.getText().toString().trim();

            if (!p1.isEmpty()) {
                player1Name = p1;
            }

            if (!p2.isEmpty()) {
                player2Name = p2;
            }

            gameMode = 1;
            resetGame();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void showDifficultyDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_difficulty);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        Button btnEasy = dialog.findViewById(R.id.btnEasy);
        Button btnMedium = dialog.findViewById(R.id.btnMedium);
        Button btnHard = dialog.findViewById(R.id.btnHard);

        btnEasy.setOnClickListener(v -> {

            aiLevel = 1;
            gameMode = 2;
            resetGame();

            Toast.makeText(
                    this,
                    "MÁY DỄ",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        btnMedium.setOnClickListener(v -> {

            aiLevel = 2;
            gameMode = 2;
            resetGame();

            Toast.makeText(
                    this,
                    "MÁY TRUNG BÌNH",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        btnHard.setOnClickListener(v -> {

            aiLevel = 3;
            gameMode = 2;
            resetGame();

            Toast.makeText(
                    this,
                    "MÁY KHÓ",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        dialog.show();
    }
    private int scoreLine(int r, int c,
                          int dx, int dy,
                          String p) {

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

        i = r - dx;
        j = c - dy;

        while (i >= 0 && i < 20 &&
                j >= 0 && j < 20 &&
                p.equals(buttons[i][j].getText().toString())) {

            count++;
            i -= dx;
            j -= dy;
        }

        switch (count) {
            case 4: return 100000;
            case 3: return 10000;
            case 2: return 1000;
            case 1: return 100;
            default: return 10;
        }
    }
    private void aiMove() {

        if (gameOver) return;

        // ===== DỄ =====
        if (aiLevel == 1) {

            ArrayList<int[]> emptyCells = new ArrayList<>();

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {

                    if (buttons[i][j].getText().toString().isEmpty()) {
                        emptyCells.add(new int[]{i, j});
                    }
                }
            }

            if (!emptyCells.isEmpty()) {

                int random =
                        (int) (Math.random() * emptyCells.size());

                int row = emptyCells.get(random)[0];
                int col = emptyCells.get(random)[1];

                buttons[row][col].setText("O");
                buttons[row][col].setTextColor(Color.RED);

                if (checkWin(row, col)) {
                    gameOver = true;
                    history.add("Winner: Máy ");
                    showWinDialog("Máy");
                }

                playerX = true;
            }

            return;
        }

        // ===== TRUNG BÌNH =====
        if (aiLevel == 2) {

            int bestScore = -1;
            int bestR = -1;
            int bestC = -1;

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {

                    if (buttons[i][j].getText().toString().isEmpty()) {

                        int score =
                                evaluate(i, j, "X") * 10
                                        + evaluate(i, j, "O");

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
                    history.add("Winner: Máy");
                    showWinDialog("Máy");
                }

                playerX = true;
            }

            return;
        }

        // ===== KHÓ =====

        int bestScore = -1;
        int bestR = -1;
        int bestC = -1;

// ƯU TIÊN THẮNG
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {

                if (buttons[i][j].getText().toString().isEmpty()) {

                    buttons[i][j].setText("O");

                    boolean win = checkWin(i, j);

                    buttons[i][j].setText("");

                    if (win) {
                        bestR = i;
                        bestC = j;

                        buttons[bestR][bestC].setText("O");
                        buttons[bestR][bestC].setTextColor(Color.RED);

                        gameOver = true;
                        history.add("Winner: Máy");
                        showWinDialog("Máy");

                        return;
                    }
                }
            }
        }

// CHẶN NGƯỜI CHƠI
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {

                if (buttons[i][j].getText().toString().isEmpty()) {

                    buttons[i][j].setText("X");

                    boolean playerWin = checkWin(i, j);

                    buttons[i][j].setText("");

                    if (playerWin) {

                        buttons[i][j].setText("O");
                        buttons[i][j].setTextColor(Color.RED);

                        playerX = true;

                        return;
                    }
                }
            }
        }

// TÍNH ĐIỂM
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {

                if (buttons[i][j].getText().toString().isEmpty()) {

                    int attack =
                            evaluate(i, j, "O") * 40;

                    int defend =
                            evaluate(i, j, "X") * 60;

                    int center =
                            (20 - Math.abs(10 - i)
                                    - Math.abs(10 - j)) * 10;

                    int score =
                            attack + defend + center;

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
                history.add("Winner: Máy");
                showWinDialog("Máy");
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
    //  HISTORY POPUP
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
    // SCORE + WIN CHECK
    // =========================
    private int evaluate(int r, int c, String p) {

        return scoreLine(r, c, 1, 0, p)
                + scoreLine(r, c, 0, 1, p)
                + scoreLine(r, c, 1, 1, p)
                + scoreLine(r, c, 1, -1, p);
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