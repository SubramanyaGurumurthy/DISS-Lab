package de.tuhh.diss.lab.sheet5;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class ColorMenu {
	
	/**
	 * {@code menu} - displays all the colors which are referenced in LEJOS 
	 * and waits for user's selection 
	 * @return selected color as {@code index}
	 */
	public int menu() {
		int col = 2, col1 = 2;
		int index = 0, flag = 0;
		String[] colors = {"None","Red", "Green", "Blue","Yellow","Magenta","Orange","White","Black"};
		int[] colorID= {-1,0,1,2,3,4,5,6,7};
		
		
		while(true){
			LCD.clear();
			
			LCD.drawString("Selct Color", 1, 1);

			if(flag == 0) {
			LCD.drawString("Red", 5, 2);	
			LCD.drawString("Green", 5, 3);
			LCD.drawString("Blue", 5,4);
			LCD.drawString("Yellow", 5, 5);
			LCD.drawString("Magenta",5,6);
			LCD.drawString("Orange", 5,7);
			LCD.drawString(">", 1, col);
			Delay.msDelay(250);
			} 
			
			if (flag>0) {
				LCD.drawString("White", 5, 2);
				LCD.drawString("Black",5,3);
				LCD.drawString(">", 1, col1);
				Delay.msDelay(250);
			}
			
			Button.waitForAnyPress();
			
			if(Button.ENTER.isDown()) {
				if(flag == 0) {
					index = col-2;
					break;
				}
				else {
					col1 = col1 + 6;
					index = col1 - 2;
					break;
				}
			}
			
			if(Button.DOWN.isDown() && flag == 0) {
				col = col+1;
				if(col>7) {
					col1 = 2;
					flag = 1;
				}
			}	
				
			if(Button.UP.isDown() && flag == 0) {
				col = col-1;
				if(col<2) {
					col1 = 3;
					flag = 1;	
				}
			}
			
			if(Button.DOWN.isDown() && flag>0) {
				col1 = col1+1;
				if(col1>3){
					col = 2;
					flag = 0;
				}
			}
			
			if(Button.UP.isDown() && flag>0) {
				col1 = col1-1;
				if(col1<2) {
					col = 7;
					flag = 0;
				}
			}
			
			
		}
		
		for(int i = 0 ; i<=9; i++){ 
			if(index == colorID[i]){
				LCD.clear();
				LCD.drawString("Selected color: ", 1, 1);
				LCD.drawString(""+colors[i], 1, 2);
				Delay.msDelay(1000);
				break;
			}			
		}
		
		return index;
	}
	
}
