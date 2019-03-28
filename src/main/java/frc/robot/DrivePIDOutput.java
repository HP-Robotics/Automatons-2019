package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.PIDOutput;

public class DrivePIDOutput implements PIDOutput {
	

	/*CALYPSO IS VICTOR ATLAS IS TALON*/
    TalonSRX m;
    TalonSRX m2;
	double mult;
	public DrivePIDOutput(TalonSRX motor, TalonSRX motor2, double multiplier)  {
        m = motor;
        m2 = motor2;
		mult = multiplier;
	}

	@Override
	public void pidWrite(double output) {
		m.set(ControlMode.PercentOutput, mult * output);
		m2.set(ControlMode.PercentOutput, mult * output);
	}

}
