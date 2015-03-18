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
        System.out.println("Got a client!");
        boolean flag = true;

        // Do whatever required to process the client's request
        try {
        	inputStream = new DataInputStream(clientSocket.getInputStream());
		    while (!clientSocket.isClosed()){
                try {
                    //get string from client
                    String taskString = inputStream.readUTF();

                    System.out.println("Recieved");
                    System.out.println(taskString);
                    if (taskString.contains("@type"))   {
                        //convert it using json
                        IExecuteTask newTask = (IExecuteTask) JsonReader.jsonToJava(taskString);
                        //add that task to the queue
                        taskModel.addExternalTask(newTask);
                    } else  {
                        // parse input for command
                        switch (taskString) {
                            case "Execute":
                                taskModel.executeAll();
                                break;
                            case "Calibrate":
                                taskModel.callibrate();
                                break;
                            default:
                                System.out.println("Command not recognized");
                        }
                    }

                }
                catch (EOFException e) {
                    clientSocket.close();
                    System.out.println("Client Closed.");
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
