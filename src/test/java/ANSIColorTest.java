import java.util.List;
import java.util.Arrays;
import java.security.SecureRandom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import snippets.ANSIColor;

@RunWith(JUnit4.class)
public class ANSIColorTest {
	private static SecureRandom random = new SecureRandom();

	@Test
	public void allColorOutputTest() {
		String reset = ANSIColor.RESET.toString();
		for (ANSIColor color : ANSIColor.values()) {
			System.out.println(color + color.name() + reset);
		}
	}

	@Test
	public void combineTest() {
		List<ANSIColor> colors = Arrays.asList(ANSIColor.values());
		ANSIColor textColor = colors.get(random.nextInt(8) + 1);
		ANSIColor backgroundColor = colors.get(random.nextInt(8) + 9);
		System.out.println(textColor.toString() + backgroundColor.toString() + textColor.name() + " with "
				+ backgroundColor.name() + colors.get(0).toString());
	}

}
