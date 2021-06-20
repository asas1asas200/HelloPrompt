package handler;

/**
 * A Handler is a <strong>Thread Object</strong>, it can call by other object
 * and run with new thread.<br>
 * Any handler has a run method to render the result string.<br>
 * If there are multiple handlers, you can configure them like follow example to
 * run in parallel.<br>
 * 
 * <pre>
 * for (Handler handler : handlers)
 * 	handler.start();
 * </pre>
 *
 * @author <a href="mailto:asas1asas200@gmail.com">Zeng</a>
 */
public abstract class Handler extends Thread {
	/**
	 * Judge if this handler will output prompts.
	 */
	public Boolean ifOutput = false;

	protected String result;

	public String getResult() {
		return result;
	};

	/**
	 * Call this function to render the result string of handler.
	 *
	 * @return {@code String} prompt messages.
	 */
	@Override
	public abstract void run();

}
