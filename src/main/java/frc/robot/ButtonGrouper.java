package frc.robot;

public class ButtonGrouper {
    Button[] group;
    LiteButton lights;
    Button lastButton;

    public ButtonGrouper(Button[] g, LiteButton lb) {
        group = g;
        lights = lb;
    }

    public void update() {
        for(Button b : group) {
            b.update();
            if (b.changed()) {
                lastButton = b;
            }
        }

        for(Button b : group) {
            if(b == lastButton){
                lights.light(b);
            }else{
                b.toggleOff();
                lights.unlight(b);
                //System.out.println(b + " unlight");
            }
        }

    }
}