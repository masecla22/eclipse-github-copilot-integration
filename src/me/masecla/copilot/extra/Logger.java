package me.masecla.copilot.extra;

public class Logger {
	public static Logger getInstance(Class<?> clazz) {
		return new Logger();
	}

	public void warn(String s, Throwable e) {
		
	}

	public void debug(String string) {
		
	}

	public void error(String string, Throwable e) {
		
	}

	public void trace(String format) {
		
	}

	public boolean isTraceEnabled() {
		return true;
	}

	public void error(String string) {
		
	}

	public void warn(String string) {
		
	}

	public boolean isDebugEnabled() {
		return true;
	}
}
