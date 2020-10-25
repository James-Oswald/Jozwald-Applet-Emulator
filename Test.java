import java.awt.Graphics;
import java.awt.Color;
import joz.applet.Applet;

public class Test extends Applet
{
	public void paint(Graphics g)
	{
		g.setColor(Color.blue);
		g.fillRect(100, 100, 100, 100);
	}
}