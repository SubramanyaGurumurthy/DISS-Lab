package de.tuhh.diss.lab.sheet4;

public class FeedbackTurn{
	
	// Creating the object of class TurnWithFeedback
	ImplementFeedbackTurn turn = new ImplementFeedbackTurn();
	
	/* USER INPUT SHOULD BE ENTERED BELOW*/
	int speed = 650;
	//Angles should be either 90, 180, 360, -90, -180 or -360, negative angle gives left turn or CCW rotation 
	int desiredangle = -360;		
	
	
	public void gyroFeedbackTurner(){
			
		turn.setSpeed(speed);
		turn.turnWithGyro(desiredangle);
	
	}	
		
	public void propTurner(){

		turn.proportionalTurn(desiredangle);
			
	}	
		
}