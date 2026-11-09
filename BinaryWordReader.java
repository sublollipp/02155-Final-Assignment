import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BinaryWordReader {

    public static int[] readBinaryFileAsWords(String filePath, ByteOrder byteOrder) throws IOException {
        // 1. Read all bytes from the file
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

        // 2. Handle cases where file size is not a multiple of 4 bytes
        int remainder = fileBytes.length % 4;
        if (remainder != 0) {
            // Pad with trailing zeros to complete the final word
            byte[] paddedBytes = new byte[fileBytes.length + (4 - remainder)];
            System.arraycopy(fileBytes, 0, paddedBytes, 0, fileBytes.length);
            fileBytes = paddedBytes;
        }

        // 3. Wrap bytes in a ByteBuffer and set the Endianness
        ByteBuffer buffer = ByteBuffer.wrap(fileBytes);
        buffer.order(byteOrder); // ByteOrder.BIG_ENDIAN or ByteOrder.LITTLE_ENDIAN

        // 4. Extract into an int array (where each int is a 4-byte word)
        int[] words = new int[fileBytes.length / 4];
        buffer.asIntBuffer().get(words);

        return words;
    }

    public static void main(String[] args) {
        try {
            // Example usage (Big-Endian is standard for network/many file formats)
            int[] words = readBinaryFileAsWords("path/to/your/file.bin", ByteOrder.BIG_ENDIAN);
            
            System.out.println("Total words read: " + words.length);
            if (words.length > 0) {
                System.out.println("First word (hex): 0x" + Integer.toHexString(words[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}