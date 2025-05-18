package kr.ac.uc.yachtdice.game;

import java.util.*;

public class ScoreCalculator {
    public static int calculate(ScoreCategory category, int[] dice) {
        Map<Integer, Integer> freq = new HashMap<>();
        int sum = 0;

        for (int d : dice) {
            freq.put(d, freq.getOrDefault(d, 0) + 1);
            sum += d;
        }

        switch (category) {
            case ONES: return sumOf(dice, 1);
            case TWOS: return sumOf(dice, 2);
            case THREES: return sumOf(dice, 3);
            case FOURS: return sumOf(dice, 4);
            case FIVES: return sumOf(dice, 5);
            case SIXES: return sumOf(dice, 6);
            case CHOICE: return sum;
            case FULL_HOUSE: {
                boolean hasThree = false;
                boolean hasTwo = false;
                for (int count : freq.values()) {
                    if (count == 3) hasThree = true;
                    if (count == 2) hasTwo = true;
                }
                return (hasThree && hasTwo) ? sum : 0;
            }
            case FOUR_OF_A_KIND:
                for (int count : freq.values()) {
                    if (count >= 4) return sum;
                }
                return 0;
            case SMALL_STRAIGHT: {
                Set<Integer> values = freq.keySet(); // 중복 제거된 주사위 값들

                if (values.containsAll(Arrays.asList(1, 2, 3, 4)) ||
                        values.containsAll(Arrays.asList(2, 3, 4, 5)) ||
                        values.containsAll(Arrays.asList(3, 4, 5, 6))) {
                    return 30;
                }
                return 0;
            }
            case LARGE_STRAIGHT:
                Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
                Set<Integer> s2 = new HashSet<>(Arrays.asList(2, 3, 4, 5, 6));
                Set<Integer> actual = freq.keySet();
                return (actual.equals(s1) || actual.equals(s2)) ? 30 : 0;
            case YACHT:
                return freq.containsValue(5) ? 50 : 0;
        }
        return 0;
    }

    private static int sumOf(int[] dice, int value) {
        int sum = 0;
        for (int d : dice) {
            if (d == value) sum += d;
        }
        return sum;
    }
}
