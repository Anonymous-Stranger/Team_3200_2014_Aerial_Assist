
package team3200.year2014.drive_systems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.RobotMap;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.controller.XBox360Controller;

/**
 * This is the base Drive class.
 * @author Raptacon
 */
public abstract class DriveBase {
    protected static RobotDrive robotDrive;
    protected final boolean useRobotDrive;
    
    protected static Talon flTalon;
    protected static Talon frTalon;
    protected static Talon blTalon;
    protected static Talon brTalon;
    
    protected static Encoder leftEncoder;
    protected static Encoder rightEncoder;
    
    protected static Gyro gyro;
    
    private static boolean aButton_Held;
    private static boolean driveForward;
    
    private static boolean initialized = false;
    
    /**
     * Creates a new Drive.
     * @param useRobotDrive Whether to initialize robotDrive or not.
     */
    protected DriveBase(boolean useRobotDrive) {
        this.useRobotDrive = useRobotDrive;
        init();
        robotDrive.setSafetyEnabled(useRobotDrive);
    }
    
    private void init() {
        if (!initialized) {
            flTalon = new Talon(RobotMap.DIGITAL_MODULE, RobotMap.FL_WHEEL);
            frTalon = new Talon(RobotMap.DIGITAL_MODULE, RobotMap.FR_WHEEL);
            blTalon = new Talon(RobotMap.DIGITAL_MODULE, RobotMap.BL_WHEEL);
            brTalon = new Talon(RobotMap.DIGITAL_MODULE, RobotMap.BR_WHEEL);

            System.out.println("Module: "+ RobotMap.DIGITAL_MODULE +" used for drive.");
            System.out.println("FL wheel at: "+ RobotMap.FL_WHEEL +" FR wheel at: "+ RobotMap.FR_WHEEL);
            System.out.println("BL wheel at: "+ RobotMap.BL_WHEEL +" BR wheel at: "+ RobotMap.BR_WHEEL);

//            if (useRobotDrive)
                robotDrive = new RobotDrive(flTalon, blTalon, frTalon, brTalon);
//            else
//                robotDrive = null;

            aButton_Held = false;
            driveForward = true;
            SmartDashboard.putBoolean("Drive_Direction", driveForward);

            leftEncoder = new Encoder(RobotMap.DIGITAL_MODULE, RobotMap.LEFT_ENCODER_A,
                    RobotMap.DIGITAL_MODULE, RobotMap.LEFT_ENCODER_B, true, CounterBase.EncodingType.k4X);
            rightEncoder = new Encoder(RobotMap.DIGITAL_MODULE, RobotMap.RIGHT_ENCODER_A,
                    RobotMap.DIGITAL_MODULE, RobotMap.RIGHT_ENCODER_B, false, CounterBase.EncodingType.k4X);

    //        leftEncoder.setReverseDirection(false);
            leftEncoder.setReverseDirection(true); // Flipped on comp bot
            leftEncoder.setMinRate(10);
            leftEncoder.setDistancePerPulse(6 * Math.PI / 250);
            leftEncoder.start();
    //        rightEncoder.setReverseDirection(true);
            rightEncoder.setReverseDirection(false); // Flipped on comp bot
            rightEncoder.setMinRate(10);
            rightEncoder.setDistancePerPulse(6 * Math.PI / 250);
            rightEncoder.start();

            gyro = new Gyro(RobotMap.ANALOG_MODULE, RobotMap.GYRO);

            SmartDashboard.putNumber("Max turn speed", 1);
        }
        initialized = true;
    }
    
    /**
     * Runs at the start of autonomous. To add something else to the start of
     * autonomous, override autoStartDrive.
     */
    public final void autoStart() {
        autoStartDrive();
    }
    /**
     * Runs during the start of autonomous. Override this to add code to the
     * start of autonomous.
     */
    protected void autoStartDrive() {}
    /**
     * Runs during autonomous.
     * @param autoStatus What state the robot is in during autonomous.
     */
    public abstract void autoRun(AutoStatusBase autoStatus);
    
    /**
     * Drive the robot. This cannot be overwritten. To change something,
     * overwrite the drive method / function with the xAxis & yAxis parameters.
     * @param control The controller to get values from.
     */
    public final void drive(XBox360Controller control) {
        double xAxis = -control.getRightX_Axis();
        double yAxis = control.getLeftY_Axis();
        
        // Limit the x axis to the value on the smartdashboard
        xAxis *= SmartDashboard.getNumber("Max turn speed");
        
        // Press the a button to reverse the drive
        if (!aButton_Held && control.getA_Button())
            driveForward = !driveForward;
        aButton_Held = control.getA_Button();
        if (!driveForward)
            yAxis *= -1;
        
        if (control.getRightBumper_Button()) { // Right bumper button slows robot
            yAxis *= 0.8;
            xAxis *= 0.8;
        }
        
        drive(control, xAxis, yAxis);
//        System.out.println("lEncoder: "+ leftEncoder.getRate() + " rEncoder: "+ rightEncoder.getRate());
        SmartDashboard.putBoolean("Drive_Direction", driveForward);
        SmartDashboard.putNumber("Left Encoder Count", leftEncoder.get());
        SmartDashboard.putNumber("Right Encoder Count", rightEncoder.get());
    }
    /**
     * Drive the robot. Override this to control the robot.
     * @param control The XBox 360 controller.
     * @param xAxis The amount to turn
     * @param yAxis The amount to drive forward
     */
    protected abstract void drive(XBox360Controller control, double xAxis, double yAxis);
    
    /**
     * Stop the robot. This cannot be overwritten. To change something,
 overwrite the stopDriveBase method / function.
     */
    public final void stop() {
        aButton_Held = false;
        driveForward = true;
        SmartDashboard.putBoolean("Drive_Direction", true);
        SmartDashboard.putNumber("Left Encoder Count", leftEncoder.get());
        SmartDashboard.putNumber("Right Encoder Count", rightEncoder.get());
        
        leftEncoder.reset();
        rightEncoder.reset();
        gyro.reset();
    }
    /**
     * Stop the robot. This can be overridden.
     */
    protected void stopDrive() {
        if (useRobotDrive)
            robotDrive.setLeftRightMotorOutputs(0, 0);
        else {
            flTalon.set(0);
            frTalon.set(0);
            blTalon.set(0);
            brTalon.set(0);
        }
    }
    
    /**
     * Get the name of this drive.
     * @return The name of the drive.
     */
    public abstract String getName();
}
