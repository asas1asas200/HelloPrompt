package snippets;

/**
 * This enum defined the ASNI color snippets for output.
 * 
 * @author <a href="mailto:asas1asas200@gmaill.com">Zeng</a>
 */
public enum ANSIColor {
	/** reset all color settings include text and background. */
	RESET("0m"),
	/** black text color. */
	BLACK("30m"),
	/** red text color. */
	RED("31m"),
	/** green text color. */
	GREEN("32m"),
	/** yellow text color. */
	YELLOW("33m"),
	/** blue text color. */
	BLUE("34m"),
	/** purple text color. */
	PURPLE("35m"),
	/** cyan text color. */
	CYAN("36m"),
	/** white text color. */
	WHITE("37m"),
	/** black background color. */
	BLACK_BACKGROUND("40m"),
	/** red background color. */
	RED_BACKGROUND("41m"),
	/** green background color. */
	GREEN_BACKGROUND("42m"),
	/** yellow background color. */
	YELLOW_BACKGROUND("43m"),
	/** blue background color. */
	BLUE_BACKGROUND("44m"),
	/** purple background color. */
	PURPLE_BACKGROUND("45m"),
	/** cyan background color. */
	CYAN_BACKGROUND("46m"),
	/** white background color. */
	WHITE_BACKGROUND("47m");

	private static final String PREFIX = "\u001B[";
	private final String text;

	private ANSIColor(final String text) {
		this.text = PREFIX + text;
	}

	@Override
	public String toString() {
		return text;
	}
}
