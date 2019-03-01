package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class AxisButton  {
	private boolean held = false;	//stands for 'held', true if the Button is being actively held down
	private double state = 0.0;	//stands for 'state', true if the Button is pressed
	private double lastState = 0.0;	//stands for 'last state', stores the previous state of the Button
	private boolean changed = false;	//stands for 'changed', true if the Button's previous state does not match its current state
	private double lastAxisTriggered = 0.0;
	private double fudgeAxis = 0.0;
	private double abutton;
	private String name;
	private Joystick stick;
	private int numb;

    public AxisButton(Joystick j, int num, String n){
		numb = num;
		stick = j;
		name = n;
	}
	
	public String getName(){
		return name;
	}

	//check if the Button is pressed
	public double getState() {
		return state;
	}
	
	//check if the Button is held down
	public boolean held() {
		return held;
	}
	
	//check if the Button has changed
	public boolean changed() {
		return changed;
	}
	
	//update the Button, should be called periodically
	public void update() {
		abutton = stick.getRawAxis(numb);
		//Make abutton less bad
		if(abutton >= -0.09 || abutton <= 0.09){
			fudgeAxis = 0.0;
		}
		if(abutton > 0.5){
			fudgeAxis = 1.0;
		}
		if(abutton < -0.5){
			fudgeAxis = -1.0;
		}

		//System.out.println("RA: " + abutton + " FA: " + fudgeAxis);

		if(fudgeAxis != 0 && (fudgeAxis != lastState)  && fudgeAxis != lastAxisTriggered ) {
			state = fudgeAxis;
			changed = true;
			lastAxisTriggered = fudgeAxis;
			//System.out.println("State switched to " + state);
			
		} else if(fudgeAxis != 0 && (fudgeAxis == lastState && fudgeAxis != lastAxisTriggered)) {
			state = 0.0;
			changed = true;
			lastAxisTriggered = fudgeAxis;
			//System.out.println("State off: " + state);
		}
		else if(fudgeAxis == 0){
			lastAxisTriggered = 0;
		}
		else
		{
			state = lastState;
			changed = false;
			//System.out.println("State not switched." + lastState + "    " + lastAxisState);
		}
		
		held = fudgeAxis != 0.0;
		lastState = state;
		
	}
	
	//reset all values
	public void reset() {
		state = 0.0;
		lastState = 0.0;
		changed = false;
	}

	public void toggleOff(){
		reset();
		update();
	}

}
