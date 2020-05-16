/**
* This class performs all operations concerning the logic of the game, this means moving players,
* knowing when someone won, providing information to clients that request it and more...
* 
* @author (Tutors who invented game) + (Andrea Lissak)
* @version 2.0
* @release 1/04/2016
* @See GameLogic.java
*/




public class GameLogic implements IGameLogic
{
	static
	{
		System.load(System.getProperty("user.dir")+"/jni/libGameLogic.so");
	}

	private Map map = null;
	private int[][] playerPosition;
	private int[] collectedGold;
	private boolean[] active;
	private boolean[] standingOnGold;
	private boolean[] standingOnExit;
	private int playerNumber;
	private int maxSocketNum;
	private int maxFromLastSession;
	private boolean doINeedToSetUpPlayer;

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
	    * @param maxPlayerCount 
		*
		* @field map
		* instance to modify the map
		* 
		* @field pos
		* object that takes care of moving players
		* 
		* @field playerPosition[][]
		* position (x,y) for each player
		* 
		* @field collectedGold[]
		* gold for each player
		* 
		* @field active[]
		* tells the state of each player
		* 
		* @field standingOnGold[]
		* if a player is standing on gold and still has not picked it up
		* 
		* @field standingOnExit[]
		* if a player is standing on an exit
		* 
		* @field playerNumber
		* the index number of each player (is set by every player before performing any computation)
		* 
		* @field maxSocketNum
		* the largest player number
		* 
	* * */
	public GameLogic(int maxPlayerCount)
	{
		doINeedToSetUpPlayer=true;
		playerNumber=-1;
		maxFromLastSession=-1;
		maxSocketNum=maxPlayerCount;//max <maxPlayerCount> clients
		playerPosition = new int[maxSocketNum][2];
		collectedGold = new int[maxSocketNum];
		active = new boolean[maxSocketNum];
		standingOnGold = new boolean[maxSocketNum];
		standingOnExit = new boolean[maxSocketNum];
		for(int i=0;i<maxPlayerCount;i++)
		{
			playerPosition[i][0]=-42;
			playerPosition[i][1]=-42;
			standingOnGold[i]=false;
			standingOnExit[i]=false;
			collectedGold[i]=0;
			active[i]=false;
		}
		map = new Map();
	}
	
	/*
	 * Sorry for this method, I could not make it... this coursework drained all my energies
	 * */
	public void setUpVarsFromFile()
	{
		doINeedToSetUpPlayer=false;
		map.prepareVarsOfTempFile();
		playerPosition=map.getplayerPosition();
		standingOnGold=map.getstandingOnGold();
		standingOnExit=map.getstandingOnExit();
		collectedGold=map.getcollectedGold();
		active=map.getactive();
		maxFromLastSession=map.getmaxFromLastSession();
	}
	
	public native String manageHello();
	public native String managePickup();
	public native String manageLook();
	public native String manageMove(char direction);
	public native Map manageGetMap();
	public native void manageSetMap(String path);
	public native void manageSetPos();
	public native void manageSetPlayerNumber(int n,int max);
	public native void managePrintMap();
	public native int[] manageInitiatePlayer();
	public native boolean manageCheckWin();
	public native String manageQuitGame();
	public native boolean manageGameRunning();
	public native void manageEliminateFromGrid(int socketNum);
	public native int[] manageChangePosition(int posX, int posY, char direction);
	public native void manageDeletePosS(int[] pos);
	public native void manageDeletePosN(int[] pos);
	public native void manageDeletePosE(int[] pos);
	public native void manageDeletePosW(int[] pos);
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This Accessor returns the map object
	    * 
	    * @return
	    * map
	* * */
	protected Map getMap()
	{
		return manageGetMap();
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method calls another method in the Map class to build the dungeon
	    * 
	    * @param file
	    * file variable passed by the server according to the map file selected by the user
	    * 
	* * */
	public void setMap(String path)
	{
		manageSetMap(path);
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets a player's position once it entered the dungeon.
	    * It's synchronized because we don't want two different clients to be spawned in the same
	    * spot.
	* * */
	synchronized public void setPos()
	{
		manageSetPos();
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets the player number and is called as the first action of any player.
	    * It's synchronized because we don't want two different clients to access the same player
	    * number.
	    * 
	    * @param n
	    * is actually the socketNumber of the serverHelper object
	    * 
	* * */
	synchronized public void setPlayerNumber(int n,int max)
	{
		manageSetPlayerNumber(n,max);
	}
	
	
	/**
	 * Prints how much gold is still required to win!
	 */
	public String hello()
	{
		return manageHello();
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method asks the PositionHandler class to move the player. If it's possible the
	    * action is carried out, else the player will stay where he is.
	    * It's synchronized because the TILE resource must not be accessed by two or more clients
	    * simultaneously
	    * 
	    * @param direction
	    * a char among W,N,E,S that indicates the direction
	    * 
	    * @localVariables/objects:
	    * newPosition: temporary support variable for player position
	    * 
	    * @return
	    * fail or success according to whether the movement is possible or not
	    * 
	* * */
	public synchronized String move(char direction)
	{
		return manageMove(direction);
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * (This method picks up gold if the player is standing on it and deletes it from the grid.
	    * It's synchronized because the GOLD resource must not be accessed by two or more clients
	    * simultaneously) NOOOO, HAS BEEN IMPROVED FOR CW2: if control on synchronization has
	    * already been performed on the "MOVE" action, there is no chance two players occupy the
	    * same tile with gold on it.
	    * 
	    * @return
	    * either success or fail message
	    * 
	* * */
	public String pickup()
	{
		return managePickup();
	}

	
	/**
	 * The method shows the dungeon around the player location.
	 */
	public String look()
	{
		return manageLook();
	}

	
	/*
	 * Prints the whole map directly to Standard out.
	 */
	public void printMap()
	{
		managePrintMap();
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method keeps randomly choosing a position all over the grid until the tile is a dot.
	    * It's synchronized because we don't want two different clients to occasionally generate
	    * the same position.
	    * 
	    * @localVariables/objects:
	    * pos[]: x,y coordinate array for position
	    * rand : random object
	    * 
	    * @return
	    * position of newly spawned player
	    * 
	* * */
	synchronized private int[] initiatePlayer()
	{	
		return manageInitiatePlayer();
	}
	

	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method checks if a player won
	    * 
	    * @return
	    * boolean variable that tells if player won
	* * */
	protected boolean checkWin()
	{
		return manageCheckWin();
	}

	
	/**
	 * Quits the game when called
	 * 
	 * @return
	 * message
	 */
	public String quitGame()
	{
		return manageQuitGame();
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells if player active
	    * 
	    * @return
	    * relative bool value
	    * 
	* * */
	public boolean gameRunning()
	{
		return manageGameRunning();
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method deletes a player from the grid
	    * 
	    * @param socketnum
	    * player number
	    * 
	* * */
	synchronized public void eliminateFromGrid(int socketNum)
	{
		manageEliminateFromGrid(socketNum);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * All these methods delete the "remaining P" trace and substitute the appropriate value.
	    * 
	    * @param pos[]
	    * position to clear
	    * 
	* * */
	public void deletePosS(int[] pos)
	{
		manageDeletePosS(pos);
		
	}
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * LOOK ABOVE
	* * */
	public void deletePosN(int[] pos)
	{
		manageDeletePosN(pos);
	
	}
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * LOOK ABOVE
	* * */
	public void deletePosE(int[] pos)
	{
		manageDeletePosE(pos);
	}
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * LOOK ABOVE
	* * */
	public void deletePosW(int[] pos)
	{
		manageDeletePosW(pos);
		
	}
	
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells where to write a new P and checks if it is actually possible in case
	    * of the new position being another player or a wall.
	    * 
	    * @param position
	    * old position (is integrated with "direction" to know where to go next)
	    * 
	    * @localVariables/objects:
	    * newPosition: support variable
	    * 
	    * @return
	    * the array that tells the new position
	    * 
	* * */
	public int[] changePosition(int posX, int posY, char direction)
	{
		return manageChangePosition(posX,posY,direction);
	}

}