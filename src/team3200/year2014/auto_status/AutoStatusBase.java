/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package team3200.year2014.auto_status;

/**
 *
 * @author Raptacon
 */
public abstract class AutoStatusBase {
    static AutoStatusBase instance;
    public long time; // Time since autonomous started
    
    public void update(long autoTime) {
        time = autoTime;
    }
    public abstract void reset();
}
