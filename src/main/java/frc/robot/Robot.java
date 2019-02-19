/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  public SnazzyMotionPlanner hatchController;
  public TalonPIDOutput hatchPIDOutput;
  public SnazzyMotionPlanner winchController;
  public TalonPIDOutput winchPIDOutput;
  public SnazzyMotionPlanner elevatorController;
  public TalonPIDOutput elevatorPIDOutput;
  //public VictorPIDOutput elevatorPIDOutput;
  public SnazzyMotionPlanner leftController;
  public SnazzyMotionPlanner rightController;
  public DrivePIDOutput rightPIDOutput;
  public DrivePIDOutput leftPIDOutput;
  public DrivePIDSourceInches leftInInches;
  public DrivePIDSourceInches rightInInches;

  public static final int DRIVER_STICK1 = 0;
  public static final int DRIVER_STICK2 = 1;
  public static final int OPERATOR_BOX = 2;

  public static final double HATCH_UP = 90.0;
  public static final double HATCH_DOWN = 155.0;
  public static final double HATCH_SAFE_BOTTOM = 250.0;
  public static final double HATCH_SAFE_TOP = 30.0;

  public static final int ENC_ERROR = 5;
  public static final int HATCH_LEVEL1 = 3000;
  public static final int HATCH_LEVEL2 = 156080;
  public static final int HATCH_LEVEL3 = 281190;
  public static final int CARGO_LEVEL1 = 103300;
  public static final int CARGO_LEVEL2 = 231580;
  public static final int CARGO_LEVEL3 = 341545;

  final static double DRIVE_ENC_TO_INCH = Math.PI * 6.0 * (1.0/2048.0);
  final static double DRIVE_INCH_TO_ENC = 1/DRIVE_ENC_TO_INCH;
  
  

  public boolean isUsingIntake;
	

  /* public static final double hatchkA = 0.0000501017;
  public static final double hatchkV = 0.000634177;
  * OLD POT VALUES
  */
  public static final double hatchkA = 0;//0.000501017;
  public static final double hatchkV = 0;//0.00634177;
  public static final double hatchP = 0.01;
  public static final double hatchI = 0.00004;

  public static final double winchP = 0.003;
  public static final double winchI = 0.00001;
  public static final double winch_max_a = 10000;
  public static final double winch_max_v = 4000;

  public static final double elevatorP = 0.00003;
  public static final double elevatorI = 0.000002;
  public static final double elevatorD = 0.001;
  public static final double elevatorkA = 0.0;//0.000005;//0.000095086;
  public static final double elevatorkV = 0;//0.00183371;
  public final static double elevator_max_a = 75000;
  public final static double elevator_max_v = 200000;

  public final static double driveP = 0.5;
  public final static double driveI = 0.05;
  public final static double driveD = 1.5;
  public static final double drivekV = 0.0108;
  public static final double driveKA = 0.002829;
  // 38.1 is about 180 degrees, 18.4 is about 90
  public boolean hatchDown = false;
  public boolean calibrating = false;
  public boolean pidTuning = false;

  public TalonSRX topRight;
  public TalonSRX topLeft;
  public TalonSRX bottomRight;
  public TalonSRX bottomLeft;

  public Encoder driveLeftEnc;
  public Encoder driveRightEnc;
  public Encoder elevatorEnc;
  public Encoder winchEnc;

  public Potentiometer winchPot;
  public Potentiometer hatchPot;

  public Joystick driverStick1;
  public Joystick driverStick2;
  public Joystick operatorBox;

  public TalonSRX roller;
  public TalonSRX leftSDS;
  public TalonSRX rightSDS;

  public TalonSRX hatch;
  public TalonSRX elevator;
  //public VictorSPX elevator;
  public TalonSRX winch;

  public DigitalInput winchDown;

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
  public Button hatchInButton1;
  public Button hatchOutButton1;
  public Button hatchInButton2;
  public Button hatchOutButton2;
  public Button calibrateButton;
  public Button winchToggleButton;


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
 
  public AxisButton sdsOperator;
  public double sdsState = 0;

  public ButtonGrouper elevatorButtons;
 
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
  public Button[] elevatorButtonArray;

  double[][] racetrackStartPlan = {{0, 0, 0}, {60, 0, 0}};
  double[][] racetrackTurnPlan = {{0, 0, 0},  {48, -48, -90}, {0, -96, -180}};
  double[][] shiftLeft = {{0, 0, 0},{36, 6, 0}};

  public double[] winchArray = {0, /*923,*/ 2592};
  public int winchPos = 0;
  public int winchCount = 0;
  public boolean winchDefault = true;
  
  TrajectoryPlanner racetrackStartTraj;
  TrajectoryPlanner racetrackTurnTraj;
  TrajectoryPlanner shiftLeftTraj;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    
		racetrackStartTraj = new TrajectoryPlanner(racetrackStartPlan,  50, 50, 50, "RacetrackStart");
    racetrackStartTraj.generate();
    racetrackTurnTraj = new TrajectoryPlanner(racetrackTurnPlan,50, 50, 50, "RacetrackTurn");
    racetrackTurnTraj.generate();
    shiftLeftTraj = new TrajectoryPlanner(shiftLeft, 100, 100, 100, "shiftLeft");
    shiftLeftTraj.generate();

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
    trigger1 = new Button(driverStick1, 1, "SDS Out");
    //thumb1 = new Button(driverStick1, 2, "SDS Out");

    aButton2 = new Button(driverStick2, 5, "A");
    bButton2 = new Button(driverStick2, 6, "B");
    xButton2 = new Button(driverStick2, 3, "X");
    yButton2 = new Button(driverStick2, 4, "Y");
    trigger2 = new Button(driverStick2, 1, "SDS In");
    //thumb2 = new Button(driverStick2, 2, "SDS Out");

    calibrateButton = new Button(driverStick1, 14, "Lib Owned");

    winchToggleButton = new Button(driverStick1, 2, "Winch Toggle");
    isUsingIntake = false;

    //hatchInButton1 = new Button(driverStick1, 2, "Hatch In");

    hatchInButton2 = new Button(driverStick2, 2, "Hatch In");

    resetButton = new Button(operatorBox, 4, "Reset Button");
    shipCargo = new Button(operatorBox, 11, "Cargo Ship Cargo");
    shipHatch = new Button(operatorBox, 5, "Cargo Ship Hatch");
    cargo3 = new Button(operatorBox, 12, "Cargo Level 3");
    hatch3 = new Button(operatorBox, 10, "Hatch Level 3");
    cargo2 = new Button(operatorBox, 6, "Cargo Level 2");
    hatch2 = new Button(operatorBox, 9, "Hatch Level 2");
    cargo1 = new Button(operatorBox, 3, "Cargo Level 1");
    hatch1 = new Button(operatorBox, 8, "Hatch Level 1");
    hatchFeeder = new Button(operatorBox, 2, "Hatch Feeder");
    hatchToggle = new Button(operatorBox, 1, "Hatch Toggle"); // change key
    //sdsIn and sdsOut are actually joysticks, so is magic button
    sdsOperator = new AxisButton(operatorBox, 0, "SDS Switch");


    lb = new LiteButton();
    Button[] elevatorButtonArray =  {cargo3, cargo2, cargo1, hatch3, hatch2, hatch1, shipCargo, shipHatch};
    elevatorButtons = new ButtonGrouper(elevatorButtonArray, lb);

    winchDown = new DigitalInput(9);
    topLeft = new TalonSRX(10);
    bottomLeft = new TalonSRX(11);
    topRight = new TalonSRX(12);
    bottomRight = new TalonSRX(13);

    driveRightEnc = new Encoder(11, 10, false, EncodingType.k4X);
    driveLeftEnc = new Encoder(13, 12, true, EncodingType.k4X);
    elevatorEnc = new Encoder(23, 24, true, EncodingType.k4X);
    winchEnc = new Encoder(21,22,false, EncodingType.k4X);

    //winch = new AnalogPotentiometer(0, 360, 30);
    hatchPot = new AnalogPotentiometer(4, 270, 0); /* 2700 Max, 2610 Min */

    //AnalogInput ai1 = new AnalogInput(0);
    //AnalogInput ai2 = new AnalogInput(2);

    //winch = new AnalogPotentiometer(ai1, 360, 30);
    //hatchPot = new AnalogPotentiometer(ai2, 360, 30);
    


    roller = new TalonSRX(30);
    leftSDS = new TalonSRX(40);
    rightSDS = new TalonSRX(22);
    winch = new TalonSRX(3);
    hatch = new TalonSRX(31);
    elevator = new TalonSRX(21);
    //elevator = new VictorSPX(0);

    SmartDashboard.putNumber("P", 0.7);
    SmartDashboard.putNumber("I", 0.015);
    SmartDashboard.putNumber("D", 1.0);
    SmartDashboard.putNumber("Setpoint", 0.0);

    hatchPIDOutput = new TalonPIDOutput(hatch, -1.0);
    winchPIDOutput = new TalonPIDOutput(winch, 1.0);
    elevatorPIDOutput = new TalonPIDOutput(elevator, 1.0);
    rightPIDOutput = new DrivePIDOutput(topRight, bottomRight, -1.0); 
    leftPIDOutput = new DrivePIDOutput(topLeft, bottomLeft, 1.0);

    leftInInches = new DrivePIDSourceInches(driveLeftEnc);
    rightInInches = new DrivePIDSourceInches(driveRightEnc);
    hatchController = new SnazzyMotionPlanner(hatchP, hatchI, 0, 0, hatchkA, hatchkV, 0, 0, hatchPot, hatchPIDOutput, 0.01, "hatch.csv", this);
    winchController = new SnazzyMotionPlanner(winchP, winchI, 0, 0, 0, 0, 0, 0, winchEnc, winchPIDOutput, 0.01, "winch.csv", this);
    elevatorController = new SnazzyMotionPlanner(elevatorP, elevatorI, elevatorD, 0, elevatorkA, elevatorkV, 0, 0, elevatorEnc, elevatorPIDOutput, 0.01, "elevator.csv", this);
    elevatorController.setOutputRange(-0.4, 1.0);
    elevatorController.setProtect(-0.1, 0.2, 500);
    //leftController = new SnazzyMotionPlanner(driveP, driveI, driveD, 0, driveKA, drivekV, 0, 0, leftInInches, leftPIDOutput, 0.01, "left.csv", this);
    //rightController = new SnazzyMotionPlanner(driveP, driveI, driveD, 0, driveKA, drivekV, 0, 0, rightInInches, rightPIDOutput, 0.01, "right.csv", this);


    
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

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }
  @Override
  public void teleopInit() {
    //elevatorController.configureGoal(0, elevator_max_v, elevator_max_a, true);
    //hatch.set(ControlMode.PercentOutput, 0.0);
    //winch.set(ControlMode.PercentOutput, 0.0);
    elevator.set(ControlMode.PercentOutput, 0.0);
    leftSDS.set(ControlMode.PercentOutput, 0.0);
    rightSDS.set(ControlMode.PercentOutput, 0.0);

  }
    
  /** 
   * This function is called periodically during operator control.
   * 
   */
  @Override
  public void teleopPeriodic() {
    dashboardPuts();
    updateButtons();
    if(calibrating) {
      calibrateNow(winchController);
      return;
    }
    if(pidTuning) {
      pidTuneNow(winchController);
      return;
    }
    hatchLogic();
    winchLogic();
    intakeLogic();
    magicLogic();
    drivingLogic();
    elevatorLogic();
    
  }


  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public void updateButtons(){
    aButton1.update();
    bButton1.update();
    xButton1.update();
    yButton1.update();
    trigger1.update();
    //thumb1.update();
    aButton2.update();
    bButton2.update();
    xButton2.update();
    yButton2.update();
    trigger2.update();
    //thumb2.update();
    //hatchInButton1.update();
    //hatchOutButton1.update();
    hatchInButton2.update();
    //hatchInOperator.update();
    //hatchOutOperator.update();
    hatchToggle.update();
    calibrateButton.update();
    winchToggleButton.update();
    elevatorButtons.update();
    sdsOperator.update();

  }
  public void intakeLogic(){

    //System.out.println(sdsOperator.getState());


    if(trigger2.on()||sdsOperator.getState() == -1.0){
      leftSDS.set(ControlMode.PercentOutput, -0.5);
      rightSDS.set(ControlMode.PercentOutput, 0.5);
      roller.set(ControlMode.PercentOutput, -0.50);
      trigger1.toggleOff();
      System.out.println("in");
      lb.light(trigger2);
      lb.unlight(trigger1);
    }
    if(trigger1.on()||sdsOperator.getState()==1.0){
      isUsingIntake = true;
      leftSDS.set(ControlMode.PercentOutput, 1.0);
      rightSDS.set(ControlMode.PercentOutput, -1.0);
      roller.set(ControlMode.PercentOutput, 0.15);
      trigger2.toggleOff();
      System.out.println("out");
      lb.light(trigger1);
      lb.unlight(trigger2);
    }

    if(!trigger1.on()&&!trigger2.on() && sdsOperator.getState()==0.0){
      leftSDS.set(ControlMode.PercentOutput, 0.0);
      rightSDS.set(ControlMode.PercentOutput, 0.0);
      roller.set(ControlMode.PercentOutput, 0.0);
      //System.out.println("TURN OFFFFF");
      //lb.unlight(thumb1);
      lb.unlight(trigger1);
      lb.unlight(trigger2);
      isUsingIntake = false;
    }
    /*if(!isUsingIntake && winchToggleButton.changed()){
      if(winchController.getSetpoint() == WINCH_UP_SETPOINT){
        winchController.configureGoal(WINCH_DOWN_SETPOINT, 100, 100, true);
      }else{
        winchController.configureGoal(WINCH_UP_SETPOINT, 100, 100, true);
      }
    }*/
  }

  public void magicLogic(){
    if(operatorBox.getRawAxis(1)==-1) {
      SmartDashboard.putBoolean("Magic Button", true);
      // TODO Magic code
    } else {
      SmartDashboard.putBoolean("Magic Button", false);
      // TODO Disable Magic Code
    }
  }

  public void drivingLogic(){
    topLeft.set(ControlMode.PercentOutput, -driverStick1.getRawAxis(1));
    bottomLeft.set(ControlMode.PercentOutput, -driverStick1.getRawAxis(1));
    topRight.set(ControlMode.PercentOutput, driverStick2.getRawAxis(1));
    bottomRight.set(ControlMode.PercentOutput, driverStick2.getRawAxis(1));
  }

  public void elevatorLogic(){
    if (!elevatorController.isEnabled()){
      elevatorController.enable();
    }

    
    /*if(aButton1.held()){
      elevator.set(ControlMode.PercentOutput, 0.4);
    }
    else if (bButton1.held()){
      elevator.set(ControlMode.PercentOutput, -0.4);
    }
    else{
      elevator.set(ControlMode.PercentOutput, 0.0);
    }*/
    if(winchDown.get()){
      if((hatch1.changed()&&hatch1.on())|| (shipHatch.changed()&&shipHatch.on())|| (hatchFeeder.changed()&&hatchFeeder.on()))
      {
        elevatorController.configureGoal(HATCH_LEVEL1-elevatorEnc.get(), elevator_max_v, elevator_max_a, false);
      } 
      else if(cargo1.changed()&&cargo1.on() )
      {
        elevatorController.configureGoal(CARGO_LEVEL1-elevatorEnc.get(), elevator_max_v, elevator_max_a, true);
      } 
      else if(hatch2.changed()&&hatch2.on())
      {
        elevatorController.configureGoal(HATCH_LEVEL2-elevatorEnc.get(), elevator_max_v, elevator_max_a,true);
      }
      else if(cargo2.changed()&&cargo2.on())
      {
        elevatorController.configureGoal(CARGO_LEVEL2-elevatorEnc.get(), elevator_max_v, elevator_max_a, true);
      }
      else if(hatch3.changed()&&hatch3.on())
      {
        elevatorController.configureGoal(HATCH_LEVEL3-elevatorEnc.get(), elevator_max_v, elevator_max_a,true);
      }
      else if(cargo3.changed()&&cargo3.on())
      {
        elevatorController.configureGoal(CARGO_LEVEL3-elevatorEnc.get(), elevator_max_v, elevator_max_a,true);
      }
    }
    
  }

  public void winchLogic(){
    if (!winchController.isEnabled()){
      winchController.enable();
    }
    if(winchToggleButton.changed()){
      winchDefault = false;
      winchCount++;
      //System.out.println("i tried");
      winchPos = winchCount % 2;
      System.out.println(winchArray[winchPos]);
      winchController.configureGoal(winchArray[winchPos]-winchEnc.get(), winch_max_v, winch_max_a, true);
    }
    //System.out.println(winchDown.get());

}

  public void hatchLogic(){
    if (hatchPot.get() >= HATCH_SAFE_TOP && hatchPot.get() <= HATCH_SAFE_BOTTOM) {
      if(!hatchController.isEnabled()){
        hatchController.enable();
      }
      if(hatchInButton2.changed() || hatchToggle.changed()){
        hatchDown=!hatchDown;
        if (!hatchDown) {
          hatchController.configureGoal(HATCH_UP-hatchPot.get(), 500, 500, true);
          lb.unlight(hatchToggle);
        }
        else {
          hatchController.configureGoal(HATCH_DOWN-hatchPot.get(), 500, 500, true);
          lb.light(hatchToggle);
        }
      }
    }else{
      hatchController.disable();
      hatch.set(ControlMode.PercentOutput, 0.0);
            //System.out.println("why it do that");
    }
  
  }

  public void dashboardPuts(){
    SmartDashboard.putNumber("hatchPot", hatchPot.get());
    SmartDashboard.putNumber("elevatorEnc", elevatorEnc.get());
    SmartDashboard.putNumber("winchEnc", winchEnc.get());
    SmartDashboard.putNumber("left enc", driveLeftEnc.get());
    SmartDashboard.putNumber("right enc", driveRightEnc.get());
    SmartDashboard.putNumber("left in", leftInInches.pidGet());
    SmartDashboard.putNumber("right in", rightInInches.pidGet());
    SmartDashboard.putNumber("elevator set", elevatorController.getSetpoint());
    SmartDashboard.putBoolean("limit switch", winchDown.get());
  }
  public void calibrateNow(SnazzyMotionPlanner p) {
    if(calibrateButton.changed()&& calibrateButton.on()){
        /*driveRightEnc.reset();
        driveLeftEnc.reset();
        leftController.enable(); 
        rightController.enable(); 
        leftController.startCalibration();
        rightController.startCalibration();
        */
        winchEnc.reset();
        p.enable();
        winchController.startCalibration();
        System.out.println("enabel");
				
    }else if (calibrateButton.changed()&& !calibrateButton.on()){
      /*
      leftController.disable();
      rightController.disable();
      */
      p.disable();
      System.out.println("disabel");
    }
    //System.out.println(calibrateButton.changed()+" " +calibrateButton.on());
  }
  
  public void pidTuneNow(SnazzyMotionPlanner p) {
      //rightController.setPID(SmartDashboard.getNumber("P", 0), SmartDashboard.getNumber("I", 0), SmartDashboard.getNumber("D", 0));
      //leftController.setPID(SmartDashboard.getNumber("P", 0), SmartDashboard.getNumber("I", 0), SmartDashboard.getNumber("D", 0));
      if(calibrateButton.on()){
          if(calibrateButton.changed()) {
            /*driveRightEnc.reset();
            driveLeftEnc.reset();
            //leftController.configureGoal(SmartDashboard.getNumber("Setpoint", 0.0), 100, 100, false);
            //rightController.configureGoal(SmartDashboard.getNumber("Setpoint", 0.0), 100, 100, false);
            
            //leftController.configureTrajectory(shiftLeftTraj.getLeftTrajectory(), false);
            //rightController.configureTrajectory(shiftLeftTraj.getRightTrajectory(), false);

            rightController.enable();
            leftController.enable();
            System.out.println("enable" + SmartDashboard.getNumber("Setpoint", 0.0));*/

            //elevator.set(ControlMode.PercentOutput, SmartDashboard.getNumber("Setpoint",0.0));

          }
          
        }else if (calibrateButton.changed()&& !calibrateButton.on()){
          /*rightController.disable();
          leftController.disable();
          System.out.println("DISABLE");*/
          elevator.set(ControlMode.PercentOutput, 0.0);
      }

      /*if (hatchPot.get() <= 1500 && hatchPot.get() >= 3500) {
        rightController.disable();
          leftController.disable();
        System.out.println("DISABLE");
      }*/
    }
	
	

}
