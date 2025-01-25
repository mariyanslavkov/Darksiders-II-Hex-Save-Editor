import java.io.*;

public class FileProcessing {

    public void processFiles(File mySaveFile, File otherSaveFile, File outputFile) throws IOException {
        long startOffset = 0x0000002F;
        long endOffset = 0x00000034;

        byte[] replacementData = readHexData(mySaveFile, startOffset, endOffset);

        try (RandomAccessFile raf = new RandomAccessFile(otherSaveFile, "r")) {
            byte[] file2Content = new byte[(int) raf.length()];
            raf.readFully(file2Content);

            System.arraycopy(replacementData, 0, file2Content, (int) startOffset, replacementData.length);

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(file2Content);
            }
        }
    }

    private byte[] readHexData(File sourceFile, long startOffset, long endOffset) throws IOException {
        byte[] data = new byte[(int) (endOffset - startOffset + 1)];
        try (RandomAccessFile raf = new RandomAccessFile(sourceFile, "r")) {
            raf.seek(startOffset);
            int bytesRead = raf.read(data);
            if (bytesRead != data.length) {
                throw new IOException("Failed to read hex data from the source file.");
            }
        }
        return data;
    }
}
