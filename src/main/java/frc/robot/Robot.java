/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
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

  public static final int DRIVER_STICK1 = 0;
  public static final int DRIVER_STICK2 = 1;
  public static final int OPERATOR_BOX = 2;

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public TalonSRX topRight;
  public TalonSRX topLeft;
  public TalonSRX bottomRight;
  public TalonSRX bottomLeft;

  public Encoder driveLeft;
  public Encoder driveRight;
  public Encoder elevatorEnc;

  public Potentiometer winch;
  public Potentiometer hatchlatch;

  public Joystick driverStick;
  public Joystick operatorBox;

  public TalonSRX roller;
  public TalonSRX leftSDS;
  public TalonSRX rightSDS;

  public TalonSRX hatch;
  public TalonSRX elevator;

  public Button thumb;
  public Button trigger;
  public Button aButton;
  public Button bButton;
  public Button xButton;
  public Button yButton;

  public Button resetButton;
  public Button magicButton;
  public Button hatch1;
  public Button hatch2;
  public Button hatch3;
  public Button cargo1;
  public Button cargo2;
  public Button cargo3;
  public Button hatchFeeder;
  public Button hatchToggle;
  public Button sdsIn;
  public Button sdsOut;
  public Button shipHatch;
  public Button shipCargo;
 
  
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

  public LiteButton lb;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    driverStick = new Joystick(DRIVER_STICK1);
    operatorBox = new Joystick(OPERATOR_BOX);

    /**
     * CAN IDs
     * 
     * <9: Don't use
     * 10-19: Drive train
     * 20: PDP don't use
     * 21-29: Misc
     * 30-39: Intake stuff
     * 40-49: Elevator stuff
     * 
     * Feel free to change
     */

    aButton = new Button(driverStick, 5, "A");
    bButton = new Button(driverStick, 6, "B");
    xButton = new Button(driverStick, 3, "X");
    yButton = new Button(driverStick, 4, "Y");
    trigger = new Button(driverStick, 1, "SDS In");
    thumb = new Button(driverStick, 2, "SDS Out");

    // resetButton = new Button(operatorBox, );
    // shipCargo = new Button(operatorBox, 2, "cargo ship cargo");
    // shipHatch = new Button(operatorBox, 3, "cargo ship hatch");
    // cargo3 = new Button(operatorBox, 4, "Cargo Level 3");
    // hatch3 = new Button(operatorBox, 5, "Hatch Level 2");
    // cargo2 = new Button(operatorBox, 6, "Cargo Level 2");
    // hatch2 = new Button(operatorBox, 7, "Hatch Level 2")
    sdsIn = new Button(operatorBox, 8, "SDS In");
    sdsOut = new Button(operatorBox, 9, "SDS Out");

    lb = new LiteButton();

    topLeft = new TalonSRX(10);
    bottomLeft = new TalonSRX(11);
    topRight = new TalonSRX(12);
    bottomRight = new TalonSRX(13);

    driveRight = new Encoder(10,11, false, EncodingType.k4X);
    driveLeft = new Encoder(12,13, true, EncodingType.k4X);
    elevatorEnc = new Encoder(14,15, false, EncodingType.k4X);

    //winch = new AnalogPotentiometer(0, 360, 30);
    //hatchlatch = new AnalogPotentiometer(0,360,30);

    ////AnalogInput ai1 = new AnalogInput(1);
    //AnalogInput ai2 = new AnalogInput(2);

    //winch = new AnalogPotentiometer(ai1, 360, 30);
    //hatchlatch = new AnalogPotentiometer(ai2, 360, 30);

    //hatch = new TalonSRX(30);
    
    //elevator = new TalonSRX(40);

    roller = new TalonSRX(30);
    leftSDS = new TalonSRX(21);
    rightSDS = new TalonSRX(22);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);


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
      leftSDS.set(ControlMode.PercentOutput, -0.5);
      rightSDS.set(ControlMode.PercentOutput, 0.5);
      roller.set(ControlMode.PercentOutput, -0.33);
      thumb.reset();
      thumb.update();
      System.out.println("in");
      lb.light(trigger);
      lb.unlight(thumb);
    }
    if(thumb.on()){
      leftSDS.set(ControlMode.PercentOutput, 1.0);
      rightSDS.set(ControlMode.PercentOutput, -1.0);
      roller.set(ControlMode.PercentOutput, 0.33);
      trigger.reset();
      trigger.update();
      System.out.println("out");
      lb.light(thumb);
      lb.unlight(trigger);
    }
    if(!trigger.on()&&!thumb.on()){
      leftSDS.set(ControlMode.PercentOutput, 0.0);
      rightSDS.set(ControlMode.PercentOutput, 0.0);
      roller.set(ControlMode.PercentOutput, 0.0);
      lb.unlight(thumb);
      lb.unlight(trigger);
      System.out.println("off");
    }

    //Drive Train
    /*topLeft.set(ControlMode.PercentOutput, -Math.pow(driverStick.getRawAxis(1), 3));
    bottomLeft.set(ControlMode.PercentOutput, -Math.pow(driverStick.getRawAxis(1), 3));
    topRight.set(ControlMode.PercentOutput, Math.pow(driverStick.getRawAxis(3), 3));
    bottomRight.set(ControlMode.PercentOutput, Math.pow(driverStick.getRawAxis(3), 3));*/

    //SmartDashboard.putNumber("left enc", driveLeft.get());
    //SmartDashboard.putNumber("right enc", driveRight.get());

   //SmartDashboard.putNumber("winch", winch.get());
    //SmartDashboard.putNumber("hatch latch", hatchlatch.get());

    


  }
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

 
}
