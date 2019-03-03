package frc.robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class TurnPIDOutput implements PIDOutput {

    public double m_value = 0.0;

	@Override
	public void pidWrite(double output) {
		m_value = output;
	}

}
