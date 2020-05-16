/**
* This class always listens for server responses.
* 
* @author Andrea Lissak
* @version 1.0
* @release 1/04/2016
* @See ClientHelper.java
*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClientHelper implements Runnable
{
	private Socket myCliSerSocket;
	protected ClientGUI clg;
	private boolean gameRunning;
	private BufferedReader fromServer;
	private boolean clientIsHuman;
	private Bot myBot;
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @field clientIsHuman
	    * true is received if the flow of execution started in the PlayGame class
	    * 
		* @field gameRunning
		* always true
		* 
		* @field myCliSerSocket
		* socket that connects to port number 40004 of localhost (client side)
		* 
		* @field clg
		* clientGui object to allow input of server messages in that class
	    * 
	* * */
	public ClientHelper(Socket myCliSerSocket, ClientGUI clg, boolean clientIsHuman)
	{
		this.clientIsHuman=clientIsHuman;
		gameRunning=true;
		this.myCliSerSocket=myCliSerSocket;
		this.clg=clg;
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @field myBot
		* bot instance
		* 
	* * */
	public ClientHelper(Socket myCliSerSocket, Bot myBot, boolean clientIsHuman) {
		this.clientIsHuman=clientIsHuman;
		gameRunning=true;
		this.myCliSerSocket=myCliSerSocket;
		this.myBot=myBot;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method contains actions that are executed simultaneously to the PlayGame or Bot
	    * classes. It is in another thread because all clients should ALWAYS listen when a player
	    * spawns, moves or disappears.
	    * What it actually does is to keep listening indefinitely for any string sent by the server.
	    * 
	    * @localVariables/objects:
	    * anyInput: support variable that holds the string sent by the server
	    * 
	    * 
	 * **/
	public void run()
	{
		try
		{
			fromServer = null;
			String anyInput=null;
			try
			{
				fromServer = new BufferedReader(new InputStreamReader(myCliSerSocket.getInputStream()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			while (gameRunning)
			{
				anyInput=readServer();
				if(anyInput==null)
				{
					break;
				}
				if(clientIsHuman)
				{
					clg.feedInput(anyInput);
				}
				else
				{
					myBot.feedInput(anyInput);
				}
				
			}
			fromServer.close();
		    myCliSerSocket.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method reads lines sent from the server in a controlled environment.
	    * 
	    * @localVariables/objects:
	    * serverMessage: temporary string var
	    * 
	    * @return
	    * server response
	    * 
	* * */
	public String readServer() throws IOException
	{
		try
		{
	 		String serverMessage=null;
	     	serverMessage = fromServer.readLine();
	 		return serverMessage;
		}
		catch (SocketException e)
		{
			return "failed to read";
		}
	}
}