/**
 * This is main class of this project.
 * <p>
 * This class will create an interface instance and run it. But it only has
 * command line interface now.
 * </p>
 * 
 * @author <a href="mailto:asas1asas200@gmail.com">Zeng</a>
 */
public class HelloPrompt {
	/**
	 * This function is the primary entry point.
	 * 
	 * @param args it has no actual effect now.
	 */
	public static void main(String[] args) {
		CLI cli = new CLI();
		cli.run();
	}
}
