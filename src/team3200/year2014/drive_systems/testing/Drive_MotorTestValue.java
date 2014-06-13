
package team3200.year2014.drive_systems.testing;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.controller.XBox360Controller;

/**
 * DriveBase each motor seperately.
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
public class Drive_MotorTestValue extends Drive_MotorTest {
    
    /**
     * Create a new instance of the DriveBase class.
     */
    public Drive_MotorTestValue() {
        super();
        SmartDashboard.putNumber("DriveSpeed", 0);
    }
    
    protected void drive(XBox360Controller control, double xAxis, double yAxis) {
        drive(control, SmartDashboard.getNumber("DriveSpeed"));
        
        System.out.println("x: "+ xAxis +" y: "+ yAxis);
//        System.out.println("\t\tleftEncoder: "+ leftEncoder.get() +" rightEncoder: "+ rightEncoder.get());
//        System.out.println("leftEncoder: "+ leftEncoder.getDistance() +" rightEncoder: "+ rightEncoder.getDistance());
//        System.out.println("\t\txIn: "+ xInput +" yIn: "+ yInput);
    }
    
    public String getName() {
        return "MotorTestValue";
    }
}
