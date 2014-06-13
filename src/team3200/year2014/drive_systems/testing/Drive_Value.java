
package team3200.year2014.drive_systems.testing;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.controller.XBox360Controller;
import team3200.year2014.drive_systems.DriveBase;

/**
 * Uses RobotDriveBase to control the robot. It drives the robot simply. Hold right
 bumper button to drive the robot based on the value of DriveBaseSpeed on the
 SmartDashboard.
 * @author Raptacon
 */
public class Drive_Value extends DriveBase {
    
    /**
     * Create a new instance of the DriveBase class.
     */
    public Drive_Value() {
        super(true);
        SmartDashboard.putNumber("DriveSpeed", 0);
    }
    
    public void autoRun(AutoStatusBase autoStatus) { }
    
    /**
     * DriveBase the robot.
     * @param control The controller to get values from.
     */
    protected void drive(XBox360Controller control, double xAxis, double yAxis) {
        if (control.getRightBumper_Button())
            robotDrive.arcadeDrive(SmartDashboard.getNumber("DriveSpeed"), 0);
        else
            robotDrive.arcadeDrive(yAxis, xAxis, true);
   
        System.out.println("x: "+ xAxis +" y: "+ yAxis);
//        System.out.println("\t\tGyro: "+ gyro.getAngle());
//        System.out.println("\t\tleftEncoder: "+ leftEncoder.get() +" rightEncoder: "+ rightEncoder.get());
//        System.out.println("leftEncoder: "+ leftEncoder.getDistance() +" rightEncoder: "+ rightEncoder.getDistance());
//        System.out.println("\t\txIn: "+ xInput +" yIn: "+ yInput);
    }
    
    public String getName() {
        return "Value";
    }
}
