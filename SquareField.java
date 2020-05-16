/**
* ESTHETIC SOLUTION to turn what's usually a rectangle into a square, this was took from:
* http://stackoverflow.com/questions/16075022/making-a-jpanel-square
* 
* @author Andrea Lissak
* @version 1.0
* @release 1/04/2016
* @See GUIMapManager.java
*/

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JPanel;

class SquareField extends JPanel
{
	public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();
        Container c = getParent();
        if (c != null) {
            d = c.getSize();
        } else {
            return new Dimension(10, 10);
        }
        int w = (int) d.getWidth();
        int h = (int) d.getHeight();
        int s = (int)((w < h ? w : h)/1.2);
        return new Dimension(s, s);
    }
}