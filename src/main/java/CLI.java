import java.util.ArrayList;
import java.util.List;

import handler.*;

public class CLI {
<<<<<<< HEAD
	private List<Handler> handlers;

	public CLI() {
=======
	private ArrayList<Handler> handlers;
	public CLI(){
>>>>>>> f2f24277948f2476af154be6fded56f10c40ca3e
		handlers = new ArrayList<Handler>();
		handlers.add(new CalendarHandler());
		handlers.add(new WeatherHandler("新竹市"));
	}

	public void run() {
		for (Handler handler : handlers) {
			if (handler.ifOutput) {
				System.out.println(handler.toString());
			}
		}
	}
}
