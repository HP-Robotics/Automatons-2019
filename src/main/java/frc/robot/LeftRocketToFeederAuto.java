
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;

public class LeftRocketToFeederAuto extends Autonomous {
	
	
	public LeftRocketToFeederAuto(Robot robot) {
		super(robot);
	}
	
	@Override
	public void init() {
		
		Blueprint[] blueprints = new Blueprint[] {
			new Blueprint(5.0, this::goStart, this::goPeriodic), 
			new Blueprint(5.0, this::secondMoveStart, this::secondMovePeriodic),
		};
		setBlueprints(blueprints);
		
		start();
	}

	
	public int goStart() {

		robot.driveLeftEnc.reset();
		robot.driveRightEnc.reset();


		robot.rightController.configureTrajectory(robot.leftCloseToShipTraj.getInvertedLeftTrajectory(), false);
		robot.leftController.configureTrajectory(robot.leftCloseToShipTraj.getInvertedRightTrajectory(), false);

			
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
	

	public int secondMoveStart() {
		robot.driveLeftEnc.reset();
		robot.driveRightEnc.reset();


		robot.rightController.configureTrajectory(robot.leftShipToFeederTraj.getRightTrajectory(), false);
		robot.leftController.configureTrajectory(robot.leftShipToFeederTraj.getLeftTrajectory(), false);

			
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
}
