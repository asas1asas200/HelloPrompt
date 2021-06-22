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

	/**
	 * The result by handler rendered.
	 */
	protected String result;

	/**
	 * Get the prompt messgae after {@code run()}.
	 *
	 * @return {@code String} prompt messages.
	 */
	public String getResult() {
		return result;
	};

	/**
	 * Call this function to render the result string of handler.
	 *
	 */
	@Override
	public abstract void run();

}
