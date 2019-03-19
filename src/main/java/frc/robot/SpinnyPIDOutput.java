package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.PIDOutput;

public class SpinnyPIDOutput implements PIDOutput {
	

	/*CALYPSO IS VICTOR ATLAS IS TALON*/
    VictorSPX m;
    VictorSPX m2;
    VictorSPX m3;
    VictorSPX m4;
	double mult;
	public SpinnyPIDOutput(VictorSPX motor, VictorSPX motor2, VictorSPX motor3, VictorSPX motor4, double multiplier)  {
        m = motor;
        m2 = motor2;
        m3 = motor3;
        m4 = motor4;
		mult = multiplier;
	}

	@Override
	public void pidWrite(double output) {
		m.set(ControlMode.PercentOutput, mult * output);
        m2.set(ControlMode.PercentOutput, mult * output);
        m3.set(ControlMode.PercentOutput, mult * output);
        m4.set(ControlMode.PercentOutput, mult * output);
	}

}