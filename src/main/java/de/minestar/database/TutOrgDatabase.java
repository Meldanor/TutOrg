package de.minestar.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
		this.prepareStatements();
	}

	// Date to text conversion because of the sqllite datase, which does not
	// support any date datatypes
	private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(
			DateFormat.MEDIUM, Locale.GERMAN);

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

	private void prepareStatements() {
		try {
			this.database
					.prepareStatement("addStudent",
							"INSERT INTO students (name, surname, tutorium, date) VALUES (?,?,?,?);");
			this.database.prepareStatement("allTutorium",
					"SELECT DISTINCT(tutorium) FROM students;");

			this.database
					.prepareStatement("allData", "SELECT * FROM students;");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addStudent(String name, String surname, String tutorium,
			Date date) {
		PreparedStatement statement = this.database.getQuery("addStudent");
		try {
			statement.setString(1, name);
			statement.setString(2, surname);
			statement.setString(3, tutorium);
			statement.setString(4, this.toText(date));
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getAllTutorium() {

		PreparedStatement statement = this.database.getQuery("allTutorium");
		List<String> tutoriums = new ArrayList<String>();
		try {

			ResultSet res = statement.executeQuery();
			while (res.next()) {
				tutoriums.add(res.getString("tutorium"));
			}
			return tutoriums;
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.<String> emptyList();
		}
	}

	public List<TutOrgData> getAll() {
		PreparedStatement statement = this.database.getQuery("allData");
		List<TutOrgData> data = new ArrayList<TutOrgData>();
		try {

			ResultSet res = statement.executeQuery();
			while (res.next()) {
				String name = res.getString("name");
				String surname = res.getString("surname");
				String tutorium = res.getString("tutorium");
				Date date = toDate(res.getString("date"));
				data.add(new TutOrgData(name, surname, tutorium, date));
			}
			return data;
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.<TutOrgData> emptyList();
		}
	}

	public class TutOrgData {
		private String name;
		private String surname;
		private String tutor;
		private Date date;

		public TutOrgData(String name, String surname, String tutor, Date date) {
			this.name = name;
			this.surname = surname;
			this.tutor = tutor;
			this.date = date;
		}

		public String getName() {
			return name;
		}

		public String getSurname() {
			return surname;
		}

		public String getTutor() {
			return tutor;
		}

		public Date getDate() {
			return date;
		}

	}

}
