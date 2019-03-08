/*
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
				new Blueprint(5.0, this::goStart, this::goPeriodic), 
                new Blueprint(1.0, this::backStart, this::backPeriodic),
				};
		setBlueprints(blueprints);
		
		start();
	}
	
	public int goStart() {

			robot.leftController.configureTrajectory(robot.stepTraj.getLeftTrajectory(), false);
			robot.rightController.configureTrajectory(robot.stepTraj.getRightTrajectory(), false);

			
			robot.leftController.enable();
			robot.rightController.enable();
			
		return 0;
    }
    
    public int backStart() {

        robot.topLeft.set(ControlMode.PercentOutput, -0.3);
        robot.bottomLeft.set(ControlMode.PercentOutput, -0.3);
        robot.topRight.set(ControlMode.PercentOutput, 0.3);
        robot.bottomRight.set(ControlMode.PercentOutput, 0.3);
        
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
		return 0;
	}


	
}

*/