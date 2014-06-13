
package team3200.year2014.controller;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This is the base class for all controllers.
 * @author Raptacon
 */
public abstract class Controller {
    Joystick joy; // The joystick to read from
    
    /**
     * Create a new controller.
     * @param slot The slot in the Dirver Station this is plugged in
     */
    public Controller(int slot) {
        joy = new Joystick(slot);
    }
    
    /**
     * Gets an axis from the controller.
     * @param axis The axis to read... Found via guess & check
     * @return The value of the axis, from -1 to 1
     */
    protected double getRawAxis(int axis) {
        return joy.getRawAxis(axis);
    }
    /**
     * Get an axis from the controller. If the value is less than the deadzone,
     * 0 is returned. This is used to make the joystick less sensitive.
     * @param axis The axis to read... Found via guess & check
     * @param deadZone The minimum value the axis can be
     * @return The value of the axis, from -1 to 1
     */
    protected double getDeadzonedAxis(int axis, double deadZone) {
        double d = joy.getRawAxis(axis);
        deadZone = Math.abs(deadZone);
        
        if (d < deadZone && d > -deadZone)
            d = 0;
        return d;
    }
    
    /**
     * Gets a button from the controller.
     * @param button The button to read... Use the Control Panel in Windows 7
     * to find out which button is which
     * @return Whether the button is pressed
     */
    protected boolean getRawButton(int button) {
        return joy.getRawButton(button);
    }
}
