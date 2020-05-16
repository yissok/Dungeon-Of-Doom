/**
* This class starts up the server, sets up the map, creates the GameLogic instance and finally
* accepts multiple client connections by putting every user data stream in a different thread.
*
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*  
*  _ _ _ _ _ _ _ _ _ _ _ _ 
* _						  _
* _	 PlayGame		Bot   _
* _			|		  |   _
* _			-----------	  _	   String
* _				  |		  _	 DATA STREAM  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
* _				  Client  _   . . . . .   _ Server										_
*  _ _ _ _ _ _ _ _ _ _ _ _		  .		  _		 |										_
*								  .		  _		 |--------								_
*								  .		  _		 |		 |								_
*								  .	. .	  _	 . . |	. .	 ServerHelper					_
*										  _		 |					|					_
*										  _		 |-------------------					_
*										  _		 |										_
*										  _		 GameLogic								_
*										  _				 |								_
*										  _				 |--------						_
*										  _				 |		 |						_
*										  _				 |		 PositionHandler		_
*										  _				 |					   |		_
*										  _				 |----------------------		_
*										  _				 |								_
*										  _				 Map							_
*										  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
*
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*  ------>> SEE UPDATED MAP IN REQUIREMENTS PDF! <<-----
*
* @author Andrea Lissak
* @version 2.0
* @release 1/04/2016
* @See Server.java
*/


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server


{
	private GameLogic myGame;
	protected Scanner userInput;
	private int maxSocketNum;
	private Socket myCliSerSocket;
	private ServerHelper myHelp[];
	private Thread myThread[];
	private final int maxPlayerCount;
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @field myGame
		* game object to start gameLogic once
		* 
		* @field userInput
		* Scanner object to accept the map
		* 
		* @field maxSocketNum
		* simply counts all clients connected and prevents the server to accept more than 10 clients
		* 
	* * */
	public Server()
	{
		maxPlayerCount=20;
		myHelp=new ServerHelper[maxPlayerCount];
		myThread=new Thread[maxPlayerCount];
		myGame=null;
		userInput=null;
		maxSocketNum=0;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method asks the user for what map to play in.
	    * 
	    * @localVariables/objects:
	    * answer: temporary string
	    * 
	* * */
	private void readMapInput()
	{
		String answer = "";
		userInput = new Scanner(System.in);
		boolean checkAgain=false;
		do
		{
			System.out.println("Do you want to load the previous game?(y/n)");
			answer=userInput.nextLine();
			if(answer.equals("y"))
			{
				checkAgain=false;
				myGame.setMap("maps/tempMap.txt");
				myGame.setUpVarsFromFile();
				System.out.println("If many clients were active while the server crashed you can"
						+ " now restart them one by one. Be aware that they are going to reload"
						+ " in the same order in which they have been started.");
			}
			else if(answer.equals("n"))
			{
				checkAgain=false;
				System.out.println("Do you want to load a specitic map?");
				System.out.println("Press enter for default map");
				answer=userInput.nextLine();
				//answer="A";
				myGame.setMap("maps/"+answer+".txt");
				System.out.println("You can now connect clients to this server.");
			}
			else
			{
				System.out.println("Only 'y' or 'n' characters\n\n\n");
				checkAgain=true;
			}
		}
		while(checkAgain);
		
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * Accessor method 
	    * 
	    * @return
	    * max sockets for now
	* * */
	public int getMaxSocketNum()
	{
		return maxSocketNum;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method initiates the global gameLogic object
	* * */
	private void initLogic()
	{
		myGame=new GameLogic(maxPlayerCount);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method creates a new thread for each client that connects to the server
	    * 
	    * @localVariables/objects:
	    * myServerSideSocket: server side socket (only specifies port)
	    * myCliSerSocket    : client side socket; this is stored in the serverHelper class thread
	    * 					  to keep the connection alive
	    * 
	* * */
	private void processConnection() throws IOException
	{
		ServerSocket myServerSideSocket = new ServerSocket(40004);
		try
		{
			maxSocketNum=0;
			while (maxSocketNum<maxPlayerCount)
			{
				myCliSerSocket = myServerSideSocket.accept();
				System.out.println((maxSocketNum+1)+") SERVER CONNECTION: "+myCliSerSocket);
				
				myHelp[maxSocketNum]=new ServerHelper(this,myCliSerSocket,maxSocketNum,myGame);
				myThread[maxSocketNum]=new Thread(myHelp[maxSocketNum]);
				myThread[maxSocketNum].start();
				doLookForAll();
				maxSocketNum++;
			}
		}
		finally
		{
			myServerSideSocket.close();
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method goes through every single thread (connected either to a bot or human client)
	    * and sends a look response even if it was not requested by those clients, to let them know
	    * about other players actions.
	* * */
	public void doLookForAll()
	{
		for(int i=0;i<maxSocketNum;i++)
		{
			myHelp[i].doLookForAll();
		}
	}
	
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This main initiates the gameLogic and the map; it finally waits for clients to connect.
	    * 
	    * @localVariables/objects:
	    * s: Server class object
	    * 
	* * */
	public static void main(String[] args) throws IOException
	{
		Server s=new Server();
		s.initLogic();
		s.readMapInput();
		s.processConnection();
	}

}