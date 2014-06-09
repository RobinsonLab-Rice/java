package model.externalcomm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

import model.tasks.basictasks.MultiTask;

public class ClientTask implements Runnable {
	
    private final Socket clientSocket;
    
    private ObjectInputStream inputStream;
    
    private TaskAdapter taskModel;
    
    public ClientTask(Socket clientSocket, TaskAdapter taskModel) {
        this.clientSocket = clientSocket;
        this.taskModel = taskModel;
    }

    @Override
    public void run() {
        System.out.println("Got a client !");

        // Do whatever required to process the client's request
        try {
        	inputStream = new ObjectInputStream(clientSocket.getInputStream());
		    while (true){
		    	MultiTask inputTask = (MultiTask) inputStream.readObject();
				processInput(inputTask);
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
    
    public void processInput(MultiTask input){
	    //taskModel
    }
    
//    private boolean isInteger(String s) {
//	    return isInteger(s,10);
//	}
//
//	private boolean isInteger(String s, int radix) {
//	    if(s.isEmpty()) return false;
//	    for(int i = 0; i < s.length(); i++) {
//	        if(i == 0 && s.charAt(i) == '-') {
//	            if(s.length() == 1) return false;
//	            else continue;
//	        }
//	        if(Character.digit(s.charAt(i),radix) < 0) return false;
//	    }
//	    return true;
//	}
	
	/**
	 * 
	 * @param multiTask
	 */
	private void sendMultiTask(String multiTask){
	}
}
