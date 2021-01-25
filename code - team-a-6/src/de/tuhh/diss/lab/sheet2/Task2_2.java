package de.tuhh.diss.lab.sheet2;

import lejos.hardware.lcd.LCD;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;

public class Task2_2 {

	static int col = 1;
	static int vol = 50;
	static int frq = 400;
	static int dur = 1000;
	
	
	public static void menudisplay() {
		boolean infi = true;
		//int[] values = {50,70,100};
		
		while(infi) { 
			LCD.drawString("Vol", 4, 1);
			LCD.drawString("Frq", 4, 2);
			LCD.drawString("Dur", 4, 3);
			LCD.drawString("play", 4, 4);
			LCD.drawInt(vol, 9, 1);
			LCD.drawInt(frq, 9, 2);
			LCD.drawInt(dur, 9, 3); 
			//Button.waitForAnyPress();
			
			/* to go through the options*/
			if(Button.DOWN.isDown()) {
				col = col+1;
				if(col>4) {
					col = 1;	
				}
				LCD.drawString(">", 1, col);
				Delay.msDelay(1000);
				LCD.clear();
				if(Button.ENTER.isDown()) {
				 if(col==1) {
						vol();
					}
					
				if(col==2) {
						fre();
					}
					
				if(col==3) {
						dur();
					}
					
				 if(col == 4) {
						play();
					}
				
			} 
			}
			
		if(Button.UP.isDown()) {
			col = col-1;
			
			if(col<1) {
				col = 4;}
				LCD.drawString(">", 1, col);
				Delay.msDelay(1000);
				LCD.clear();
				
				if(Button.ENTER.isDown()) {
					if(col==1) {
						vol();
						
					}
					
					 if(col==2) {
						fre();
					}
					
					 if(col==3) {
						dur();
					}
					
					 if(col == 4) {
						play();
					}
					 
				
			}
				
				
				
			}
		}
		
	}
	
	public static void vol() {
		boolean infi = true;
		
		LCD.clear();
		while(infi){
			LCD.drawString(">Vol", 4, 1);
			LCD.drawString("Frq", 4, 2);
			LCD.drawString("Dur", 4, 3);
			LCD.drawString("play", 4, 4);
			LCD.drawInt(vol, 9, 1);
			LCD.drawInt(frq, 9, 2);
			LCD.drawInt(dur, 9, 3);
			
			
			//LCD.drawString("Vol",4,1);
			LCD.drawInt(vol, 9,1);
			Button.waitForAnyPress();
			if (Button.RIGHT.isDown()) {
				vol = vol+10;
				if(vol>100) {
					vol = 100;
				}
				LCD.drawInt(vol, 9, 1);
				
				
				}
			 if (Button.LEFT.isDown()) {
				vol = vol-10;
				if(vol<0) {
					vol = 0;
					}
				LCD.drawInt(vol, 9, 1);
				
				}
			if(Button.RIGHT.isUp() && Button.LEFT.isUp())
			{menudisplay();}
			Delay.msDelay(750);
			LCD.clear();
			//menudisplay();
			}
	}
		
	public static void fre() {
		
		boolean infi = true;
		LCD.clear();
		while(infi){
			LCD.drawString("Vol", 4, 1);
			LCD.drawString(">Frq", 4, 2);
			LCD.drawString("Dur", 4, 3);
			LCD.drawString("play", 4, 4);
			LCD.drawInt(vol, 9, 1);
			LCD.drawInt(frq, 9, 2);
			LCD.drawInt(dur, 9, 3);
			//LCD.drawString("Frq",4,3);
			LCD.drawInt(frq, 9, 2);
			Button.waitForAnyPress();
			if (Button.RIGHT.isDown()) {
				frq = frq+10;
				if(frq>1000) {
					frq = 1000;
				}
				LCD.drawInt(frq, 9, 2);
				
				
				}
			 if (Button.LEFT.isDown()) {
				frq = frq-10;
				if(frq<10) {
					frq = 10;
					}
				LCD.drawInt(frq, 9, 2);
				}
			if(Button.RIGHT.isUp() && Button.LEFT.isUp())
			{menudisplay();}
			Delay.msDelay(750);
			
			LCD.clear();
			//menudisplay();
			
			}
	}
		public static void dur() {
			boolean infi = true;
			LCD.clear();
			
			while(infi){
				LCD.drawString("Vol", 4, 1);
				LCD.drawString("Frq", 4, 2);
				LCD.drawString(">Dur", 4, 3);
				LCD.drawString("play", 4, 4);
				LCD.drawInt(vol, 9, 1);
				LCD.drawInt(frq, 9, 2);
				LCD.drawInt(dur, 9, 3);
				//LCD.drawString("Dur",4,3);
				LCD.drawInt(dur, 9, 3);
				Button.waitForAnyPress();
				if (Button.RIGHT.isDown()) {
					dur = dur+500;
					if(dur>5000) {
						dur= 5000;
					}
					LCD.drawInt(dur, 9, 3);
					
					}
			   if (Button.LEFT.isDown()) {
					dur = dur-500;
					if(dur<500) {
						dur = 500;
						}
					LCD.drawInt(dur, 9, 3);
					
					}
				if(Button.RIGHT.isUp() && Button.LEFT.isUp())
					{menudisplay();}
				Delay.msDelay(750);
				
				LCD.clear();
				
				
				}
		}
			
			public static void play() {
				LCD.drawString("Vol", 4, 1);
				LCD.drawString("Frq", 4, 2);
				LCD.drawString("Dur", 4, 3);
				LCD.drawString(">play", 4, 4);
				LCD.drawInt(vol, 9, 1);
				LCD.drawInt(frq, 9, 2);
				LCD.drawInt(dur, 9, 3);
				
				//LCD.drawString("Playing", 4, 5);
				Sound.setVolume(vol);
				Sound.playTone(frq, dur,vol);
				LCD.clear();
				
			//	menudisplay();
			}
		
	}