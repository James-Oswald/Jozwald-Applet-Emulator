import java.awt.Color;
import java.awt.Graphics;

public class Box implements Runnable
{
	private volatile int x, y, w, h;
	
	public Box(int x, int y)
	{
		w = 0;
		h = 0;
		this.x = x;
		this.y = y;
		new Thread(this).start();
	}
	
	public void run()
	{
		while(true)
		{
			if(x < 500)
			{
				w++;
				h++;
			}
			else
			{
				w = 0;
				h = 0;
			}
			try
			{
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.red);
		g.drawRect(x, y, w, h);
	}
}