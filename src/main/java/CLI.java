import java.util.ArrayList;
import java.util.List;

import handler.*;

/**
 * CLI is a interface for calling all handlers and display prompt on screen.
 *
 * @author <a href="mailto:asas1asas200@gmaill.com">Zeng</a>
 */
public class CLI {
	private List<HandlerThread> handlers;

	/**
	 * A HandlerThread denotes a thread for processing handler, it can run handler
	 * in parallel and store the result prompt.
	 */
	public class HandlerThread extends Thread {
		private Handler handler;
		private String result;

		/**
		 * Constructor of HandlerThread, passing a handler to initialize it.
		 * 
		 * @param handler The handler that will run in future.
		 */
		public HandlerThread(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			result = handler.toString();
		}

		/**
		 * Get the result prompt after run.
		 * 
		 * @return {@code String} Result prompt string.
		 */
		public String getResult() {
			return result;
		}
	}

	/**
	 * This contructor defined the handlers that will run.
	 */
	public CLI() {
		handlers = new ArrayList<HandlerThread>();
		handlers.add(new HandlerThread(new CalendarHandler()));
		handlers.add(new HandlerThread(new WeatherHandler()));
	}

	/**
	 * Call this function to run all handlers and prompt it!
	 */
	public void run() {
		for (Thread handler : handlers) {
			handler.run();
		}
		for (Thread handler : handlers) {
			try {
				handler.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (HandlerThread handler : handlers) {
			System.out.println(handler.getResult());
		}
	}
}
