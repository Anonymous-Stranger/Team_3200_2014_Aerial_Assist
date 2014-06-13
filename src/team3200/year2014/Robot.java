/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package team3200.year2014;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team3200.year2014.auto_status.AutoStatusBase;
import team3200.year2014.auto_status.MenuChooser;
import team3200.year2014.auto_status.NoAuto;
import team3200.year2014.auto_status.NoBallAuto;
import team3200.year2014.auto_status.OneBallAuto;
import team3200.year2014.auto_status.OneBallAutoDrive;
import team3200.year2014.auto_status.TwoBallAuto;
import team3200.year2014.controller.XBox360Controller;
import team3200.year2014.drive_systems.DriveBase;
import team3200.year2014.drive_systems.Drive_Simple_Auto_Drive;
import team3200.year2014.drive_systems.Drive_Simple_Auto_Still;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    private final static String[] autoChoices = {
            "No Auto", "Drive-Only Auto", "One Ball Auto", "Two Ball Auto", "One Ball Auto Drive Shot"};
    
    MenuChooser autoChoice;
    AutoStatusBase autoStatus;
    
    XBox360Controller control;
    XBox360Controller subControl;
    DriveBase drive; // The drive subsystem
    HashiHarvester harvester; // The harvester with the two chopsticks, or hashi
    PneumaticShooter shooter;
    
    long autoStartTime; // The time autonomous started
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        control = new XBox360Controller(1);
        subControl = new XBox360Controller(2);
        
        autoChoice = new MenuChooser(autoChoices, 3);
        autoStatus = new NoAuto();
        
        // Define the subsystems
        drive = new Drive_Simple_Auto_Still();
        harvester = new HashiHarvester();
        shooter = new PneumaticShooter();
        
        SmartDashboard.putString("Drive type", drive.getName());
        SmartDashboard.putBoolean("Compressor_Enabled", true);
        
        System.out.println("Initialized");
    }
    
    /**
     * When a driver presses Start or Select, sends true to the SmartDashboard.
     */
    public void testControls() {
        SmartDashboard.putBoolean("Driver 1", control.getStart_Button() || control.getSelect_Button());
        SmartDashboard.putBoolean("Driver 2", subControl.getStart_Button() || subControl.getSelect_Button());
    }
    
    /**
     * This is run when the robot should just wait.
     */
    public void idle() {
        drive.stop();
        harvester.stop();
        shooter.setPistons(false); // retract the pistons
        shooter.disable();
        
        autoChoice.update();
    }
    
    /**
     * Enable the compressor if the SmartDashboard value for it is true.
     */
    public void tryEnableCompressor() {
        if (SmartDashboard.getBoolean("Compressor_Enabled"))
            shooter.startCompressor();
        else
            shooter.stopCompressor();
    }
    
    /**
     * This runs when disabled mode begins.
     */
    public void disabledInit() {
        idle();
        shooter.stopCompressor();
        shooter.disable();
        System.out.println("Disabled Start");
    }
    /**
     * This runs once in a while during disabled mode.
     */
    public void disabledPeriodic() {
        idle();
        testControls();
    }

    /**
     * This runs when disabled mode begins.
     */
    public void autonomousInit() {
        autoStartTime = System.currentTimeMillis();
        
        // Set the autonomous
        if (autoChoice.getSelectedInt() == 1) {
            autoStatus = new NoBallAuto();
            drive = new Drive_Simple_Auto_Still();
            System.out.println("Using drive-only auto");
            
        } else if (autoChoice.getSelectedInt() == 2) {
            autoStatus = new OneBallAuto();
            drive = new Drive_Simple_Auto_Still();
            System.out.println("Using one ball auto, still shot");
            
        } else if (autoChoice.getSelectedInt() == 3) {
            autoStatus = new TwoBallAuto();
            drive = new Drive_Simple_Auto_Still();
            System.out.println("Using two ball auto");
            
        } else if (autoChoice.getSelectedInt() == 4) {
            autoStatus = new OneBallAutoDrive();
            drive = new Drive_Simple_Auto_Drive();
            System.out.println("Using one ball auto, drive shot");
        } else {
            autoStatus = new NoAuto();
            drive = new Drive_Simple_Auto_Still();
            System.out.println("Using no auto");
        }
        
        autoStatus.reset();
        
        drive.autoStart();
        harvester.autoStart();
        tryEnableCompressor();
        System.out.println("Autonomous Start");
    }
    /**
     * This runs once in a while during autonomous mode.
     */
    public void autonomousPeriodic() {
        long autoTime = System.currentTimeMillis() - autoStartTime;
        autoStatus.update(autoTime);
        drive.autoRun(autoStatus);
        harvester.autoRun(autoStatus);
        shooter.autoRun(autoStatus);
    }

    /**
     * This runs when tele-op mode begins.
     */
    public void teleopInit() {
        idle();
        if (SmartDashboard.getBoolean("Compressor_Enabled"))
            shooter.startCompressor();
        else
            shooter.stopCompressor();
        System.out.println("Teleop start");
    }
    /**
     * This runs once in a while during tele-op mode.
     */
    public void teleopPeriodic() {
        drive.drive(control);
        shooter.operateShooter(control, subControl);
        harvester.operateHarvester(control, subControl, shooter.getDryFireStartTime());
        
        testControls();
        autoChoice.update();
        tryEnableCompressor();
    }
    
    /**
     * This runs once in a while during test mode.
     */
    public void testPeriodic() {
        idle();
        autoChoice.update();
    }
    
}
