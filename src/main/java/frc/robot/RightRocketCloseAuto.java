
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;

public class RightRocketCloseAuto extends Autonomous {
	
	
	public RightRocketCloseAuto(Robot robot) {
		super(robot);
	}
	
	@Override
	public void init() {
		
		Blueprint[] blueprints = new Blueprint[] {
				new Blueprint(0.5, this::winchStart, this::winchPeriodic),
				new Blueprint(5.0, this::goStart, this::goPeriodic), 
				new Blueprint(45.0, this::winchWaitStart, this::winchWaitPeriodic),
				new Blueprint(1.0, this::elevatorStart, this::elevatorPeriodic),
				new Blueprint(2.0, this::secondMoveStart, this::secondMovePeriodic),
				new Blueprint(2.0, this::hatchDownStart, this::hatchDownPeriodic),
				new Blueprint(5.0, this::backStart, this::backPeriodic),
				new Blueprint(5.0, this::lowerStart, this::lowerPeriodic),
				//back up and elevator down
				};
		setBlueprints(blueprints);
		
		start();
	}

	public int winchStart(){

		robot.winchController.configureGoal(robot.winchArray[1]-robot.winchEnc.get(), robot.winch_max_v, robot.winch_max_a, true);

		robot.winchController.enable();

		return 0;
	}

	public int winchPeriodic(){
		nextStage();
		return 0;
	}
	
	public int goStart() {

	
			robot.leftController.configureTrajectory(robot.rightRocketCloseTraj.getLeftTrajectory(), false);
			robot.rightController.configureTrajectory(robot.rightRocketCloseTraj.getRightTrajectory(), false);

			
			robot.leftController.enable();
			robot.rightController.enable();
			
		return 0;
    }
    
   
	

	
	public int goPeriodic() {
		if(robot.leftController.isPlanFinished()&&robot.rightController.isPlanFinished()) {
			
			robot.leftController.reset();
			robot.rightController.reset();
				
			nextStage();
		}
		return 0;
	}

	public int winchWaitStart(){
		
		return 0;
	}

	public int winchWaitPeriodic(){
		if(robot.winchDown.get()){
			nextStage();
		}
		return 0;
	}
    
    public int elevatorStart() {
	
		robot.elevatorController.configureGoal(robot.HATCH_LEVEL2-robot.elevatorEnc.get(), robot.elevator_max_v, robot.elevator_max_a, true);
		
		robot.elevatorController.enable();
        
        return 0;
    }
    
    public int elevatorPeriodic() {
		nextStage();
		
		return 0;
	}

	public int secondMoveStart() {
		robot.driveLeftEnc.reset();
		robot.driveRightEnc.reset();


		robot.leftController.configureGoal(48, 100, 100, false);
		robot.rightController.configureGoal(48, 100, 100, false);

		
		robot.leftController.enable();
		robot.rightController.enable();
		
	return 0;
}



public int secondMovePeriodic() {
	if(robot.leftController.isPlanFinished()&&robot.rightController.isPlanFinished()&&robot.elevatorController.isPlanFinished()) {
		
		robot.leftController.reset();
		robot.rightController.reset();
			
		nextStage();
	}
	return 0;
}

public int hatchDownStart(){
	robot.hatchController.configureGoal(robot.HATCH_DOWN-robot.hatchPot.get(), 500, 500, true);
	robot.hatchController.enable();
	return 0;
}
	
public int hatchDownPeriodic(){
	if(robot.hatchController.isPlanFinished()){
		
		nextStage();
	}
	return 0;
}

public int backStart(){
	robot.driveLeftEnc.reset();
	robot.driveRightEnc.reset();

	robot.leftController.configureGoal(-8.0, 100, 100, false);
	robot.rightController.configureGoal(-8.0, 100, 100, false);
		
	robot.leftController.enable();
	robot.rightController.enable();

	return 0;
}

public int backPeriodic(){

	if(robot.leftController.isPlanFinished()&&robot.rightController.isPlanFinished()){
		robot.leftController.reset();
		robot.rightController.reset();
		nextStage();
	}

	return 0;
}

public int lowerStart(){

	robot.elevatorController.configureGoal(0-robot.elevatorEnc.get(), robot.elevator_max_v, robot.elevator_max_a, false);

	robot.elevatorController.enable();

	return 0;
}

public int lowerPeriodic(){
	nextStage();
	return 0;
}


}

