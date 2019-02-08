package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PIDOutput;

public class TalonPIDOutput implements PIDOutput {
	
	TalonSRX m;
	public TalonPIDOutput(TalonSRX motor)  {
		m = motor;
	}

	@Override
	public void pidWrite(double output) {
		m.set(ControlMode.PercentOutput, -output);
		
	}

}
