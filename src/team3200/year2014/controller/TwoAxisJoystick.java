// TODO: Add method to get slider axis.

package team3200.year2014.controller;

/**
 * This is the two axis joystick.
 * @author Raptacon
 */
public class TwoAxisJoystick extends Controller {
    double deadZone = 0.2;

    public TwoAxisJoystick(int slot) {
        super(slot);
    }
    
    // Methods to get the various buttons and axis on the controller
    /**
     * Returns whether the trigger button is held or not
     * @return whether trigger button is held
     */
    public boolean getTriggerButton() {
        return getRawButton(1);
    }
    /**
     * Returns whether the button is held or not. num must be within 2 - 11. 2
     * and 11 work as well. These buttons are labeled on the controller.
     * @param num The button to get
     * @return whether trigger button is held
     */
    public boolean getButton(int num) {
        return (num > 1 && num < 12) ? getRawButton(num) : false;
    }
    
    /**
     * Returns the joystick's x axis value. Right is positive, left is negative.
     * @return the left joystick's x axis value
     */
    public double getX_Axis() {
        return getDeadzonedAxis(1, deadZone);
    }
    /**
     * Returns the joystick's y axis value. Up is positive, down is negative.
     * @return the joystick's y axis value
     */
    public double getY_Axis() {
        return -getDeadzonedAxis(2, deadZone);
    }
    /**
     * Returns the joystick's slider value. Up is positive, down is negative.
     * @return the joystick's slider value
     */
    public double getSlider_Axis() {
        return -getDeadzonedAxis(3, deadZone);
    }
}
