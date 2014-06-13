
package team3200.year2014.drive_systems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.auto_status.NoAuto;
import team3200.year2014.auto_status.NoBallAuto;
import team3200.year2014.auto_status.OneBallAuto;
import team3200.year2014.auto_status.TwoBallAuto;

/**
 *
 * @author Owner
 */
public class Drive_Simple_Auto_Still extends Drive_SimpleBase {
    private double distance;
    private boolean autoPart2Started;

    protected void setDistance() {
        distance = 26;
        SmartDashboard.putNumber("Auto Drive Distance", distance);
    }

    public void autoStartDrive() {
        autoPart2Started = false;
        autoStartAny();
        pastError.putDouble(distance);
    }
    private void autoStartAny() {
        distance = SmartDashboard.getNumber("Auto Drive Distance");
        pastError.clear();
        totalError = 0;
    }
    
    private final double errorAllowed = 3;
    public void autoRun(AutoStatusBase autoStatus) {
        if (autoStatus instanceof NoAuto)
            driveGradual(0, 0);
        else if (autoStatus instanceof NoBallAuto)
            noBallAuto((NoBallAuto) autoStatus);
        else if (autoStatus instanceof OneBallAuto)
            oneBallAuto((OneBallAuto) autoStatus);
        else if (autoStatus instanceof TwoBallAuto)
            twoBallAuto((TwoBallAuto) autoStatus);
        else
            driveGradual(0, 0);
    }
    
    private long genericAuto(long time, boolean part1Done, boolean lastShotDone, long lastShotStartTime) {
        long result = -1;
        
        if (!part1Done) { // Drive to the line
            driveDistance(distance);
            System.out.println("\t\tavg: "+ pastError.getAverageValue());

            boolean nowPart1Done = pastError.getAverageValue() < errorAllowed && pastError.getAverageValue() > -errorAllowed
                    && pastError.getRecentDouble() < errorAllowed && pastError.getRecentDouble() > -errorAllowed; // Sees if it's just finished
            if (!part1Done && nowPart1Done) {
                result = time;
                System.out.println("Part 1 done");
            }
            
        } else if (!lastShotDone) { // Wait till shots are done...
            driveGradual(0, 0);
            
        } else { // Drive into the zone
            if (time - lastShotStartTime < 2000) {
                if (!autoPart2Started) { // If just started 2nd part of 1 ball auto...
                    autoStartAny();
                    distance += 36;
                    pastError.putDouble(distance);
                }
                autoPart2Started = true;
                driveGradual(0,0);
            } else {
                driveDistance(distance);
            }
        }
        
        return result;
    }
    
    private void noBallAuto(NoBallAuto autoStatus) {
        genericAuto(autoStatus.time, true, true, 0);
    }
    private void oneBallAuto(OneBallAuto autoStatus) {
        long timeDone = genericAuto(autoStatus.time, autoStatus.readyFire(), autoStatus.shootStarted(), autoStatus.shootStartTime);
        if (timeDone != -1)
            autoStatus.readyFireTime = timeDone;
    }
    
    void twoBallAuto(TwoBallAuto autoStatus) {
        if (!autoStatus.isBallCaught()) { // Wait if ball isn't caught yet
            driveGradual(0, 0);
            
        } else {
            long timeDone = genericAuto(autoStatus.time, autoStatus.isShootReady(), autoStatus.isShotsDone(), autoStatus.shotsDoneTime);
            if (timeDone != -1)
                autoStatus.shootReadyTime = timeDone;
        }
    }
}
