/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

  public Joystick driverStick1;
  public Joystick driverStick2;
  public Joystick operatorBox;

  public TalonSRX roller;
  public TalonSRX leftSDS;
  public TalonSRX rightSDS;

  public TalonSRX hatch;
  public TalonSRX elevator;

  public Button thumb1;
  public Button trigger1;
  public Button aButton1;
  public Button bButton1;
  public Button xButton1;
  public Button yButton1;
  public Button thumb2;
  public Button trigger2;
  public Button aButton2;
  public Button bButton2;
  public Button xButton2;
  public Button yButton2;

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
  public Button sdsout;
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
    driverStick1 = new Joystick(DRIVER_STICK1);
    driverStick2 = new Joystick(DRIVER_STICK2);
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

    aButton1 = new Button(driverStick1, 5, "A");
    bButton1 = new Button(driverStick1, 6, "B");
    xButton1 = new Button(driverStick1, 3, "X");
    yButton1 = new Button(driverStick1, 4, "Y");
    trigger1 = new Button(driverStick1, 1, "SDS In");
    thumb1 = new Button(driverStick1, 2, "SDS Out");

    aButton2 = new Button(driverStick1, 5, "A");
    bButton2 = new Button(driverStick1, 6, "B");
    xButton2 = new Button(driverStick1, 3, "X");
    yButton2 = new Button(driverStick1, 4, "Y");
    trigger2 = new Button(driverStick1, 1, "SDS In");
    thumb2 = new Button(driverStick1, 2, "SDS Out");

    //resetButton = new Button(operatorBox, );
    shipCargo = new Button(operatorBox, 2, "cargo ship cargo");
    shipHatch = new Button(operatorBox, 3, "cargo ship hatch");
    cargo3 = new Button(operatorBox, 4, "Cargo Level 3");
    cargo2 = new Button(operatorBox, 5, "Cargo Level 2");
    cargo1 = new Button(operatorBox, 6, "Cargo Level 1");

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
    aButton1.update();
    bButton1.update();
    xButton1.update();
    yButton1.update();
    trigger1.update();
    thumb1.update();
    aButton2.update();
    bButton2.update();
    xButton2.update();
    yButton2.update();
    trigger2.update();
    thumb2.update();
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
    if(trigger1.on() || trigger2.on()){
      leftSDS.set(ControlMode.PercentOutput, -0.5);
      rightSDS.set(ControlMode.PercentOutput, 0.5);
      roller.set(ControlMode.PercentOutput, -0.33);
      thumb1.reset();
      thumb2.reset();
      thumb1.update();
      thumb2.update();
      System.out.println("in");
      lb.light(trigger1);
      lb.unlight(thumb1);
    }
    if(thumb1.on() || thumb2.on()){
      leftSDS.set(ControlMode.PercentOutput, 1.0);
      rightSDS.set(ControlMode.PercentOutput, -1.0);
      roller.set(ControlMode.PercentOutput, 0.33);
      trigger1.reset();
      trigger2.reset();
      trigger1.update();
      trigger2.update();
      System.out.println("out");
      lb.light(thumb1);
      lb.unlight(trigger1);
    }
    if(!trigger1.on()&&!thumb1.on()&&!trigger2.on()&&!thumb2.on()){
      leftSDS.set(ControlMode.PercentOutput, 0.0);
      rightSDS.set(ControlMode.PercentOutput, 0.0);
      roller.set(ControlMode.PercentOutput, 0.0);
      System.out.println("off");
    }

    //Drive Train
    topLeft.set(ControlMode.PercentOutput, -driverStick1.getRawAxis(1));
    bottomLeft.set(ControlMode.PercentOutput, -driverStick1.getRawAxis(1));
    topRight.set(ControlMode.PercentOutput, driverStick2.getRawAxis(1));
    bottomRight.set(ControlMode.PercentOutput, driverStick2.getRawAxis(1));
    }

    //SmartDashboard.putNumber("left enc", driveLeft.get());
    //SmartDashboard.putNumber("right enc", driveRight.get());

   //SmartDashboard.putNumber("winch", winch.get());
    //SmartDashboard.putNumber("hatch latch", hatchlatch.get());

    


  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

 
}
