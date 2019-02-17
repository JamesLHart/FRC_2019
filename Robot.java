/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// These came from the 2018 code
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
// import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Victor;

// Needed for Camera
edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // From 2018 code
  private DifferentialDrive m_myRobot;
  private Joystick gamePad;	
  Spark m_leftFront, m_leftRear, m_rightFront, m_rightRear, winchMotor, intakeLeft, intakeRight;
  SpeedControllerGroup spool, m_left, m_right;
  Victor spoolLeft, spoolRight, intake;
  // Constants for button mapping
  int winchUp = 1;
  int winchDown = 2;
  int pistonBack = 5;
  int pistonOut = 6;

  double WINCHFACTOR = 0.5;
  double PISTONFACTOR = 0.75;
  double DRIVEFACTOR = 0.9;
  

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    CameraServer.getInstance().startAutomaticCapture("USB Camera 0", 0);
    CameraServer.getInstance().startAutomaticCapture("USB Camera 1", 1);

    		// Set up Xbox controller
		gamePad = new Joystick(0);
		//Map gamepad axes to channels
		gamePad.setYChannel(1);
		gamePad.setXChannel(4);
		// Create motor objects for drive train
		m_leftFront = new Spark(3);
		m_leftRear = new Spark(4);
		
		m_rightFront = new Spark(1);
		m_rightRear = new Spark(2);
		
		
		// Create motor objects for spool
		spoolLeft = new Victor(5);
    spoolRight = new Victor(6);
    
    //Create motor object for intake
    intake = new Victor(7);
		
    // Groups motors
    //From 2018 code
    m_left = new SpeedControllerGroup(m_leftFront,m_leftRear);
		m_right = new SpeedControllerGroup(m_rightFront,m_rightRear);
    spool = new SpeedControllerGroup(spoolLeft,spoolRight);
    
    m_myRobot = new DifferentialDrive(m_left, m_right);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    //From 2018 code
   /*Timer timer ;
			m_left = new SpeedControllerGroup(m_leftFront,m_leftRear);
			m_right = new SpeedControllerGroup(m_rightFront,m_rightRear);
			spool = new SpeedControllerGroup(intakeLeft,intakeRight);
			
			m_left.set(1.0);
			m_right.set(1.0);
			Timer.delay(1.5);
			m_left.set(0.0);
			m_right.set(0.0);
			spool.set(1.0);
			Timer.delay(1.0);
			m_left.set(-1.0);
			m_right.set(-1.0);
			Timer.delay(0.5);
			m_left.set(0.0);
			m_right.set(0.0);
			winchMotor.set(1.0);
			Timer.delay(2.0);
      winchMotor.set(0.0);
      */
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    m_myRobot.arcadeDrive(gamePad.getY()*DRIVEFACTOR, gamePad.getX()*DRIVEFACTOR);
		
		if (gamePad.getRawButton(winchUp)) {
			spool.set(1.0 * WINCHFACTOR);
		} 
		else if (gamePad.getRawButton(winchDown)) {
			spool.set(-1.0 * WINCHFACTOR);
    }
    else if (gamePad.getRawButton(pistonOut)) {
      intake.set(1.0 * PISTONFACTOR);
    }
    else if (gamePad.getRawButton(pistonBack)) {
      intake.set(-1.0 * PISTONFACTOR);
    }
		else {
      spool.set(0.0);
      intake.set(0.0);
		}		
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
