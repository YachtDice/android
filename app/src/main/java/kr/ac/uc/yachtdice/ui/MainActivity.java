package kr.ac.uc.yachtdice.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import kr.ac.uc.yachtdice.R;
import kr.ac.uc.yachtdice.game.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private GameManager gameManager;

    private ImageView[] diceImages = new ImageView[5];
    private Button[] diceButtons = new Button[5];

    private TextView txtCurrentPlayer;
    private Button btnRoll;
    private Button btnNextTurn;
    private TableLayout scoreTable;

    private TextView chatMessages;
    private EditText chatInput;
    private ScrollView chatScroll;
    private Button sendButton;

    private final int[] playerColors = { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> playerNames = getIntent().getStringArrayListExtra("players");
        if (playerNames == null || playerNames.isEmpty()) {
            playerNames = new ArrayList<>();
            playerNames.add("Player 1");
            playerNames.add("Player 2");
        }

        gameManager = new GameManager();
        for (String name : playerNames) {
            gameManager.addPlayer(new Player(name));
        }

        txtCurrentPlayer = findViewById(R.id.playerText);
        btnRoll = findViewById(R.id.rollButton);
        btnNextTurn = findViewById(R.id.nextTurnButton);
        scoreTable = findViewById(R.id.scoreTable);

        for (int i = 0; i < 5; i++) {
            int imgId = getResources().getIdentifier("diceImg" + i, "id", getPackageName());
            int btnId = getResources().getIdentifier("diceBtn" + i, "id", getPackageName());

            diceImages[i] = findViewById(imgId);
            diceButtons[i] = findViewById(btnId);

            int index = i;
            diceButtons[i].setOnClickListener(v -> {
                gameManager.getCurrentPlayer().toggleHold(index);
                updateDiceUI();
            });
        }

        btnRoll.setOnClickListener(v -> {
            Player currentPlayer = gameManager.getCurrentPlayer();
            if (currentPlayer.canRoll()) {
                currentPlayer.rollDice();
                updateDiceUI();
            } else {
                Toast.makeText(this, "더 이상 굴릴 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnNextTurn.setOnClickListener(v -> {
            Player currentPlayer = gameManager.getCurrentPlayer();

            if (!currentPlayer.hasSelectedScoreThisTurn()) {
                Toast.makeText(this, "점수를 먼저 선택해야 턴을 넘길 수 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            currentPlayer.resetTurn();  // rollCount, holds, dice 초기화
            gameManager.nextTurn();
            updateUI();
        });

        chatMessages = findViewById(R.id.chatMessages);
        chatInput = findViewById(R.id.chatInput);
        chatScroll = findViewById(R.id.chatScroll);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            String message = chatInput.getText().toString().trim();
            if (!message.isEmpty()) {
                chatMessages.append("\n" + message);
                chatInput.setText("");
                chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
            }
        });

        setupScoreTable();
        updateUI();
    }

    private void setupScoreTable() {
        TableRow header = new TableRow(this);
        header.addView(makeCell("카테고리"));

        List<Player> players = gameManager.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            TextView nameCell = makeCell(players.get(i).getName());
            nameCell.setTextColor(playerColors[i % playerColors.length]);
            nameCell.setTag("player_name_" + i); // 현재 턴 강조용
            header.addView(nameCell);
        }
        scoreTable.addView(header);

        for (ScoreCategory category : ScoreCategory.values()) {
            TableRow row = new TableRow(this);
            row.addView(makeCell(getDisplayName(category)));

            for (Player p : players) {
                TextView scoreCell = makeCell("-");
                scoreCell.setTag(p.getName() + "_" + category.name());
                scoreCell.setOnClickListener(v -> handleScoreSelection(category));
                row.addView(scoreCell);
            }
            scoreTable.addView(row);
        }
    }

    private void updateUI() {
        updateTurnUI();
        updateDiceUI();
        updateScoreBoardUI();
    }

    private void updateTurnUI() {
        int currentIndex = gameManager.getCurrentPlayerIndex();
        txtCurrentPlayer.setText("🎯 현재 턴: " + gameManager.getCurrentPlayer().getName());
        txtCurrentPlayer.setTextColor(playerColors[currentIndex % playerColors.length]);

        // 점수판 상단 플레이어 이름 강조
        for (int i = 0; i < gameManager.getPlayers().size(); i++) {
            TextView nameView = scoreTable.findViewWithTag("player_name_" + i);
            if (nameView != null) {
                nameView.setBackgroundColor(i == currentIndex ? Color.LTGRAY : Color.TRANSPARENT);
            }
        }
    }

    private void updateDiceUI() {
        int[] values = gameManager.getCurrentPlayer().getDiceValues();
        boolean[] holds = gameManager.getCurrentPlayer().getHoldStatus();
        for (int i = 0; i < 5; i++) {
            int value = values[i];
            int resId = (value == 0) ? R.drawable.dice_face_1 : getResources().getIdentifier("dice_face_" + value, "drawable", getPackageName());

            if (resId != 0) {
                diceImages[i].setImageResource(resId);
            } else {
                Log.e("Dice", "이미지 리소스 없음: dice_face_" + value);
            }

            diceImages[i].setAlpha(holds[i] ? 0.5f : 1.0f);
        }
    }

    private void updateScoreBoardUI() {
        for (Player player : gameManager.getPlayers()) {
            for (Map.Entry<ScoreCategory, Integer> entry : player.getScoreBoard().entrySet()) {
                String tag = player.getName() + "_" + entry.getKey().name();
                TextView tv = scoreTable.findViewWithTag(tag);
                if (tv != null) {
                    tv.setText(String.valueOf(entry.getValue()));
                    tv.setEnabled(false);
                }
            }
        }
    }

    private void handleScoreSelection(ScoreCategory category) {
        Player currentPlayer = gameManager.getCurrentPlayer();
        if (currentPlayer.getRollCount() == 0) {
            Toast.makeText(this, "주사위를 먼저 굴려야 점수를 선택할 수 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPlayer.selectCategory(category)) {
            updateScoreBoardUI();
            if (gameManager.isGameOver()) {
                showGameResult();
            } else {
                // 점수 선택 후 직접 버튼 눌러야 턴이 넘어가도록 변경됨
            }
        } else {
            Toast.makeText(this, "이미 선택한 항목입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGameResult() {
        StringBuilder result = new StringBuilder("게임 종료!\n");
        for (Player p : gameManager.getPlayers()) {
            result.append(p.getName()).append(" 점수: ").append(p.getTotalScore()).append("\n");
        }
        Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
    }

    private TextView makeCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(12, 6, 12, 6);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    private String getDisplayName(ScoreCategory category) {
        switch (category) {
            case ONES: return "1";
            case TWOS: return "2";
            case THREES: return "3";
            case FOURS: return "4";
            case FIVES: return "5";
            case SIXES: return "6";
            case CHOICE: return "Choice";
            case FOUR_OF_A_KIND: return "4Kind";
            case FULL_HOUSE: return "Full";
            case SMALL_STRAIGHT: return "S. Straight";
            case LARGE_STRAIGHT: return "L. Straight";
            case YACHT: return "Yacht";
        }
        return category.name();
    }
}
