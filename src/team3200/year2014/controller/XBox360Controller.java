
package team3200.year2014.controller;

/**
 * Handles the X Box 360 Controller.
 * @author Raptacon
 */
public class XBox360Controller extends Controller {
    /********************************** X-Box 360 Buttons *************************************/
    
    // The buttons on the X-Box 360 Controller
    private static final int A_BTTN = 1;
    private static final int B_BTTN = 2;
    private static final int X_BTTN = 3;
    private static final int Y_BTTN = 4;
    private static final int START_BTTN = 8;
    private static final int SELECT_BTTN = 7;
    private static final int LEFT_STICK_PRESS = 9;
    private static final int RIGHT_STICK_PRESS = 10;
    
    private static final int LEFT_BUMPER_BTTN = 5;
    private static final int RIGHT_BUMPER_BTTN = 6;
    
    // The axis on the X-Box 360 Controller
    private static final int D_PAD_X_AXIS = 6;
    private static final int LEFT_STICK_X_AXIS = 1;
    private static final int LEFT_STICK_Y_AXIS = 2;
    private static final int RIGHT_STICK_X_AXIS = 4;
    private static final int RIGHT_STICK_Y_AXIS = 5;
    
    private static final int TRIGGER_BTTN_AXIS = 3; // Left trigger = postive, right = negative
    
    private static final double deadZone = 0.133;
    
    public XBox360Controller(int slot) {
        super(slot);
    }
    
    // Methods to get the various buttons and axis on the controller
    /**
     * Returns whether the A button is held or not
     * @return whether A button is held
     */
    public boolean getA_Button() {
        return getRawButton(A_BTTN);
    }
    /**
     * Returns whether the B button is held or not
     * @return whether B button is held
     */
    public boolean getB_Button() {
        return getRawButton(B_BTTN);
    }
    /**
     * Returns whether the X button is held or not
     * @return whether X button is held
     */
    public boolean getX_Button() {
        return getRawButton(X_BTTN);
    }
    /**
     * Returns whether the Y button is held or not
     * @return whether Y button is held
     */
    public boolean getY_Button() {
        return getRawButton(Y_BTTN);
    }
    /**
     * Returns whether the Start button is held or not
     * @return whether Start button is held
     */
    public boolean getStart_Button() {
        return getRawButton(START_BTTN);
    }
    /**
     * Returns whether the Select button is held or not
     * @return whether Select button is held
     */
    public boolean getSelect_Button() {
        return getRawButton(SELECT_BTTN);
    }
    /**
     * Returns whether the left stick is pushed down or not
     * @return whether left stick is pushed down or not
     */
    public boolean getLeftStick_Button() {
        return getRawButton(LEFT_STICK_PRESS);
    }
    /**
     * Returns whether the right stick is pushed down or not
     * @return whether right stick is pushed down or not
     */
    public boolean getRightStick_Button() {
        return getRawButton(RIGHT_STICK_PRESS);
    }
    /**
     * Returns whether the Left Bumper button is held or not
     * @return whether Left Bumper button is held
     */
    public boolean getLeftBumper_Button() {
        return getRawButton(LEFT_BUMPER_BTTN);
    }
    /**
     * Returns whether the Right Bumper button is held or not
     * @return whether Right Bumper button is held
     */
    public boolean getRightBumper_Button() {
        return getRawButton(RIGHT_BUMPER_BTTN);
    }

    /**
     * Returns the left joystick's x axis value. Right is positive, left is
     * negative.
     * @return the left joystick's x axis value
     */
    public double getLeftX_Axis() {
        return getDeadzonedAxis(LEFT_STICK_X_AXIS, deadZone);
    }
    /**
     * Returns the left joystick's y axis value. Up is positive, Down is
     * negative.
     * @return the left joystick's y axis value
     */
    public double getLeftY_Axis() {
        return -getDeadzonedAxis(LEFT_STICK_Y_AXIS, deadZone);
    }
    /**
     * Returns the right joystick's x axis value. Right is positive, left is
     * negative.
     * @return the right joystick's x axis value
     */
    public double getRightX_Axis() {
        return getDeadzonedAxis(RIGHT_STICK_X_AXIS, deadZone);
    }
    /**
     * Returns the right joystick's y axis value. Up is positive, Down is
     * negative.
     * @return the right joystick's y axis value
     */
    public double getRightY_Axis() {
        return -getDeadzonedAxis(RIGHT_STICK_Y_AXIS, deadZone);
    }
    /**
     * Returns the trigger button's axis value. The left trigger button
     * increases the value, and the right trigger button decreases the value.
     * The maximum is 1, and the minimum is -1.
     * @return the trigger button's axis value
     */
    public double getTrigger_Axis() {
        return getRawAxis(TRIGGER_BTTN_AXIS);
    }
    /**
     * Returns the D-Pad's x axis value. Right is positive, left is negative.
     * @return the trigger button's axis value
     */
    public double getDpadX_Axis() {
        return getRawAxis(D_PAD_X_AXIS);
    }
}
