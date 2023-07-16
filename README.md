# DISS-Lab winter 2020

## Task

This repository contains the solution to the challenge of the *Design and Implementation of Software Systems* course, where the goal is to navigate a robot through an undisclosed maze and find a target wall of a specified color. The challenge is designed to test the student's ability to design an algorithm that makes the robot explore the maze, accurately detect colors, and make strategic decisions.

### Challenge Summary:

* Target Color Selection: At the beginning of the challenge, the robot displays a menu to select the target color from red, green, and blue.
* Exploration Start: After the user selects the target color, the robot starts exploring the maze to locate the wall of the specified color.
* Beep Indication: The robot produces a beep sound to signal the start of exploration and another beep when it finds the target wall.
* Time Limit: To add an element of urgency, there is a time limit of 5 minutes (tentative) to complete the task. The time between the beeps is measured at normal simulation speed.
* Detecting the Target Wall: The maze contains a wall of the specified target color at a dead end. The robot must accurately detect the correct color and beep only when in front of the target wall.
* Maze Assumptions: The maze is built on a grid system, closed to prevent the robot from leaving the maze, and does not contain loops. It may have other colored walls, but only one matches the specified target color.
* Start Position: The robot's start position is not revealed until the challenge begins. However, we can assume that the robot will start at the center of a tile.

![Simulation of simple maze](https://github.com/SubramanyaGurumurthy/DISS-Lab/blob/main/simulation%20installation%20guidelines/1.%20simulatorDemo.gif)

Gif: Simulation of a simple maze example. The maze used for testing final challenge is unknown, hence the gif is not available
### Solution approach:
* 360-Degree Turn: The robot begins by performing a full 360-degree turn, measuring distances in all directions. This initial scan helps the robot understand the open spaces and identify corners at the starting point.
* Choosing the Optimal Path: If there are multiple possible movements, the robot selects the path with the lesser distance, optimizing its movement through the maze.
* Encountering a Dead End: When the robot reaches a dead end, with three sides closed, it checks for the specified target color. If not found, the robot enters the backtrack mode.
* Backtrack Mode: In the backtrack mode, the robot explores alternate pathways between the paths it has already traversed. This strategy ensures comprehensive coverage of the entire maze, increasing the chances of finding the target color.
* Locating the Target Color: After exploring all possible paths, the robot eventually finds the wall with the specified target color, completing the challenge successfully.

The A* search algorithm and the strategic approach of exploration and backtracking enable the robot to efficiently navigate the maze, detect the target color, and meet the time limit requirements.

## Licence/Disclaimer
The lab sheets and other files are the property of the TUHH and this webpage is being shared for the purpose of showing my skills.
