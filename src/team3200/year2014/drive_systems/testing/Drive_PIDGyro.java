
package team3200.year2014.drive_systems.testing;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.controller.XBox360Controller;

/**
 * Drives the robot with RobotDrive. Hold the B button to drive straight.
 * @author Raptacon
 */
public class Drive_PIDGyro extends Drive_PIDBase {
    
    public Drive_PIDGyro() {
        super();
        setConstants(0.22, 0, 0); // (P, I, D)
    }
    
    double getError() {
        double error = gyro.getAngle() % 360;
        if (error > 180)
            error -= 360;
        else if (error < -180)
            error += 360;
        
        return error;
    }        
    
    public String getName() {
        return "PID";
    }
}
