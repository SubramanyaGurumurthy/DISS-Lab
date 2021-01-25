package de.tuhh.diss.lab.sheet2;
import MazebotSim.MazebotSimulation;
import MazebotSim.Visualization.GuiMazeVisualization;

//import lejos.hardware.lcd.TextLCD;


public class MainSimulated {
	

	public static void main(String[] args) {
		
		MazebotSimulation sim = new MazebotSimulation("Mazes/TestArea.png", 1.5,  1.5);
		GuiMazeVisualization gui = new GuiMazeVisualization(1.5, sim.getStateAccessor());
		sim.scaleSpeed(1);
		sim.setRobotPosition(0.75, 0.75, 90);

		sim.startSimulation();
		gui.startVisualization();
//		task2_2_dummy.menudisplay();
//		Task2_2.menudisplay();
//		Task2_1.print(new String[10]);
		Task2_3.straightdriving();
		
		
		sim.stopSimulation();
	}
}
