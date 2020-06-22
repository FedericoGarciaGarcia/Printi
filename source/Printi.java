/**
 * Author: Federico Garcia Garcia
 * License: GPL-3.0
 * Created on: 21/06/2020 1:30
 */

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.concurrent.AbstractExecutorService;
import java.util.List;
import java.util.ArrayList;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.*;
import java.lang.StringBuilder;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.*;

import java.util.HashMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Printi implements NativeKeyListener  {
    
    // Flag for key press
    private boolean printHasBeenPressed;
    
    // Printers
    private PrintService[] printServices;
    private String[] paperSizes;
    private HashMap<String, PrintService> printersMap;
    private HashMap<String, Integer> printerNamesMap;
	
	// Papers
    private HashMap<String, Integer> paperSizeNamesMapNamesMap;
	
	// Languages
    private HashMap<String, Integer> languageNamesMap;
    private ArrayList<HashMap<String, Integer>> languages;
    
	
    // Robot
    private Robot robot;
    
	// Configuration
    private Config config;
	
	// Trayicon
	private TrayIcon trayIcon;
	
	// Menus
	private EasyPopupMenu popup;
	
    // Start everything
    public Printi() {
        /*
         * VARIABLES
         */
        printHasBeenPressed = false;
		
        printersMap      = new HashMap<String, PrintService>();
		printerNamesMap  = new HashMap<String, Integer>();
		languageNamesMap = new HashMap<String, Integer>();
		paperSizeNamesMapNamesMap = new HashMap<String, Integer>();
		
		languages = new ArrayList<HashMap<String, Integer>>();
		
		paperSizes = new String[] {"B5", "A4", "B4", "A3"};
		
        System.out.println("Number of paper sizes: " + paperSizes.length);
		for (int i=0; i<paperSizes.length; i++) {
            System.out.println("  "+paperSizes[i]);
            paperSizeNamesMapNamesMap.put(paperSizes[i], i);
        }
		
        config = new Config();
		
        robot = null;
		
        try {
            robot = new Robot();
        }
        catch(Exception e) {
            System.out.println("Could not create robot");
            e.printStackTrace();
            System.exit(1);
        }
    
        /*
         * INIT EVERYTHNING
         */
		loadLanguages();
        initPriters();
        hookKeyboard();
		
        // Check the if the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        // If it is, work
        else {
            // Create tray system
			createTray();
			
			// Set default config
			defaultConfig();
            
            // Load config.
            loadConfig();
			
			// Set initial state
			initialState();
            
            // Save it again, in case the file was missing some configs
            saveConfig();
        }
        
        System.out.println("Successfully created TrayIcon");
    }
	
	// Load each language available
	private void loadLanguages() {
	}
    
    // Get available printers
    // Create dictionary of printers
    private void initPriters() {
        printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Number of print services: " + printServices.length);

        for (int i=0; i<printServices.length; i++) {
            System.out.println("  "+printServices[i].getName());
            printersMap.put(printServices[i].getName(), printServices[i]);
			printerNamesMap.put(printServices[i].getName(), i);
        }
    }
        
    // Init keyboard interception
    private void hookKeyboard() {
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);

            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        }
        catch(Exception e) {
            System.out.println("Could not hook keyboard");
            e.printStackTrace();
            return;
        }
        
        System.out.println("Successfully hooked keyboard");
    }
	
	// Create Tray System and popup menu with actions
	private void createTray() {
		// Create popup menu
		popup = new EasyPopupMenu();
		
		// Null Image for now
		Image image = null;
		
		// Try to load the image
		try {
			image = ImageIO.read(getClass().getResource("/images/icon.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		// Get the OS tray image size
		final SystemTray tray = SystemTray.getSystemTray();
		Dimension trayIconSize = tray.getTrayIconSize();
		
		// Rescale image to that size
		image = image.getScaledInstance(trayIconSize.width, trayIconSize.height, Image.SCALE_SMOOTH);
		
		// Create tray icon system
		trayIcon = new TrayIcon(image, "EasyKeyToPrint");
		
		// Create a pop-up menu components
		popup.addMenu("Color");
			popup.addCheckboxToMenu("Color", "Black and white");
			popup.addCheckboxToMenu("Color", "Color");
		popup.addMenu("Orientation");
			popup.addCheckboxToMenu("Orientation", "Landscape");
			popup.addCheckboxToMenu("Orientation", "Portrait");
		popup.addMenu("Fit");
			popup.addCheckboxToMenu("Fit", "Maintain aspect ratio");
			popup.addCheckboxToMenu("Fit", "Stretch");
		popup.addMenu("Paper size");
			for(String paper : paperSizes)
			popup.addCheckboxToMenu("Paper size", paper);
		popup.addMenu("Taskbar");
			popup.addCheckboxToMenu("Taskbar", "Show taskbar");
			popup.addCheckboxToMenu("Taskbar", "Hide taskbar");
		popup.addMenu("Date");
			popup.addCheckboxToMenu("Date", "Print date");
			popup.addCheckboxToMenu("Date", "Don't print date");
			/*
		popup.addMenu("Title");
			popup.addMenuItemToMenu("Title", "Print title");
			popup.addCheckboxToMenu("Title", "Don't print title");
			*/
		popup.addSeparator();
		popup.addMenu("Notification");
			popup.addCheckboxToMenu("Notification", "Show notification");
			popup.addCheckboxToMenu("Notification", "Hide notification");
		popup.addMenu("Print dialog");
			popup.addCheckboxToMenu("Print dialog", "Show dialog");
			popup.addCheckboxToMenu("Print dialog", "Hide dialog");
		popup.addMenu("Printer");
			for(PrintService p : printServices)
			popup.addCheckboxToMenu("Printer", p.getName());
		popup.addSeparator();
		popup.addMenu("Language");
			popup.addCheckboxToMenu("Language", "English");
			popup.addCheckboxToMenu("Language", "Japanese");
		popup.addMenuItem("Help");
		popup.addMenuItem("About");
		popup.addSeparator();
		popup.addMenuItem("Exit");
		
		trayIcon.setPopupMenu(popup);
		
		// Add actions
		popup.addActionToMenuItem("Exit", new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  exit();
			  }
		});
		
		for(int i=0; i<popup.getMenu("Color").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Color", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.color = ii;
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Orientation").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Orientation", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.orientation = ii;
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Fit").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Fit", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.fit = ii;
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Paper size").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Paper size", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.paperSize = paperSizes[ii];
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Taskbar").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Taskbar", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.taskbar = ii;
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Date").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Date", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.date = ii;
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Notification").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Notification", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.notification = ii;
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Print dialog").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Print dialog", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.printDialog = ii;
					saveConfig();
				}
			});
		}
		
		for(int i=0; i<popup.getMenu("Printer").getChildren().size(); i++) {
			final int ii = i;
			popup.addActionToCheckbox("Printer", ii, new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
					config.printer = printServices[ii].getName();
					saveConfig();
				}
			});
		}
			
		// Try and add the tray system
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}
	}
	
	// Set the initial state of the UI, with checked boxes and all that
	private void initialState() {
		popup.selectCheckbox("Color",        config.color);
		popup.selectCheckbox("Orientation",  config.orientation);
		popup.selectCheckbox("Fit",          config.fit);
		popup.selectCheckbox("Paper size",   paperSizeNamesMapNamesMap.get(config.paperSize));
		popup.selectCheckbox("Taskbar",      config.taskbar);
		popup.selectCheckbox("Date",         config.date);
		popup.selectCheckbox("Notification", config.notification);
		popup.selectCheckbox("Print dialog", config.printDialog);
		popup.selectCheckbox("Printer",      printerNamesMap.get(config.printer));
		popup.selectCheckbox("Language",     0);
	}
	
	private void defaultConfig() {
        config.color        = 0;
        config.orientation  = 0;
        config.fit          = 0;
		config.paperSize    = "A4";
        config.taskbar      = 0;
        config.date         = 0;
        config.notification = 0;
        config.printDialog  = 0;
		config.printer     = printServices[0].getName();
		config.language    = "English";
	}
    
    // Try and load configuration
    private void loadConfig() {
		// Load config file
		try {
			File configFile = new File("config.ini");
			
			if (configFile.createNewFile()) {
				System.out.println("config.ini file created");
			} else {
				// Read line by line
				BufferedReader reader = new BufferedReader(new FileReader("config.ini"));
				ArrayList<String> strings = new ArrayList<String>();
				String line = null;
				
				while ((line = reader.readLine()) != null) {
					// Do not add empty lines
					if(line.length() > 0)
					strings.add(line);
				}
				reader.close();
				
				// Set config
				for(int i=0; i<strings.size(); i++) {
					if(strings.get(i).equals("[Color]"))
						config.color = Integer.parseInt(strings.get(i+1));
					
					if(strings.get(i).equals("[Orientation]"))
						config.orientation = Integer.parseInt(strings.get(i+1));
					
					if(strings.get(i).equals("[Fit]"))
						config.fit = Integer.parseInt(strings.get(i+1));
					
					if(strings.get(i).equals("[Paper size]"))
						config.paperSize = strings.get(i+1);
					
					if(strings.get(i).equals("[Taskbar]"))
						config.taskbar = Integer.parseInt(strings.get(i+1));
					
					if(strings.get(i).equals("[Date]"))
						config.date = Integer.parseInt(strings.get(i+1));
					
					if(strings.get(i).equals("[Notification]"))
						config.notification = Integer.parseInt(strings.get(i+1));
					
					if(strings.get(i).equals("[Print dialog]"))
						config.printDialog = Integer.parseInt(strings.get(i+1));
					
					if(strings.get(i).equals("[Printer]"))
						config.printer = strings.get(i+1);
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    // Save configuration
    private void saveConfig() {
		System.out.println("Settings: ");
		
		// Save config file
		try (PrintWriter out = new PrintWriter("config.ini")) {
			out.println("[Color]");
			out.println(config.color);
			
			out.println("[Orientation]");
			out.println(config.orientation);
			
			out.println("[Fit]");
			out.println(config.fit);
			
			out.println("[Paper size]");
			out.println(config.paperSize);
			
			out.println("[Taskbar]");
			out.println(config.taskbar);
			
			out.println("[Date]");
			out.println(config.date);
			
			out.println("[Notification]");
			out.println(config.notification);
			
			out.println("[Print dialog]");
			out.println(config.printDialog);
			
			out.println("[Printer]");
			out.println(config.printer);
			
			out.println("[Language]");
			out.println(config.language);
			
			out.close();
			
			System.out.println("  color: "        +config.color);
			System.out.println("  orientation: "  +config.orientation);
			System.out.println("  fit: "          +config.fit);
			System.out.println("  taskbar: "      +config.taskbar);
			System.out.println("  date: "         +config.date);
			System.out.println("  paper size: "   +config.paperSize);
			System.out.println("  printer: "      +config.printer);
			System.out.println("  notification: " +config.notification);
			System.out.println("  print dialog: " +config.printDialog);
			System.out.println("  language: "     +config.language);
			System.out.println("  config.ini saved");
		}
		catch(IOException e) {
			System.out.println("Could not save config.ini");
			e.printStackTrace();
		}
    }
    
    // Take a screenshot and print
    private void print() {
        // Take screenshot
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Remove taskbar if requested
		if(config.taskbar == 1) {
			Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			int taskBarHeight = screenSize.height - winSize.height;

			screenSize.height -= taskBarHeight;
        }
		
        // Take screenshot
        Rectangle screenRect = new Rectangle(screenSize);
        BufferedImage imageColor = robot.createScreenCapture(screenRect);
		
		BufferedImage image;
		
        // If option of black and white is selected, make the image grayscale, just in case
		if(config.color == 0) {
			BufferedImage imageGray = new BufferedImage(imageColor.getWidth(), imageColor.getHeight(), BufferedImage.TYPE_BYTE_GRAY);  
			Graphics g = imageGray.getGraphics();  
			g.drawImage(imageColor, 0, 0, null);  
			g.dispose();
			image = imageGray;
        }
		else {
			image = imageColor;
		}
        // Also, change the opacicity of the printer //TODO
        //attrib.add(Chromaticity.MONOCHROME);
        
        // Create printer and start thread
        Printer printer = new Printer(image, printersMap.get(config.printer), config, trayIcon);
        printer.run();
    }
	
	private void exit() {
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (Exception ex) {
			System.out.println("Could not unhook keyboard");
			ex.printStackTrace();
		}
		
		System.out.println("Exit");
		System.exit(0);
	}
    
    // Cehck key pressed
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (!((e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0) // User can take normal screenshot with SHIFT keys
		 && e.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN && e.getRawCode() == 44) {// e.getRawCode() == 44 Solves the problem of mistaking with asterisk
            // Only print once. We prevent from people holding the print button too long
            // and printing multiple times by accident
            if(!printHasBeenPressed) {
                print();
            }
            
            printHasBeenPressed = true;
        }
    }
    
    // Check key released
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN ) {
            printHasBeenPressed = false; // We can print again
        }
    }
    
    // Check typed key - UNUSED
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
    
    public static void main(String [] args) {
        new Printi();
    }
}
