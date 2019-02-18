package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PIDOutput;

public class TalonPIDOutput implements PIDOutput {
	
	TalonSRX m;
	double mult;
	public TalonPIDOutput(TalonSRX motor, double multiplier)  {
		m = motor;
		mult = multiplier;
	}

	@Override
	public void pidWrite(double output) {

			m.set(ControlMode.PercentOutput, mult * output);
	}

}
