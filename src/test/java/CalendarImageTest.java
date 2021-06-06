import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import snippets.CalendarImage;

@RunWith(JUnit4.class)
public class CalendarImageTest {
	private CalendarImage calendarImage = new CalendarImage();

	private void sizeAndPrintTest(List<String> image, int excepted, String name, int number) {
		int actual = image.size();
		assertEquals(String.format("%s %d's size is invalid.", name, number), excepted, actual);
		for (String line : image) {
			System.out.print(line);
			System.out.println('#');
		}
		System.out.println();
	}

	@Test
	public void allImageTest() {
		// Month test
		for (int i = 1; i <= 12; i++) {
			List<String> image = calendarImage.getMonth(i);
			sizeAndPrintTest(image, 5, "Month", i);
		}

		// Day test
		for (int i = 1; i <= 31; i++) {
			List<String> image = calendarImage.getDay(i);
			sizeAndPrintTest(image, 11, "Day", i);
		}

		// dayOfWeek test
		for (int i = 1; i <= 7; i++) {
			List<String> image = calendarImage.getDayOfWeek(i);
			sizeAndPrintTest(image, 4, "DayOfWeek", i);
		}
	}
}
