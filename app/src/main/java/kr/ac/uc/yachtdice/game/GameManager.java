package kr.ac.uc.yachtdice.game;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getScoreBoard().size() < ScoreCategory.values().length) {
                return false;
            }
        }
        return true;
    }
}
