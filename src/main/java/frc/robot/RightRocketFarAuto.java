/*
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;

public class RightRocketFarAuto extends Autonomous {
	
	
	public RightRocketFarAuto(Robot robot) {
		super(robot);
	}
	
	@Override
	public void init() {
		
		Blueprint[] blueprints = new Blueprint[] {
				new Blueprint(5.0, this::goStart, this::goPeriodic), 
                new Blueprint(1.0, this::turnStart, this::turnPeriodic),
				};
		setBlueprints(blueprints);
		
		start();
	}
	
	public int goStart() {

			robot.leftController.configureTrajectory(robot.rocketRightFarTraj.getLeftTrajectory(), false);
			robot.rightController.configureTrajectory(robot.rocketRightFarTraj.getRightTrajectory(), false);

			
			robot.leftController.enable();
			robot.rightController.enable();
			
		return 0;
    }
    
    public int turnStart() {

        robot.leftController.configureGoal(32, 100, 100, false);
		robot.rightController.configureGoal(-32, 100, 100, false);

			
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
    
    public int turnPeriodic() {
		if(robot.leftController.isPlanFinished()&&robot.rightController.isPlanFinished()) {
			
			robot.leftController.reset();
			robot.rightController.reset();
				
			nextStage();
		}
		return 0;
	}


	
}

*/