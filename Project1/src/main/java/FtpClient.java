import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/*
 * @author ayushkumar
 * @created on 11/02/23
 */
public class FtpClient {
    public Socket serverConnection;
    public ObjectOutputStream outputStream;
    public ObjectInputStream inputStream;
    public int port;

    public FtpClient(int port) {
        this.port = port;
    }

    public void startClient() {
        try {
            System.out.println("[Client] Starting Client...");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("[Client] Connecting to server on port " + port + "...");
            serverConnection = new Socket("localhost", port);
            outputStream = new ObjectOutputStream(serverConnection.getOutputStream());
            inputStream = new ObjectInputStream(serverConnection.getInputStream());
            while(true) {
                System.out.println("[Client] Enter any of these commands - (get <filename> || upload <filename>):");
                String[] command = bufferedReader.readLine().split(" ");
                if(command.length != 2) {
                    System.err.println("Invalid input. Expected command followed by file name. Terminating...");
                    return;
                }
                String commandType = command[0];
                String fileName = command[1];
                String modifiedFileName;
                System.out.println("[Client] Received command = " + commandType + " for file = " + fileName);
                switch (commandType) {
                    case "get":
                        modifiedFileName = CommonConfig.BASE_DIR + "new" + fileName;
                        outputStream.writeUTF(commandType);
                        outputStream.writeUTF(fileName);
                        outputStream.flush();
                        int statusCode = inputStream.readInt();
                        if(statusCode == 200) {
                            System.out.println("[Client] Downloading file = " + fileName);
                            FtpUtils.receive(modifiedFileName, inputStream);
                            System.out.println("[Client] Downloaded file = " + fileName);
                        } else {
                            System.err.println("[Client] file = " + fileName + " doesn't exist on server.");
                        }
                        break;
                    case "upload":
                        modifiedFileName = CommonConfig.BASE_DIR + fileName;
                        if(!FtpUtils.checkIfFileExists(modifiedFileName)) {
                            System.err.println("File named " + fileName + " doesn't exist. Retry.");
                            continue;
                        }
                        outputStream.writeUTF(commandType);
                        outputStream.writeUTF(fileName);
                        System.out.println("[Client] Uploading file = " + fileName);
                        FtpUtils.send(modifiedFileName, outputStream);
                        outputStream.flush();
                        statusCode = inputStream.readInt();
                        if(statusCode == 200) {
                            System.out.println("[Client] File upload success.");
                        } else {
                            System.err.println("[Client] Couldn't upload file successfully.");
                        }
                        break;
                }
            }
        } catch (IOException ioException) {
            System.err.println("Port Busy");
            ioException.printStackTrace();
        }  finally {
            try {
                outputStream.close();
                inputStream.close();
                serverConnection.close();
            } catch (IOException e) {
                System.err.println("Exception in closing connection");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        FtpClient ftpClient = new FtpClient(port);
        ftpClient.startClient();
    }

}
