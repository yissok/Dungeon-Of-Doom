/**
* This class is the brain of the bot that tells him how to behave.
* 
* @author Andrea Lissak
* @version 2.0
* @release 1/04/2016
* @See Bot.java
*/


import java.io.IOException;
import java.util.Random;



public class Bot extends PlayGame
{
	private Random random;
	private static final char [] DIRECTIONS = {'N','S','E','W'};
	private String answer;
	private String act;
	private int phase;
	private boolean locked;//PHASE 1
	
	private char whereToGo;
	private boolean switchApproach;//PHASE 2
	private String serverResp;
	
	private boolean doingMovement;
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @field random
		* random object to choose a random direction in case an unexpected major error not due to
		* the AI occurs
		* 
		* @field DIRECTIONS[]
		* possible choices of random move
		* 
		* @field answer
		* server response as a FAIL or SUCCESS string
		* 
		* @field act
		* bot action
		* 
		* @field phase
		* phase in which the bot is located (either "go top left" mode or "sweep" mode)
		* 
		* @field locked
		* if the bot found the spot from which he can start sweeping (does not have to be
		* necessarily the top left corner)
		* 
		* @field whereToGo
		* next direction to follow
		* 
		* @field switchApproach
		* if sweeping down then switchApproach = true
		* 
		* @field serverResp
		* holds whatever was sent back from the server
		* 
		* @field doingMovement
		* if the bot is performing a movement action, then the value of the field is true
		* 
	* * */
	public Bot() throws IOException
	{
		super(false);
		phase=0;
		whereToGo='N';
		answer="SUCCESS";
		locked=false;
		switchApproach=false;
		act="";
		doingMovement=false;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is not used, it's just here as a last resort, no need to look at it
	* * */
	private String randomThings(int i) throws IOException
	{
		String botAns="";
		switch(i%3)
		{
			case 0:
				act="LOOK";
				newAction(act);
				try{Thread.sleep(100);}
				catch(InterruptedException e){}
				botAns=serverResp;		
			break;
			case 1:
				act="PICKUP";
				newAction(act);
				try{Thread.sleep(100);}
				catch(InterruptedException e){}
				botAns=serverResp;		
			break;
			case 2:
				act="MOVE " + DIRECTIONS[random.nextInt(4)];
				newAction(act);
				try{Thread.sleep(100);}
				catch(InterruptedException e){}
				botAns=serverResp;		
			break;
			default:
				act="LOOK";
				newAction(act);
				try{Thread.sleep(100);}
				catch(InterruptedException e){}
				botAns=serverResp;		
			break;
		}
		return botAns;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method aims to get to the top left corner, this ideally would work, but only when
	    * the dungeon is populated just with this bot. Other human players can foresee what he is
	    * going to do and get on its way to annoy him and making him think he's on the top left
	    * corner when he is actually not.
	    * 
	    * @param i
	    * index of number of actions performed by bot
	    * 
	    * @localVariables/objects:
	    * botAns: copy of response from server
	    * 
	    * @return
	    * response from server
	    * 
	* * */
	private String goToTopLeft(int i) throws IOException
	{
		String botAns="";
		if(answer.equals("FAIL"))
		{
			if(locked)
			{
				phase++;
				whereToGo='E';
				answer="SUCCESS TOP CORNER";
				sweepField(i);
			}
			else
			{
				locked=true;
				whereToGo='W';
				botAns=microMovement();
			}
		}
		else if(answer.equals("SUCCESS"))
		{
			botAns=microMovement();
		}
		else
		{
			System.out.println("not a valid answer");
		}
		return botAns;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method makes the bot go from left to right continuously and steps down a tile when
	    * it meets an obstacle. When it thinks it reached the bottom, it steps up one after 
	    * sweeping rows.
	    * As before other human players can foresee what he is going to do and get on its way to 
	    * annoy him and making him think he has reached the wall when it's actually a player.
	    * 
	    * @param i
	    * index of number of actions performed by bot
	    * 
	    * @localVariables/objects:
	    * botAns: copy of response from server
	    * 
	    * @return
	    * response from server
	* * */
	private String sweepField(int i) throws IOException
	{
		String botAns="";
		{
			if(answer.equals("FAIL"))
			{
				char previousDir=whereToGo;
				if(switchApproach)
				{
					botAns=microMovement();
					switchApproach=false;
				}
				else
				{
					chooseVerticalDirectionAccordingToState();
					botAns=microMovement();
					if(botAns.equals("FAIL"))//if there is a blockage while going down then go back (might be wall or player)
					{
						switchPhase();
					}
					chooseHorizontalDirectionAccordingToState(previousDir);
				}
				
			}
			else//keep performing the horizontal movement
			{
				botAns=microMovement();
			}
		}
		
		return botAns;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method changes the phase unlocking for just a moment the switchApproach variable
	    * 
	* * */
	private void switchPhase()
	{
		if(phase==1)
		{
			phase++;
		}
		else if(phase==2)
		{
			phase--;
		}
		switchApproach=true;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells to set the new direction according to the phase the bot is in 
	    * 
	* * */
	private void chooseVerticalDirectionAccordingToState()
	{
		if(phase==1)
		{
			whereToGo='S';
		}
		else if(phase==2)
		{
			whereToGo='N';
		}
		else
		{
			System.err.println("UNEXPECTED DIRECTION (phase issue)");
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells to set the new direction as opposite as before
	    * 
	    * @param previousDir
	    * previous direction
	    * 
	* * */
	private void chooseHorizontalDirectionAccordingToState(char previousDir)
	{
		if(previousDir=='E')
		{
			whereToGo='W';
		}
		else if(previousDir=='W')
		{
			whereToGo='E';
		}
		else
		{
			System.err.println("UNEXPECTED DIRECTION");
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells the bot to move in the specified direction
	    * 
	    * @localVariables/objects:
	    * botAns: copy of response from server
	    * @return
	    * response from server
	    * 
	* * */
	private String microMovement() throws IOException
	{
		String botAns="";
		act="MOVE "+whereToGo;
		newAction(act);
		try{Thread.sleep(100);}
		catch(InterruptedException e){}
		botAns=answer;		
		return botAns;
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells the bot what to do according to the index; if the switch is entered
	    * it's going to direct the bot to which of the two strategies to follow
	    * 
	    * @param i
	    * index of number of actions performed by bot
	    * 
	    * @localVariables/objects:
	    * botAns: copy of response from server
	    * 
	    * @return
	    * response from server
	    * 
	* * */
	private String botPossibilities(int i) throws IOException
	{
		String botAns="";
		if(i%3==0)
		{
			act="PICKUP";
			newAction(act);
			try{Thread.sleep(100);}
			catch(InterruptedException e){}
			botAns=serverResp;		
		}
		else if(i%3==1)
		{
			switch(phase)
			{
				case 0:botAns=goToTopLeft(i);
				break;
				case 1:botAns=sweepField(i);
				break;
				case 2:botAns=sweepField(i);
				break;
				default:botAns=randomThings(i);
				break;
			}
		}
		else
		{
			act="LOOK";
			newAction(act);
			try{Thread.sleep(100);}
			catch(InterruptedException e){}
			botAns=serverResp;		
		}
		
		
		return botAns;
	} 
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells the bot to pickup and look for every movement that is done
	    * 
	    * @localVariables/objects:
	    * i						: index of number of actions performed by bot
	    * answerToLookAndPickup : local answer not to mess up the answer to movements ("answer")
	    * 
	* * */
	public void update() throws IOException
	{	
		int i=0;
		String answerToLookAndPickup="";
		do
		{
			
			if(i%3==1)//if moves
			{
				doingMovement=true;
				System.out.println("act: MOVE");

				answer=botPossibilities(i);
				printBotAnswer(answer);
				doingMovement=false;
			}
			else if(i%3==2) //if looks
			{
				System.out.println("act: LOOK");

				answerToLookAndPickup=botPossibilities(i);
				printBotAnswer(answerToLookAndPickup);
			}
			else//if picks up
			{
				System.out.println("act: PICKUP");
				answerToLookAndPickup=botPossibilities(i);
				printBotAnswer(answerToLookAndPickup);
			}
			dontWinTooQuick();
			i++;
			if(isKeepRunning()==false)
			{
				break;
			}
		}
		while (true);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method stops execution for a little amount of time to avoid bots being all over the 
	    * place
	* * */
	private void dontWinTooQuick()
	{
		try
		{
		    Thread.sleep(800);//pause the thread for a second
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method prints the resulting response to the bot
	    * 
	    * @param answer
	    * server reponse
	    * 
	* * */
	private void printBotAnswer(String answer)
	{
		System.out.println("ans: "+answer);
		System.out.println("\n\n\n\n\n\n");
		
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is exclusively called from the outside (by "ClientHelper") and if it receives
	    * a SUCCESS or FAIL response during a movement it updates the variable "answer", the most
	    * important variable that the bot uses to check if his path is free or not
	    * 
	    * @param response
	    * is always assigned to the "serverResp" field which is used to print the map and the result
	    * of a pickup. It also updates the variable "answer" which the bot uses to check if his 
	    * path is free or not
	    * 
	 * **/
	public void feedInput(String response)
	{
		if(doingMovement)
		{
			if((response.equals("SUCCESS"))||(response.equals("FAIL")))
			{
				answer=response;
			}
		}
		System.out.println("resp feed: "+response);
		this.serverResp=response;
	}
	
}
