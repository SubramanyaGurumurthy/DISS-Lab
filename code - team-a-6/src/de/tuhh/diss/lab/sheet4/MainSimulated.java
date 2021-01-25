package de.tuhh.diss.lab.sheet4;

import MazebotSim.MazebotSimulation;
import MazebotSim.Visualization.GuiMazeVisualization;
import lejos.utility.Delay;



public class MainSimulated {
	

	public static void main(String[] args) {
		
		MazebotSimulation sim = new MazebotSimulation("Mazes/TestArea.png", 1.5,  1.5);		
		GuiMazeVisualization gui = new GuiMazeVisualization(1.5, sim.getStateAccessor());
		sim.scaleSpeed(1);
		sim.setRobotPosition(0.75, 0.75, 90);
		
		sim.startSimulation();
		gui.startVisualization();
		
		// Calling the different functions for different tasks
//		SimTurn.simpleTurn();
		Turner turn = new PropTurn();
		turn.setSpeed(400);
		turn.turn(90);
		
//		FeedbackTurn turn = new FeedbackTurn();
//		turn.gyroFeedbackTurner();
//		turn.propTurner();
		
		Delay.msDelay(100);
		sim.stopSimulation();
	}
}

