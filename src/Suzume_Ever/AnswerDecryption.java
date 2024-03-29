
import java.util.Stack;

public class AnswerDecryption {
    public static void main(String[] args) {
        Stack<String> binaryCode = new Stack<>();
        Stack<String> reverse = new Stack<>();
        int input = 17355;
        int secretKey = 7;
        // Convert the integer to binary format
        String binaryString = Integer.toBinaryString(input);
        System.out.println("Encrypt Binary: " + binaryString);

        // Divide the binary string into blocks of three digits
        StringBuilder result = new StringBuilder();
        int startIndex = binaryString.length() % 3 == 0 ? 0 : 3 - (binaryString.length() % 3);
        for (int i = startIndex; i < binaryString.length(); i += 3) {
            String block = binaryString.substring(i, i + 3);
            binaryCode.push(block);            
        }

        for (int i = 0; i < binaryString.length() / 3; i++) {
            String block1 = binaryCode.pop();
            if (secretKey % 2 != 0) {
                int blockValue = Integer.parseInt(block1, 2);
                blockValue -= 1;
                block1 = Integer.toBinaryString(blockValue);
                if (block1.length() < 3) {
                    block1 = String.format("%3s", block1).replace(" ", "0");
                }
            }
            reverse.push(block1);

            // Convert the secret key to binary, shift right, and convert to decimal
            String secretKeyBinary = Integer.toBinaryString(secretKey);
            int shiftedSecretKey = Integer.parseInt(secretKeyBinary, 2) >> 1;
            secretKey = Integer.parseInt(Integer.toBinaryString(shiftedSecretKey), 2);
        }
        
        while (!reverse.isEmpty()) {
            String block2 = reverse.pop();
            result.append(block2);
        }
        String combinedBinary = result.toString();
        System.out.println("Decrypt Binary: "+combinedBinary);
        int decimal = Integer.parseInt(combinedBinary, 2);

        System.out.println("Decrypt Answer: " + decimal);
    }
}
