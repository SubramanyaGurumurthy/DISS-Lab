package de.tuhh.diss.lab.sheet4;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class SimTurn implements Turner{

	final static int DELAY = 1000;
	final static int ZERO_REFF = 0;
	final static int QUARTER_CIRCLE = 90;
	final static int HALF_CIRCLE = 180;
	final static int FULL_CIRCLE = 360;
	final static int X = 1;									// LCD X position
	final static int Y = 1;									// LCD Y position
	
	// Declaring the objects to begin the motor
	EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	
	public void setSpeed(int degreesPerSecond) {		
		
		// setting the speed for the motor
		rightMotor.setSpeed(degreesPerSecond);
		leftMotor.setSpeed(degreesPerSecond);
	}
	
	public void turn(int degrees) {	
		
		//Left turn
		if (degrees<ZERO_REFF) {
			rightMotor.backward();
			leftMotor.forward();
			rightMotor.rotate(degrees, true);
			leftMotor.rotate(-degrees, true);
			rightMotor.waitComplete();
			leftMotor.waitComplete();
		}
		
		// Right turn
		else {
			rightMotor.forward();
			leftMotor.backward();
			rightMotor.rotate(degrees, true);
			leftMotor.rotate(-degrees, true);	
			rightMotor.waitComplete();
			leftMotor.waitComplete();
		}
		
		// Stopping and closing the rightMotor, leftMotor objects
		rightMotor.stop();
		leftMotor.stop();
		rightMotor.close();
		leftMotor.close();
	}
		
	public static void simpleTurn() {
		
		//Constants declaration
		
		final float ROTATION_CONST = 2184.636f;
		final int[] ANGLE_DIV_ARRAY = {90, 180, 360, -90, -180, -360};
		
		/* Angle factor is 0.47, which is found after dividing the circumference of wheel (169.6) by 360 (degrees)
		 circumference of wheel base = 728.212
		 Rotation constant = 3 * (circumference / AngleFactor)
		 Hence rotation constant = 2184.636
		  */
		
		/* USER INPUT SHOULD BE ENTERED BELOW*/
		int speed = 500;
		//Angles should be either 90, 180, 360, -90, -180 or -360, negative angle gives left turn or CCW rotation 
		int inputAngle = -90;  		
		
		// Variables declaration
		int  angleDiv, degrees = 0;
		
		//comparing the input angles to 90deg, 180deg, 360 degree
		if (Math.abs(inputAngle) == QUARTER_CIRCLE  || Math.abs(inputAngle) == HALF_CIRCLE || Math.abs(inputAngle) == FULL_CIRCLE){

			for(int i =0; i<ANGLE_DIV_ARRAY.length;i++){
				if(inputAngle == ANGLE_DIV_ARRAY[i]){ 
					// Finding the division factor for the input
					angleDiv = (FULL_CIRCLE/inputAngle);
					// Calculating degrees based on user input
					degrees = (int)(ROTATION_CONST/angleDiv);
				} 
			}	
			
			// Calling the methods to initiate turning 
			SimTurn turn = new SimTurn();
			LCD.drawString("Turning "+inputAngle, X, Y);
			Delay.msDelay(DELAY);
			turn.setSpeed(speed);	
			turn.turn(degrees);
			LCD.clear();
		}
		
		else {
			LCD.drawString("Input angle error", X, Y);
			Delay.msDelay(DELAY);
		}
		
	}
}