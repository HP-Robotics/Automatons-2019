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
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

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

  public TalonSRX hatch;
  public TalonSRX elevator;
  public TalonSRX roller;
  public TalonSRX leftSDS;
  public TalonSRX rightSDS;


  public boolean previousServ;
  public boolean servoUp;

  Button xButton;
  Button aButton;
  Button yButton;
  Button bButton;
  Button trigger;
  Button thumb;

  private static final double CAMERA_HEIGHT = 44.5;
  private static final double TARGET_HEIGHT = 31.25;
  private static final double CAMERA_ANGLE = -2.75;
  private static final double CENTERX = 5.0;
  public double oVertAngle;
  public double lVertAngle;
  public double rVertAngle;
  public double horzAngle;
  public double dx;
  public double mdy;
  public double ody;
  public double ldy;
  public double rdy;
  public double xp;
  public double yp;
  public double heading;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    driverStick = new Joystick(0);
    operatorStick = new Joystick(1);

    aButton = new Button(driverStick.getRawButton(5));
    bButton = new Button(driverStick.getRawButton(6));
    xButton = new Button(driverStick.getRawButton(3));
    yButton = new Button(driverStick.getRawButton(4));
    trigger = new Button(driverStick.getRawButton(1));
    thumb = new Button(driverStick.getRawButton(2));

    topLeft = new TalonSRX(10);
    bottomLeft = new TalonSRX(11);
    topRight = new TalonSRX(12);
    bottomRight = new TalonSRX(13);

    hatch = new TalonSRX(30);
    
    elevator = new TalonSRX(40);

    roller = new TalonSRX(20);
    leftSDS = new TalonSRX(21);
    rightSDS = new TalonSRX(22);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    previousServ = false;
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
    aButton.update();
    bButton.update();
    xButton.update();
    yButton.update();
    trigger.update();
    thumb.update();
    
    topLeft.set(ControlMode.PercentOutput, driverStick.getRawAxis(1));
    bottomLeft.set(ControlMode.PercentOutput, driverStick.getRawAxis(1));
    topRight.set(ControlMode.PercentOutput, -driverStick.getRawAxis(3));
	  bottomRight.set(ControlMode.PercentOutput, -driverStick.getRawAxis(3));

    /*horzAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    oVertAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    lVertAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty0").getDouble(0)*27;
    rVertAngle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty1").getDouble(0)*27;

    ody = distanceCalc(oVertAngle);
    ldy = distanceCalc(lVertAngle);
    rdy = distanceCalc(rVertAngle);

    dx = ody*Math.tan(Math.toRadians(horzAngle));
    //System.out.println("x: "+ dx + ", y: " + ody + ", angle: " + horzAngle );
    
    xp = (Math.pow(rdy,2)-Math.pow(ldy,2))/(4*CENTERX);
    yp = Math.sqrt(Math.pow(ldy,2)-Math.pow(((Math.pow(rdy,2)-Math.pow(ldy,2))/(4*CENTERX))-CENTERX,2));
    heading = Math.atan(xp/yp);
    System.out.println("d: "+oVertAngle+", d0: "+ lVertAngle + ", d1: "+rVertAngle+", x': "+ xp +", y': "+yp+", heading: "+ heading);*/
    if(trigger.on()){
      leftSDS.set(ControlMode.PercentOutput, 0.7);
      rightSDS.set(ControlMode.PercentOutput, 0.7);
      roller.set(ControlMode.PercentOutput, 0.5);
      thumb.reset();
    }else if(thumb.on()){
      leftSDS.set(ControlMode.PercentOutput, -0.7);
      rightSDS.set(ControlMode.PercentOutput, -0.7);
      roller.set(ControlMode.PercentOutput, -0.5);
      trigger.reset();
    }else{
      leftSDS.set(ControlMode.PercentOutput, 0.0);
      rightSDS.set(ControlMode.PercentOutput, 0.0);
      roller.set(ControlMode.PercentOutput, 0.0);
    }

    

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public double distanceCalc(double tangle){
    mdy = (TARGET_HEIGHT-CAMERA_HEIGHT)/ Math.tan(Math.toRadians(tangle+CAMERA_ANGLE));
    double dy;
    if(mdy>40){
      dy = mdy + mdy*(0.000153563751273*mdy*mdy - 0.009043606024534*mdy + 0.116352752118378);
    }else  {
      dy = mdy;
    }
    return dy;
  }
}
