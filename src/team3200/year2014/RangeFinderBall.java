
package team3200.year2014;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This detects whether the ultrasonic sees the ball or not in the shooter.
 * @author Raptacon
 */
public class RangeFinderBall {
    private final AnalogChannel rangeFinder;

    /**
     * Create the range finder.
     * @param analogModule The analog module it is in
     * @param channel The channel it is in
     */
    public RangeFinderBall(int analogModule, int channel) {
        rangeFinder = new AnalogChannel(analogModule, channel);
        SmartDashboard.putNumber("Ball In Voltage", 1.4);
        SmartDashboard.putNumber("Ball Range Voltage", 0);
    }
    
    /**
     * Returns whether the ball is in or not.
     * @return Whether the ball is in or not
     */
    public boolean isBallIn() {
        SmartDashboard.putNumber("Ball Range Voltage", rangeFinder.getVoltage());
        return (rangeFinder.getVoltage() > SmartDashboard.getNumber("Ball In Voltage", 1.8));
    }
}
