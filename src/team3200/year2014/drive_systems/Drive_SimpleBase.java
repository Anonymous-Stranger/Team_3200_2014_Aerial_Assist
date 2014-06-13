
package team3200.year2014.drive_systems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.PastValueStorage;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.controller.XBox360Controller;

/**
 * Uses the RobotDriveBase class to drive the robot. It gradually increases
 * speed.
 * @author Raptacon
 */
public abstract class Drive_SimpleBase extends DriveBase {
    private final double minDriveSpeed = 0.5;
    
    // Used in driveGradual
    private double xInput;
    private double yInput;
    
    // Used in the autonomous PID control.
    protected PastValueStorage pastError;
    protected double totalError;
    
    /**
     * Create a new instance of the Drive_SimpleBase class.
     */
    public Drive_SimpleBase() {
        super(true);
        xInput = 0;
        yInput = 0;
        
        SmartDashboard.putNumber("maxInput", 0.08); // Controls how fast you can change speed of motors (0.08 = good)
        
        pastError = new PastValueStorage(10);
        setPidConstants();
    }
    
    /**
     * Sets how far to drive with the autonomous PID control.
     */
    protected abstract void setDistance();
    /**
     * Sets some PID constants.
     */
    private void setPidConstants() {
        setDistance();
        SmartDashboard.putNumber("autoDrive_P", 0.015);
        SmartDashboard.putNumber("autoDrive_I", 0.0);
        SmartDashboard.putNumber("autoDrive_D", 0);
    }
    
    public abstract void autoStartDrive();
    public abstract void autoRun(AutoStatusBase autoStatus);
    
    /**
     * Gets the distance the robot has driven.
     * @return Distance driven, in inches
     */
    private double getDistance() {
        return (leftEncoder.getDistance() + rightEncoder.getDistance())/2.0;
    }
    /**
     * Drives a set distance using a PID control.
     * @param distance The distance to drive
     */
    protected void driveDistance(double distance) {
        // Read the PID constants from the Smartdashboard
        double p = SmartDashboard.getNumber("autoDrive_P");
        double i = SmartDashboard.getNumber("autoDrive_I");
        double d = SmartDashboard.getNumber("autoDrive_D");
        
        // Calculate the PID...
        double error = distance - getDistance();
        double pControl = p * error;
        double iControl = i * totalError;
        double dControl = d * (pastError.getRecentDouble() - error);
        
        double speed = pControl + iControl + dControl;
        speed = Math.min(Math.max(speed, -1.0), 1.0); // Keep the speed iwthin acceptable values
        
        // If the speed is too low, increase it so the robot actually moves...
        if (speed > 0.01 && speed < minDriveSpeed)
            speed = minDriveSpeed;
        else if (speed > -minDriveSpeed && speed < -0.01)
            speed = -minDriveSpeed;
        
        driveGradual(speed, 0);
        
        // Store some values
        pastError.putDouble(error);
        totalError += error;
        totalError = Math.max(Math.min(totalError, 1000.0), -1000.0);
//        System.out.println("error: "+ error + " speed: "+ speed);
    }
    
    /**
     * Drive the robot.
     * @param control The controller to get values from.
     */
    final protected void drive(XBox360Controller control, double xAxis, double yAxis) {
        driveGradual(yAxis, xAxis);
//        System.out.println("\t\tlEncoder: "+ leftEncoder.get() +" r: "+ rightEncoder.get());
    }
    /**
     * Slowly speed up/down the motors so wheels.
     * @param yAxis The joystick's y-axis input (drives forward)
     * @param xAxis The joystick's x-axis input (turns)
     */
    final void driveGradual(double yAxis, double xAxis) {
        double maxChange = SmartDashboard.getNumber("maxInput");

        double xChange = xAxis - xInput;
        double yChange = yAxis - yInput;

        xInput += Math.max(Math.min(xChange, maxChange), -maxChange);
        yInput += Math.max(Math.min(yChange, maxChange), -maxChange);

        robotDrive.arcadeDrive(yInput, xInput, true);
    }
    
    protected void stopDrive() {
        driveGradual(0,0); // Slow down the robot until it stops
    }
    
    public String getName() {
        return "Simple";
    }
}
