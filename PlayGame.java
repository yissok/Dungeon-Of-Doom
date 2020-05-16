/**
* This class starts up the clients that want to connect to the server. Thus it takes in inputs and 
* serves them to the Client class that will control the access to the whole game network.
*
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
* @See PlayGame.java
*/


import java.io.IOException;



public class PlayGame
{	
	protected Client cl;
	protected ClientGUI clg;
	protected Bot myBot;
	private boolean keepRunning;
	private boolean clientIsHuman;
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @field cl
		* global object to access methods from Client class
		* 
		* @field userInput
		* global object to read data from client
		* 
		* @field keepRunning
		* tells the while loop that listens for server responses if the connection was shut down
		* 
	* * */
	public PlayGame() throws IOException
	{
		clientIsHuman=true;
		setKeepRunning(true);
		clg = new ClientGUI(this);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields. To differentiate humans and bots.
	    * 
	    * @param clientIsHuman
	    * boolean value
	    * 
	 * **/
	public PlayGame(boolean clientIsHuman) throws IOException
	{
		this.clientIsHuman=clientIsHuman;
		setKeepRunning(true);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method calls the right client constructor to let him know where to send the messages
	    * received by the server (to the bot in this case)
	    * 
	    * @param myBot
	    * instance of the bot class
	    * 
	 * **/
	public void setBotGame(Bot myBot)
	{
		try
		{
			cl = new Client(myBot,false);
		}
		catch (IOException e){}
		
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method creates the new instance of the client end of the game for a human player
	    * 
	    * @param clg
		* clientGui object to allow input of server messages in that class
	    * 
	 * **/
	public void setUpClient(ClientGUI clg) throws IOException
	{
		cl = new Client(clg,clientIsHuman);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method was the old while loop, we don't need it since we have a gui
	    * 
	* * */
	/*public void update() throws IOException
	{
		String answer = "";
		String input=null;
		do
		{
			input=readUserInput();
			answer=howManyLinesToRead(input);
			printAnswer(answer);
			if(isKeepRunning()==false)
			{
				break;
			}
		}
		while (true);
	}*/
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sends any command it has been told to the server
	    * 
	    * @param chore
	    * the command string
	    * 
	 * **/
	public void newAction(String chore) throws IOException
	{
		cl.submitQuery(chore);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method posts a look command
	 * **/
	public void lookUpdate() throws IOException
	{
		cl.submitQuery("LOOK");
	}
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method simply prints a string
	    * 
	    * @param 
	    * string to be printed out
	* * */
	protected void printAnswer(String answer)
	{
		System.out.println(answer);
	}

		
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* This method starts the game
	* * */
	public static void main(String [] args) throws IOException
	{
		new PlayGame();//PlayGame game = 
		System.out.println("You may now use MOVE, LOOK, QUIT and any other legal commands");
		//game.update();	
	}

	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Getters and setters for to allow the bot to interact with keepRunning
	* * */
	public boolean isKeepRunning() {
		return keepRunning;
	}


	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}
}