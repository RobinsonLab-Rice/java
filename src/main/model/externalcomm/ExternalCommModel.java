package main.model.externalcomm;

import main.model.tasks.TaskModel;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

/**
 * External model, handles communication with other programs, relaying this information to
 * the appropriate models.
 * @author Christian
 *
 */
public class ExternalCommModel {

	/**
	 * Adapter from this model to the task model.
	 */
    TaskModel taskModel;
	
	ServerSocket welcomeSocket;
	
	BufferedReader inputStream;
	
	OutputStream outputStream;
	
	String clientSentence;

	public ExternalCommModel(){}

    /* On initialization, connects to given adapters and starts the server. */
	public void start(TaskModel taskModel){
        this.taskModel = taskModel;
		startServer();
	}
	
	public void startServer() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                System.out.println("Waiting for clients to connect...");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    clientProcessingPool.submit(new ClientTask(clientSocket, taskModel));
                }
            } catch (IOException e) {
                System.err.println("Unable to process client request");
                e.printStackTrace();
            }
            }
        };
        (new Thread(serverTask)).start();
    }
}
