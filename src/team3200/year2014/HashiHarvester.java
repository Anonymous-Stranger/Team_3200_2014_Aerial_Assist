
package team3200.year2014;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.auto_status.OneBallAuto;
import team3200.year2014.auto_status.TwoBallAuto;
import team3200.year2014.controller.XBox360Controller;

/**
 * The harvester. It spins two sticks. They rotate up/down to grab the ball.
 * @author Raptacon
 */
public class HashiHarvester {
//    private final double hashiSpeed = -0.5;
//    private final double hashiSpeed = 0.3; // Comp bot is flipped
    
    private final Relay rotate;
    private final Talon leftHashi;
    private final Talon rightHashi;
    
    private final DigitalInput hashiLowered;
    
    private int rot; // Used to slow down the rotation of the harvester
    
    private long hashiDownTime;
    private long hashiUpTime;
    
    private static class Rotate {
        static Rotate up = new Rotate(1);
        static Rotate down = new Rotate(-1);
        static Rotate stop = new Rotate(0);
        final int value;
        
        private Rotate(int value) {
            this.value = value;
        }
    }
    
    public HashiHarvester() {
        rotate = new Relay(RobotMap.DIGITAL_MODULE, RobotMap.HASHI_ROTATE);
        leftHashi = new Talon(RobotMap.DIGITAL_MODULE, RobotMap.HASHI_LEFT);
        rightHashi = new Talon(RobotMap.DIGITAL_MODULE, RobotMap.HASHI_RIGHT);
        
        hashiLowered = new DigitalInput(RobotMap.DIGITAL_MODULE, RobotMap.HASHI_LOWER_LIM);
        
        rot = 0;
        SmartDashboard.putBoolean("Limit Switch_Active", true);
        
        SmartDashboard.putNumber("Hashi Down Time", 900); // 1st part of auto
        SmartDashboard.putNumber("Hashi Up Time", 3300); // after 1st ball is shot
        SmartDashboard.putNumber("Hashi Speed", 0.5);
    }
    
    void autoStart() {
        hashiDownTime = (long) SmartDashboard.getNumber("Hashi Down Time");
        hashiUpTime = (long) SmartDashboard.getNumber("Hashi Up Time");
    }
    
    void autoRun(AutoStatusBase autoStatus) {
        if (autoStatus instanceof OneBallAuto)
            oneBallAuto((OneBallAuto) autoStatus);
        else if (autoStatus instanceof TwoBallAuto)
            twoBallAuto((TwoBallAuto) autoStatus);
        else {
            rotateHashi(Rotate.stop);
            spinHashi(0);
        }
    }
    
    private void oneBallAuto(OneBallAuto autoStatus) {
        rotateHashi(Rotate.down);
    }
    
    private void twoBallAuto(TwoBallAuto autoStatus) {
        double hashiSpeed = SmartDashboard.getNumber("Hashi Speed");
        
        if (autoStatus.time < hashiDownTime) { // Lower the hashi for 250 milliseconds
//        if (autoStatus.time < 2000) { // superInflated
            rotateHashi(Rotate.down);
            spinHashi(hashiSpeed);
        
//        } else if (autoStatus.time < 2000) { // Try to grab the ball
//            spinHashi(0);
//            rotateHashi(Rotate.down);
            
        } else { // After ball grabbed...
            if (!autoStatus.isBallCaught())
                autoStatus.ballCaughtTime = autoStatus.time;
            
            if (autoStatus.isShot1Started()) {
                if (autoStatus.time - autoStatus.shot1StartTime < 2000) {
                    spinHashi(0);
                    rotateHashi(Rotate.stop);
                    
                // Wait until 2 seconds after shooting & spin hashi (pick up the ball)
                } else if (autoStatus.time - autoStatus.shot1StartTime < hashiUpTime) {
                    spinHashi(hashiSpeed);
                    rotateHashi(Rotate.up, -1, 1);
                    
                // After picking up the ball, wait
                } else if (autoStatus.time - autoStatus.shot1StartTime < 4000) {
                    rotateHashi(Rotate.stop);
                    spinHashi(0);
                
                    
                // Lower the chopsticks
                } else if (autoStatus.time - autoStatus.shot1StartTime < 4500) {
                    rotateHashi(Rotate.down);
                    spinHashi(0);
                    
                // Done
                } else {
                    if (!autoStatus.isBall2Ready())
                        autoStatus.ball2ReadyTime = autoStatus.time;
                    
                    rotateHashi(Rotate.down);
                    spinHashi(0);
                }
            } else {
                spinHashi(0);
                rotateHashi(Rotate.stop);
            }
        }
    }
    
    private void spinHashi(double speed) {
            leftHashi.set(speed);
            rightHashi.set(speed);
    }
    private void rotateHashi(Rotate value) {
        rotateHashi(value, 0, 1);
    }
        
    private void rotateHashi(Rotate value, int startRotVal, int endRotVal) {
        if (rot >= endRotVal)
            rot = startRotVal;
        else
            rot = 1;
        
        if (value == Rotate.up) {
//            rotate.set(Relay.Value.kReverse);
            if (rot > 0)
                rotate.set(Relay.Value.kForward); // Comp bot is flipped
            else
                rotate.set(Relay.Value.kOff);
        } else if (value == Rotate.down) {
            if (!hashiLowered.get() || !SmartDashboard.getBoolean("Limit Switch_Active")) { // Check if can rotate down
//                rotate.set(Relay.Value.kForward);
                if (rot > 0)
                    rotate.set(Relay.Value.kReverse); // Comp bot is flipped
                else
                    rotate.set(Relay.Value.kOff);
//                System.out.println("Dropping...");
//                System.out.println("\t\tlimit: "+ hashiLowered.get());
//                System.out.println("\t\tsmartdash: "+ (!SmartDashboard.getBoolean("Limit Switch_Active")));
//                System.out.println();
            } else
                rotate.set(Relay.Value.kOff);
        } else if (value == Rotate.stop) {
            rotate.set(Relay.Value.kOff);
            rot = 0;
        }
//        System.out.println("rot: "+ rot);
        SmartDashboard.putBoolean("Hashi Lowered", hashiLowered.get());
    }
    
    /**
     * Control the harvester.
     * @param control The controller
     * @param joyControl The joystick
     */
    void operateHarvester(XBox360Controller control, XBox360Controller subControl) {
        operateHarvester(control, subControl, -1);
    }
    
    /**
     * Control the harvester.
     * @param control The controller
     * @param joyControl The joystick
     */
    void operateHarvester(XBox360Controller control, XBox360Controller subControl, long dryFireStartedTime) {
        double hashiSpeed = SmartDashboard.getNumber("Hashi Speed");
        
        // Use the left y axis to rotate the harvester, if it's not too far down
        if (dryFireStartedTime == -1) { // If not dry firing
            if (subControl.getLeftY_Axis() > 0.8) // Forwards on the left joystick rotates the harvester down
                rotateHashi(Rotate.down);
            else if (subControl.getLeftY_Axis() < -0.8) // Backwards on the left joystick rotates the harvester up
                rotateHashi(Rotate.up);
            else {
                rotateHashi(Rotate.stop);
            }

        } else { // If dry firing...
            rotateHashi(Rotate.down);
        }
        
        if (subControl.getA_Button()) { // Use the A button to spin the hashis (for intake)
            spinHashi(hashiSpeed);
//            System.out.println("Spin in");
            
        } else if (subControl.getB_Button()) { // Use the B button to spin the hashis (for spit out)
            spinHashi(-hashiSpeed);
//            System.out.println("Spin out");
            
        } else {
            spinHashi(0);
        }
    }
    
    /**
     * Stop the harvester.
     */
    void stop() {
        spinHashi(0);
        rotateHashi(Rotate.stop);
    }
}
