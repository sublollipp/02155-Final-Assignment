import java.nio.ByteOrder;

public class TestBin {
    static String binPath = "";
    public static void main(String[] args) {
        try {
            int[] instructionArray = BinaryWordReader.readBinaryFileAsWords(binPath, ByteOrder.LITTLE_ENDIAN);
        } catch (Exception e) {
            System.out.println("Something didn't work when converting the file to an int array. Oh well...");
        }
        
    }
}
