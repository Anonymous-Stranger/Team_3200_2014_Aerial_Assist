
package team3200.year2014.drive_systems.testing;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.controller.XBox360Controller;
import team3200.year2014.drive_systems.DriveBase;

/**
 * The base class for driving straight using a PID control.
 * 
 * Uses the RobotDrive to control the robot. This drives the robot simply.
 * Hold B button to drive straight using a PID.
 * @author Raptacon
 */
abstract class Drive_PIDBase extends DriveBase {
    
    // Used in drivePID
    boolean PID_enabled;
    double maxTotalError = 200; // Restrain the totalError by this value (for I, in PID)
    
    double prevError;
    double totalError;
    
    // Used in driveGradual
    private double xInput;
    private double yInput;
    
    /**
     * Create a new instance of the DriveBase class.
     */
    public Drive_PIDBase() {
        super(true);
        SmartDashboard.putNumber("DriveSpeed", 0);
        
        PID_enabled = false;
        setConstants(0, 0, 0);
        
        prevError = 0;
        totalError = 0;
        
        SmartDashboard.putNumber("maxInput", 0.08); // Controls how fast you can change speed of motors (0.08 = good)
        xInput = 0;
        yInput = 0;
    }
    
    /**
     * Sets the PID constants in the SmartDashboard.
     * @param pConstant The P used in drivePID
     * @param iConstant The I used in drivePID
     * @param dConstant The D used in drivePID
     */
    final void setConstants(double pConstant, double iConstant, double dConstant) {
        SmartDashboard.putNumber("drive_P", pConstant);
        SmartDashboard.putNumber("drive_I", iConstant);
        SmartDashboard.putNumber("drive_D", dConstant);
    }
    
    public void autoRun(AutoStatusBase autoStatus) {
        driveGradual(0, 0);
    }
    
    protected void drive(XBox360Controller control, double xAxis, double yAxis) {

        if (control.getB_Button()) { // Hold right bumper button to drive PID
//            if (xAxis == 0 && yAxis > .15) { // If it should use the PID
                if (!PID_enabled) { // If the PID just started...
                    prevError = 0;
                    totalError = 0;
                }
                PID_enabled = true;
                drivePID(SmartDashboard.getNumber("DriveSpeed"));
//                drivePID(yAxis);
//            } else {
//                driveGradual(yAxis, xAxis);
//                PID_enabled = false;
//            }
        } else {
//            robotDrive.arcadeDrive(yAxis, xAxis, true);
            driveGradual(yAxis, xAxis);
            PID_enabled = false;
        }
   
//        System.out.println("x: "+ xAxis +" y: "+ yAxis);
        System.out.println("xIn: "+ xInput +" yIn: "+ yInput);
        System.out.println("\t\tPID: "+ PID_enabled);
//        System.out.println("\t\tGyro: "+ gyro.getAngle());
//        System.out.println("\t\tleftEncoder: "+ leftEncoder.get() +" rightEncoder: "+ rightEncoder.get());
//        System.out.println("leftEncoder: "+ leftEncoder.getDistance() +" rightEncoder: "+ rightEncoder.getDistance());
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
    
    /**
     * Gets how far the bot is turned to the right.
     * @return How far the bot is turned to the right
     */
    abstract double getError();
    /**
     * Use a PID loop to drive straight.
     * @param yAxis The amount to drive forward
     */
    final void drivePID(double yAxis) {
        double p = SmartDashboard.getNumber("drive_P");
//        double i = SmartDashboard.getNumber("drive_I");
//        double d = SmartDashboard.getNumber("drive_D");
        
        double error = getError();
        
        double pControl = p * error;
//        double iControl = i * totalError;
//        double dControl = d * (prevError - error);
        
        double turn = pControl;
        turn = Math.min(Math.max(turn, -0.6), 0.6);
        
        System.out.println("Constants=p: "+ p);
        System.out.println("p: "+ pControl +" turn: "+ turn);
        
        driveGradual(yAxis, turn);
        
        prevError = error;
        totalError += error;
        totalError = Math.min(Math.max(totalError, -maxTotalError), maxTotalError);
    }
    
    protected void stopDrive() {
        driveGradual(0,0); // Slow down the robot until it stops
//        robotDrive.setLeftRightMotorOutputs(0, 0);
        PID_enabled = false;
    }
}
