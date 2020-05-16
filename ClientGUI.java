

/**
* This class sets up the window of the client and its GUI components; it is able to do everything
* the text UI used to do but with a graphical interface.
* 
* @author Andrea Lissak
* @version 1.0
* @release 1/04/2016
* @See ClientGUI.java
*/
import java.awt.event.*;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;



public class ClientGUI extends JFrame implements ActionListener,ComponentListener
{	
	private PlayGame myGame;
	private GUIMapManager myMapGUI;
	private SquareField sq;
	
	private JPanel pContainer;
	private JPanel pMap;
	private JPanel pJoystick;
	private JPanel pLogMessages;
	private JPanel pMenu;
	private JPanel pTitle;
	private JPanel pGameInfo;
	
	private JButton btnMoveN;
	private JButton btnMoveS;
	private JButton btnMoveW;
	private JButton btnMoveE;
	private JButton btnPickup;
	private JButton btnQuit;
	
	private JLabel lblTitle;
	private JTextField tfGold;
	private JTextArea taLog;

	
	private boolean gameRunning;
	private boolean firstLook;
	
	private char moveWhere;
	
	protected String serverResp;
	private int goldCoins;
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Calls methods which initialize fields.
		* I am not going to list all components as it would be redundant and useless since the names
		* already speak for themselves.
		* 
		* @field myGame
		* instance of PlayGame class that allows this class to send messages to the server
		* 
		* @field myMapGUI
		* instance of a class that allows to modify the graphical map
		* 
		* @field sq
		* instance of a class that turns rectangular panels into square panels
		* 
		* @field gameRunning
		* is true until the connection is closed
		* 
		* @field firstLook
		* variable to allow the cool animation at the beginning to occur just once
		* 
		* @field moveWhere
		* character that expresses the direction of movement (N, W, S, E) used mainly for animation
		* purposes
		* 
		* @field serverResp
		* string that holds server responses
		* 
		* @field goldCoins
		* a variable that avoids the client to ask "HELLO" to the server, the value is local to this
		* class only
		* 
	* * */
	public ClientGUI(PlayGame g) throws IOException
	{
		setUpVariables(g);
		
		setUpMapLayout();
		setTitleLayout();
		setGameInfoLayout();
		setJoystickLayout();
		setLogMessagesLayout();
		setMenuLayout();
		setMainLayout();
		
		setColors();
		setUpListeners();
        
        this.myGame.setUpClient(this);
        initialCheck();
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method initializes fields and components
	    * 
	    * @param g
	    * to initialize myGame
	    * 
	 * **/
	private void setUpVariables(PlayGame g)
	{
		this.pContainer=new JPanel();
		this.pMap=new JPanel();
		this.pJoystick=new JPanel();
		this.pLogMessages=new JPanel();
		this.pMenu=new JPanel();
		this.pTitle=new JPanel();
		this.pGameInfo=new JPanel();
		this.sq=new SquareField();
	
		this.btnMoveN = new JButton("N");
		this.btnMoveS = new JButton("S");
		this.btnMoveW = new JButton("W");
		this.btnMoveE = new JButton("E");
		this.btnPickup = new JButton("¤");
		btnPickup.setFont(new Font(btnPickup.getName(), Font.PLAIN, 18));
		this.btnQuit = new JButton("Quit");
		
		this.lblTitle = new JLabel("Dungeon Of Doom");
		this.lblTitle.setForeground(Color.orange);
		lblTitle.setFont(new Font(lblTitle.getName(), Font.BOLD, 16));
		this.tfGold = new JTextField(" Gold Coins: 0 ");
		this.tfGold.setEditable(false);
		this.taLog = new JTextArea(3, 20);
		
		this.firstLook=true;
		this.moveWhere='X';
		this.gameRunning=true;
		this.myGame=g;
		this.serverResp=null;
		this.goldCoins=0;
		
		this.myMapGUI=new GUIMapManager();
		this.myMapGUI.setBackground(Color.BLACK);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets up the container for the map and initializes the GUIMapManager map
	    * 
	    * @localVariables/objects:
	    * c: constraints for the layout that stretch the content of the sq panel to the borders
	    * 
	 * **/
	private void setUpMapLayout()
	{
		pMap.setLayout(new GridBagLayout());
		
		sq.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx=1;
		c.weighty=1;
		c.fill = GridBagConstraints.BOTH;
		
		myMapGUI.setMapLayout();
		sq.add(myMapGUI,c);
		
		pMap.add(sq);
		
	}

	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method adds the title to the top panel
	    * 
	 * **/
	private void setTitleLayout()
	{
		pTitle.setLayout(new GridBagLayout());
		pTitle.add(lblTitle);
	}


	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets up the top right panel that tells how much gold was collected; it also
	    * defines the space that has to be left between the component and the border
	    * 
	    * @localVariables/objects:
	    * c: constraints as above
	    * 
	 * **/
	private void setGameInfoLayout()
	{
		pGameInfo.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx=1;
		c.weighty=1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(10, 60, 10, 60);
		pGameInfo.add(tfGold,c);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets up the bottom right panel that contains the button to disconnect; it also
	    * defines the space that has to be left between the component and the border
	    * 
	    * @localVariables/objects:
	    * c: constraints as above
	    * 
	 * **/
	private void setMenuLayout()
	{
		pMenu.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx=1;
		c.weighty=1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(10, 60, 10, 60);
		pMenu.add(btnQuit,c);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets up the bottom left panel that contains the textArea for log messages; it
	    * also defines the space that has to be left between the component and the border
	    * 
	    * @localVariables/objects:
	    * c: constraints as above
	    * 
	 * **/
	private void setLogMessagesLayout()
	{
		taLog.setText("Use \"N\", \"E\", \"S\" and \"W\" buttons to MOVE, use the central \"¤\" button to PICKUP coins and press the \"quit\" button to end the game.");
		taLog.setLineWrap(true);
		taLog.setEditable(false);
		
		
		pLogMessages.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx=1;
		c.weighty=1;
		c.fill = GridBagConstraints.BOTH;
		c.insets=new Insets(10, 20, 10, 20);
		
		pLogMessages.add(taLog,c);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets up the middle right panel that contains the joyStick for movements; it
	    * thus sets the control panel in the middle
	    * 
	    * @localVariables/objects:
	    * sq:
	    * c: constraints as above
	    * 
	 * **/
	private void setJoystickLayout()
	{
		SquareField sq=new SquareField();
		sq.setBackground(new Color(0, 77, 38));
		sq.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		btnMoveN.setMargin(new Insets(0, 0, 0, 0));
		btnMoveS.setMargin(new Insets(0, 0, 0, 0));
		btnMoveW.setMargin(new Insets(0, 0, 0, 0));
		btnMoveE.setMargin(new Insets(0, 0, 0, 0));
		btnPickup.setMargin(new Insets(0, 0, 0, 0));
		
		c.insets=new Insets(5, 5, 5, 5);
		c.weightx=1;
		c.weighty=1;
		c.fill = GridBagConstraints.BOTH;
		
		c.gridx = 1;
		c.gridy = 0;
		sq.add(btnMoveN, c);
		
		c.gridx = 0;
		c.gridy = 1;
		sq.add(btnMoveW, c);
		
		c.gridx = 1;
		c.gridy = 1;
		sq.add(btnPickup, c);
		
		c.gridx = 2;
		c.gridy = 1;
		sq.add(btnMoveE, c);
		
		c.gridx = 1;
		c.gridy = 2;
		sq.add(btnMoveS, c);
		
		pJoystick.setLayout(new GridBagLayout());
		
		pJoystick.add(sq);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method tells all the sub panels above where to stand
	    * 
	    * @localVariables/objects:
	    * c: constraints that also set coordinates for components now
	    * 
	 * **/
	public void setMainLayout()
	{
		pContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx=1;
		c.weighty=1;
		c.gridx = 0;
		c.gridy = 0;
		pContainer.add(pTitle, c);

		c.weightx=0.7;
		c.weighty=1;
		c.gridx = 1;
		c.gridy = 0;
		pContainer.add(pGameInfo, c);

		c.weightx=2;
		c.weighty=2;
		c.gridx = 0;
		c.gridy = 1;
		pContainer.add(pMap, c);

		c.weightx=0.7;
		c.weighty=1;
		c.gridx = 1;
		c.gridy = 1;
		pContainer.add(pJoystick, c);

		c.weightx=1;
		c.weighty=1;
		c.gridx = 0;
		c.gridy = 2;
		pContainer.add(pLogMessages, c);

		c.weightx=0.7;
		c.weighty=1;
		c.gridx = 1;
		c.gridy = 2;
		pContainer.add(pMenu, c);
		
		this.add(pContainer);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(600,400));
		this.setMaximumSize(new Dimension(1000,1000));
		this.setTitle("Dungeon Of Doom");
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets colors for all subPanels
	    * 
	 * **/
	public void setColors()
	{
		pContainer.setBackground(new Color(0, 77, 38));
		pMap.setBackground(new Color(0, 102, 51));
		pJoystick.setBackground(new Color(0, 77, 38));
		pLogMessages.setBackground(new Color(0, 61, 31));
		pMenu.setBackground(new Color(0, 51, 26));
		pTitle.setBackground(new Color(0, 61, 31));
		pGameInfo.setBackground(new Color(0, 51, 26));
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method makes sure that buttons actually do something when they are pressed
	    * 
	 * **/
	public void setUpListeners()
	{
		btnMoveN.addActionListener(this);
		btnMoveS.addActionListener(this);
		btnMoveW.addActionListener(this);
		btnMoveE.addActionListener(this);
		btnPickup.addActionListener(this);
		btnQuit.addActionListener(this);
		
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent winEvt)
            {
            	moveWhere='X';
				doWhat("QUIT");
                System.exit(0);
            }
        });
        this.addComponentListener(this);
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is a spawn update to already perform a LOOK command when the game starts
	    * 
	 * **/
	private void initialCheck() throws IOException
	{
		serverResp="";
		myGame.lookUpdate();
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method resizes the window on a diagonal that keeps the same ratio (H/W). This
	    * solution was taken from the web as I was quite ignorant on the knowledge of component
	    * event objects. (//http://stackoverflow.com/questions/5449893/j-frame-resizing-in-an-aspect
	    * -ratio)
	    * 
	    * @param e
	    * interrupt object launched on resize
	    * 
	    * @localVariables/objects:
	    * W: width
	    * H: height
	    * b: rectangle of window
	    * 
	 * **/
	public void componentResized(ComponentEvent e) {
	    int W = 450;  
	    int H = 350;  
	    Rectangle b = e.getComponent().getBounds();
	    e.getComponent().setBounds(b.x, b.y, b.width, b.width*H/W);
	}
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * Methods that need to be overridden
	 * **/
	public void componentMoved(ComponentEvent e){}
	public void componentShown(ComponentEvent e){}
	public void componentHidden(ComponentEvent e){}
	
	

////////////////////////////////////////////////////////////////////////////////////////////////////  
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
	
		//HERE ENDS THE LAYOUT PART. NOW LOGIC IS APPLIED TO COMPONENTS
	
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method talks to the PlayGame class, and asks it to send a message to the server. It
	    * is called when the player presses a button.
	    * 
	    * @param act
	    * the actual message to be sent
	    * 
	 * **/
	private void doWhat(String act)
	{
		try
		{
			serverResp="";
			myGame.newAction(act);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}


	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is to stop execution for n° milliseconds
	    * 
	    * @param nMS
	    * n° of milliseconds
	    * 
	 * **/
	public void sleepAWhile(int nMS)
	{
		try
		{
			Thread.sleep(nMS);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	

	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is only called by the client helper whenever it receives a stream from the
	    * server. Here the string is recognized and based on what it is. If it's 25 characters
	    * long it's treated as a map, however since a map is passed as 5 strings of 5 characters
	    * there's a control structure that avoids the map being treated as another piece of data
	    * (this only if the protocol is kept the way it is; if the protocol is changed, this method
	    * needs to be changed too)
	    * 
	    * @param response
	    * string from server
	    * 
	 * **/
	public void feedInput(String response)
	{
		System.out.println(response);
		if(response.length()==5&&this.serverResp.length()<26)
		{
			if(this.serverResp.length()%5!=0)
			{
				this.serverResp=response;
			}
			else
			{
				this.serverResp=this.serverResp+response;
			}
		}
		else
		{
			this.serverResp=response;
			if(!response.equals("\n"))
			{
				manageResponseForGUI(response);
			}
		}
		
		if(this.serverResp.length()==25)
		{
			if(firstLook==true)
			{
				if(gameRunning)
				{
					firstLook=false;
					myMapGUI.setUpBeforeConverting(serverResp,moveWhere);
					gameRunning=false;
					myMapGUI.convertTilesFirstLook();
					gameRunning=true;
					moveWhere='X';
				}
			}
			else
			{
				if(gameRunning)
				{
					myMapGUI.setUpBeforeConverting(serverResp,moveWhere);
					gameRunning=false;
					myMapGUI.convertTiles(serverResp,false);
					gameRunning=true;
					moveWhere='X';
				}
				this.serverResp="";
			}
		}
	}
	

	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method looks at the input from the server and decides how to update the textArea, 
	    * the textField and the map. It is called in particular cases of input from "feedInput".
	    * 
	    * @param response
	    * response from server
	    * 
	    * @localVariables/objects:
	    * completeMex: what is already written on the textArea
	    * 
	 * **/
	private void manageResponseForGUI(String response)
	{
		String completeMex;
		completeMex=taLog.getText();
		if((!((completeMex.endsWith("exit"))||(response.equals("\n"))))&&(gameRunning))
		{
			if((response.equals("FAIL"))||(response.startsWith("SUCCESS")))
			{
				completeMex=response;
				taLog.setText(response+"\n");
				
				if(response.startsWith("SUCCESS, GOLD COINS: "))
				{
					goldCoins++;
					this.tfGold.setText(" Gold Coins: "+goldCoins+" ");
				}
			}
			else
			{
				
				if((response.equals("Congratulations!!! "))||(response.equals(" You have escaped the Dungeon of Dooom!!!!!! "))||(response.equals("Thank you for playing!"))||(response.equals("The game will now exit")))
				{
					if(response.equals("Congratulations!!! "))
					{
						taLog.setText("");
					}
					taLog.append(response);
					if(response.endsWith("exit"))
					{
						if(gameRunning)
						{
							serverResp="GGPGGGGPGGPPPPPGPPPGGGPGG";
							myMapGUI.setUpBeforeConverting(serverResp,moveWhere);
							gameRunning=false;
							myMapGUI.convertTilesFirstLook();
							moveWhere='X';
						}
					}
				}
				else
				{
					taLog.append(response);
				}
			}
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is triggered any time the player presses one of the buttons; according to the
	    * source button, it performs different operations, (mainly what it takes to tell the server
	    * to MOVE, PICKUP and QUIT, the LOOK function is implied in feature 1 and 2 since it is
	    * performed for all whenever any player does something)
	    * 
	    * @param e
	    * interrupt signal
	    * 
	 * **/
	public void actionPerformed(ActionEvent e)
	{
		if(gameRunning)
		{
			taLog.setText("");
			if(e.getSource()==btnMoveN)
			{
				moveWhere='N';
				doWhat("MOVE N");
			}
			else if(e.getSource()==btnMoveS)
			{
				moveWhere='S';
				doWhat("MOVE S");
			}
			else if(e.getSource()==btnMoveW)
			{
				moveWhere='W';
				doWhat("MOVE W");
			}
			else if(e.getSource()==btnMoveE)
			{
				moveWhere='E';
				doWhat("MOVE E");
			}
			else if(e.getSource()==btnPickup)
			{
				moveWhere='X';
				doWhat("PICKUP");
			}
			else if(e.getSource()==btnQuit)
			{
				moveWhere='X';
				doWhat("QUIT");
			}
		}
	}
}