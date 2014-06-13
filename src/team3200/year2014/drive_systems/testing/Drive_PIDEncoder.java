
package team3200.year2014.drive_systems.testing;

/**
 * Drives the robot with RobotDrive. Hold the B button to drive straight.
 * @author Raptacon
 */
public class Drive_PIDEncoder extends Drive_PIDBase {
    public Drive_PIDEncoder() {
        super();
        setConstants(0.22, 0, 0); // (P, I, D)
    }
    
    double getError() {
//        return rightEncoder.getRate() - leftEncoder.getRate();
        return 0;
    }
    
    public String getName() {
        return "PID Encoder";
    }
}
