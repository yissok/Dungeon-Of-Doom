/**
* This class spawns a bot.
* 
* @author Andrea Lissak
* @version 1.0
* @release 1/04/2016
* @See BotLauncher.java
*/

import java.io.IOException;

public class BotLauncher
{
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method 
	    * 
	    * @param 
	    * 
	    * 
	    * @localVariables/objects:
	    * command: 
	    * 
	    * @return
	    * 
	 * **/
	
	   
	
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This main method creates a bot instance and calls two methods to get it running
	 * **/
	public static void main(String [] args) throws IOException 
	{
		Bot game = new Bot();
		game.setBotGame(game);
		game.update();	
	}
	
}

