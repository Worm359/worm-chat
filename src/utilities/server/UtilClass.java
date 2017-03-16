package utilities.server;
import java.util.Scanner;
//import java.util.PrintWriter;
import common.Message;
import server.ServerClass;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * UtilClass is responsible for giving control over server side
 * java classes
*/
public class UtilClass implements Runnable
{
	private Scanner keyboard;
	private ServerClass server;
	//private PrintWriter printer;
	boolean exit = false;
	
	public UtilClass(ServerClass server)
	{
		//this.keyboard = new Scanner(in);
		this.server = server;
		this.keyboard = new Scanner(System.in);
	}
	
	public void run()
	{
		System.out.println("WormChat server started running. \n For exit press q.");
		MAIN_LOOP: while(true)
		{
			String input = keyboard.nextLine();
			if(input!=null)
				switch (input)
				{
					case "q":
						{
							stopServer(server, "Command q received on server side");		
							break MAIN_LOOP;
						}
				}
			
		}
	}
	
	public static synchronized void stopServer(ServerClass serverObj, String code)
	{
		if(serverObj.exitFlag != true)
		{
			System.out.println("Server is shutting down. Good bye! \n Server stopping with code: '"+code+"'");
			serverObj.exitFlag = true;	
			try
			{
				Socket s = new Socket("127.0.0.1", serverObj.port);
				//s.getOutputStream().write(END_WAITING);
				//s.getOutputStream().flush();
				s.close();

			}
			catch(UnknownHostException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else {System.out.println("Server still getting comands to shut down with code: '"+code+"'");}
	}

    public static void print(Message message) {
    }

/*	public static void isCommand(String mess)
	{
		return mess.startsWith("c: ");
	}
	public executeCommand(RunnableConnectionHandler client, String command)
	{
		switch(command.substring(3))
		{
			case "quit":
			{

				break;
			}
		}
	}
*/
}

