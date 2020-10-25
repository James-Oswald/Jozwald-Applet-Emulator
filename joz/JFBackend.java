import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.net.URLClassLoader;
import java.net.URL;
import java.lang.ClassLoader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JButton;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import javax.swing.DefaultListModel;

public class JFBackend
{	
	private static volatile ArrayList <Class> classes;
	private static ArrayList <String> loaded;
	private static Class <?> applet;
	private static DefaultListModel<String> lm;
	private static JFrame holder;
	
	public static void main(String[] args)
	{
		classes = new ArrayList <Class> ();
		loaded = new ArrayList <String> ();
		lm = new DefaultListModel <String> ();
		try
		{
			ClassLoader appletLoader = new URLClassLoader(new URL[]{new URL(System.getProperty("user.dir").replace("F:", "file:\\") + "\\applet\\")});
			applet = appletLoader.loadClass("joz.applet.Applet");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() 
		{
			public void run() 
			{
				for(String s : loaded)
				{
					new File(System.getProperty("user.dir") + "\\" + s + ".class").delete();
				}
			}
		}));
		Thread intro;
		if(args.length != 0)
		{
			intro = new Thread(new Runnable()
			{
				public void run()
				{
					JFrame intro = new JFrame();
					ImageIcon ico = new ImageIcon("logo.png");
					intro.setSize(ico.getIconWidth() + 20, ico.getIconHeight() + 20);
					intro.setUndecorated(true);
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					intro.setLocation(dim.width / 2 - intro.getSize().width / 2, dim.height / 2 - intro.getSize().height / 2);
					intro.add(new JPanel()
					{
						@Override public void paintComponent(Graphics g)
						{
							super.paintComponent(g);
							g.drawImage(new ImageIcon("logo.png").getImage(), 10, 10, null);
						}
					});
					intro.setVisible(true);
					try
					{
						Thread.sleep(2000);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					intro.setVisible(false);
				}
			});
		}
		else
		{
			intro = new Thread(new Runnable(){public void run(){}});
		}
		Thread load = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					String src = System.getProperty("user.dir") + "\\..";
					File[] allFiles = new File(src).listFiles();
					String name, extention;
					for(File file : allFiles)
					{
						extention = file.getName().lastIndexOf('.') > 0 ? file.getName().substring(file.getName().lastIndexOf('.') + 1) : "";
						if(extention.equals("class"))
						{
							name = file.getName().substring(0, file.getName().lastIndexOf('.'));
							loaded.add(name);
							Files.copy(Paths.get(src + "\\" + file.getName()), Paths.get(System.getProperty("user.dir") + "\\" + name + ".class"), StandardCopyOption.REPLACE_EXISTING);
							ClassLoader loader = new URLClassLoader(new URL[]{new URL(System.getProperty("user.dir").replace("C:", "file:\\"))});
							Class<?> pane = loader.loadClass(name);
							if(applet.isAssignableFrom(pane))
							{
								classes.add(pane);
							}
						}
					}
					for(int i = 0; i < classes.size(); i++)
					{
						lm.addElement(classes.get(i).getName());
					}
					intro.join();
					JFrame frame = new JFrame("Jozwald Applet Emulator");
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					JLabel head = new JLabel("Found " + classes.size() + " possible applets.", new ImageIcon("top.png"), JLabel.CENTER);
					panel.add(head);
					JList<String> list = new JList <String>(lm);
					list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					JScrollPane scroll = new JScrollPane(list); 
					scroll.setPreferredSize(new Dimension(head.getIcon().getIconWidth(), 400));
					panel.add(scroll);
					JPanel bpanel = new JPanel();
					bpanel.setLayout(new FlowLayout());
					holder = new JFrame("Jozwald Applet Emulator");
					holder.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					holder.setSize(500, 500);
					holder.setIconImage(new ImageIcon("lex.png").getImage());
					Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
					holder.setLocation(dim2.width / 2 - holder.getSize().width / 2, dim2.height / 2 - holder.getSize().height / 2);
					Method setFrame = applet.getMethod("setFrame", JFrame.class);
					setFrame.invoke(null, (Object)holder);
					JButton start = new JButton("Run");
					start.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							try
							{
								if(list.getSelectedValue() != null)
								{
									holder.getContentPane().removeAll();
									for(int i = 0; i < classes.size(); i++)
									{
										if(classes.get(i).getName() == list.getSelectedValue())
										{
											classes.get(i).newInstance();
											break;
										}
									}
								}
							}
							catch(Exception ex)
							{
								System.out.println(ex.getMessage());
								ex.printStackTrace();
							}
						}
					});
					bpanel.add(start);
					JButton stop = new JButton("Terminate");
					stop.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							holder.dispose();
						}
					});
					bpanel.add(stop);
					JButton up = new JButton("Update");
					up.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							try
							{
								Runtime.getRuntime().exec("java JFBackend");
								System.exit(0);
							}
							catch(Exception ex)
							{
								System.out.println(ex.getMessage());
								ex.printStackTrace();
							}
						}
					});
					bpanel.add(up);
					panel.add(bpanel);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setIconImage(new ImageIcon("lex.png").getImage());
					frame.add(panel);
					frame.pack();
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
					frame.setVisible(true);
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		intro.start();
		load.start();
	}
}




