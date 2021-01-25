package de.tuhh.diss.lab.sheet3;


import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Task3_4 {
	
public static void GyroSensor() {
		
		//Constant Declaration		
		final int SPEED = 650;
		final int THREE_SEC = 3000;
		final int DELAY_200MS = 200;
		final int DELAY_100MS = 100;
		final int OFFSET = 0;
		final int FIRST_POS = 0;
		
		// Variables declaration
		int sampleSizeAngle, angle_To_Disp, Turn= 0;
		float [] AngleArray;
		
		//Initializing the motors
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		
		//Initializing the gyro sensor
		EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
		
		rightMotor.setSpeed(SPEED);
		leftMotor.setSpeed(SPEED);
		
		// calling necessary methods & fetching the angle from gyro sensor
		SampleProvider gyroAngle = gyro.getAngleMode();
		sampleSizeAngle = gyroAngle.sampleSize();
		AngleArray = new float[sampleSizeAngle];
		gyroAngle.fetchSample(AngleArray, OFFSET);
		
		// Printing the initial angle before beginning the rotation
		LCD.drawString("before: "+AngleArray[FIRST_POS] , 1, 1);
		Delay.msDelay(THREE_SEC);
		LCD.clear();
		
		// resetting the angle to zero before beginning the rotation, to remove the accumulated angle
		gyro.reset();											
		
		while(true) {
			//To rotate the robot in anti-clockwise
			rightMotor.backward();
			leftMotor.forward();
			
			//Fetching angle from the sensor using the "Fetchsample" again while motors are moving
			AngleArray = new float[sampleSizeAngle];
			gyroAngle.fetchSample(AngleArray, OFFSET);
			angle_To_Disp = (int)AngleArray[FIRST_POS];
			
			if(angle_To_Disp >= 360) {

				Delay.msDelay(DELAY_200MS);
				gyro.reset();
				Turn++;
				LCD.drawString("after reset" +angle_To_Disp, 1, 1);
				Delay.msDelay(DELAY_100MS);
			}		
			
			LCD.drawString("after reset" +angle_To_Disp, 1, 1);
			Delay.msDelay(DELAY_200MS);
			LCD.clear();
			
			/*The robot will keep rotating until "ENTER KEY" is pressed and 
			once it is pressed it stops and prints the number of full turns.*/
			if(Button.ENTER.isDown()){                                     
				rightMotor.stop(true);                                 //to stop both the motors at the same time.
				leftMotor.stop(true);
				rightMotor.close();
				leftMotor.close();
				LCD.drawString("Num of turns:"+Turn, 1, 2);
				Delay.msDelay(THREE_SEC);
				break;
			}
		}
		
	rightMotor.close();
	leftMotor.close();
	gyro.close();
		
	}
	
}
	
