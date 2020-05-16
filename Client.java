/**
* This class stands between PlayGame and the Server class. It's responsible for simple and
* highly repetitive and "mechanic" computation
* 
* @author Andrea Lissak
* @version 2.0
* @release 1/04/2016
* @See Client.java
*/


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Client
{
	private PrintWriter toServer;
	private Socket myCliSerSocket;
	protected ClientGUI clg;
	protected PlayGame g;
	private Bot myBot;
	private int hostNum=40004;
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @param clg
	    * clientGui object to allow input of server messages in that class
	    * 
	    * @param clientIsHuman
	    * true is received if the flow of execution started in the PlayGame class
	    * 
	    * 
		* 
		*
		* @field myCliSerSocket
		* socket that connects to port number 40004 of localhost (client side)
		* 
		* @field toServer
		* printWriter object to send the string data stream TO the server
		* 
		* @field fromServer
		* bufferedReader object to receive the string data stream FROM the server
		* 
	* * */
	public Client(ClientGUI clg, boolean clientIsHuman) throws IOException
	{
		try
		{
			this.clg=clg;
			myCliSerSocket = new Socket("localhost", hostNum);
			toServer = new PrintWriter(myCliSerSocket.getOutputStream(), true);
			new Thread(new ClientHelper(myCliSerSocket,clg,clientIsHuman)).start();
		}
		catch(ConnectException e)
		{
			System.err.println("Complete operations on the server before running the client.");
			System.err.println("\nThe server is either not running or has not loaded the map yet.");
			System.exit(1);
		}
	}
	
	
	   

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		* 
		* @param myBot
	    * bot instance
	    * 
	    * @param clientIsHuman
	    * false is received if the flow of execution started in the BotLauncher class
		* 
	* * */
	public Client(Bot myBot, boolean clientIsHuman) throws IOException
	{
		try
		{
			this.myBot=myBot;
			myCliSerSocket = new Socket("localhost", hostNum);
			toServer = new PrintWriter(myCliSerSocket.getOutputStream(), true);
			new Thread(new ClientHelper(myCliSerSocket,myBot,clientIsHuman)).start();
		}
		catch(ConnectException e)
		{
			System.err.println("Complete operations on the server before running the client.");
			System.err.println("\nThe server is either not running or has not loaded the map yet.");
			System.exit(1);
		}
	}






	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method holds the input end of the I/O between client and server; it just gives the 
	    * data
	    * 
	    * @param query
	    * command for request
	    * 
	* * */
	public void submitQuery(String query) throws IOException
	{
		toServer.println(query);
	}
	
}