
# Implementation of FTP client and server

This project aims to build a client-server application that allows the functionality of file transfer (uploads and downloads) to/from server.


## Components

- FtpServer.java
  - It contains code related to server. Upon initialization, server waits for a client to join.  
    - In case of a Get command, it first checks if the requested file exists, if it doesn't then 404 status code is returned, otherwise, along with 200 status code, the file is transferred to client (in chunks of 1000 bytes).
    - In case of an Upload command, server creates a file name starting with "new" and stores the incoming data in that file. Upon successful upload, it returns 200 status code to client.  
- FtpClient.java
  - It contains the code related to client. Following is the flow of code:
    - As soon as this class is initiated, client waits for one of these commands:
      - get <filename> : To get an existing file from server.
      - upload <filename> : To upload a new fil on server.
    - After a connection between server and client is established, client can perform get/upload functions repeatedly and can kill the application in case he wants to exit.
    - For each type of request, client tells server the command type and name of the file before an actual file transfer happens.
    - Get: Since this is a download request from server, first the status code returned by server is checked, and if status code is 200 then client can start receiving the file, otherwise "file doesn't exist" error is shown to client.
    - Upload: This functionality uploads the given file to server, and success message is shown to the user when upload is successful.
    - Added a functionality of status codes to inform client about the failure/success operations. In case of get command, if such file is requested which does not exist on server, then server will return 404 and an error message is shown to user, otherwise success message is shown in case of status code 200.
  
- FtpUtils.java
  - It contains stateless send and receive methods.
- CommonConfig.java
  - It contains all the configurable properties of the project in one common place such as server's default port number, base directory of files, Size of Chunks, Status Codes, etc.


## Steps to run:
* Run FtpServer.java to start the server which waits for client to connect to the port on which it is listening. 
* Run FtpClient.java to initiate a client using ftpclient <port> command.
* Then client waits for one of these commands:
  1. get <filename> : To get an existing file from server.
  2. upload <filename> : To upload a new fil on server.
* Use the above commands to upload/download files to/from server.
* Both server and client runs in infinite loop to support a continuity of operations for the ease of user.

## Commands:
- Compilation:
  ```
    javac -target 1.8 -source 1.8 FtpClient.java
    javac -target 1.8 -source 1.8 FtpServer.java
    ```
- Creating Alias:
  ```
    vi ~/.bash_profile
    alias ftpclient='java -cp "<path-to-client-class>" FtpClient'
    alias ftpserver='java -cp "<path-to-server-class>" FtpServer'
    ```

- Run
  ```
  ftpserver
  ftpclient 8000
  ```


    

