package Suzume;

/*
 * author @nuraimandanial
 */
public class AnswerEncryption {
    public static int encryptAnswer(int answer) {
        int secretKey = 7;
        String binaryString = Integer.toBinaryString(answer);

        // Step 3: Divide the binary string into blocks of three digits
        int blockSize = 3;
        StringBuilder paddedBinaryString = new StringBuilder();

        for (int i = binaryString.length() - 1; i >= 0; i -= blockSize) {
            int endIndex = i + 1;
            int startIndex = Math.max(i - blockSize + 1, 0);
            String block = binaryString.substring(startIndex, endIndex);

            // Step 4a: Add the value of (secret key modulo 2) to the binary value of the block
            int binaryValue = Integer.parseInt(block, 2);
            int modifiedValue = binaryValue + (secretKey % 2);

            // Step 4b: Convert the modified value back to binary format with three digits
            String paddedBlock = String.format("%" + blockSize + "s", Integer.toBinaryString(modifiedValue)).replace(' ', '0');

            paddedBinaryString.insert(0, paddedBlock);
        }

        // Step 5: Convert the padded binary string back to an integer
        int encryptedAnswer = Integer.parseInt(paddedBinaryString.toString(), 2);

        return encryptedAnswer;
    }

    public static void main(String[] args) {
        int decryptedAnswer = 17355;
        int encryptedAnswer = encryptAnswer(decryptedAnswer);
        System.out.println("Encrypted Answer: " + encryptedAnswer);
    }
}
