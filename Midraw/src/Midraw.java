import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Midraw {

	 public static void main(String[] args)
	
	 {
		 
		  IntroFrame intro = new IntroFrame();
		 new MidrawInstrument(0).Intro();
		  try{
			  Thread.sleep(3000);
		  }catch(Exception e){}
		  intro.setVisible(false);
		  
		  new MidrawFrame("Midraw");
	 
	 }
}
class IntroFrame extends JFrame{
	public IntroFrame()
	{
		setSize(400,300);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frm = super.getSize();
		int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
		int ypos = (int)(screen.getHeight() /2 - frm.getHeight() / 2);
		setLocation(xpos, ypos);
		setResizable(false);
		setUndecorated(true);
		setVisible(true);

		
		ImageIcon intro=new ImageIcon(".\\images\\introg.gif");
		JButton introbutton = new JButton(intro);
		introbutton.setBorderPainted(false);
		introbutton.setBounds(0, 0, 400, 300);
		introbutton.setIcon(intro);
		introbutton.setRolloverIcon(intro);
		introbutton.setRolloverSelectedIcon(intro);
		introbutton.setSelectedIcon(intro);
		introbutton.setPressedIcon(intro);
		
		add(introbutton);
		
	}
}