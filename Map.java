/**This class handles the loading of the map and its translation between game and file
* in both ways.
* NOTE: I am deeply sorry for the lack of comments, but time issues prevented me from writing
*       them for the methods in this class (points 3 and 4 of CW3). I hope they are easy to 
* 		follow and understand. Again I apologise for the additional work this might imply.
* @author Tutors+Andrea Lissak
* @version 1.0
* @release 27/04/2016
* @See Map.java
*/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class Map
{
	static
	{
		System.load(System.getProperty("user.dir")+"/jni/libMap.so");
	}
	public native void manageReadMap(String mapFile);
	public native void manageLoadMap(char [][] a);
	public native boolean manageSetWin(String in);
	public native boolean manageSetName(String in);
	
	private char[][] map;
	private String mapName;
	private int totalGoldOnMap;
	
	private int[][] playerPosition;
	private int[] collectedGold;
	private boolean[] active;
	private boolean[] standingOnGold;
	private boolean[] standingOnExit;
	private int maxFromLastSession;
	
	
	public Map(){
		playerPosition = new int[20][2];
		collectedGold = new int[20];
		active = new boolean[20];
		standingOnGold = new boolean[20];
		standingOnExit = new boolean[20];
		for(int i=0;i<20;i++)
		{
			playerPosition[i][0]=-42;
			playerPosition[i][1]=-42;
			standingOnGold[i]=false;
			standingOnExit[i]=false;
			collectedGold[i]=0;
			active[i]=false;
		}
		map = null;
		mapName = "";
		totalGoldOnMap = -1;
	}
	
	public Map(String mapFile)
	{
		this();
		readMap(mapFile);
	}
	
	/**
	 * Reads a map from a given file with the format:
	 * name <mapName>
	 * win <totalGold>
	 * 
	 * @par am map A File pointed to a correctly formatted map file
	 */
	public void readMap(String mapFile)
	{
		manageReadMap(mapFile);
	}
	private void loadMap(char [][] a)
	{
		manageLoadMap(a);
	}
	private boolean setWin(String in)
	{
		return manageSetWin(in);
	}
	private boolean setName(String in)
	{
		return manageSetName(in);
	}
	
		/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets up the temp file
	    * 
	    * 
	* * */
	private void setUpTemp() throws IOException
	{
		try
		{
			PrintWriter writer = new PrintWriter("maps/tempMap.txt", "UTF-8");
			for(int i=0;i<getMapHeight();i++)
			{
				for(int j=0;j<getMapWidth();j++)
				{
					writer.print(map[i][j]);
				}
				writer.println();
			}
			writer.println("$");
			
			
			for(int i=0;i<20;i++)
			{
				writer.print(i);
				writer.print(" ");
				writer.print(playerPosition[i][0]);
				writer.print(" ");
				writer.print(playerPosition[i][1]);
				writer.print(" ");
				writer.print((standingOnGold[i])?1:0);
				writer.print(" ");
				writer.print((standingOnExit[i])?1:0);
				writer.print(" ");
				writer.print(collectedGold[i]);
				writer.print(" ");
				writer.print((active[i])?1:0);
				writer.print(" ");
				writer.println();
			}
			
			
			
			
			writer.close();
		}
		catch (FileNotFoundException e){}
		catch (UnsupportedEncodingException e){}
	
		
	}
	
		/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method writes to the temp file at every impact
	    * 
	    * 
	* * */

	public void writeToTemp()
	{
		
		try
		{
			PrintWriter writer = new PrintWriter("maps/tempMap.txt", "UTF-8");
			for(int i=0;i<getMapHeight();i++)
			{
				for(int j=0;j<getMapWidth();j++)
				{
					writer.print(map[i][j]);
				}
				writer.println();
			}
			writer.println("$");
			
			
			for(int i=0;i<20;i++)
			{
				writer.print(i);
				writer.print(" ");
				writer.print(playerPosition[i][0]);
				writer.print(" ");
				writer.print(playerPosition[i][1]);
				writer.print(" ");
				writer.print((standingOnGold[i])?1:0);
				writer.print(" ");
				writer.print((standingOnExit[i])?1:0);
				writer.print(" ");
				writer.print(collectedGold[i]);
				writer.print(" ");
				writer.print((active[i])?1:0);
				writer.print(" ");
				writer.println();
			}
			writer.println("name "+getMapName());
			writer.println("win "+getWin());
			
			
			
			writer.close();
		}
		catch (FileNotFoundException e){}
		catch (UnsupportedEncodingException e){}
		
	}
	
		/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method reads the info from the temp file when a recovery is required
	    * 
	    * 
	* * */

	private char[][] readFromTemp()
	{
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new FileReader(new File("maps","tempMap.txt")));
		}
		catch (FileNotFoundException e)
		{
			System.err.println("err: tempMap.txt not found");
		}
		
		char[][] localMap = new char[getMapHeight()][getMapWidth()];		
		try
		{
			String in = reader.readLine();
		
			for (int i = 0; i < getMapHeight(); i++)
			{
				for (int j = 0; j < getMapWidth(); j++)
				{
					localMap[i][j] = in.charAt(j);
				}
				in = reader.readLine();
				if(in.equals("$"))
				{
					break;
				}
			}
		}
		catch (IOException e) {}
		
		
		return localMap;
	}
	
	
	/**
	 * The method replaces a char at a given position of the map with a new char
	 * @param y the vertical position of the tile to replace
	 * @param x the horizontal position of the tile to replace
	 * @param tile the char character of the tile to replace
	 * @return The old character which was replaced will be returned.
	 */
	protected char replaceTile(int y, int x, char tile) {
		char output = map[y][x];
		map[y][x] = tile;
		writeToTemp();
		return output;
	}
	protected void printMap()
	{
		map=readFromTemp();
		for (int y = 0; y < getMapHeight(); y++)
		{
			for (int x = 0; x < getMapWidth(); x++)
			{
				System.out.print(map[y][x]);
			}
			System.out.println();
		}
	}
	/**
	 * The method returns the Tile at a given location. The tile is not removed.
	 * @param y the vertical position of the tile to replace
	 * @param x the horizontal position of the tile to replace
	 * @param tile the char character of the tile to replace
	 * @return The old character which was replaced will be returned.
	 */
	protected char lookAtTile(int y, int x)
	{
		map=readFromTemp();
		if (y < 0 || x < 0 || y >= map.length || x >= map[0].length)
			return '#';
		char output = map[y][x];
		
		return output;
	}
	/**
	 * This method is used to retrieve a map view around a certain location.
	 * The method should be used to get the look() around the player location.
	 * @param y Y coordinate of the location
	 * @param x X coordinate of the location
	 * @param radius The radius defining the area which will be returned. 
	 * Without the usage of a lamp the standard value is 5 units.
	 * @return
	 */
	protected char[][] lookWindow(int y, int x, int radius)
	{
		map=readFromTemp();
		char[][] reply = new char[radius][radius];
		for (int i = 0; i < radius; i++) {
			for (int j = 0; j < radius; j++) {
				int posX = x + j - radius/2;
				int posY = y + i - radius/2;
				if (posX >= 0 && posX < getMapWidth() && 
						posY >= 0 && posY < getMapHeight())
					reply[j][i] = map[posY][posX];
				else
					reply[j][i] = '#';
			}
		}
		reply[0][0] = 'X';
		reply[radius-1][0] = 'X';
		reply[0][radius-1] = 'X';
		reply[radius-1][radius-1] = 'X';
		
		return reply;
	}
	public int getWin()
	{
		return totalGoldOnMap;
	}
	public String getMapName() {
		return mapName;
	}
	protected int getMapWidth() {
		return map[0].length;
	}
	protected int getMapHeight() {
		return map.length;
	}
	
	
	
	
	
	
	
	
	
	
	
		/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is called by the gamelogic class and stores the values of the file into 
	    * map's field, which are going to be passed to gamelogic
	    * 
	* * */

	public void prepareVarsOfTempFile()
	{
		//System.out.println("METHOD: prepareVarsOfTempFile");
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new FileReader(new File("maps","tempMap.txt")));
		}
		catch (FileNotFoundException e)
		{
			System.err.println("err: tempMap.txt not found");
		}
		
		
		try
		{
			String in = reader.readLine();
			for (int i = 0; i < getMapHeight(); i++)
			{
				in = reader.readLine();
				if(in.equals("$"))
				{
					break;
				}
			}
			boolean checkLast=true;
			for (int i = 0; i < 20; i++)
			{
				in = reader.readLine();
				String a[]=in.split(" ");
				playerPosition[i][0]=Integer.parseInt(a[1]);
				playerPosition[i][1]=Integer.parseInt(a[2]);
				standingOnGold[i]=(Integer.parseInt(a[3])==1)?true:false;
				standingOnExit[i]=(Integer.parseInt(a[4])==1)?true:false;
				collectedGold[i]=Integer.parseInt(a[5]);
				active[i]=(Integer.parseInt(a[6])==1)?true:false;
				if((playerPosition[i][0]<(-10))&&(checkLast))
				{
					maxFromLastSession=i-1;
					checkLast=false;
				}
			}
			in = reader.readLine();
			setName(in);
			in = reader.readLine();
			setWin(in);
			
			
			
			
			
		}
		catch (IOException e) {}
		
		
	}
		/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is called to give the map class the current state of the game, which
	    * is saved to file with the last instruction
	    * 
	* * */

	public void updateTempDetails(int[][] playerPosition2, int[] collectedGold2, boolean[] active2, boolean[] standingOnGold2, boolean[] standingOnExit2)
	{
		playerPosition=playerPosition2;
		collectedGold=collectedGold2;
		active=active2;
		standingOnGold=standingOnGold2;
		standingOnExit=standingOnExit2;
		writeToTemp();
	}
	public int[][] getplayerPosition() {
		return playerPosition;
	}
	public boolean[] getstandingOnGold() {
		return standingOnGold;
	}
	public boolean[] getstandingOnExit() {
		return standingOnExit;
	}
	public int[] getcollectedGold() {
		return collectedGold;
	}
	public boolean[] getactive() {
		return active;
	}
	public int getmaxFromLastSession() {
		return maxFromLastSession;
	}
	

}
