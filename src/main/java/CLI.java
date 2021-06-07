import java.util.ArrayList;
import java.util.List;

import handler.*;

/**
 * CLI is a interface for calling all handlers and display prompt on screen.
 *
 * @author <a href="mailto:asas1asas200@gmaill.com">Zeng</a>
 */
public class CLI {
	private List<Handler> handlers;

	/**
	 * This contructor defined the handlers that will run.
	 */
	public CLI() {
		handlers = new ArrayList<Handler>();
		handlers.add(new CalendarHandler());
	}

	/**
	 * Call this function to run all handlers and prompt it!
	 */
	public void run() {
		for (Handler handler : handlers) {
			if (handler.ifOutput) {
				System.out.println(handler.toString());
			}
		}
	}
}
