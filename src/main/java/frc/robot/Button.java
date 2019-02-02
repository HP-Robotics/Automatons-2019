package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Button  {
	private boolean held = false;	//stands for 'held', true if the Button is being actively held down
	private boolean state = false;	//stands for 'state', true if the Button is pressed
	private boolean lastState = false;	//stands for 'last state', stores the previous state of the Button
	private boolean changed = false;	//stands for 'changed', true if the Button's previous state does not match its current state
	private boolean cbutton;
	private String name;
	private Joystick stick;
	private int numb;

    public Button(Joystick j, int num, String n){
		numb = num;
		stick = j;
		name = n;
	}
	
	public String getName(){
		return name;
	}

	//check if the Button is pressed
	public boolean on() {
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
		cbutton = stick.getRawButton(numb);
		if(cbutton && (cbutton != lastState)) {
			state = !state;
			changed = true;
			
		} else {
			changed = false;
		}
		
		held = cbutton;
		lastState = cbutton;
	}
	
	//reset all values
	public void reset() {
		state = false;
		lastState = false;
		changed = false;
	}

}
