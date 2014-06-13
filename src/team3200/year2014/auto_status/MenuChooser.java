/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package team3200.year2014.auto_status;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Raptacon
 */
public class MenuChooser {
    long lastUpdateTime;
    
    String[] choices;
    boolean[] status;
    
    public MenuChooser(String[] choices, int defChoice) {
        this.choices = choices;
        status = new boolean[choices.length];
        
        setAllChoicesFalse();
        if (defChoice >= 0 && defChoice < choices.length)
            SmartDashboard.putBoolean(choices[defChoice], true);
        
        lastUpdateTime = -1;
    }
    public MenuChooser(String[] choices) {
        this(choices, -1);
    }
    
    private void setAllChoicesFalse() {
        for (int c = 0; c < choices.length; c++) {
            status[c] = false;
            SmartDashboard.putBoolean(choices[c], false);
        }
    }
    
    public String getSelectedName() {
        String result = null;
        int choice = getSelectedInt();
        
        if (choice != -1)
            result = choices[choice];
        
        return result;
    }
    
    public int getSelectedInt() {
        int result = -1;
        
        for (int i = 0; i < choices.length; i++) { // Search for the choice...
            if (SmartDashboard.getBoolean(choices[i])) {
                result = i;
                break;
            }
        }
        
        return result;
    }
    
    public void update() {
        long currTime = System.currentTimeMillis();
        
        if (currTime - lastUpdateTime > 500) { // Update the chooser every 500 milliseconds
            lastUpdateTime = currTime;
            
            for (int i = 0; i < choices.length; i++) {
                boolean value = SmartDashboard.getBoolean(choices[i]);

                if (value && value != status[i]) { // If another value was set to true...
                    setAllChoicesFalse(); // Set all other options to false
                    status[i] = true;
                    SmartDashboard.putBoolean(choices[i], true);
                    break;
                }
            }
        }
    }
}
