
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;

public class StepAuto extends Autonomous {
	
	
	public StepAuto(Robot robot) {
		super(robot);
	}
	
	@Override
	public void init() {
		
		Blueprint[] blueprints = new Blueprint[] {
				new Blueprint(1.0, this::holdStart, this::holdPeriodic),
				new Blueprint(5.0, this::goStart, this::goPeriodic), 
                new Blueprint(2.0, this::backStart, this::backPeriodic),
				};
		setBlueprints(blueprints);
		
		start();
	}

	public int holdStart(){
		robot.hatchController.configureGoal(60-robot.hatchPot.get(), 500, 500, true);
		robot.hatchController.enable();

		return 0;
	}

	public int holdPeriodic(){
		nextStage();
		return 0;
	}
	
	public int goStart() {

			robot.driveLeftEnc.reset();
			robot.driveRightEnc.reset();

			robot.leftController.configureTrajectory(robot.stepTraj.getLeftTrajectory(), false);
			robot.rightController.configureTrajectory(robot.stepTraj.getRightTrajectory(), false);

			
			robot.leftController.enable();
			robot.rightController.enable();
			
		return 0;
    }
    
    public int backStart() {

        robot.driveLeftEnc.reset();
			robot.driveRightEnc.reset();

			robot.leftController.configureGoal(-24.0, 50, 50, false);
			robot.rightController.configureGoal(-24.0, 50, 50, false);

			
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
    
    public int backPeriodic() {
		if(robot.leftController.isPlanFinished()&&robot.rightController.isPlanFinished()) {
			
			robot.leftController.reset();
			robot.rightController.reset();
				
			nextStage();
		}
		return 0;
	}


	
}

