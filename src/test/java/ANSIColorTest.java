package ntou.cs.java2021.helloprompt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ANSIColorTest {
	@Test
	public void allColorOutputTest() {
		String reset = ANSIColor.RESET.toString();
		for(ANSIColor color: ANSIColor.values()){
			System.out.println(color + color.name() + reset);
		}
	}

}