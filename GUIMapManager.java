/**
* This class manages the map in all ways, it only receives the input map and the direction of
* movement and uses this to draw and move objects on the map.
* 
* @author Andrea Lissak
* @version 1.0
* @release 1/04/2016
* @See GUIMapManager.java
*/


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class GUIMapManager extends JPanel
{
	private char moveWhere;
	private String mapLog;
	private JPanel pCell[][]=new JPanel[5][5];
	private final int pixelNumber=8;
	private JPanel pCellMov[][][][]=new JPanel[5][5][pixelNumber][pixelNumber];
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		* Constructor. Initializes fields.
		*
		* @field moveWhere
		* character that expresses the direction of movement (N, W, S, E) used mainly for animation
		* purposes
		* 
		* @field mapLog
		* a string that represents the map in the old text UI way; this is the source information
		* used to express the same thing but with panels
		* 
		* @field pCell[][]
		* array of panels that represent "macro" pixels of the map
		* 
		* @field pCellMov[][][][]
		* array of panels that represent "micro" pixels of each "macro" pixel of the map
		* 
		* @field pixelNumber
		* number of "micro" pixels rows and columns
		* 
	* * */
	public GUIMapManager()
	{
		this.moveWhere='X';
		this.mapLog=".........................";
		initializeGrid();
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method creates a new panel for every element of the array and calls the method below
	    * the current one.
	    * 
	    * @localVariables/objects:
	    * i: columns
	    * j: rows
	    * 
	 * **/
	private void initializeGrid()
	{
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				this.pCell[i][j]=new JPanel();
				this.pCell[i][j].setLayout(new GridBagLayout());
				initializePixelsOfAllCells(i,j);
			}
		}
	}


	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method creates a (pixelNumber) X (pixelNumber) display in each cell. It also tells
	    * them where to go in the "mini" layout
	    * 
	    * @localVariables/objects:
	    * m: columns
	    * n: rows
	    *  
	 * **/
	private void initializePixelsOfAllCells(int i, int j)
	{
		GridBagConstraints cCells = new GridBagConstraints();
		cCells.weightx=1;
		cCells.weighty=1;
		cCells.fill = GridBagConstraints.BOTH;
		for(int m=0;m<pixelNumber;m++)
		{
			for(int n=0;n<pixelNumber;n++)
			{
				this.pCellMov[i][j][m][n]=new JPanel();
				if((m+n)%2==0)
				{
					this.pCellMov[i][j][m][n].setBackground(new Color(0, 103, 48));
				}
				else
				{
					this.pCellMov[i][j][m][n].setBackground(new Color(0, 90, 60));
				}
				cCells.gridx = m;
				cCells.gridy = n;
				pCell[i][j].add(pCellMov[i][j][m][n],cCells);
			}
		}
	}


	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method is called before any convertTiles method.
	    * 
	    * @param serverResp
	    * response fed by server
	    * 
	    * @param moveWhere
	    * direction before moving
	 * **/
	public void setUpBeforeConverting(String serverResp, char moveWhere)
	{
		mapLog=serverResp;
		this.moveWhere=moveWhere;
	}
	

	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method puts the cells in their right place on the map (with gridBagLayout)
	    * 
	    * @localVariables/objects:
	    * i: columns
	    * j: rows
	    * 
	 * **/
	public void setMapLayout()
	{
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx=1;
		c.weighty=1;
		c.fill = GridBagConstraints.BOTH;
		for (int i=0;i<5;i++)
		{
            for (int j=0;j<5;j++)
            {
                c.gridx = j;
                c.gridy = i;
                
                pCell[i][j].setBackground(new Color(0, 103, 48));
                
                this.add(pCell[i][j], c);
            }
        }
		this.setBackground(new Color(0, 103, 48));
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method sets the right colors for cells according to the corresponding mapLog
	    * 
	    * @localVariables/objects:
	    * indexOfMap: support variable that holds the position of the current character to be
	    * 			  converted
	    * i: 		  columns
	    * j:  		  rows
	    * 
	 * **/
	public void convertTiles(String log, boolean resized)
	{
		if(log!=null&&log.length()>0)
		{
			mapLog=log;
			int indexOfMap=0;
			for (int i=0;i<5;i++)
			{
	            for (int j=0;j<5;j++)
	            {
	            	indexOfMap=i*5+j;
	            	switch(log.charAt(indexOfMap))
	            	{
	            	case'.':
		    			pCell[i][j].setBackground(Color.black);
		    			break;
	            	case'#':
		    			pCell[i][j].setBackground(Color.orange);
	            		break;
	            	case'P':
		    			pCell[i][j].setBackground(Color.blue);
	            		break;
	            	case'G':
		    			pCell[i][j].setBackground(Color.yellow);
	            		break;
	            	case'E':
		    			pCell[i][j].setBackground(Color.white);
	            		break;
	            	default:
		    			pCell[i][j].setBackground(Color.cyan);
	            		break;
	            	}

	            }
	        }
			performAnimation();
			for (int i=0;i<5;i++)
			{
	            for (int j=0;j<5;j++)
	            {
	            	pCell[i][j].repaint();
	            	pCell[i][j].revalidate();
	            	
	            }
	        }
			this.repaint();
			this.revalidate();
			moveWhere='X';
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method does the same thing as the above method except it has some delays between
	    * every cell conversion to make it look better. Then it also initializes the pixel colors
	    * with the background of the container
	    * 
	    * @localVariables/objects:
	    * i: columns
	    * j: rows
	    * 
	 * **/
	public void convertTilesFirstLook()
	{
		if(mapLog!=null&&mapLog.length()>0)
		{
			sleepAWhile(80);
			for (int i=0;i<5;i++)
			{
	            for (int j=0;j<5;j++)
	            {
	            	sleepAWhile(60);
	            	
	            	switch(mapLog.charAt(i*5+j))
	            	{
	            	case'.':pCell[i][j].setBackground(Color.black);
	            		break;
	            	case'#':pCell[i][j].setBackground(Color.orange);
	            		break;
	            	case'P':pCell[i][j].setBackground(Color.blue);
	            		break;
	            	case'G':pCell[i][j].setBackground(Color.yellow);
	            		break;
	            	case'E':pCell[i][j].setBackground(Color.white);
	            		break;
	            	default:pCell[i][j].setBackground(Color.cyan);
	            		break;
	            	}
	            	for(int m=0;m<pixelNumber;m++)
	        		{
	        			for(int n=0;n<pixelNumber;n++)
	        			{
	        				pCellMov[i][j][m][n].setBackground(pCell[i][j].getBackground());
	                    }
	        		}
	            }
	        }
			moveWhere='X';
			convertTiles(mapLog,true);
		}
	}
	
	
	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method runs through all elements of the (pixelNumber) X (pixelNumber) array, however
	    * it differentiates in the increasing or decreasing of the counters and their order, this
	    * is all to make the illusion realistic.
	    * 
	    * @localVariables/objects:
	    * movSpeed: delay of frames
	    * m: x value
	    * n: y value
	 * **/
	private void performAnimation()
	{
		int movSpeed=30;

		switch(moveWhere)
    	{
    	case'N':
	    		for(int n=0;n<pixelNumber;n++)
	    		{
	    			sleepAWhile(movSpeed);
	    			for(int m=pixelNumber-1;m>=0;m--)
	    			{
	    				changeSamePixelOnAll5By5Tiles(m,n);
	    			}
	    		}
	    		break;
    	case'S':
	    		for(int n=pixelNumber-1;n>=0;n--)
	    		{
	        		sleepAWhile(movSpeed);
	    			for(int m=0;m<pixelNumber;m++)
	    			{
	    				changeSamePixelOnAll5By5Tiles(m,n);
	                }
	    		}
	    		break;
    	case'W':
	    		for(int m=0;m<pixelNumber;m++)
	    		{
	        		sleepAWhile(movSpeed);
	    			for(int n=0;n<pixelNumber;n++)
	    			{
	    				changeSamePixelOnAll5By5Tiles(m,n);
	                }
	    		}
	    		break;
    	case'E':
	    		for(int m=pixelNumber-1;m>=0;m--)
	    		{
	        		sleepAWhile(movSpeed);
	    			for(int n=0;n<pixelNumber;n++)
	    			{
	    				changeSamePixelOnAll5By5Tiles(m,n);
	                }
	    		}
	    		break;
    	default:
	    		for(int m=0;m<pixelNumber;m++)
	    		{
	    			for(int n=0;n<pixelNumber;n++)
	    			{
	    				changeSamePixelOnAll5By5Tiles(m,n);
	                }
	    		}
    		break;
    	}
	}


	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method actually modify (ideally) simultaneously one single pixel 25 times
	    * 
	    * @param m
	    * x position of the same pixel of all cells
	    * 
	    * @param n
	    * y position of the same pixel of all cells
	    * 
	 * **/
	private void changeSamePixelOnAll5By5Tiles(int m, int n)
	{
		for (int i=0;i<5;i++)
		{
            for (int j=0;j<5;j++)
            {
				pCellMov[i][j][m][n].setBackground(pCell[i][j].getBackground());
			}
		}
		for (int i=0;i<5;i++)
		{
            for (int j=0;j<5;j++)
            {
            	pCell[i][j].repaint();
            	pCell[i][j].revalidate();
            	
            }
        }
	}


	   /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    * This method blocks execution for a while
	    * 
	    * @param nMS
	    * number of milliseconds to wait
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
	    * Accessor for moveWhere
	 * **/
	public char getMoveWhere()
	{
		return moveWhere;
	}
}