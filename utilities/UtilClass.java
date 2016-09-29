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
		while(!exit)
		{
			String input = keyboard.nextLine();
			if(input!=null)
				switch (input)
				{
					case "q":
						{
							System.out.println("Server received quit command. Good bye!");
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
								
							ServerClass.releaseResourses();
							break;

						}
				}
			
		}
	}
}

