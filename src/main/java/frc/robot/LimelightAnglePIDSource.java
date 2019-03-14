package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LimelightAnglePIDSource implements PIDSource {
    
	
	private NetworkTableEntry txEntry = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx");
	/*private NetworkTableEntry tzEntry = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tz");*/
    private NetworkTableEntry tvEntry = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv");

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
		return txEntry.getDouble(0.0);
    }
    
    public boolean isValid() {
		/*System.out.println("tx: " + txEntry.getDouble(0.0) + " tv: " + tvEntry.getDouble(0.0) + " tz: " + tzEntry.getDouble(0.0));*/
		return tvEntry.getDouble(0.0) == 1.0; /*&& Math.abs(txEntry.getDouble(0.0))< 40.0 && tzEntry.getDouble(0.0) < 120.0;*/
    }
}