package de.tuhh.diss.lab.sheet3;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;


public class Task3_1 {

	public static void Color_Detection() 
	{
		//Constants declaration
		final int SPEED = 650;
		final int ANGLE = - 2150;
		final int DELAY = 250;
		final int OFFSET = 0;
		final int FIRST_POS = 0;
		
		int colorsize, detectedcolor;
		float [] colorsample;
		
		//ColorID and colors are corresponding to each other
		int[] ColorID= {-1,0,1,2,3,4,5,6,7};
		String[] Colors = {"None","Red", "Green", "Blue", "Yellow", "Magenta","Orange","White","Black"};
		
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		
		rightMotor.setSpeed(SPEED);
		leftMotor.setSpeed(SPEED);
		
		rightMotor.backward();
		leftMotor.backward();
		
		rightMotor.rotate(ANGLE, true);
		leftMotor.rotate(ANGLE, true);
		
		//fetching the color detected from the sensor
		EV3ColorSensor colSens = new EV3ColorSensor(SensorPort.S1);
		SensorMode color = colSens.getColorIDMode();
		
		while(true) {
		colorsize = color.sampleSize();
		colorsample = new float[colorsize];
		color.fetchSample(colorsample, OFFSET);
		detectedcolor = (int)colorsample[FIRST_POS];
		
		//Condition to check the detected color continuously
		
		for(int i = 0; i<9;i++){ 
			if(detectedcolor == ColorID[i]){
				LCD.drawString(""+Colors[i], 1, 2);
				rightMotor.waitComplete();
				leftMotor.waitComplete();
				rightMotor.stop();
				leftMotor.stop();
				
				rightMotor.close();
				leftMotor.close();
				
				Delay.msDelay(DELAY);
				LCD.clear();
				break;
				}
		}

	colSens.close();
		
	}}

}