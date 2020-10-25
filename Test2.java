import java.awt.Graphics;
import java.awt.Color;
import joz.applet.Applet;
import javax.swing.JOptionPane;

public class Test2 extends Applet implements Runnable
{	
	private Box box;

	public void init()
	{
		box = new Box(0, 0);
		new Thread(this).start();
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				
			}
			repaint();
		}
	}
	
	public void paint(Graphics g)
	{
		box.draw(g);
	}
}