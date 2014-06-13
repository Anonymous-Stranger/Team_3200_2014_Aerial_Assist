
package team3200.year2014;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * This class lets you use the Maxbotics EZ range finders easily.
 * @author Raptacon
 */
public class Ultrasonic {
    private AnalogChannel sonic;
    
    /**
     * Creates a new ultrasonic.
     * @param analogModule The module to use
     * @param channel The channel it is in
     */
    public Ultrasonic(int analogModule, int channel) {
        sonic = new AnalogChannel(analogModule, channel);
    }
    
    /**
     * Return the voltage.
     * @return 
     */
    public double getVoltage() {
        return sonic.getVoltage();
    }
    
    /**
     * Gets the distance, in inches. Calculation from:
     * http://www.maxbotix.com/articles/016.htm
     * @return Distance from the back of the ultrasonic
     */
    public double get() {
        double voltage = 5;
        return get(voltage);
    }
    /**
     * Gets the distance, in inches. Calculation from:
     * http://www.maxbotix.com/articles/016.htm
     * @param suppliedVoltage The voltage the ultrasonic is getting.
     * @return Distance from the back of the ultrasonic
     */
    private double get(double suppliedVoltage) {
        double voltsPerInch = suppliedVoltage / 512;
        
        return (sonic.getVoltage() / voltsPerInch);
    }
}
