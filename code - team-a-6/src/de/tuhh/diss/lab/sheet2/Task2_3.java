package de.tuhh.diss.lab.sheet2;

import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.Button;


public class Task2_3 {

	public static void straightdriving() {
		float anglefactor = 0.4711f;
		float angle=0, dr = 500; //entering the required distance to be covered in 'mm'
		int intAngle =0;		
		
		
		/*The ratio between large to small gear is 1:3
		  And the circumference of the wheel from given dia is 169.6
		  Hence the angle required to be rotated to cover the given distance is [distance*(169.6/360)] = 3 * [distance/ 0.4711]
		  anglefactor = 0.4711, distance = dr
		  */
		
		
		/*finding out the angle, that is required by wheel to be moved based on distance input*/
		angle = dr / anglefactor; 
		intAngle = (int) (angle);
		intAngle = 3 * intAngle;
		
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);

		
		LCD.drawString("Hello", 4, 4);
		LCD.drawString("Press buttonto move", 5, 5);
		
		
		LCD.drawInt(intAngle, 2, 2);
		
		Button.waitForAnyPress();
		LCD.clear();
		
		rightMotor.setSpeed(250);
		leftMotor.setSpeed(250);
		
		rightMotor.forward();
		leftMotor.forward();
		

		rightMotor.rotate(intAngle, true);
		leftMotor.rotate(intAngle, true);
		
		rightMotor.waitComplete();
		leftMotor.waitComplete();
		
		if(rightMotor.isMoving())
		{
			LCD.drawString(" Right Yes", 4, 4);
			Delay.msDelay(2000);
		}
		 if(leftMotor.isMoving()) {
			LCD.drawString("Left yes", 5, 5);
			Delay.msDelay(2000);
		}
		else {
			LCD.drawString("None", 5, 5);
		}
 
		rightMotor.stop();
		leftMotor.stop();
		
		rightMotor.close();
		leftMotor.close();
	}

	

}
