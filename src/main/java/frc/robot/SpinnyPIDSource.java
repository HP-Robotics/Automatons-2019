package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class SpinnyPIDSource implements PIDSource {

    Encoder e1;
    Encoder e2;
    
	public SpinnyPIDSource(Encoder enc1, Encoder enc2){
        e1 = enc1;
        e2 = enc2;
    }

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
        return (e1.pidGet()-e2.pidGet())*Robot.DRIVE_ENC_TO_INCH;
    }
    
  
}