package client;
import common.Message;
import utilities.client.UtilClientClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

public class RunnableClientReceiver implements Runnable
{
	private ClientClass clientObj = null;
	private Socket clientSocket = null;
	private BufferedReader reader = null; 
	public RunnableClientReceiver(Socket socket, ClientClass client) throws IOException
	{
		if(socket!=null && client!=null)
		{
			this.clientSocket = socket;
			this.clientObj = client;
		}
		else throw new IllegalArgumentException("Trying to pass null as a parameter to Receiver class");
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}	      

	public void run()
	{
		String inputMessage = null;
		try
		{
			while((inputMessage=reader.readLine())!=null)
			{
				Message message = new Message(inputMessage);
				UtilClientClass.print(message);
			}
		}
		catch(IOException e)
		{
			System.out.println("Cannot receive information from server anymore");

		}
	}


}
