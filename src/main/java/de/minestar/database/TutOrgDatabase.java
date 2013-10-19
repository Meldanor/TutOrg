package de.minestar.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class TutOrgDatabase {

	private Database database;

	public TutOrgDatabase() {
		init();
	}

	private void init() {
		database = new SQLiteDatabase("data");
		database.createStructureIfNeeded(getClass().getResourceAsStream(
				"/structure.sql"));
	}

	// Date to text conversion because of the sqllite datase, which does not
	// support any date datatypes
	private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(
			DateFormat.LONG, Locale.GERMAN);

	private String toText(Date date) {
		return DATE_FORMAT.format(date);
	}

	private Date toDate(String text) {
		try {
			return DATE_FORMAT.parse(text);
		} catch (ParseException e) {
			return null;
		}
	}

}
