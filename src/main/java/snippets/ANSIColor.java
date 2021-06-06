package snippets;

public enum ANSIColor {
	RESET("0m"),
	BLACK("30m"),
	RED("31m"),
	GREEN("32m"),
	YELLOW("33m"),
	BLUE("34m"),
	PURPLE("35m"),
	CYAN("36m"),
	WHITE("37m"),
	BLACK_BACKGROUND("40m"),
	RED_BACKGROUND("41m"),
	GREEN_BACKGROUND("42m"),
	YELLOW_BACKGROUND("43m"),
	BLUE_BACKGROUND("44m"),
	PURPLE_BACKGROUND("45m"),
	CYAN_BACKGROUND("46m"),
	WHITE_BACKGROUND("47m");
	


	private static final String PREFIX = "\u001B[";
	private final String text;

	ANSIColor(final String text){
		this.text = PREFIX + text;
	}

	@Override
	public String toString(){
		return text;
	}
}
