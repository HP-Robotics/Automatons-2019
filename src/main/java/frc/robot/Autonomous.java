
package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Autonomous {
	Robot robot;
	Timer timer;
	double initTime;
	int stage = 0;
	StageDataElement[] stageData;

	class StageDataElement {
		Blueprint blueprint;
		boolean entered;
	}

	public Autonomous(Robot robot) {
		this.robot = robot;
	}

	public void setBlueprints(Blueprint[] b) {
		stageData = new StageDataElement[b.length];

		for(int i = 0; i < b.length; i++) {
			stageData[i] = new StageDataElement();

			stageData[i].blueprint = b[i];
			stageData[i].entered = false;
		}
	}

	public void start() {

		for(int i = 0; i < stageData.length; i++) {
			stageData[i].entered = false;
		}
		robot.driveLeftEnc.reset();
		robot.driveRightEnc.reset();

		robot.leftController.reset();
		robot.rightController.reset();

		stage = 0;
		initTime = Timer.getFPGATimestamp();

		timer = new Timer();
		timer.reset();
		timer.start();
	}

	public boolean checkStageTimeout() {
		if(stageData == null) {
			return true;
		}
		if (stage < 0 || stage >= stageData.length) {
			robot.topLeft.set(ControlMode.PercentOutput, 0.0);
			robot.bottomLeft.set(ControlMode.PercentOutput, 0.0);
			robot.topRight.set(ControlMode.PercentOutput, 0.0);
			robot.bottomRight.set(ControlMode.PercentOutput, 0.0);
			return true;
		}

		if (timer.get() > stageData[stage].blueprint.m_timeout) {

			System.out.printf("stage %d timed out\n", stage);
			nextStage();
			return true;
		}
		return false;
	}

	public void nextStage() {
		System.out.printf("Stage Finished: %d\tTime: %f\tTotal Time: %f\n",stage,timer.get(),Timer.getFPGATimestamp() - initTime);
		timer.reset();
		stage++;

		if(stage >= stageData.length) {
			end();
		}
	}
	
	public void stopAll() {
		stage = stageData.length;
	}
	public void end() {
		System.out.println("-----");
		System.out.printf("Auto Finished:\tTotal Time: %f\n",Timer.getFPGATimestamp() - initTime);

		robot.rightController.reset();
		robot.leftController.reset();
	}

	public void init() {
		System.out.println("Override me!");
	}

	public void periodic() {
		int stageAtEntry = stage;
		if(checkStageTimeout()) {
			return;
		}
		if(!stageData[stageAtEntry].entered) {
			stageData[stageAtEntry].blueprint.m_start.getAsInt();
			stageData[stageAtEntry].entered = true;
		}
		
		if(stage == stageAtEntry) {
			stageData[stage].blueprint.m_periodic.getAsInt();	
		}
	}
}
