package de.tuhh.diss.lab.sheet5;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;



public class MotorBasics {
	
	private final int OPTI_SPEED = 200;
	
	EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	
	
	/**
	 * {@code leftTurn} - turns robot in anti-clockwise direction
	 */
	public void leftTurn(){
		rightMotor.backward();
		leftMotor.forward();
	}
	
	/**
	 * {@code rightTurn} - turns robot in clockwise direction
	 */
	public void rightTurn(){
		rightMotor.forward();
		leftMotor.backward();
	}
	
	/**
	 * {@code forwardMove} - moves robot forward
	 */
	public void forwardMove() {		
		leftMotor.backward();
		rightMotor.backward();				
	}
	
	/**
	 * {@code backwardMove} - moves robot backward
	 */
	public void backwardMove() {
		setSpeed(OPTI_SPEED);	
		
		leftMotor.forward();
		rightMotor.forward();	
		
	} 
	
	/**
	 * {@code stopMotors} - stops robot movement
	 */
	public void stopMotors() {		
		leftMotor.stop(true);
		rightMotor.stop(true);				
	}
	
	/**
	 * {@code setSpeed} - sets speed based on the input
	 * @param - int degreesPerSecond
	 */
	public void setSpeed(int degreesPerSecond) {		
		// setting the speed for the motor
		rightMotor.setSpeed(degreesPerSecond);
		leftMotor.setSpeed(degreesPerSecond);
	}
	
	/**
	 * {@code closeMotors} - closes {@code rightMotor} and {@code leftMotor} object
	 */
	public void closeMotors() {
		leftMotor.close();
		rightMotor.close();		
		
	}
}
