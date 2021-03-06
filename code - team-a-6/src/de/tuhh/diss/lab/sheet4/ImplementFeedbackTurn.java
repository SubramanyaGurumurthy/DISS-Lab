package de.tuhh.diss.lab.sheet4;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ImplementFeedbackTurn extends SimTurn {
	
	// declaring the objects necessary for gyro sensor
 	EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
	SampleProvider gyroAngle = gyro.getAngleMode();
	
	
	// Declaring constants globally
	final int FIRST_INDEX = 0;
	final int GYRO_OFFSET = 0;	
	
	//Global Variable declaration
	int SampleSizeAngle = 0;
	float [] AngleArray;
	int absDegree = 0;
	
	public void turnWithGyro(int degrees) {
		
		absDegree = Math.abs(degrees);						//ignoring the sign and storing the value
		
		// Declaring necessary gyro sensor functions
		SampleSizeAngle = gyroAngle.sampleSize();
		AngleArray = new float[SampleSizeAngle];
		
		// Checking whether the input angle entered is either 90, 180 or 360 degrees
		if (absDegree == QUARTER_CIRCLE  || absDegree == HALF_CIRCLE || absDegree == FULL_CIRCLE) {
			
			// Fetching angle from gyro and resetting the gyro value to zero before beginning to turn		
			gyroAngle.fetchSample(AngleArray, GYRO_OFFSET);
			gyro.reset();
			AngleArray[FIRST_INDEX] = Math.abs(AngleArray[FIRST_INDEX]);
			
			LCD.drawString("Turning to "+degrees, X, Y);
			Delay.msDelay(DELAY);
			LCD.clear();
			
			// here delta is absDegree, Epsilon is AngleArray[0] according to the lab sheet
			while (absDegree > AngleArray[FIRST_INDEX]) {
				gyroAngle.fetchSample(AngleArray, GYRO_OFFSET);
				AngleArray[FIRST_INDEX] = Math.abs(AngleArray[FIRST_INDEX]);
				if(degrees > ZERO_REFF) {
					rightTurn();
				}
				else {
					leftTurn();					
				}
	
			}
			
			// stopping the motors
			rightMotor.stop(true);
			leftMotor.stop(true);		
			rightMotor.close();
			leftMotor.close();
		}
		
		else {
			LCD.drawString("Input angle error", X, Y);
			Delay.msDelay(DELAY);
		}
	}
	
	
	
	public void proportionalTurn(int degrees) {
		//Local constants declaration
		final int GAIN = 6;
		final int MIN_SPEED = 100; 
		
		//Local Variable declaration
		float speed, difference;
		
		absDegree = Math.abs(degrees);							//ignoring the sign and storing the value
		
		// Declaring necessary gyro sensor functions
		SampleSizeAngle = gyroAngle.sampleSize();
		AngleArray = new float[SampleSizeAngle];
		
		// Checking whether the input angle entered is either 90, 180 or 360 degrees
		if (absDegree == QUARTER_CIRCLE  || absDegree == HALF_CIRCLE || absDegree == FULL_CIRCLE) {
			
			// Fetching angle from gyro and resetting the gyro value to zero before beginning to turn
			gyroAngle.fetchSample(AngleArray, GYRO_OFFSET);
			gyro.reset();
			AngleArray[FIRST_INDEX] = Math.abs(AngleArray[FIRST_INDEX]);
			LCD.drawString("Turning to "+degrees, X, Y);
			Delay.msDelay(DELAY);
			LCD.clear();
			
			// rotation begins here with proportional control feedback
			while (absDegree > AngleArray[FIRST_INDEX]) {
				gyroAngle.fetchSample(AngleArray, GYRO_OFFSET);
				AngleArray[FIRST_INDEX] = Math.abs(AngleArray[FIRST_INDEX]);
				difference = absDegree - AngleArray[FIRST_INDEX]; 
				
				//calculating speed based on gyro angle feedback
				speed = difference * GAIN;
				
				if(speed<MIN_SPEED) {
					speed = MIN_SPEED;
				}
				
				setSpeed((int)speed);
				
				// deciding the turning method, i.e., CW or CCW
				if(degrees > ZERO_REFF) {
					rightTurn();
				}
				else {
					leftTurn();
				}
	
			}
			rightMotor.stop(true);
			leftMotor.stop(true);
		}
		
		else {
			LCD.drawString("Input angle error", X, Y);
			Delay.msDelay(DELAY);
		}
		
	}
	
	private void leftTurn(){
		rightMotor.backward();
		leftMotor.forward();
	}

	private void rightTurn(){
		rightMotor.forward();
		leftMotor.backward();
	}

}