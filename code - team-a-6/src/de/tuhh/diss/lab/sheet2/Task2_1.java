package de.tuhh.diss.lab.sheet2;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Task2_1 {

	public static void print(String[] args) {
		int i=0;
		//prints the word in a banner, thrice
		 while(i<3)
        {for(int j=5; j<12; j++)  //max cell width is 17 ,counting the letters according and printing them on LCD
		{LCD.drawString("Hello",j,1);
          
          Delay.msDelay(1000);
          LCD.clearDisplay();
          }
	i++;}
	}


}
