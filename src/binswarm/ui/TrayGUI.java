package binswarm.ui;

import java.awt.*;
import java.awt.event.*;


public class TrayGUI
{
	public static TrayIcon trayIcon;
	
    public TrayGUI()
    {
    	//Constructor
    	
        if (SystemTray.isSupported())
        {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("images/tray.gif");

            MouseListener mouseListener = new MouseListener() {
                
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse clicked!");
                }
                public void mouseEntered(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse entered!");
                }
                public void mouseExited(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse exited!");
                }
                public void mousePressed(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse pressed!");
                }
                public void mouseReleased(MouseEvent e) {
                    System.out.println("Tray Icon - Mouse released!");
                }

            };

            ActionListener exitListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                	//Exit the app
                    System.exit(0);
                }
            };
            
            ActionListener showStatusListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    //Show status
                	GUI.frame.setVisible(true);
                }
            };
            
            PopupMenu popup = new PopupMenu();
            MenuItem exitItem = new MenuItem("Exit");
            MenuItem showStatusItem = new MenuItem("Show Status");
            showStatusItem.addActionListener(showStatusListener);
            exitItem.addActionListener(exitListener);
            popup.add(showStatusItem);
            popup.add(exitItem);

            trayIcon = new TrayIcon(image, "BinSwarm", popup);

            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(mouseListener);

            try {
                  tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }

        }
        else {
            System.err.println("System tray is currently not supported.");
        }
    }    
}
