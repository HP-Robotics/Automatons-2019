/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Servo;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  public static final int DRIVER_STICK = 0;
  public static final int OPERATOR_STICK = 1;
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  public TalonSRX topRight;
  public TalonSRX topLeft;
  public TalonSRX bottomRight;
  public TalonSRX bottomLeft;
  public Joystick driverStick;
  public Joystick operatorStick;
  public Servo hatchServo;
  public double highServo;
  public double lowServo;
  public boolean previous;
  public boolean servoUp;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    driverStick = new Joystick(0);
    operatorStick = new Joystick(1);

    topLeft = new TalonSRX(10);
    bottomLeft = new TalonSRX(11);
    topRight = new TalonSRX(12);
    bottomRight = new TalonSRX(13);

    hatchServo = new Servo(0);
    
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    SmartDashboard.putNumber("Servo High", 0.6);
    SmartDashboard.putNumber("Servo Low", 0.3);


    hatchServo.set(0.5);
    previous = false;
    servoUp = false;
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
    highServo = SmartDashboard.getNumber("Servo High", 0.6);
    lowServo = SmartDashboard.getNumber("Servo High", 0.3);
    if(highServo > 1.0)
      highServo = 1.0;
    if(lowServo < 0.0)
      lowServo = 0.0;
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
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
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
    topLeft.set(ControlMode.PercentOutput, -Math.pow(driverStick.getRawAxis(1), 3));
    bottomLeft.set(ControlMode.PercentOutput, -Math.pow(driverStick.getRawAxis(1), 3));
    topRight.set(ControlMode.PercentOutput, Math.pow(driverStick.getRawAxis(3), 3));
    bottomRight.set(ControlMode.PercentOutput, Math.pow(driverStick.getRawAxis(3), 3));

    if(driverStick.getRawButton(3)) {
      if(!previous) {
        previous=true;
        servoUp=!servoUp;
      }

      if(servoUp) {
        hatchServo.set(highServo);
      } else {
        hatchServo.set(lowServo);
      }

    } else {
      previous = false;
    }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
