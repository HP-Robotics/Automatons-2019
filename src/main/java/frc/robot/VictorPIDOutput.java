package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.PIDOutput;

public class VictorPIDOutput implements PIDOutput {
	
	VictorSPX m;
	double mult;
	public VictorPIDOutput(VictorSPX motor, double multiplier)  {
		m = motor;
		mult = multiplier;
	}

	@Override
	public void pidWrite(double output) {

			m.set(ControlMode.PercentOutput, mult * output);
	}

}
