/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package team3200.year2014.drive_systems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.auto_status.OneBallAuto;

/**
 *
 * @author Owner
 */
public class Drive_Simple_Auto_Drive extends Drive_SimpleBase {
    private boolean startedAuto;
    private double distance; // ihn inches
    private double distanceToShoot; // ihn inches
    
    protected void setDistance() {
        distance = 4 * 12;
        SmartDashboard.putNumber("Auto Drive Distance", distance);
        SmartDashboard.putNumber("Auto Distance Shoot", 10);
    }

    public void autoStartDrive() {
        distance = SmartDashboard.getNumber("Auto Drive Distance");
        distanceToShoot = SmartDashboard.getNumber("Auto Distance Shoot");
        pastError.clear();
        pastError.putDouble(distance);
        totalError = 0;
        
        startedAuto = false;
    }

    public void autoRun(AutoStatusBase autoStatus) {
        if (autoStatus instanceof OneBallAuto)
            oneBallAuto((OneBallAuto) autoStatus);
        else
            driveGradual(0, 0);
    }
    
    public void oneBallAuto(OneBallAuto autoStatus) {
        if (startedAuto || autoStatus.isHotGoal()) { // Wait for the high goal to begin
            driveDistance(distance);
            double distanceDriven = distance - pastError.getRecentDouble();

            boolean nowDone = distanceDriven > distanceToShoot;

            // Signal it is ready to fire
            if (!autoStatus.readyFire() && nowDone) {
                autoStatus.readyFireTime = autoStatus.time;
                System.out.println("Ready to fire!");
            }
            
            startedAuto = true;
        } else {
            driveGradual(0, 0);
        }
    }
    
}
