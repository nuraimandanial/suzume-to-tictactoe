package Suzume_DecryptAnswer;
import java.util.ArrayList;
import java.util.Stack;


public class AnswerDecryption_Cheah {

    public static void main(String [] args){
        int number = 17355;
        int blockSize = 3;
        int secretKey = 7;

        System.out.println("Encryption Number: " + number);

        // PART 1: To binary
        String binaryString = Integer.toBinaryString(number);
        System.out.println("Encryption number in binary: " + binaryString);

        // PART 2 AND 3: Divide into block and store into arraylist and convert the block into decimal value
        binaryStringDivider(binaryString, blockSize);

        // PART 4: Create a stack to store the data in the arraylist
        Stack<Integer> stack = insertIntoStack(blockToDecimalConvert(binaryString, blockSize));

        // PART 5: Modify the value of each block using stack
        modifyStackValues(stack, secretKey);

        // PART 6: Create an arraylist to store the value in stack and convert into binary value
        ArrayList<String> binaryBlocks = setBinaryValueForDigits(stack, blockSize);

        // PART 7: Delete the block and combine the binary value into binary form of final answer
        String combinedBinaryString = combineBlocks(binaryBlocks);

        // PART 8: Final answer
        final int finalAnswer = Integer.parseInt(combinedBinaryString, 2);
        System.out.println("Answer Encryption: " + finalAnswer);
    }

    // PART 2-3
    public static void binaryStringDivider(String x, int y) {
        String binaryString = x;
        int blockSize = y;

        ArrayList<String> blocks = divideIntoBlocks(binaryString, blockSize);
        ArrayList<Integer> decimalValues = blockToDecimalConvert(binaryString, blockSize);

        // Print the blocks
        System.out.print("Binary divided into block: [");
        for (int i = blocks.size() - 1; i >= 0; i--) {
            System.out.printf("[%s]", blocks.get(i));
        }
        System.out.println("]");

        System.out.print("Value of block in decimal: [");
        for (int i = decimalValues.size() - 1; i >= 0; i--) {
            System.out.printf("[%d]", decimalValues.get(i));
        }
        System.out.println("]");
    }

    public static ArrayList<String> divideIntoBlocks(String binaryString, int blockSize) {
        ArrayList<String> blocks = new ArrayList<>();
        int numBlocks = (int) Math.ceil((double) binaryString.length() / blockSize);

        for (int i = 0; i < numBlocks; i++) {
            int startIndex = Math.max(0, binaryString.length() - blockSize - (i * blockSize));
            int endIndex = Math.max(0, binaryString.length() - (i * blockSize));
            String block = binaryString.substring(startIndex, endIndex);
            blocks.add(block);
        }

        return blocks;
    }

    public static ArrayList<Integer> blockToDecimalConvert(String binaryString, int blockSize) {
        ArrayList<Integer> decimalValues = new ArrayList<>();
        ArrayList<String> blocks = divideIntoBlocks(binaryString, blockSize);

        for (String block : blocks) {
            int decimalValue = Integer.parseInt(block, 2); // Convert block from binary to decimal
            decimalValues.add(decimalValue);
        }

        return decimalValues;
    }

    // PART 4
    public static Stack<Integer> insertIntoStack(ArrayList<Integer> decimalValues) {
        Stack<Integer> stack = new Stack<>();

        // Insert decimal values into the stack
        for (int value : decimalValues) {
            stack.push(value);
        }

        System.out.print("Value in stack before modified: ");
        for (int i = stack.size() - 1; i >= 0 ; i--) {
            int value = stack.get(i);
            System.out.print(value);
        }
        System.out.println();

        return stack;
    }

    // PART 5
    public static void modifyStackValues(Stack<Integer> stack, int secretKey) {
        int secretKeyModulo2 = secretKey % 2;
        String secretKeyBinaryValue = Integer.toBinaryString(secretKey);

        for (int i = 0; i < stack.size(); i++) {
            int value = stack.get(i);
            int modifiedValue = value;
            if(secretKeyModulo2 == 1){
                modifiedValue -= 1;
            }
            secretKeyBinaryValue = String.format("%3s", secretKeyBinaryValue).replace(" ", "0");
            secretKey = Integer.parseInt(secretKeyBinaryValue, 2);
            int shiftedValue = secretKey >> 1; // Shift right by 1 bit
            secretKeyBinaryValue = Integer.toBinaryString(shiftedValue);
            secretKeyModulo2 = shiftedValue % 2;

            stack.set(i, modifiedValue);
        }

        System.out.print("Value in stack after modified: ");
        for (int i = stack.size() - 1; i >= 0 ; i--) {
            int value = stack.get(i);
            System.out.print(value);
        }
        System.out.println();

    }

    // PART 6
    public static ArrayList<String> setBinaryValueForDigits(Stack<Integer> stack, int blockSize) {
        ArrayList<String> binaryBlocks = new ArrayList<>();

        while (!stack.isEmpty()) {
            int value = stack.pop();

            StringBuilder binaryBlock = new StringBuilder();
            for (int j = 0; j < blockSize; j++) {
                int digit = value % 2;
                binaryBlock.insert(0, digit);
                value /= 2;
            }
            binaryBlocks.add(binaryBlock.toString());
        }

        System.out.print("Value of block in decimal: [");
        for (int i = 0; i < binaryBlocks.size(); i++) {
            String binaryBlock = binaryBlocks.get(i);
            System.out.printf("[%s]", binaryBlock);
        }
        System.out.println("]");

        return binaryBlocks;
    }

    // PART 7
    public static String combineBlocks(ArrayList<String> binaryBlocks) {
        StringBuilder combinedBinaryString = new StringBuilder();

        for (int i = 0; i < binaryBlocks.size(); i++) {
            combinedBinaryString.append(binaryBlocks.get(i));
        }
        System.out.println("Combined Binary String: " + combinedBinaryString);
        return combinedBinaryString.toString();
    }

}
