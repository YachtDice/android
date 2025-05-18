package kr.ac.uc.yachtdice.game;

import java.util.Random;

public class Dice {
    private int[] values = new int[5];
    private boolean[] held = new boolean[5];
    private Random random = new Random();

    public void roll() {
        for (int i = 0; i < 5; i++) {
            if (!held[i]) {
                values[i] = random.nextInt(6) + 1;
            }
        }
    }

    public void toggleHold(int index) {
        held[index] = !held[index];
    }

    public int[] getValues() {
        return values;
    }

    public boolean[] getHoldStatus() {
        return held;
    }

    public void reset() {
        held = new boolean[5];
        values = new int[5];
    }
}
