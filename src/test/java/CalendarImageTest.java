package ntou.cs.java2021.helloprompt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CalendarImageTest {
	private CalendarImage calendarImage = new CalendarImage();
	@Test
	public void showAllMonths(){
		for(int i=1;i<=12;i++){
			for(String line: calendarImage.getMonth(i)){
				System.out.print(line);
				System.out.println("#");
			}
			System.out.println();
		}
	}

	@Test
	public void showAllDays(){
		for(int i=1;i<=31;i++){
			for(String line: calendarImage.getDay(i)){
				System.out.print(line);
				System.out.println('#');
			}
			System.out.println();
		}
	}

	@Test
	public void showAllDaysOfWeek(){
		for(int i=1;i<=7;i++){
			for(String line: calendarImage.getDayOfWeek(i)){
				System.out.print(line);
				System.out.println('#');
			}
			System.out.println();
		}
	}
}