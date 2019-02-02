package frc.robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiteButton {

    public void light(Button b){
        SmartDashboard.putBoolean(b.getName(), true);
    }

    public void unlight(Button b){
        SmartDashboard.putBoolean(b.getName(), false);

    }
    
}