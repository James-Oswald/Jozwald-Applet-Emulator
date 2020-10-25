package joz.applet;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;

public abstract class Applet extends JPanel
{
	private static JFrame frame = new JFrame();
	
	public abstract void paint(Graphics g);
	public abstract void init();
	
	public static void setFrame(JFrame frame_)
	{
		frame = frame_;
	}
	
	public Applet()
	{
		init();
		frame.add(this);
		frame.setVisible(true);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		paint(g);
	}
	
	public void repaint()
	{
		super.repaint();
	}
	
	public void setBackground(Color c)
	{
		super.setBackground(c);
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
}