package 문제해결기법.Assignment_30;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class poker {
    private static StringBuilder sb = new StringBuilder();
    private static String[] cards;
    private static String[] selectedCards = new String[5];
    private static Map<String, Integer> rankMap = new HashMap<>();
    static {
        rankMap.put("Top", 0);
        rankMap.put("One Pair", 1);
        rankMap.put("Two Pair", 2);
        rankMap.put("Triple", 3);
        rankMap.put("Straight", 4);
        rankMap.put("Flush", 5);
        rankMap.put("Full House", 6);
        rankMap.put("Four Card", 7);
        rankMap.put("Straight Flush", 8);
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_30\\poker.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_30\\test.inp"));
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            cards = br.readLine().split(" ");
            sb.append(findBestHand(0, 0)).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    public static String findBestHand(int index, int count) {
        if (count == 5)
            return evaluateHand(selectedCards);
        if (index == 7)
            return "Top";
        selectedCards[count] = cards[index];
        String hand1 = findBestHand(index + 1, count + 1);
        String hand2 = findBestHand(index + 1, count);
        return getHigherRank(hand1, hand2);
    }

    public static String evaluateHand(String[] hand) {
        Map<Character, Integer> shapeCount = new HashMap<>();
        Map<Character, Integer> numberCount = new HashMap<>();
        int[] numbers = new int[5];
        boolean isFlush = false;
        boolean isStraight = false;
        boolean existA = false;
        for (int i = 0; i < 5; i++) {
            char shape = hand[i].charAt(0);
            char number = hand[i].charAt(1);
            shapeCount.put(shape, shapeCount.getOrDefault(shape, 0) + 1);
            if (number == 'A' || number == '1') {
                numbers[i] = 1;
                existA = true;
                numberCount.put('1', numberCount.getOrDefault('1', 0) + 1);
                continue;
            } else if (number == 'T') {
                numbers[i] = 10;
            } else if (number == 'J') {
                numbers[i] = 11;
            } else if (number == 'Q') {
                numbers[i] = 12;
            } else if (number == 'K') {
                numbers[i] = 13;
            } else {
                numbers[i] = number - '0';
            }
            numberCount.put(number, numberCount.getOrDefault(number, 0) + 1);
        }
        Arrays.sort(numbers);
        if ((numbers[1] == numbers[0] + 1 && numbers[2] == numbers[0] + 2 &&
                numbers[3] == numbers[0] + 3 && numbers[4] == numbers[0] + 4) ||
                (existA && numbers[1] == 10 && numbers[2] == 11 && numbers[3] == 12 && numbers[4] == 13)) {
            isStraight = true;
        }
        if (shapeCount.size() == 1) {
            isFlush = true;
        }
        if (isFlush && isStraight) {
            return "Straight Flush";
        } else if (numberCount.containsValue(4)) {
            return "Four Card";
        } else if (numberCount.containsValue(3) && numberCount.containsValue(2)) {
            return "Full House";
        } else if (isFlush) {
            return "Flush";
        } else if (isStraight) {
            return "Straight";
        } else if (numberCount.containsValue(3)) {
            return "Triple";
        } else if (numberCount.containsValue(2)) {
            int pairCount = 0;
            for (int count : numberCount.values())
                if (count == 2)
                    pairCount++;
            return pairCount == 2 ? "Two Pair" : "One Pair";
        } else {
            return "Top";
        }
    }

    public static String getHigherRank(String hand1, String hand2) {
        int rank1 = rankMap.get(hand1);
        int rank2 = rankMap.get(hand2);
        return rank1 > rank2 ? hand1 : hand2;
    }
}
