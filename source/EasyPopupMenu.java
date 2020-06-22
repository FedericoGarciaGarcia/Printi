/**
 * Author: Federico Garcia Garcia
 * License: GPL-3.0
 * Created on: 22/06/2020 9:00
 */
 
import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class EasyPopupMenu extends PopupMenu {
	
	// Map of all the items
    private HashMap<String, ParentalMenu> menuMap;
    private HashMap<String, MenuItem> menuItemMap;
    private HashMap<String, CheckboxMenuItem> checkboxMap;
	
	public EasyPopupMenu() {
		menuMap     = new HashMap<String, ParentalMenu>();
		menuItemMap = new HashMap<String, MenuItem>();
		checkboxMap = new HashMap<String, CheckboxMenuItem>();
	}
	
	// Add a menu to the popup menu
	public void addMenu(String s) {
		ParentalMenu m = new ParentalMenu(s);
		menuMap.put(s, m);
		add(m);
	}
	
	// Add a menu item to the popup menu
	public void addMenuItem(String s) {
		MenuItem m = new MenuItem(s);
		menuItemMap.put(s, m);
		add(m);
	}
	
	// Add a menu item to an existing menu
	public void addMenuItemToMenu(String sm, String smi) {
		MenuItem m = new MenuItem(smi);
		menuMap.get(sm).add(m);
		menuItemMap.put(smi, m);
	}
	
	// Add a checkbox to an existing menu
	public void addCheckboxToMenu(String sm, String smc) {
		CheckboxMenuItem c = new CheckboxMenuItem(smc);
		menuMap.get(sm).add(c);
		checkboxMap.put(smc, c);
		
		menuMap.get(sm).addChild(c);
		
		// Make all the elements of the menu unselected, and this one selected
		c.addItemListener(new ItemListener() {
			  public void itemStateChanged(ItemEvent event) {
				  unselectAll(menuMap.get(sm), c);
			  }
		});
	}
	
	// Add menu to existing menu
	public void addMenuToMenu(String sm1, String sm2) {
		ParentalMenu m = new ParentalMenu(sm2);
		menuMap.get(sm1).add(m);
		menuMap.put(sm2, m);
	}
	
	// Select the ith item from a menu with checkboxes
	public void selectCheckbox(String sm, int i) {
		menuMap.get(sm).getChild(i).setState(true);
	}
	
	// Add actions to items
	public void addActionToMenuItem(String smi, ActionListener a) {
		menuItemMap.get(smi).addActionListener(a);
	}
	
	// Add actions to items
	public void addActionToCheckbox(String sm, int i, ItemListener a) {
		menuMap.get(sm).getChild(i).addItemListener(a);
	}
	
	// Get menu
	public ParentalMenu getMenu(String sm) {
		return menuMap.get(sm);
	}
	
    // Unselect everything
    private void unselectAll(ParentalMenu menu, CheckboxMenuItem item) {
        for (int j=0; j<menu.getChildren().size(); j++) {
            menu.getChild(j).setState(false);
        }
        
        // Select this one
        item.setState(true);
        
        // Save configuration
        //saveConfig();
    }
}

class ParentalMenu extends Menu {
	
	// If the parent is null, it is an element in the main panel of the popupmenu
	private ArrayList<CheckboxMenuItem> children;
	
	public ParentalMenu(String label) throws HeadlessException {
		super(label);
		children = new ArrayList<CheckboxMenuItem>();
    }
	
	public void addChild(CheckboxMenuItem child) {
		children.add(child);
	}
	
	public ArrayList<CheckboxMenuItem> getChildren() {
		return children;
	}
	
	public CheckboxMenuItem getChild(int i) {
		return children.get(i);
	}
}

/*
class EasyCheckboxMenuItem extends CheckboxMenuItem {
	
	// If the parent is null, it is an element in the main panel of the popupmenu
	private Menu parent;
	
	public EasyCheckboxMenuItem(String label, Menu parent) throws HeadlessException {
        setLabel(label);
		this.parent = parent;
    }
	
	public Menu getParent() {
		return parent;
	}
}*/