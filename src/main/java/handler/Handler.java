package handler;

public abstract class Handler {
	public Boolean ifOutput;

	protected abstract void readConfig(String fileName);

	public abstract String toString();
}
