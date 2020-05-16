/**
* This class is called from the Server and "keeps in touch" with a specific client. It secondly
* performs a spell check on the input and finally directly communicates with gameLogic
* 
* @author Andrea Lissak
* @version 2.0
* @release 1/04/2016
* @See ServerHelper.java
*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHelper implements Runnable
{
	private int socketNum;
	private Socket myCliSerSocket;
	private GameLogic myGame;
	private Server ser;
	private PrintWriter toClient;
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @field socketNum
		* this can be referred as a "player number"
		* 
		* @field myCliSerSocket
		* instance of socket of client, the most important variable in the network part
		* 
		* @field myGame
		* instance of the gameLogic, allows direct changes to the game
		* 
	* * */
	public ServerHelper(Server ser, Socket myCliSerSocket,int socketNum, GameLogic myGame)
	{
		this.myCliSerSocket=myCliSerSocket;
		this.socketNum=socketNum;
		this.myGame=myGame;
		this.ser=ser;
		myGame.setPlayerNumber(socketNum,socketNum);
		myGame.setPos();
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This is the implemented thread method.
	    * This method runs an infinite loop until conditions of winning, no connection and close
	    * requests are met. It continuously waits for client data and responds.
	    * 
	    * @localVariables/objects:
	    * inputFromClient	: BufferedReader object to read the client socket
	    * toClient			: PrintWriter to send data to client
	    * anyInput			: temporary request holder
	    * response			: temporary response holder
	    * 
	    * 
	* * */
	public void run()
	{
		try
		{
			BufferedReader inputFromClient = null;
			toClient = null;
			String anyInput=null;
			String response=null;
			try
			{
				inputFromClient = new BufferedReader(new InputStreamReader(myCliSerSocket.getInputStream()));
				toClient = new PrintWriter(myCliSerSocket.getOutputStream(), true);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			while (true)
			{
				anyInput=inputFromClient.readLine();
				myGame.setPlayerNumber(socketNum,-1);
				response=parseCommand(anyInput);
				if(myGame.checkWin())
				{
					ser.doLookForAll();
					System.out.println("User "+(socketNum+1)+" disconnected");
					toClient.println("Congratulations!!! \n You have escaped the Dungeon of Dooom!!!!!! "+"\n"+"Thank you for playing!"+"\n"+"The game will now exit\nSUCCESS");
					break;
				}
				try
				{
					if(anyInput.equals("QUIT"))
					{
						myGame.eliminateFromGrid(socketNum);
						ser.doLookForAll();
						System.out.println("User "+(socketNum+1)+" disconnected");
						toClient.println(response);
						toClient.println("FAIL");
						break;
					}
				}
				catch(NullPointerException e)
				{
					myGame.eliminateFromGrid(socketNum);
					ser.doLookForAll();
					System.out.println("User "+(socketNum+1)+" disconnected");
					break;
				}
				
				toClient.println(response);
				String[] command = anyInput.trim().split(" ");
				if(command[0].equals("MOVE"))
				{
					ser.doLookForAll();
				}
			}
			inputFromClient.close();
			toClient.close();
		    myCliSerSocket.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * Added method that tells the Server class that spawned all threads to notify also all other
	    * threads.
	* * */
	public void doLookForAll()
	{
		try
		{
			myGame.setPlayerNumber(socketNum,-1);
			toClient.println(parseCommand("LOOK"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	

	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method checks if the protocol is followed, if the request is correctly written it
	    * gives out the data required, if it is not it returns "FAIL"
	    * 
	    * @param readUserInput
	    * 
	    * 
	    * @localVariables/objects:
	    * command: string array that holds different words in the request (mainly used for "MOVE")
	    * answer : temporary string
	    * 
	    * @return
	    * either consistent data or a fail
	* * */
	private String parseCommand(String readUserInput) throws IOException
	{
		String [] command=null;
		String answer = "FAIL";
		try
		{
			command = readUserInput.trim().split(" ");
			switch (command[0].toUpperCase())
			{
				case "HELLO":
					answer = hello();
					break;
				case "MOVE":
					if (command.length==2)
					answer = move(command[1].charAt(0));
				break;
				case "PICKUP":
					answer = pickup();
					break;
				case "LOOK":
					answer = look();
					break;
				case "QUIT":
					answer=myGame.quitGame();
					
					break;
				default:
					answer = "FAIL";
			}
			
			return answer;
		}
		catch (NullPointerException e)
		{
			myCliSerSocket.close();
			return "FAIL";
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * All these methods call the corresponding function in game logic
	    * 
	    * @return
	    * the response to the specific query
	    * 
	* * */
	public String hello() throws IOException {
		String retSt=myGame.hello();
		return retSt;
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * <See above>
	    * 
	* * */
	public String move(char direction) throws IOException {
		return myGame.move(direction);
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * <See above>
	* * */
	public String pickup() throws IOException {
		return myGame.pickup();
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * <See above>
	* * */
	public String look() throws IOException {
		return myGame.look();
	}
}