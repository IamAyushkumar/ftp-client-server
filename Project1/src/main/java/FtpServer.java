import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * @author ayushkumar
 * @created on 11/02/23
 */
public class FtpServer {
    public ServerSocket welcomeSocket;
    public Socket clientConnection;
    public ObjectInputStream inputStream;
    public ObjectOutputStream outputStream;

    public void start() {
        try {
            welcomeSocket = new ServerSocket(CommonConfig.COMMON_PORT, 10);
            System.out.println("[Server] Server up on port = " + CommonConfig.COMMON_PORT + ". Waiting for client connection.");
            clientConnection = welcomeSocket.accept();
            System.out.println("[Server] Connection received from " + clientConnection.getInetAddress().getHostName() + ".");
            inputStream = new ObjectInputStream(clientConnection.getInputStream());
            outputStream = new ObjectOutputStream(clientConnection.getOutputStream());

            while (true) {
                String command = inputStream.readUTF();
                String fileName = inputStream.readUTF();
                System.out.println("[Server] Received command = " + command + " for file = " + fileName);
                String modifiedFileName;
                switch (command) {
                    case "upload":
                        modifiedFileName =  CommonConfig.BASE_DIR + "new" + fileName;
                        System.out.println("[Server] Receiving file = " + fileName);
                        FtpUtils.receive(modifiedFileName, inputStream);
                        outputStream.flush();
                        outputStream.writeInt(CommonConfig.StatusCodes.SUCCESS);
                        outputStream.flush();
                        System.out.println("[Server][Success] file = " + fileName + " received successfully from client.");
                        break;
                    case "get":
                        modifiedFileName = CommonConfig.BASE_DIR + fileName;
                        if(FtpUtils.checkIfFileExists(modifiedFileName)) {
                            outputStream.writeInt(CommonConfig.StatusCodes.SUCCESS);
                            outputStream.flush();
                            System.out.println("[Server] Sending file = " + fileName + " to client");
                            FtpUtils.send(modifiedFileName, outputStream);
                        } else {
                            outputStream.writeInt(CommonConfig.StatusCodes.FILE_NOT_FOUND);
                            outputStream.flush();
                        }
                        break;
                }
            }

        } catch (IOException ioException) {
            System.err.println("Exception: Port busy");
            ioException.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
                clientConnection.close();
            } catch (IOException e) {
                System.err.println("Exception in closing connection");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FtpServer ftpServer = new FtpServer();
        ftpServer.start();
    }

}
