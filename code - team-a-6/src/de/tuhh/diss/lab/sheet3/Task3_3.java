package de.tuhh.diss.lab.sheet3;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import 	java.text.DecimalFormat;



public class Task3_3 {
	// Constants declaration 
	final static int FIRST_POSI = 0;
	final static int OFFSET = 0;
	final static int SPEED = 650;
	final static int DIST_CONV = 100;
	final static int DELAY = 250;
	final static int FIVE_CM = 5;
	
	public static void Approach_Wall() {
		
		//Local Variable declaration
		float Distance;
		
		// Restricting the decimal points to 2 decimals
		DecimalFormat df = new DecimalFormat("0.00");
		
		// Declaration of the motor class to initiate motor & & calling necessary methods
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);	
		
		rightMotor.setSpeed(SPEED);
		leftMotor.setSpeed(SPEED);
		
		rightMotor.backward();
		leftMotor.backward();
				
		while(true) {
			
		Distance = Ultrasonic_Sens(); 		//fetching distance
		Distance = Distance * DIST_CONV; 	//converting it to cm

		LCD.drawString(""+df.format(Distance)+"cm", 1,1);
		Delay.msDelay(DELAY);
		LCD.clear();
		String Color ="";
		
		// if distance is less than 5cm, stopping the motor 
		if(Distance<FIVE_CM) {
			leftMotor.stop(true);
			rightMotor.stop(true);
			
			Color = Colorsens();
			LCD.drawString("Color = ", 1, 1);
			LCD.drawString("" +Color, 2, 2);
			LCD.drawString("Dist= "+df.format(Distance)+"cm", 1,4);
			
			break;
			}						
		}
		rightMotor.close();
		leftMotor.close();
		
	}
	
	
	public static float Ultrasonic_Sens() {
	
		//Local Variable declaration
		int Dist_Size;
		float[] Dist_Sample;
		
		// ultrasonic sensor class object declaration & calling necessary methods		
		EV3UltrasonicSensor distSens = new EV3UltrasonicSensor(SensorPort.S4);
		SampleProvider dist = distSens.getDistanceMode();
		Dist_Size = dist.sampleSize();
		Dist_Sample = new float[Dist_Size];
		
		dist.fetchSample(Dist_Sample, OFFSET); 	//fetching the distance
		distSens.close();
		return Dist_Sample[FIRST_POSI];			// returning the distance readings
		
	}

	public static String Colorsens() {
		//Local Variable declaration
		int Color_Size, Detected_Color;
		float [] Color_Sample;
		String Color_Detected = "";
		
		int[] ColorID= {-1,0,1,2,3,4,5,6,7};
		String[] Colors = {"None","Red", "Green", "Blue","Yellow","Magenta","Orange","White","Black"};
		
		// Color sensor class object declaration & calling necessary methods
		EV3ColorSensor colSens = new EV3ColorSensor(SensorPort.S1);
		SensorMode color = colSens.getColorIDMode();
		Color_Size = color.sampleSize();
		Color_Sample = new float[Color_Size];
		
		color.fetchSample(Color_Sample, OFFSET);			//fetching the color
		Detected_Color = (int)Color_Sample[FIRST_POSI];		// storing the ID of the color in "detectedcolor" variable
		
		for(int i = 0 ; i<=8; i++){ 
			if(Detected_Color == ColorID[i]){
				Color_Detected = Colors[i];
				break;
			}			
		}
		
		colSens.close();
		return Color_Detected;			// Returnig the color in the form of a string
		
	}
	
	
}
