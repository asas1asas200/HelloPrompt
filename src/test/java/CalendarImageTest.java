package ntou.cs.java2021.helloprompt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CalendarImageTest {
	@Test
	public void showAllMonths(){
		CalendarImage calendarImage = new CalendarImage();
		for(int i=1;i<=12;i++){
			for(String line: calendarImage.getMonth(i)){
				System.out.print(line);
				System.out.println("#");
			}
			System.out.println();
		}
	}
}