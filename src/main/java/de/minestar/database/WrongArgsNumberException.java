package de.minestar.database;

public class WrongArgsNumberException extends Exception {

	private static final long serialVersionUID = -2057864528812382609L;

	public WrongArgsNumberException() {
		super();
	}

	public WrongArgsNumberException(String message) {
		super(message);
	}

}
