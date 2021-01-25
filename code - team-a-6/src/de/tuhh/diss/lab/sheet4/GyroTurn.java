package de.tuhh.diss.lab.sheet4;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class GyroTurn implements Turner {
	
	// Declaring the objects to begin the motor
			EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
			EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
			
	public void setSpeed(int degreesPerSecond) {
		// setting the speed for the motor
				rightMotor.setSpeed(degreesPerSecond);
				leftMotor.setSpeed(degreesPerSecond);
	}
	public void	turn(int degrees) {
		
		final int DELAY = 1000;
		final int ZERO_REFF = 0;
		final int QUARTER_CIRCLE = 90;
		final int HALF_CIRCLE = 180;
		final int FULL_CIRCLE = 360;
		final int X = 1;									// LCD X position
		final int Y = 1;									// LCD Y position
		final int FIRST_INDEX = 0;
		final int GYRO_OFFSET = 0;
		
		int SampleSizeAngle = 0;
		float [] AngleArray;
		int absDegree = 0;
		
		absDegree = Math.abs(degrees);			//ignoring the sign and storing the value
		
	 	EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
		SampleProvider gyroAngle = gyro.getAngleMode();
		
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
	
	
	private void leftTurn(){
		rightMotor.backward();
		leftMotor.forward();
	}

	private void rightTurn(){
		rightMotor.forward();
		leftMotor.backward();
	}
}
