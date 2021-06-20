import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import handler.*;

/**
 * CLI is a interface for calling all handlers and display prompt on screen.
 *
 * @author <a href="mailto:asas1asas200@gmail.com">Zeng</a>
 */
public class CLI {
	private List<Handler> handlers;

	/**
	 * This contructor defined the handlers that will run.
	 */
	public CLI(String[] args) {
		Map<String, String> argsMapping = parseArgs(args);
		handlers = new ArrayList<Handler>();
		handlers.add(new CalendarHandler());
		String location = argsMapping.getOrDefault("-l", "");
		if (location.isEmpty())
			handlers.add(new WeatherHandler());
		else
			handlers.add(new WeatherHandler(location));
		handlers.add(new GitHandler());
	}

	/**
	 * Call this function to run all handlers and prompt it!
	 */
	public void run() {
		for (Thread handler : handlers) {
			handler.start();
		}
		for (Thread handler : handlers) {
			try {
				handler.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Handler handler : handlers) {
			if(handler.ifOutput)
				System.out.println(handler.getResult());
		}
	}

	private Map<String, String> parseArgs(String[] args) {
		Map<String, String> argsMapping = new HashMap<String, String>();
		Boolean isKey = true;
		String key = "";
		for (String arg : args) {
			if (isKey) {
				if (arg.contains("-")) {
					key = arg.substring(0, 2);
				} else {
					return argsMapping;
				}
			} else {
				argsMapping.put(key, arg);
			}
			isKey = !isKey;
		}
		return argsMapping;
	}
}
