/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package team3200.year2014.auto_status;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Owner
 */
public class OneBallAuto extends AutoStatusBase {
    private static class HotGoalTime {
        private final static HotGoalTime first5Secs = new HotGoalTime(1);
        private final static HotGoalTime last5Secs = new HotGoalTime(2);
        private final int value;
        
        private HotGoalTime(int v) {
            value = v;
        }
    }
    public long shootStartTime; // When it started shooting
    public long readyFireTime; // When it's ready to start shooting
    private HotGoalTime hotGoal;
    
    public OneBallAuto() {
        reset();
    }
    
    public void update(long autoTime) {
        time = autoTime;
        if (autoTime < 3000) {
            if (SmartDashboard.getString("COLOR_SAMPLE_NEAREST", "").equals("0000FF"))
                hotGoal = HotGoalTime.first5Secs;
        } else {
            if (hotGoal == null)
                hotGoal = HotGoalTime.last5Secs;
        }
    }
    
    public final void reset() {
        shootStartTime = -1;
        readyFireTime = -1;
        time = -1;
        hotGoal = null;
    }
    
    public boolean isHotGoal() {
        boolean result = false;
        if (hotGoal == HotGoalTime.first5Secs && time <= 5000)
            result = true;
        if (hotGoal == HotGoalTime.last5Secs && time > 5000)
            result = true;
        
        return result;
    }
    
    public boolean readyFire() {
        return readyFireTime != -1;
    }
    public boolean shootStarted() {
        return shootStartTime != -1;
    }
}
