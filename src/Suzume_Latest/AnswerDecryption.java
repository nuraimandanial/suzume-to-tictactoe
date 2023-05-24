package Suzume_Latest;

import java.util.ArrayList;
import java.util.List;

public class AnswerDecryption {
    private int value;
    private final int KEY = 7;

    private List<String> binaryBlocks;
    private StringBuilder binaryString;
    
    public AnswerDecryption(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void binaryToBlocks() {
        String binaryValue = Integer.toBinaryString(this.value);

        binaryBlocks = new ArrayList<>();

        int remainder = binaryValue.length() % 3;
        if (remainder != 0) {
            binaryValue = "0".repeat(3 - remainder) + binaryValue;
        }

        for (int i = binaryValue.length() - 1; i >= 0; i -=3) {
            int end = i + 1;
            int start = Math.max(0, end - 3);
            binaryBlocks.add(0, binaryValue.substring(start, end));
        }
    }

    public void addSecretKey() {
        binaryString = new StringBuilder();
        for (String string : binaryBlocks) {
            string = Integer.toBinaryString(Integer.parseInt(string, 2) + (KEY % 2));
            if (string.length() < 3) {
                string = "0" + string;
            }
            if (string.length() == 4) { 
                String replace = binaryString.substring(0, binaryString.length() - 1) + "1";
                binaryString.setLength(0);
                binaryString.append(replace);
                string = "000";
            }
            binaryString.append(string);
        }
        System.out.println(binaryString);
    }

    public static void main(String[] args) {
        AnswerDecryption answerDecryption = new AnswerDecryption(17355);
        answerDecryption.binaryToBlocks();
        answerDecryption.addSecretKey();
        System.out.println(Integer.parseInt(answerDecryption.binaryString.toString(), 2));
    }
}
