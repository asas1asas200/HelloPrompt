
import java.util.List;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

import snippets.WeatherImage;

@RunWith(JUnit4.class)
public class WeatherImageTest {
	@Test
	public void unknownDescDetect() {
		List<String> allOfDesc = new ArrayList<String>();
		try {
			InputStream descFile = WeatherImageTest.class.getResourceAsStream("./allOfDesc.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(descFile));
			String line;
			while ((line = br.readLine()) != null) {
				allOfDesc.add(line);
			}

		} catch (Exception e) {
			Assert.fail("An error occured when loading file: " + e.toString());
		}
		for (String line : allOfDesc) {
			List<String> image = WeatherImage.getWeatherImage(line);
			Assert.assertFalse(String.format("%s is an unknown description.", line), WeatherImage.isUnknown(image));
		}
	}
}
