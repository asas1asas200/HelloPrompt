package handler;

/**
 * This abstract class defined the basic properties of handler. It can be called
 * from other class by
 * 
 * <pre>
 * if (handler.ifOutput)
 * 	System.out.println(handler.toString());
 * </pre>
 *
 * @author <a href="mailto:asas1asas200@gmail.com">Zeng</a>
 */
public abstract class Handler {
	/**
	 * Judge if this handler will output prompts.
	 */
	public Boolean ifOutput;

	/**
	 * This function can get config file by passing filename.
	 * 
	 * @param fileName filename of config file.
	 */
	protected abstract void readConfig(String fileName);

	/**
	 * Call this function to get the prompt string of handler.
	 *
	 * @return {@code String} prompt messages.
	 */
	public abstract String toString();
}
