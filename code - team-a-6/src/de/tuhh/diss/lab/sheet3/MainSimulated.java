package de.tuhh.diss.lab.sheet3;
import MazebotSim.MazebotSimulation;
import MazebotSim.Visualization.GuiMazeVisualization;

//import lejos.hardware.lcd.TextLCD;


public class MainSimulated {
	

	public static void main(String[] args) {
		
		/*USING DIFFERENT IMAGES*/
		
//		MazebotSimulation sim = new MazebotSimulation("Mazes/maze_1_3by4.png", 1.5,  1.5);
		MazebotSimulation sim = new MazebotSimulation("Mazes/TestArea.png", 1.5,  1.5);
//		MazebotSimulation sim = new MazebotSimulation("Mazes/Simple3By3.png", 1.5,  1.5);
				
		GuiMazeVisualization gui = new GuiMazeVisualization(1.5, sim.getStateAccessor());
		sim.scaleSpeed(1);
		
		/*SETTING ROBOT TO DIFFERENT START POSITIONS*/		
		
//		sim.setRobotPosition(1.30, 0.50, 0);
//		sim.setRobotPosition(0.90, 0.90, 180);
//		sim.setRobotPosition(1.20, 0.50, 0);
		sim.setRobotPosition(0.75, 0.75, 90);
		
		sim.startSimulation();
		gui.startVisualization();
		
		/*CALLING DIFFERENT METHODS*/ 
		
//		Task3_2.Distance_Sensing();
//		Task3_1.Color_Detection();
		Task3_3.Approach_Wall();
//		Task3_4.GyroSensor();
		
		sim.stopSimulation();
	}
}
