/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package team3200.year2014.auto_status;

/**
 *
 * @author Owner
 */
public class TwoBallAuto extends AutoStatusBase {
    public long ballCaughtTime; // When the 2nd ball is partly grabbed
    public long ball2ReadyTime; // When the 2nd ball is on the shooter
    
    public long shootReadyTime; // When the robot is positioned to shoot
    public long shot1StartTime; // When the 1st shot was fired
    public long shotsDoneTime; // When the 2nd shot was fired

    public void reset() {
        ballCaughtTime = -1;
        ball2ReadyTime = -1;

        shootReadyTime = -1;
        shot1StartTime = -1;
        shotsDoneTime = -1;
    }
    
    public boolean isBallCaught() {
        return ballCaughtTime != -1;
    }
    public boolean isBall2Ready() {
        return ball2ReadyTime != -1;
    }
    public boolean isShootReady() {
        return shootReadyTime != -1;
    }
    public boolean isShot1Started() {
        return shot1StartTime != -1;
    }
    public boolean isShotsDone() {
        return shotsDoneTime != -1;
    }
}
