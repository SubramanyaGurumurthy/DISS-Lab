package de.tuhh.diss.lab.sheet5;

import MazebotSim.MazebotSimulation;
import MazebotSim.Visualization.GuiMazeVisualization;
import lejos.utility.Delay;


public class MainSimulated {

	public static void main(String[] args) {
		MazebotSimulation sim = new MazebotSimulation("Mazes/4X4_1.png", 1.40,  1.40);
		GuiMazeVisualization gui = new GuiMazeVisualization(1.40, sim.getStateAccessor());
		sim.scaleSpeed(1);
		sim.setRobotPosition(0.175, 0.175 , 90);
		
		sim.startSimulation();
		gui.startVisualization();
		
		Decision maze = new Decision();
		maze.mazeSolver();		
		Delay.msDelay(100);
		sim.stopSimulation();

	}

}
