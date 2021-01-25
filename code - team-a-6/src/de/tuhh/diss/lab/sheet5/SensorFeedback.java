package de.tuhh.diss.lab.sheet5;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;


public class SensorFeedback {
	
	private final int DIST_CONV = 100;
	private final int OFFSET = 0;
	private final int FIRST_POS = 0;
	private final int FIRST_INDEX = 0;
	private final int GYRO_OFFSET = 0;
	
	private int SampleSizeAngle = 0;
	private float [] AngleArray;
	
	EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
	SampleProvider gyroAngle = gyro.getAngleMode();
	
	/**
	 * {@code ultrasonicFeedback} - checks the distance of the obstacle in front using 
	 * {@code EV3UltrasonicSensor} object
	 * @return - float value of the distance 
	 */
		public float ultrasonicFeedback() {
			 
			int distSize;
			float Distance;
			float[] distSample;
			
			//Motor & ultrasonic sensor class object declaration & calling necessary methods
			
			EV3UltrasonicSensor distSens = new EV3UltrasonicSensor(SensorPort.S4);

			SampleProvider dist = distSens.getDistanceMode();
			
			distSize = dist.sampleSize();
			distSample = new float[distSize];
			dist.fetchSample(distSample, OFFSET);			//fetching the distance
			Distance = distSample[FIRST_POS];
			Distance = Distance * DIST_CONV; 			//converting to cm
			
			distSens.close();
			return Distance;			
		}
		
		
		/**
		 * {@code gyroSensorFeedback} - checks the angle of the robot using 
		 * {@code EV3GyroSensor} object
		 * @return - float value of the current angle 
		 */
		public float gyroSensorFeedback() {						
						
			// Declaring necessary gyro sensor functions
			SampleSizeAngle = gyroAngle.sampleSize();
			AngleArray = new float[SampleSizeAngle];
							
			// Fetching angle from gyro and resetting the gyro value to zero before beginning to turn
			gyroAngle.fetchSample(AngleArray, GYRO_OFFSET);
			AngleArray[FIRST_INDEX] = Math.abs(AngleArray[FIRST_INDEX]);	
			gyro.close();
			return AngleArray[FIRST_INDEX];			
			 
		} 
		
		
		/**
		 * {@code gyroReset} - resets the current orientation of robot to zero using
		 * {@code EV3GyroSensor} object
		 */
		public void gyroReset() {
			SampleSizeAngle = gyroAngle.sampleSize();
			AngleArray = new float[SampleSizeAngle];
			gyroAngle.fetchSample(AngleArray, GYRO_OFFSET);
			gyro.reset();
		}
		
		
		/**
		 * {@code colorSensor} - checks the color of the wall using 
		 * {@code EV3ColorSensor} object
		 * @return - int (ID value of the detected color) 
		 */
		public int colorSensor() {
			//Local Variable declaration
			int colorSize, detectedColor;
			float [] colorSample;
			
			// Color sensor class object declaration & calling necessary methods
			EV3ColorSensor colSens = new EV3ColorSensor(SensorPort.S1);
			SensorMode color = colSens.getColorIDMode();
			colorSize = color.sampleSize();
			colorSample = new float[colorSize];
			
			color.fetchSample(colorSample, OFFSET);			//fetching the color
			detectedColor = (int)colorSample[FIRST_INDEX];		// storing the ID of the color in "detectedcolor" variable
			
			colSens.close();
			
			return detectedColor;			// Returnig the color in the form of a string
				
		}

}
