package main.model.externalcomm;

import java.io.*;
import java.net.Socket;

import com.cedarsoftware.util.io.JsonReader;

import main.model.tasks.TaskModel;
import main.model.tasks.basictasks.IExecuteTask;

public class ClientTask implements Runnable {
	
    private final Socket clientSocket;
    
    private DataInputStream inputStream;
    
    private TaskModel taskModel;
    
    public ClientTask(Socket clientSocket, TaskModel taskModel) {
        this.clientSocket = clientSocket;
        this.taskModel = taskModel;
    }

    @Override
    public void run() {
        System.out.println("Got a client !");

        // Do whatever required to process the client's request
        try {
        	inputStream = new DataInputStream(clientSocket.getInputStream());
		    while (true){
                try {
                    //get string from client
                    String taskString = inputStream.readUTF();
                    //convert it using json
                    IExecuteTask newTask = (IExecuteTask) JsonReader.jsonToJava(taskString);
                    //add that task to the queue
                    taskModel.appendTaskToQueue(newTask);
                }
                catch (EOFException e) {
                    //do nothing, normal execution
                }
		    }
		    
		} catch (Exception e1) {
			e1.printStackTrace();
		}

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
