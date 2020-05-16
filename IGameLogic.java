/**
   /\      /\      /\      /\      /\      /\      /\      /\      /\      /\      /\      /\      /\      /\      /\   
  //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\    //\\  
 ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\  ///\\\ 
////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\
 
 										THIS INTERFACE HAS JUST BEEN SLIGHTLY CHANGED
 
\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////\\\\////
 \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\///  \\\/// 
  \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//    \\//  
   \/      \/      \/      \/      \/      \/      \/      \/      \/      \/      \/      \/      \/      \/      \/   

*
* 
* 
* @author Tutors + Andrea Lissak
* @version 1.0
* @release 1/04/2016
* @See IGameLogic.java
*/

import java.io.File;

public interface IGameLogic
{
	public void setMap(String path);//changed from "File" to "String"
	
	public String hello();
	
	public String move(char direction);
	
	public String pickup();
	
	public String look();
	
	public boolean gameRunning();
	
	public String quitGame();
}
