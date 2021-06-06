import java.util.ArrayList;
import java.util.List;

import handler.*;

public class CLI {
	private List<Handler> handlers;

	public CLI() {
		handlers = new ArrayList<Handler>();
		handlers.add(new CalendarHandler());
	}

	public void run() {
		for (Handler handler : handlers) {
			if (handler.ifOutput) {
				System.out.println(handler.toString());
			}
		}
	}
}
