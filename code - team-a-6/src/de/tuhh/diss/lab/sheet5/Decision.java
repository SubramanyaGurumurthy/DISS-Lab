package de.tuhh.diss.lab.sheet5;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

/**
 * {@code Decision} class is the initial point of maze searching 
 *
 */
public class Decision {
	
	private final int RIGHT = 3;
	private final int LEFT = 4;
	private final int STRAIGHT = 1;
	private final int REVERSE = -1;
	private final int INI_I_J = 3;
	private final int EXT_LOW = 0;
	private final int EXT_HIGH = 6;
	private final int BLOCK_SIZE = 35;
	private final int SHORT_MIN_DISTANCE = 5;
	private final int BACK_DISTANCE = 12;
	private final int SAFE_DISTANCE = 16;
	private final int GAIN = 25;
	private final int GAIN_TURN = 6;
	private final int MIN_SPEED = 120;
	private final int MAX_SPEED = 760; 
	private final int OPTI_SPEED = 200;
	private final int MIN_TURN_SPEED = 80;
	private final int FRQ = 400;		
	private final int VOL = 50;
	private final int X = 1;
	private final int Y = 1;
	private final int DELAY = 500;
	private final int[] COLOR_ID= {-1,0,1,2,3,4,5,6,7};
	
	/**
	 * Global variable of class Decision
	 */
	private int colorToBeDetected, colorFeedback, nextAngle, i, j, distanceReff;
	
	/**
	 * Global variable of class Decision
	 */
	private float distance, gyroFeedback = 0;
	
	
	/**
	 * {@code firstTurn} - stores which turn is implemented first
	 * {@code firstTurn = 3} if right turn
	 * {@code firstTurn = 4} if left turn
	 */
	private int firstTurn = 0;
	
	/**
	 * {@code tracker} - keeps track of the path that the robot has traveled
	 */
	private float [][] tracker = new float[7][7];
	 
	/**
	 * {@code vertical} - used to reference the robot orientation to decide if it is
	 					in horizontal or vertical position.
	 */
	private boolean verticle = true;
	
	/**
	 * {@code flagVer} - used to reference the robot orientation to decide if it is
	 					moving upwards or downwards direction.
	 */
	private boolean flagVer = true; 
	
	/**
	 * {@code flagHor} - used to reference the robot orientation to decide if it is
	 					moving towards left or right direction.
	 */
	private boolean flagHor = false;
	
	MotorBasics drive = new MotorBasics();
	SensorFeedback sensor = new SensorFeedback();
	ColorMenu colorInput = new ColorMenu();
	
	/**
	 * {@code mazeSolver} - It is the initial point of the program which is called 
	 * by mainSimulated class. It takes input from colorSensor input from user and 
	 * initiates the color searching algorithm
	 *  
	 */
	public void mazeSolver() {
		float newDist;
		int  turnCount = 0;
		int [] angle = {90, 270, -90}; 
		 
		trackerInit();
		i = INI_I_J;
		j = INI_I_J;
		colorToBeDetected = colorInput.menu();
		colorFeedback = sensor.colorSensor();
		
		LCD.clear();
		Delay.msDelay(DELAY);
		Sound.setVolume(VOL);
		Sound.playTone(FRQ, DELAY, VOL); 
		
		initialTurnCheck();
		distance = sensor.ultrasonicFeedback();
		
		while(true) {
			distance = sensor.ultrasonicFeedback();
			
			if(distance >= SHORT_MIN_DISTANCE) {
				distanceReff = (int)distance;
				tracker(STRAIGHT);
				straightDrive(SHORT_MIN_DISTANCE);
			}

			else {
					colorFeedback = sensor.colorSensor();
					if (colorFeedback == colorToBeDetected) {
						solvedMaze(colorFeedback);
						break;
					}
					backwardMove();
					nextAngle = angle[turnCount];
					turnToAngle(nextAngle);
					
					turnCount++;	
					if (turnCount >1) {
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
					}
					if (turnCount >2) {
						turnCount = 0;
					}
					
					newDist = sensor.ultrasonicFeedback();
				
					if(newDist > SAFE_DISTANCE ) { 
						  if(nextAngle == angle[0]) {
							tracker(RIGHT);
							firstTurn = RIGHT;
							turnAlgo(firstTurn);
							break;
						  }
						  else if( nextAngle == angle[1]) {
							tracker(LEFT);
							firstTurn = LEFT;
							turnAlgo(firstTurn); 
							break;
						  }
						  else if( nextAngle == angle[2]) {
							  tracker(REVERSE);
							  traceBack();

						  }
						  
						  
					}	 
						
					
				}
				
			}

	}
	
	
	/**
	 * {@code initialTurnCheck} - Rotates robot and Checks distances from all 
	 * the directions and sets robot to 180degree orientation if it is 
	 * surrounded by 3 walls, else sets to initial orientation itself
	 */
	private void initialTurnCheck() {
		final int HALF_CIRCLE = 180;
		final int QUARTER_ANGLE = 90;
		
		int count = 0, arrayPosi = 0;
		float [] angleArray = {0,0,0,0};
		
		while(count < 4) {
			angleArray[arrayPosi] = sensor.ultrasonicFeedback();
			turnToAngle(QUARTER_ANGLE);
			sensor.gyroReset();
			gyroFeedback = sensor.gyroSensorFeedback();
			arrayPosi++;
			if(arrayPosi > 3) {
				arrayPosi = 0;
			}
			count++;	
		}
		
		count = 0;
		for(int i = 0; i<4; i++) {
			if(angleArray[i] < 20) {
				count++;
			}
		}
		
		if(count>2) {
			flagVer = false;
			turnToAngle(HALF_CIRCLE);
		}
		
		sensor.gyroReset();
	}
	
	/**
	 * {@code turnAlgo} - explores the new areas and updates tracker. Searches for given colored
	 * wall.  
	 * @param firstTurn - integer which is passed while calling this method, sets which turn to take 
	 * when obstacle is detected.
	 */
	
	private void turnAlgo(int firstTurn) {
		int rightAngle[] = {90, 270, -90};
		int leftAngle[] = {-90, -270, 90};
		int currentAngle = 0;
		float newDist; 
		sensor.gyroReset();
		gyroFeedback = sensor.gyroSensorFeedback();
		LCD.clear();
		distance = sensor.ultrasonicFeedback();
		
		while(firstTurn == RIGHT) {	
			distance = sensor.ultrasonicFeedback();
			
			if( distance >= SHORT_MIN_DISTANCE) {
				distanceReff = (int)distance;
				tracker(STRAIGHT);
				straightDrive(SHORT_MIN_DISTANCE);
			}
			
			else {
				colorFeedback = sensor.colorSensor();
				
				if (colorFeedback == colorToBeDetected) {
					solvedMaze(colorFeedback);
					break;
				}
				
				backwardMove();
				nextAngle = rightAngle[currentAngle];
				turnToAngle(nextAngle);
				currentAngle++;
				if(currentAngle > 2) {
					currentAngle = 0;
				}
				if(currentAngle > 1) {
					gyroFeedback = sensor.gyroSensorFeedback();
					sensor.gyroReset();
				}
				
				newDist = sensor.ultrasonicFeedback();
				
				if(newDist > SAFE_DISTANCE) {
					if(nextAngle == rightAngle[0]) {
						tracker(RIGHT);
					}
					else if(nextAngle == rightAngle[1]) {
						tracker(LEFT);
					}
					else if(nextAngle == rightAngle[2]) {
						tracker(REVERSE);
						traceBack();
					}
					currentAngle= 0;
					gyroFeedback = sensor.gyroSensorFeedback();
					sensor.gyroReset();
				}	
							
			}	
		}
		
		while(firstTurn == LEFT) {	
			distance = sensor.ultrasonicFeedback();
			
			if(distance >= SHORT_MIN_DISTANCE) {
				distanceReff = (int)distance;
				tracker(STRAIGHT);
				straightDrive(SHORT_MIN_DISTANCE);
			}
			
			else{					
				colorFeedback = sensor.colorSensor();
				
				if (colorFeedback == colorToBeDetected) {
					solvedMaze(colorFeedback);
					break;
				}
				
				backwardMove();
				nextAngle = leftAngle[currentAngle];
				turnToAngle(nextAngle);
				currentAngle++;
				if(currentAngle > 2) {
					currentAngle = 0;
				}
				if(currentAngle > 1) {
					gyroFeedback = sensor.gyroSensorFeedback();
					sensor.gyroReset();
				}	
				
				newDist = sensor.ultrasonicFeedback();
				if(newDist > SAFE_DISTANCE) {
					if(nextAngle == leftAngle[0]) {
						tracker(LEFT);
					}
					else if(nextAngle == leftAngle[1]) {
						tracker(RIGHT);
					}
					else if(nextAngle == leftAngle[2]) {
						tracker(REVERSE);
						traceBack();
					}
					
					currentAngle= 0;
					gyroFeedback = sensor.gyroSensorFeedback();
					sensor.gyroReset();					
				}
							
			}	
		}
	}
	
	/**
	 * {@code solvedMaze} - confirms and displays the color found based on input 
	 * @param colorFeedback - int which passed as the detected color
	 */
	private void solvedMaze(int colorFeedback) {
		
		final String[] COLORS = {"None","Red", "Green", "Blue","Yellow","Magenta","Orange","White","Black"};
		String colorFound = "";
		
		drive.stopMotors();
		Sound.setVolume(VOL);
		Sound.playTone(FRQ, DELAY, VOL);
		
		for(int i = 0 ; i<=8; i++){ 
			if(colorFeedback == COLOR_ID[i]){
				colorFound = COLORS[i];
				break;
			}			
		}
		LCD.clear();
		LCD.drawString("Color Found: ", X, Y);
		LCD.drawString(" "+colorFound, (X), (Y+Y));
		Delay.msDelay(DELAY);
		drive.stopMotors();
		drive.closeMotors();
	}
	
	
	private void straightDrive( int dist) {
		distance = sensor.ultrasonicFeedback();
		
		while(distance >= dist) {
			distance = sensor.ultrasonicFeedback();
			drive.setSpeed(speed(distance, 0));
			drive.forwardMove();				
		}
		
	}
	
	private void turnToAngle (int angle) {
		float  difference;
		int  turnSpeed;
		gyroFeedback = sensor.gyroSensorFeedback();
		
		while(Math.abs(angle)> gyroFeedback) {	
			gyroFeedback = sensor.gyroSensorFeedback();
			difference = Math.abs(angle) - gyroFeedback;
			turnSpeed = speed(difference,1); 
			drive.setSpeed(turnSpeed);
			if(angle>0) {
				drive.rightTurn();
			}
			else {
				drive.leftTurn();
			} 
			
		}		
	}
	
	
	private int speed(float param, int index) {
		int speed;
		
		if (index == 1) {
			speed = (int) (param * GAIN_TURN);
			if(speed < MIN_TURN_SPEED) {
				speed = MIN_TURN_SPEED;
			}
			else if(speed > MAX_SPEED) {
				speed = MAX_SPEED;
			}
			return speed;
		}
		
		else {
			speed = (int) (param * GAIN);
			if(speed < MIN_SPEED) {
				speed = MIN_SPEED;
			}
			else if(speed > MAX_SPEED) {
				speed = MAX_SPEED;
			}
			return speed;
			
		}
		
	}
	
	private void backwardMove() {
		drive.stopMotors();

		drive.setSpeed(OPTI_SPEED);	
		distance = sensor.ultrasonicFeedback();
		while(distance < BACK_DISTANCE ){
			distance = sensor.ultrasonicFeedback();
			drive.backwardMove();
		}	
		drive.closeMotors();
	}
	
	private void moveBlock() {
		int blockReff = 0;
		
		distance =sensor.ultrasonicFeedback();
		blockReff = (int)distance - BLOCK_SIZE;
		
		while(distance >= blockReff) {
			distance =sensor.ultrasonicFeedback();
			drive.setSpeed(speed(distance, 0));
			drive.forwardMove();
		}	
		
	}
	
	/**
	{@code trackerInit} - initializes all the elements of tracker(2d array) to zeros.   
  */
	private void trackerInit() {
		
		for(i = 0; i<7; i++) {
			for(j = 0; j< 7; j++) {
				tracker[i][j] = 0;
			}
		}
		
	}
	
	/**
	This method sets individual elements of tracker array to values based on inputs
	 and pre-existing variable which are
	 {@code verticle}, {@code flagHor}, {@code flagVer}, {@code turningCount}.
	@param trackerInput - int [RIGHT, LEFT, STRAIGHT, REVERSE]   
  */
	private void tracker(int trackerInput) {
		int inputDeterminer;

		if(trackerInput == STRAIGHT && verticle) {
		
			inputDeterminer = distanceReff / BLOCK_SIZE;

			while(inputDeterminer> 0) {
				if(tracker[i][j]== 0 ) {
					tracker[i][j] = STRAIGHT;
				}
				if(inputDeterminer >= 1) {
					if(flagVer) {
						i--;
					}
					
					else if(!flagVer) {
						i++;
					}
					 
				}
				inputDeterminer --;	
			}	
					
			
		}
				
		
		else if(trackerInput == STRAIGHT && !verticle) {
			
			inputDeterminer = distanceReff / BLOCK_SIZE;
			
			while(inputDeterminer> 0) {
				if(tracker[i][j] == 0 ) {
					tracker[i][j] = STRAIGHT;
				}
				
				if(inputDeterminer >= 1) {
					if(flagHor) {
						j++;
					}
					
					else if(!flagHor) {
						j--;
					}
					
				}	
				
				inputDeterminer --;
				
			}
		
		}
		
		else if(trackerInput == LEFT ) {
			
			tracker[i][j] = LEFT;
			if(verticle) {
				if(flagVer) {
					verticle = false;
					flagHor = false;
				}
				
				else if(!flagVer) {
					verticle = false;
					flagHor = true;
				}
			}
			
			else if(!verticle) {				
				if(flagHor) {
					verticle = true; 
					flagVer = true;
				}
				
				else if(!flagHor) {
					verticle = true;
					flagVer = false;
				}
				
			}
			
		}
		
		else if(trackerInput == RIGHT ) {
	
			tracker[i][j] = RIGHT;
			
			if(verticle) {
				if(flagVer) {
					verticle = false;
					flagHor = true;
				}
				
				else if(!flagVer) {
					verticle = false;
					flagHor = false;
				}
			}
			
			else if(!verticle) {				
				if(flagHor) {
					verticle = true; 
					flagVer = false;
				}
				
				else if(!flagHor) {
					verticle = true;
					flagVer = true;
				}
				
			}
			
		}
		
		else if(trackerInput == REVERSE) {
			tracker[i][j] = REVERSE;
			
			if(verticle && flagVer) {
				flagVer = false;
			}
			
			else if(verticle && !flagVer) {
				flagVer = true;
			}
			
			else if(!verticle && flagHor) {
				flagHor = false;
			}
			
			else if(!verticle && !flagHor) {
				flagHor = true;
			}
		}
		
	}
	
	/**
	 * {@code traceBack} - traces back the route and checks if there were any ways in
	 * between in the traced path, works based on the tracker array's values. 
	 */
	private void traceBack() {
		LCD.clear();
		LCD.drawString(" tracing back", X, Y);
		final int LEFT_TURN = -90;
		final int RIGHT_TURN = 90;
		
		int [] angle= {90, 270, 90};
		
		int currI, currJ, currAngle = 0, decider ;
		float dist;
		
		drive.stopMotors();
		sensor.gyroReset();
		gyroFeedback = sensor.gyroSensorFeedback();
	
		currI = i;
		currJ = j;
		
		
		while(true) {
			
			if(tracker[currI][currJ]== 0) {
				
				decider = directionCheck();
				if(decider == 0) {
					i = currI;
					j = currJ;
					sensor.gyroReset();
					gyroFeedback = sensor.gyroSensorFeedback();
					tracker(RIGHT);
					if(verticle) {
						if(flagVer) {
							flagHor = true;
							verticle = false;
						}
						
						else if(!flagVer) {
							flagHor = false;
							verticle = false;
						}
						
					}
					else if(!verticle) {
						if(flagHor) {
							verticle = true;
							flagVer = false;
						}
						else if(!flagHor) {
							verticle = true;
							flagVer = true;
						}
					}
					
					
					if(firstTurn != RIGHT ) {
						firstTurn = RIGHT;
					}
					turnAlgo(firstTurn);

				}
				
				else if(decider == 1) {
					i = currI;
					j = currJ;
					sensor.gyroReset();
					gyroFeedback = sensor.gyroSensorFeedback();
					tracker(LEFT);
					if(verticle) {
						if(flagVer) {
							flagHor = false;
							verticle = false;
						}
						
						else if(!flagVer) {
							flagHor = true;
							verticle = false;
						}
						
					}
					else if(!verticle) {
						if(flagHor) {
							verticle = true;
							flagVer = true;
						}
						else if(!flagHor) {
							verticle = true;
							flagVer = false;
						}
					}
					
					if(firstTurn != LEFT ) {
						firstTurn = RIGHT;
					}
					turnAlgo(firstTurn);
				}
				
				else if(decider == 2) {
					sensor.gyroReset();
					gyroFeedback = sensor.gyroSensorFeedback();
					i = currI;
					j = currJ;
					tracker(STRAIGHT);
					moveBlock();
					if(verticle ) {
						if(flagVer) {
							currI--;
						}
						else if(!flagVer) {
							currI++;
						}
					}
					
					if(!verticle) {
						if(flagHor) {
							currJ++;
						}
						
						else if(!flagHor) {
							currJ--;
						}
					}
					
					if(firstTurn != RIGHT) {
						firstTurn = RIGHT;
					}
					i = currI;
					j = currJ;
					
					turnAlgo(firstTurn);
				}
			}

			
			
			if(tracker[currI][currJ] == REVERSE) {
				moveBlock();

				if(verticle && flagVer) {
					currI --;
				}
				else if(verticle && !flagVer) {
					currI++;
				}
				
				else if(!verticle && flagHor) {
					currJ++;
				}
				
				else if(!verticle && !flagHor) {
					currJ--;
				}
			}
			
			if (currI== INI_I_J && currJ == INI_I_J) {
				
				decider = directionCheck();
				if(decider == 0) {
					i = currI;
					j = currJ;
					sensor.gyroReset();
					gyroFeedback = sensor.gyroSensorFeedback();
					if(firstTurn != RIGHT ) {
						firstTurn = RIGHT;
					}
					turnAlgo(firstTurn);

				}
				
				else if(decider == 1) {
					i = currI;
					j = currJ;
					sensor.gyroReset();
					gyroFeedback = sensor.gyroSensorFeedback();
					if(firstTurn != LEFT ) {
						firstTurn = LEFT;
					}
					turnAlgo(firstTurn);
				}
				
				else if(decider == 2) {
					sensor.gyroReset();
					gyroFeedback = sensor.gyroSensorFeedback();
					moveBlock();
					if(verticle && flagVer ) {
						currI--;
					}
					
					else if(verticle && !flagVer ) {
						currI++;
					}
					
					else if(!verticle && flagHor ) {
						currJ++;
					}
					
					else if(!verticle && !flagHor ) {
						currJ--;
					}
				}		
					
				
			}

			if(tracker[currI][currJ] == STRAIGHT) {
				
				if(verticle && flagVer) {
					if(currJ == EXT_LOW ) {
						turnToAngle(RIGHT_TURN);
						dist = sensor.ultrasonicFeedback();
						
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							tracker(RIGHT);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							if(firstTurn != RIGHT ) {
								firstTurn = RIGHT;
							}
							turnAlgo(firstTurn);
							break;
						}
						

						else if(dist < SAFE_DISTANCE) {
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							turnToAngle(LEFT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							currI--;
						}
					}
					
					else if(currJ == EXT_HIGH) {
						turnToAngle(LEFT_TURN);
						dist = sensor.ultrasonicFeedback();
						
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							tracker(LEFT);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							if(firstTurn != LEFT ) {
								firstTurn = LEFT;
							}
							turnAlgo(firstTurn);
							break;
						}
						
						else if(dist < SAFE_DISTANCE) {
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							turnToAngle(RIGHT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							currI--;
						}
							
					}
					
					else {
						
						decider = directionCheck();
							if(decider == 0) {
								i = currI;
								j = currJ;
								tracker(RIGHT);
								sensor.gyroReset();
								gyroFeedback = sensor.gyroSensorFeedback();
								if(firstTurn != RIGHT ) {
									firstTurn = RIGHT;
								}
								turnAlgo(firstTurn);
								break;
								
							}
							
							else if(decider == 1) {
								i = currI;
								j = currJ;
								tracker(LEFT);
								sensor.gyroReset();
								gyroFeedback = sensor.gyroSensorFeedback();
								if(firstTurn != LEFT ) {
									firstTurn = LEFT;
								}
								turnAlgo(firstTurn);
								break;
							}
							
							else if(decider == 2 ) {
								moveBlock();
								currAngle = 0;
								currI--;
							}
						
					}
					
				}
				

				if(verticle && !flagVer) {
					if(currJ == EXT_LOW ) {
						turnToAngle(LEFT_TURN);
						dist = sensor.ultrasonicFeedback();
						
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							tracker(LEFT);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							if(firstTurn != LEFT ) {
								firstTurn = LEFT;
							}
							turnAlgo(firstTurn);
							break;
						}
						
						else if(dist < SAFE_DISTANCE) {
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							turnToAngle(RIGHT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							currI++;
						}						
					}
					
					else if(currJ == EXT_HIGH) {
						turnToAngle(RIGHT_TURN);
						dist = sensor.ultrasonicFeedback();
						
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							tracker(RIGHT);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							if(firstTurn != RIGHT ) {
								firstTurn = RIGHT;
							}
							turnAlgo(firstTurn);
							break;
						}
						
						else if(dist < SAFE_DISTANCE) {
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							turnToAngle(LEFT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							currI++;
						}
							
					}
					
					else {
						 decider = directionCheck();
						if(decider == 0) {
							i = currI;
							j = currJ;
							tracker(RIGHT);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							if(firstTurn != RIGHT ) {
								firstTurn = RIGHT;
							}
							turnAlgo(firstTurn);
							break;
							
						}
						
						else if(decider == 1) {
							i = currI;
							j = currJ;
							tracker(LEFT);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							if(firstTurn != LEFT ) {
								firstTurn = LEFT;
							}
							turnAlgo(firstTurn);
							break;
						}
						
						else if(decider == 2 ) {
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							currAngle = 0;
							currI++;
						}
												
				}
			}
			
			if(!verticle && flagHor) {
				if(currI == EXT_LOW ) {
					turnToAngle(RIGHT_TURN);
					dist = sensor.ultrasonicFeedback();
						
					if(dist > SAFE_DISTANCE) {
						i = currI;
						j = currJ;
						tracker(RIGHT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != RIGHT ) {
							firstTurn = RIGHT;
						}
						turnAlgo(firstTurn);
						break;
					}
					
					else if(dist < SAFE_DISTANCE) {
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						turnToAngle(LEFT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						currJ++;
					}
					
				}
				
				else if(currI == EXT_HIGH) {
					turnToAngle(LEFT_TURN);
					dist = sensor.ultrasonicFeedback();
					
					if(dist > SAFE_DISTANCE) {
						i = currI;
						j = currJ;
						tracker(LEFT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != LEFT ) {
							firstTurn = LEFT;
						}
						turnAlgo(firstTurn);
						break;
					}
					
					else if(dist < SAFE_DISTANCE) {
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						turnToAngle(RIGHT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						currJ++;
					}
						
				}
				
				else {
					decider = directionCheck();
						
					if(decider == 0) {
						i = currI;
						j = currJ;
						tracker(RIGHT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != RIGHT ) {
							firstTurn = RIGHT;
						}
						turnAlgo(firstTurn);
						break;
						
					}
					
					else if(decider == 1) {
						i = currI;
						j = currJ;
						tracker(LEFT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != LEFT ) {
							firstTurn = LEFT;
						}
						turnAlgo(firstTurn);
						break;
					}
					
					else if(decider == 2 ) {
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						currAngle = 0;
						currJ++;
					}	
				}
			}
		
			if(!verticle && !flagHor) {
				if(currI == EXT_LOW ) {
					turnToAngle(LEFT_TURN);
					dist = sensor.ultrasonicFeedback();
				
					if(dist > SAFE_DISTANCE) {
						i = currI;
						j = currJ;
						tracker(LEFT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != LEFT ) {
							firstTurn = LEFT;
						}
						turnAlgo(firstTurn);
						break;
					}
				
					else if(dist < SAFE_DISTANCE) {
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						turnToAngle(RIGHT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						currJ--;
					}						
				}
			
				else if(currI == EXT_HIGH) {
					turnToAngle(RIGHT_TURN);
					dist = sensor.ultrasonicFeedback();
					
					if(dist > SAFE_DISTANCE) {
						i = currI;
						j = currJ;
						tracker(RIGHT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != RIGHT ) {
							firstTurn = RIGHT;
						}
						turnAlgo(firstTurn);
						break;
					}
					
					else if(dist < SAFE_DISTANCE) {
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						turnToAngle(LEFT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						currJ--;
					}
						
				}
				
				else {
					decider = directionCheck();
					if(decider == 0) {
						i = currI;
						j = currJ;
						tracker(RIGHT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != RIGHT ) {
							firstTurn = RIGHT;
						}
						turnAlgo(firstTurn);
						break;
						
					}
					
					else if(decider == 1) {
						i = currI;
						j = currJ;
						tracker(LEFT);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						if(firstTurn != LEFT ) {
							firstTurn = LEFT;
						}
						turnAlgo(firstTurn);
						break;
					}
					
					else if(decider == 2 ) {
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						currAngle = 0;
						currJ--;

					}							
			}
				
		}	
		
	}
	
			if(tracker[currI][currJ] == LEFT) {
				
				if(firstTurn == RIGHT) {
					if(verticle && flagVer) {
						turnToAngle(RIGHT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						flagHor = true;
						verticle = false;
						currJ++;
					}
					
					else if(verticle && !flagVer) {
						turnToAngle(RIGHT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						flagHor = false;
						verticle = false;
						currJ--;
					}
					
					else if (!verticle && flagHor) {
						turnToAngle(RIGHT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						verticle = true;
						flagVer = false;
						currI++;
					}
					
					else if(!verticle && !flagHor) {
						turnToAngle(RIGHT_TURN);
						sensor.gyroReset();
						gyroFeedback = sensor.gyroSensorFeedback();
						moveBlock();
						verticle = true;
						flagVer = true;
						currI--;
					}
				
				}
				else if(firstTurn == LEFT ) {
					if(verticle && flagVer) {
						dist = sensor.ultrasonicFeedback();
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							turnAlgo(firstTurn);
							break;
						}
						
						else if(dist < SAFE_DISTANCE) {
							turnToAngle(RIGHT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							verticle = false;
							flagHor = true;
							currJ++;
						}
					}
					
					else if(verticle && !flagVer) {
						dist = sensor.ultrasonicFeedback();
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							turnAlgo(firstTurn);
							break;
						}
						
						else if(dist < SAFE_DISTANCE) {
							turnToAngle(RIGHT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							verticle = false;
							flagHor = false;
							currJ--;
						}
						
					}
					
					else if(!verticle && flagHor) {
						dist = sensor.ultrasonicFeedback();
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							turnAlgo(firstTurn);
							break;
						}
						
						else if(dist < SAFE_DISTANCE) {
							turnToAngle(RIGHT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							verticle = true;
							flagVer = false;
							currI++;
						}
						
					}
					
					
					else if(!verticle && !flagHor) {
						dist = sensor.ultrasonicFeedback();
						if(dist > SAFE_DISTANCE) {
							i = currI;
							j = currJ;
							turnAlgo(firstTurn);
							break;
						}
						
						else if(dist < SAFE_DISTANCE) {
							turnToAngle(RIGHT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							verticle = true;
							flagVer = true;
							currI--;
						}
						
					}
					
				}
				
				
			}
			
			if(tracker[currI][currJ] == RIGHT) {
				
				if(firstTurn == LEFT) {
					if(verticle ) {
						if(flagVer) {
							turnToAngle(LEFT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							flagHor = false;
							verticle = false;
							currJ--;	
						}
						
						if(!flagVer) {
							turnToAngle(LEFT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							flagHor = true;
							verticle = false;
							currJ++;
						}
						
					}
					
					if (!verticle ) {
						if(flagHor) {
							turnToAngle(LEFT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							verticle = true;
							flagVer = true;
							currI--;
						}
						
						else if(!flagHor) {
							turnToAngle(LEFT_TURN);
							sensor.gyroReset();
							gyroFeedback = sensor.gyroSensorFeedback();
							moveBlock();
							verticle = true;
							flagVer = false;
							currI++;
						}
					}
				}
				else if(firstTurn == RIGHT ) {
					
					if(verticle ) {
						if(flagVer) {
							dist = sensor.ultrasonicFeedback();
							if(dist > SAFE_DISTANCE) {
								i = currI;
								j = currJ;
								turnAlgo(firstTurn);
								break;
							}
							
							else if(dist < SAFE_DISTANCE) {
								sensor.gyroReset();
								gyroFeedback = sensor.gyroSensorFeedback();
								turnToAngle(LEFT_TURN);
								moveBlock();
								sensor.gyroReset();
								gyroFeedback = sensor.gyroSensorFeedback();
								verticle = false;
								flagHor = false;
								currJ--;
								}
						}
						
						else if(!flagVer) {
							dist = sensor.ultrasonicFeedback();
							if(dist > SAFE_DISTANCE) {
								i = currI;
								j = currJ;
								turnAlgo(firstTurn);
								break;
							}
							
							else if(dist < SAFE_DISTANCE) {
								turnToAngle(LEFT_TURN);
								sensor.gyroReset();
								gyroFeedback = sensor.gyroSensorFeedback();
								moveBlock();
								verticle = false;
								flagHor = true;
								currJ++;
							}
							
						}
						
						
					}
					else if(!verticle ) {
						if(flagHor) {
							dist = sensor.ultrasonicFeedback();
							if(dist > SAFE_DISTANCE) {
								i = currI;
								j = currJ;
								turnAlgo(firstTurn);
								break;
							}
							
							else if(dist < SAFE_DISTANCE) {
								turnToAngle(LEFT_TURN);
								sensor.gyroReset();
								gyroFeedback = sensor.gyroSensorFeedback();
								moveBlock();
								verticle = true;
								flagVer = true;
								currI--;
							}
						}
						
						else if(!flagHor) {
							dist = sensor.ultrasonicFeedback();
							if(dist > SAFE_DISTANCE) {
								i = currI;
								j = currJ;
								turnAlgo(firstTurn);
								break;
							}
							
							else if(dist < SAFE_DISTANCE) {
								turnToAngle(RIGHT_TURN);
								sensor.gyroReset();
								gyroFeedback = sensor.gyroSensorFeedback();
								moveBlock();
								verticle = true;
								flagVer = false;
								currI++;
							}	
						}
					}
				}	
			}
		}		
	}
	
	/**
	 * {@code directionCheck} - checks both right and left direction distances
	 * @return int - which signifies orientation of robot at which the distance is 
	 * greater than 16 cm 
	 */
	private int directionCheck() {
		int currAngle = 0;
		int [] angle = {90, 270, 90};
		float dist;
		
			while(true) {
				nextAngle = angle[currAngle];
				turnToAngle(nextAngle);
				dist = sensor.ultrasonicFeedback();
				
				if(dist > SAFE_DISTANCE) {
					if(currAngle == 0) {
						return 0;
					}
					
					if(currAngle == 1) {
						return 1;
						
					}
					
					else if(currAngle == 2) {
						return 2;
					}
					
				}
				
				currAngle++;
				if(currAngle == 2) {
					sensor.gyroReset();
					gyroFeedback = sensor.gyroSensorFeedback();
				}
				if(currAngle > 2) {
					currAngle = 0;
				}								
		}	
			
	}
	
}	