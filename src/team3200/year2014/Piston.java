// TODO: Replace this with DoubleSolenoid

package team3200.year2014;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class controls the state of the two spikes on the piston.
 * @author Raptacon
 */
public class Piston {
    private final Solenoid solenoid1;
    private final Solenoid solenoid2;
    private boolean inverted;
    
    /**
     * Creates a new piston.
     * @param pistonModule The digital module the solenoids are in
     * @param solenoid1 The channel the first solenoid is in
     * @param solenoid2 The channel the second solenoid is in
     */
    public Piston(int pistonModule, int solenoid1, int solenoid2) {
        this.solenoid1 = new Solenoid(pistonModule, solenoid1);
        this.solenoid2 = new Solenoid(pistonModule, solenoid2);
        
        inverted = false;
    }
    
    /**
     * Sets if the pistons are inverted.
     * @param value Whether the piston is inverted.
     */
    public void setInverted(boolean value) {
        inverted = value;
    }
    
    /**
     * Whether to extend or retract the piston.
     * @param value Whether to extend or retract the piston.
     */
    public void set(boolean value) {
        solenoid1.set(value != inverted);
        solenoid2.set(value == inverted);
    }
}
