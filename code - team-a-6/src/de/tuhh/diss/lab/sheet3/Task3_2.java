package de.tuhh.diss.lab.sheet3;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;



public class Task3_2 {
	
	public static void Distance_Sensing() {
		// Constants declaration
		final int SPEED = 650;
		final int DELAY = 250;
		final int DIST_CONV = 100;
		final int TEN_CM = 10;
		final int OFFSET = 0;
		final int FIRST_POS = 0;
		
		// Variable declaration
		int Dist_Size;
		float Distance;
		float[] Dist_Sample;
		
		//Motor & ultrasonic sensor class object declaration & calling necessary methods
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		
		EV3UltrasonicSensor distSens = new EV3UltrasonicSensor(SensorPort.S4);
		
		rightMotor.setSpeed(SPEED);
		leftMotor.setSpeed(SPEED);
		
		rightMotor.backward();
		leftMotor.backward();
		
		while(true) {
		LCD.clear();
		SampleProvider dist = distSens.getDistanceMode();
		
		Dist_Size = dist.sampleSize();
		Dist_Sample = new float[Dist_Size];
		dist.fetchSample(Dist_Sample, OFFSET);			//fetching the distance
		Distance = Dist_Sample[FIRST_POS];
		Distance = Distance * DIST_CONV; 			//converting to cm
		
		LCD.drawString(""+Distance+"cm", 1,1);
		Delay.msDelay(DELAY);
		
			if(Distance<TEN_CM) {
				// Stopping the motor if its less than 6cm distance
				rightMotor.stop(true);
				leftMotor.stop(true);
			
				rightMotor.close();
				leftMotor.close();
				break;
			}
			
			
		}
		
		distSens.close();

	}
}
