
package team3200.year2014;

/**
 * Holds the values for initializing various parts of the robot.
 * @author Raptacon
 */
public abstract class RobotMap {
    
    public static final int ANALOG_MODULE = 1; // (cRIO)
    public static final int DIGITAL_MODULE = 1; // (cRIO)
    public static final int SOLENOID_MODULE = 1; // (cRIO)
    
    // The PWM channels for the drive motors
    public static final int FL_WHEEL = 3; // The Front Left wheel (PWM)
    public static final int FR_WHEEL = 1; // The Front Right wheel (PWM)
    public static final int BL_WHEEL = 4; // The Back Left wheel (PWM)
    public static final int BR_WHEEL = 2; // The Back Right wheel (PWM)
    
    
    // The PWM channels for the encoders
    public static final int LEFT_ENCODER_A = 3; // (Digital I/O)
    public static final int LEFT_ENCODER_B = 4; // (Digital I/O)
    public static final int RIGHT_ENCODER_A = 1; // (Digital I/O)
    public static final int RIGHT_ENCODER_B = 2; // (Digital I/O)
    
    
    // The PWM channel for the gyro
    public static final int GYRO = 1; // (Analog channel)
    
    
    // The Analog channel for the rangefinders
    public static final int RANGE_UNDER_BALL = 3; // (Analog channel)
    public static final int RANGE_FRONT_BOT = 2; // (Analog channel)
    
    
    // The PWM channels for the hashi
    public static final int HASHI_ROTATE = 2; // The spike that rotates the Hashi (Relay)
    public static final int HASHI_LEFT = 7; // The talon that rotates the left Hashi (PWM)
    public static final int HASHI_RIGHT = 5; // The talon that rotates the left Hashi (PWM)
    
    public static final int HASHI_LOWER_LIM = 5; // The digital input that stops the hashi from rotating into the bumper (Digital I/O)
    
    // The Compressor's channels
    public static final int COMPRESSOR_PRESSURE_SWITCH = 6; // The digital input the pressure switch is in (Digital input)
    public static final int COMPRESSOR_RELAY = 1; // The relay channel the compressor's relay is in (Relay)
    
    
    // The Solenoid channels
    //1 = middle
    //2 = right
    //3 = left
    public static final int LEFT_PISTON_SOLA = 5; // The first solenoid of the first piston (Solenoid)
    public static final int LEFT_PISTON_SOLB = 6; // The first solenoid of the second piston (Solenoid)
    public static final int MIDDLE_PISTON_SOLA = 1; // The second solenoid of the first piston (Solenoid)
    public static final int MIDDLE_PISTON_SOLB = 2; // The second solenoid of the second piston (Solenoid)
    public static final int RIGHT_PISTON_SOLA = 3; // The third solenoid of the first piston (Solenoid)
    public static final int RIGHT_PISTON_SOLB = 4; // The third solenoid of the second piston (Solenoid)
}
