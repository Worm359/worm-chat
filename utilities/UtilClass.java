package utilities;
import java.util.Scanner;
//import java.util.PrintWriter;
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
	//private PrintWriter printer;
	boolean exit = false;
	
	public UtilClass()
	{
		//this.keyboard = new Scanner(in);
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

							stopServer("Command q received on server side");		
							break MAIN_LOOP;

						}
				}
			
		}
	}
	
	public static synchronized void stopServer(String code)
	{
		if(ServerClass.exitFlag != true)
		{
			System.out.println("Server is shutting down. Good bye! \n Server stopping with code: '"+code+"'");
			ServerClass.exitFlag = true;	
			try
			{
				Socket s = new Socket("127.0.0.1", 1234);
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
}

