package kr.ac.uc.yachtdice.game;

import java.util.*;

public class Player {
    private String name;
    private int[] dice = new int[5];
    private boolean[] holds = new boolean[5];
    private int rollCount = 0;
    private Map<ScoreCategory, Integer> scoreBoard = new EnumMap<>(ScoreCategory.class);

    public int getRollCount() {
        return rollCount;
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasSelectedScoreThisTurn() {
        return rollCount == 0 && !scoreBoard.isEmpty();
    }

    public int[] getDiceValues() {
        return dice;
    }

    public boolean[] getHoldStatus() {
        return holds;
    }

    public Map<ScoreCategory, Integer> getScoreBoard() {
        return scoreBoard;
    }

    public boolean canRoll() {
        return rollCount < 3;
    }

    public void rollDice() {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            if (!holds[i]) {
                dice[i] = rand.nextInt(6) + 1;
            }
        }
        rollCount++;
    }

    public void toggleHold(int index) {
        holds[index] = !holds[index];
    }

    public boolean selectCategory(ScoreCategory category) {
        if (scoreBoard.containsKey(category)) return false;
        int score = ScoreCalculator.calculate(category, dice);
        scoreBoard.put(category, score);
        rollCount = 0;
        Arrays.fill(holds, false);
        return true;
    }
    public void resetTurn() {
        rollCount = 0;
        Arrays.fill(holds, false);
        Arrays.fill(dice, 0);
    }

    public int getTotalScore() {
        return scoreBoard.values().stream().mapToInt(Integer::intValue).sum();
    }
    public boolean hasSelectedAll() {
        return scoreBoard.size() == ScoreCategory.values().length;
    }
}
