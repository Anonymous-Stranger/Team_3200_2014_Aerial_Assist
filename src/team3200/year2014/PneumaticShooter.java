
package team3200.year2014;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.auto_status.OneBallAuto;
import team3200.year2014.auto_status.TwoBallAuto;
import team3200.year2014.controller.XBox360Controller;

/**
 * This is the pneumatic shooter.
 * @author Raptacon
 */
public class PneumaticShooter {
    private final Compressor compressor;
    private final Piston leftPiston; // The left piston on the arm... There's supposed to be three
    private final Piston middlePiston; // The middle piston on the arm... There's supposed to be three
    private final Piston rightPiston; // The right piston on the arm... There's supposed to be three
    
    private final RangeFinderBall ballIn; // Detects whether there is a ball inside or not
    
    private long timeStarted; // The time the pistons extended
    private long timeFire; // The time to extend the pistons
    
    private final long minTimeShoot = 100; // The minimum the time shoot should get to
    private final long maxTimeShoot = 350; // The maximum the time shoot should get to
    
    private long airPulseIncreaseStart; // When the increase air pulse button was held...
    private long airPulseDecreaseStart; // When the decrease air pulse button was held...
    
    private final long airPulseChangeDelay = 250; // Amount of time to hold the button to change the air pulse
    
    private boolean dryFireButtonHeld; // If dry fire button is held
    private long dryFireStart; // Time dry fire started
    
    private boolean aShootButtonHeld; // If any shoot button is held
    private boolean powSwitchButtonHeld; // If quick switch button is held
    
    private final long[] RANGE_FOR_DIST = new long[20]; // The amount of time the pistons should extend for a set distance to enter the high goal
    
    /**
     * This class holds constants for ranges for special shots. Ranges are in
     * milliseconds.
     */
    private class CustomRanges {
        static final long BOUNCE_PASS = 500; // Shoot weak so teamates can grab it
        static final long TRUSS = 500; // Shoot so that robot can self-catch
        static final long FULL_POWER = 500; // Shoot max power
    }
    
    /**
     * Creates the pneumatic shooter.
     */
    public PneumaticShooter() {
        // Define pneumatics-related items
        compressor  = new Compressor(RobotMap.DIGITAL_MODULE, RobotMap.COMPRESSOR_PRESSURE_SWITCH,
                RobotMap.DIGITAL_MODULE, RobotMap.COMPRESSOR_RELAY);
        leftPiston = new Piston(RobotMap.SOLENOID_MODULE, RobotMap.LEFT_PISTON_SOLA, RobotMap.LEFT_PISTON_SOLB);
        middlePiston = new Piston(RobotMap.SOLENOID_MODULE, RobotMap.MIDDLE_PISTON_SOLA, RobotMap.MIDDLE_PISTON_SOLB);
        rightPiston = new Piston(RobotMap.SOLENOID_MODULE, RobotMap.RIGHT_PISTON_SOLA, RobotMap.RIGHT_PISTON_SOLB);
        
        leftPiston.set(false);
        middlePiston.set(false);
        rightPiston.set(false);
        
        ballIn = new RangeFinderBall(RobotMap.ANALOG_MODULE, RobotMap.RANGE_UNDER_BALL);
        
        timeStarted = -1;
        timeFire = -1;
        
        aShootButtonHeld = false;
        powSwitchButtonHeld = false;
        airPulseIncreaseStart = -1;
        airPulseDecreaseStart = -1;
        
        dryFireButtonHeld = false;
        dryFireStart = -1;
        
        RANGE_FOR_DIST[6] = 500;
        RANGE_FOR_DIST[10] = 600;
        
        SmartDashboard.putNumber("Time Extend (milliseconds)", 350);
        SmartDashboard.putNumber("Auto 2nd Ball Air pulse (milliseconds)", 250);
        SmartDashboard.putBoolean("Ball Sensor_Active", true);
        SmartDashboard.putBoolean("Ball in", false);
        
        SmartDashboard.putBoolean("Unstuckify", false);
    }
    
    /**
     * Allow the compressor to be turned on.
     */
    public void startCompressor() {
        compressor.start();
    }
    /**
     * Prevent the compressor from turning on.
     */
    public void stopCompressor() {
        compressor.stop();
    }
    
    /**
     * Stop the shooter.
     */
    public void disable() {
        airPulseIncreaseStart = -1;
        airPulseDecreaseStart = -1;
        
        dryFireButtonHeld = false;
        dryFireStart = -1;
        
        aShootButtonHeld = false;
        powSwitchButtonHeld = false;
        
        SmartDashboard.putBoolean("Ball in", ballIn.isBallIn());
        SmartDashboard.putBoolean("Unstuckify", false);
    }
    
    void autoRun(AutoStatusBase autoStatus) {
        if (autoStatus instanceof OneBallAuto)
            oneBallAuto((OneBallAuto) autoStatus);
        else if (autoStatus instanceof TwoBallAuto)
            twoBallAuto((TwoBallAuto) autoStatus);
        
        SmartDashboard.putBoolean("Ball in", ballIn.isBallIn());
    }
    
    void oneBallAuto(OneBallAuto autoStatus) {
        long timeShoot = (long) SmartDashboard.getNumber("Time Extend (milliseconds)");
        // Fire a 1/2 second after it is ready
        if (autoStatus.readyFire() && autoStatus.time - autoStatus.readyFireTime > 500l &&
                !autoStatus.shootStarted() && canShoot() && autoStatus.isHotGoal()) {
            firePistonsTimed(timeShoot);
            autoStatus.shootStartTime = autoStatus.time;
            System.out.println("Firing");
        }
        idle();
    }
    
    void twoBallAuto(TwoBallAuto autoStatus) {
        long timeShoot = (long) SmartDashboard.getNumber("Time Extend (milliseconds)");
        long timeShoot2 = (long) SmartDashboard.getNumber("Auto 2nd Ball Air pulse (milliseconds)");
        
        // Fire a 1/2 second after 1st ball is ready
        if (autoStatus.isShootReady() && autoStatus.time - autoStatus.shootReadyTime > 500 &&
                !autoStatus.isShot1Started() && canShoot()) {
            firePistonsTimed(timeShoot);
            autoStatus.shot1StartTime = autoStatus.time;
            System.out.println("Firing #1");
            
        // Fire a 1/2 second after 2nd ball is ready
        } else if (autoStatus.isBall2Ready() && autoStatus.time - autoStatus.ball2ReadyTime > 500 &&
                !autoStatus.isShotsDone() && canShoot()) {
            firePistonsTimed(timeShoot2);
            autoStatus.shotsDoneTime = autoStatus.time;
            System.out.println("Firing #2");
        }
        
        idle();
    }
    
    /**
     * Extend or retract the pistons.
     * @param value Whether to extend or retract the pistons.
     */
    public void setPistons(boolean value) {
        leftPiston.set(value);
        middlePiston.set(value);
        rightPiston.set(value);
        
        timeStarted = -1;
        timeFire = -1;
        
        aShootButtonHeld = false; // The pistons have been set manually, so assume not controlling shots
    }
    
    /**
     * Returns whether it can shoot.
     * @return Whether it can shoot.
     */
    private boolean canShoot() {
        return ballIn.isBallIn() || !SmartDashboard.getBoolean("Ball Sensor_Active");
    }
    
    /**
     * Make the pistons extend for a set amount of time.
     */
    void idle() {
        if (timeStarted != -1 &&
                System.currentTimeMillis() - timeStarted > timeFire) { // If the pistons were set to extend for a certain amount of time, and it's passed...
            leftPiston.set(false);
            middlePiston.set(false);
            rightPiston.set(false);
            timeStarted = -1;
            timeFire = -1;
        }
        SmartDashboard.putBoolean("Ball in", ballIn.isBallIn());
    }
    
    /**
     * Runs the shooter.
     * @param control The xbox controller to use
     * @param subControl The other xbox controller to use
     */
    public void operateShooter(XBox360Controller control, XBox360Controller subControl) {
        long timeShoot = (long) SmartDashboard.getNumber("Time Extend (milliseconds)");

        // Change air pulse buttons
        // Increase the air pulse by 50
        if (subControl.getRightBumper_Button()) {
            timeShoot = roundDown(timeShoot, 50);
            
            if (airPulseIncreaseStart == -1) { // If the button was just pressed...
                airPulseIncreaseStart = System.currentTimeMillis();
                timeShoot = changeTimeShoot(timeShoot, 50);
            } else if (System.currentTimeMillis() - airPulseIncreaseStart >= airPulseChangeDelay) { // If the button was held long enough...
                airPulseIncreaseStart = System.currentTimeMillis();
                timeShoot = changeTimeShoot(timeShoot, 50);
            }
        } else { // If the button was released
                airPulseIncreaseStart = -1;
        }
        
        // Decrease the air pulse by 50
        if (subControl.getLeftBumper_Button()) {
            timeShoot = roundDown(timeShoot, 50);
            
            if (airPulseDecreaseStart == -1) { // If the button was just pressed...
                airPulseDecreaseStart = System.currentTimeMillis();
                timeShoot = changeTimeShoot(timeShoot, -50);
            } else if (System.currentTimeMillis() - airPulseDecreaseStart >= airPulseChangeDelay) { // If the button was held long enough...
                airPulseDecreaseStart = System.currentTimeMillis();
                timeShoot = changeTimeShoot(timeShoot, -50);
            }
        } else { // If the button was released
                airPulseDecreaseStart = -1;
        }
        
        // Set the air pulse to 145, 155, or 350 milliseconds.
        if (!powSwitchButtonHeld) {
            if (subControl.getTrigger_Axis() > 0.8) // Left trigger
                timeShoot = 145l;
            else if (subControl.getTrigger_Axis() < -0.8) // Right trigger
                timeShoot = 350l;
            else if (subControl.getY_Button()) // Y Button
                timeShoot = 155l;
        }
        powSwitchButtonHeld = subControl.getTrigger_Axis() > 0.8 || subControl.getTrigger_Axis() < -0.8 || subControl.getY_Button();
        
        // Fire the ball
        if (canShoot()) {
            if (control.getTrigger_Axis() < -0.8) { // Shoot by reading a value off smartdashboard (right trigger button)
                firePistonsTimed(timeShoot);
                aShootButtonHeld = true;
            } else { // No shoot buttons are held...
                aShootButtonHeld = false;
            }
        }
        
        dryFire(subControl);
        idle();
        
        SmartDashboard.putNumber("Time Extend (milliseconds)", timeShoot);
    }
    
    /**
     * Fire the shooter with a low air pulse.
     * @param subControl The second controller
     */
    void dryFire(XBox360Controller subControl) {
        if (subControl.getX_Button() && dryFireStart == -1 && !dryFireButtonHeld) {
            dryFireStart = System.currentTimeMillis();
            firePistonsTimed(30);
        }
        dryFireButtonHeld = subControl.getX_Button();
        
        // Stop the dry fire after a second, or after the button is released
        if (System.currentTimeMillis() - dryFireStart > 1000 || ! dryFireButtonHeld)
            dryFireStart = -1;
        SmartDashboard.putBoolean("Unstuckify", dryFireStart != -1);
    }
    /**
     * Returns when the dry fire began.
     * @return Time the dry fire began, in milliseconds.
     */
    long getDryFireStartTime() {
        return dryFireStart;
    }
    
    /**
     * Round a number down to the nearest allowed value.
     * @param value The value to decrease
     * @param roundToNearest The lowest acceptable value. (this value, multiplied by any integer, are also allowed)
     * @return The rounded value
     */
    private long roundDown(long value, long roundToNearest) {
        return value - (value % roundToNearest);
    }
    /**
     * Changes the smartdashboard value by a certain amount. It keeps the value
     * within the min / max values.
     * @param timeShoot The current timeShoot value
     * @param changeAmount The amount to change it by
     * @return The new timeShoot
     */
    private long changeTimeShoot(long timeShoot, long changeAmount) {
        timeShoot += changeAmount;
        timeShoot = Math.max(Math.min(timeShoot, maxTimeShoot), minTimeShoot);
        
        return timeShoot;
    }
    
    /**
     * Fire the pistons so the ball goes through the high goal a certain
     * distance away, in feet.
     * @param distance The distance the goal is from the shooter.
     */
    private void fire(int distance) {
        long bestTime = 0;
        int rangeDiff = distance;

        // Loop through the array and find the closest range with some data
        for (int i = 0; i < RANGE_FOR_DIST.length; i++) {
            int diff = Math.abs(distance - i);
            if (RANGE_FOR_DIST[i] > 0 && diff <= rangeDiff) {
                rangeDiff = diff;
                bestTime = RANGE_FOR_DIST[i];
            }
        }
        
        firePistonsTimed(bestTime);
    }
    
    /**
     * Fire the pistons for a certain amount of time, in milliseconds.
     * @param time The amount of time to fire the pistons for
     */
    private void firePistonsTimed(long time) {
        if (!aShootButtonHeld && timeStarted == -1 && time > 0) { // If the pistons aren't set to be extended and should be extended...
            timeStarted = System.currentTimeMillis();
            timeFire = time;
            
            leftPiston.set(true);
            middlePiston.set(true);
            rightPiston.set(true);
        }
    }
}
