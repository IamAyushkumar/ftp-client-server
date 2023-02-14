import java.io.*;

/*
 * @author ayushkumar
 * @created on 11/02/23
 */
public class FtpUtils {

    public static void send(String FileName, ObjectOutputStream outputStream) {
        try {
            File file = new File(FileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            outputStream.writeLong(file.length());
            outputStream.flush();
            byte[] chunk = new byte[CommonConfig.CHUNK_SIZE];
            int bytesRead;
            while((bytesRead = fileInputStream.read(chunk)) != -1) {
                outputStream.write(chunk, 0, bytesRead);
                outputStream.flush();
            }
            fileInputStream.close();
        } catch (IOException ioException) {
            System.err.println("Exception in uploading file.");
            ioException.printStackTrace();
        }
        System.out.println("Done sending.");
    }

    public static void receive(String fileName, ObjectInputStream inputStream) {
        try {
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            long size = inputStream.readLong();
            byte[] chunk = new byte[CommonConfig.CHUNK_SIZE];
            int bytesTaken;
            while(size > 0 && (bytesTaken = inputStream.read(chunk, 0, CommonConfig.CHUNK_SIZE)) > 0) {
                fileOutputStream.write(chunk, 0, bytesTaken);
                size -= bytesTaken;
            }
            fileOutputStream.close();
        } catch (IOException ioException) {
            System.err.println("Exception in downloading file.");
            ioException.printStackTrace();
        }
        System.out.println("Done receiving.");
    }

    public static boolean checkIfFileExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

}
