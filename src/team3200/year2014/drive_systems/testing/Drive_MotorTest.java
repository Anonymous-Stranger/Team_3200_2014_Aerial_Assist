
package team3200.year2014.drive_systems.testing;

import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.controller.XBox360Controller;
import team3200.year2014.drive_systems.DriveBase;

/**
 * DriveBase each motor seperately. Use the left joystick to control the speed.
 * X = Front Left motor
 * Y = Front Right motor
 * A = Back Left motor
 * B = Back Right motor
 * 
 * Diagram:
 * X Y
 * A B
 * @author Raptacon
 */
public class Drive_MotorTest extends DriveBase {
    
    /**
     * Create a new instance of the DriveBase class.
     */
    public Drive_MotorTest() {
        super(false);
    }
    
    public void autoRun(AutoStatusBase autoStatus) {
        stop();
    }
    
    protected void drive(XBox360Controller control, double xAxis, double yAxis) {
        drive(control, yAxis);
        
        System.out.println("x: "+ xAxis +" y: "+ yAxis);
//        System.out.println("\t\tleftEncoder: "+ leftEncoder.get() +" rightEncoder: "+ rightEncoder.get());
//        System.out.println("leftEncoder: "+ leftEncoder.getDistance() +" rightEncoder: "+ rightEncoder.getDistance());
//        System.out.println("\t\txIn: "+ xInput +" yIn: "+ yInput);
    }
    
    /**
     * Controls each motor seperately.
     * Press the following buttons to set forwardValue to that motor.
     * x = front left, y = front right
     * a = back left, b = back right
     * @param control The XBox controller.
     * @param forwardValue The value to set the motors at.
     */
    protected void drive(XBox360Controller control, double forwardValue) {
        if (control.getX_Button())
            flTalon.set(forwardValue);
        else
            flTalon.set(0);
        
        if (control.getA_Button())
            blTalon.set(forwardValue);
        else
            blTalon.set(0);
        
        if (control.getY_Button())
            frTalon.set(forwardValue);
        else
            frTalon.set(0);
        
        if (control.getB_Button())
            brTalon.set(forwardValue);
        else
            brTalon.set(0);
    }
    
    public String getName() {
        return "MotorTest";
    }
}
