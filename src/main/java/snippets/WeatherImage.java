package snippets;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class WeatherImage {
//@formatter:off
	private final static List<String> UNKNOWN = Arrays.asList(
"    .-.      ",
"     __)     ",
"    (        ",
"     `-᾿     ",
"      •      ");
	private final static List<String> CLOUD = Arrays.asList(
"             ",
"\033[38;5;250m     .--.    \033[0m",
"\033[38;5;250m  .-(    ).  \033[0m",
"\033[38;5;250m (___.__)__) \033[0m",
"             ");
	private final static List<String> FOG = Arrays.asList(
"             ",
"\033[38;5;251m _ - _ - _ - \033[0m",
"\033[38;5;251m  _ - _ - _  \033[0m",
"\033[38;5;251m _ - _ - _ - \033[0m",
"             ");
	private final static List<String> RAIN = Arrays.asList(
"\033[38;5;240;1m     .-.     \033[0m",
"\033[38;5;240;1m    (   ).   \033[0m",
"\033[38;5;240;1m   (___(__)  \033[0m",
"\033[38;5;21;1m  ‚ʻ‚ʻ‚ʻ‚ʻ   \033[0m",
"\033[38;5;21;1m  ‚ʻ‚ʻ‚ʻ‚ʻ   \033[0m");
	private final static List<String> SUN_AND_CLOUD = Arrays.asList(
"\033[38;5;226m   \\  /\033[0m      ",
"\033[38;5;226m _ /\"\"\033[38;5;250m.-.    \033[0m",
"\033[38;5;226m   \\_\033[38;5;250m(   ).  \033[0m",
"\033[38;5;226m   /\033[38;5;250m(___(__) \033[0m",
"             ");
	private final static List<String> SUN_AND_RAIN = Arrays.asList(
"\033[38;5;226m _`/\"\"\033[38;5;250m.-.    \033[0m",
"\033[38;5;226m  ,\\_\033[38;5;250m(   ).  \033[0m",
"\033[38;5;226m   /\033[38;5;250m(___(__) \033[0m",
"\033[38;5;111m     ʻ ʻ ʻ ʻ \033[0m",
"\033[38;5;111m    ʻ ʻ ʻ ʻ  \033[0m");
	private final static List<String> SUN = Arrays.asList(
"\033[38;5;226m    \\   /    \033[0m",
"\033[38;5;226m     .-.     \033[0m",
"\033[38;5;226m  ‒ (   ) ‒  \033[0m",
"\033[38;5;226m     `-᾿     \033[0m",
"\033[38;5;226m    /   \\    \033[0m");
	private final static List<String> RAIN_AND_THUNDER = Arrays.asList(
"\033[38;5;240;1m     .-.     \033[0m",
"\033[38;5;240;1m    (   ).   \033[0m",
"\033[38;5;240;1m   (___(__)  \033[0m",
"\033[38;5;21;1m  ‚ʻ\033[38;5;228;5m⚡\033[38;5;21;25mʻ‚\033[38;5;228;5m⚡\033[38;5;21;25m‚ʻ \033[0m",
"\033[38;5;21;1m  ‚ʻ‚ʻ\033[38;5;228;5m⚡\033[38;5;21;25mʻ‚ʻ  \033[0m");
	private final static List<String> SUN_AND_RAIN_AND_THUNDER = Arrays.asList(
"\033[38;5;226m _`/\"\"\033[38;5;250m.-.    \033[0m",
"\033[38;5;226m  ,\\_\033[38;5;250m(   ).  \033[0m",
"\033[38;5;226m   /\033[38;5;250m(___(__) \033[0m",
"\033[38;5;228;5m    ⚡\033[38;5;111;25mʻ ʻ\033[38;5;228;5m⚡\033[38;5;111;25mʻʻ\033[0m",
"\033[38;5;111m    ʻ ʻ ʻ ʻ  \033[0m");
	private final static List<String> SNOW = Arrays.asList(
"\033[38;5;240;1m     .-.     \033[0m",
"\033[38;5;240;1m    (   ).   \033[0m",
"\033[38;5;240;1m   (___(__)  \033[0m",
"\033[38;5;255;1m   * * * *   \033[0m",
"\033[38;5;255;1m  * * * *    \033[0m");

	private final static List<String> TEMP = Arrays.asList(
"  _          ",
" | |" + ANSIColor.RED + "___H%3s°C" + ANSIColor.RESET,
" |" + ANSIColor.RED_BACKGROUND + " " + ANSIColor.RESET + "|         ",
" |" + ANSIColor.RED_BACKGROUND + " " + ANSIColor.RESET + "|" + ANSIColor.CYAN + "___L%3s°C" + ANSIColor.RESET,
" |" + ANSIColor.RED_BACKGROUND + " " + ANSIColor.RESET + "|         ",
" (" + ANSIColor.RED_BACKGROUND + ANSIColor.PURPLE + "_" + ANSIColor.RESET + ")         ");

//@formatter:on

	public static List<String> getWeatherImage(List<String> weatherDescs) {
		// Because only index 0 of list contains the information we need.
		return getWeatherImage(weatherDescs.get(0));
	}

	public static List<String> getTempImage(String high, String low, Boolean lastLineOffset) {
		List<String> temp = getTempImage(high, low);
		if (!lastLineOffset)
			temp.set(5, " (" + ANSIColor.RED_BACKGROUND + ANSIColor.PURPLE + "_" + ANSIColor.RESET + ")");
		return temp;
	}

	public static List<String> getTempImage(String high, String low) {
		List<String> temp = new ArrayList<String>(TEMP);
		temp.set(1, String.format(temp.get(1), high));
		temp.set(3, String.format(temp.get(3), low));
		return temp;
	}

	public static List<String> getWeatherImage(String weatherDesc) {
		if (weatherDesc.contains("陰") || weatherDesc.contains("雲")) {
			if (weatherDesc.contains("晴")) {
				if (weatherDesc.contains("雷"))
					return SUN_AND_RAIN_AND_THUNDER;
				else if (weatherDesc.contains("雨"))
					return SUN_AND_RAIN;
				else
					return SUN_AND_CLOUD;
			} else if (weatherDesc.contains("雨")) {
				if (weatherDesc.contains("雷"))
					return RAIN_AND_THUNDER;
				else
					return RAIN;
			} else {
				return CLOUD;
			}
		} else if (weatherDesc.contains("晴")) {
			return SUN;
		} else if (weatherDesc.contains("雨")) {
			if (weatherDesc.contains("雷"))
				return RAIN_AND_THUNDER;
			else
				return RAIN;
		} else if (weatherDesc.contains("雪") || weatherDesc.contains("冰")) {
			return SNOW;
		} else if (weatherDesc.contains("霧")) {
			return FOG;
		}
		return UNKNOWN;
	}

	public static Boolean isUnknown(List<String> ANSIImage) {
		return ANSIImage.equals(UNKNOWN);
	}

}
