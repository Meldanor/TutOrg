package de.meldanor.tutorg;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import de.minestar.database.TutOrgDatabase.TutOrgData;

public class ConsoleHandler {

	private Scanner scanner;

	private PrintStream output;

	public ConsoleHandler(InputStream input, PrintStream output) {
		this.scanner = new Scanner(input);
		this.output = output;
	}

	public void run() {
		output.println("Was moechten Sie tun?");
		output.println("(1) Neue Studenten eintragen");
		output.println("(2) Statistik anzeigen");

		int option = scanner.nextInt();
		scanner.nextLine();
		if (option == 1) {
			addNewStudent();
		} else if (option == 2) {
			showStatistic();
		}
	}

	private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(
			DateFormat.MEDIUM, Locale.GERMAN);

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+$");

	private void addNewStudent() {
		output.println("Tag eingeben (\"Heute\" fuer Heute, Format: Tag.Monat.Jahr. Beispiel ist 24.03.1991)");
		String day = scanner.nextLine();
		Date date = null;
		if (day.equalsIgnoreCase("Heute")) {
			date = new Date();
		} else {
			try {
				date = DATE_FORMAT.parse(day);
			} catch (ParseException e) {
				output.println("Wrong format!");
				return;
			}
		}
		output.println("Tag = " + DATE_FORMAT.format(date));
		output.println("Welches Tutorium? Zahl fuer vorhandenes, Text fuer neues Tutorium");
		List<String> tutoriums = Core.database.getAllTutorium();
		int i = 1;
		for (String string : tutoriums) {
			output.print("(" + i + ") " + string + " ");
			++i;
		}
		output.println();

		String tutorium = scanner.nextLine();
		if (NUMBER_PATTERN.matcher(tutorium).matches()) {
			int option = Integer.parseInt(tutorium) - 1;
			tutorium = tutoriums.get(option);
		}
		output.println("Tutorium = " + tutorium);

		output.println("Tragen Sie einzeln Namen ein. Sind Sie fertig, geben Sie 'X' ein.");

		int counter = 0;
		while (true) {
			output.print("Vorname : ");
			String surname = scanner.nextLine();
			if (surname.equals("X"))
				break;
			output.print("Nachname : ");
			String name = scanner.nextLine();
			Core.database.addStudent(name, surname, tutorium, date);
			output.println(surname + " " + name + " eingetragen!");
			++counter;
		}
		output.println("Es wurden " + counter + " Personen eingetragen!");
	}

	private void showStatistic() {

		output.println("Auflistung");
		List<TutOrgData> data = Core.database.getAll();
		for (TutOrgData tutOrgData : data) {
			output.print(tutOrgData.getSurname() + "\t" + tutOrgData.getName()
					+ "\t" + tutOrgData.getTutor() + "\t"
					+ DATE_FORMAT.format(tutOrgData.getDate())
					+ System.getProperty("line.separator"));
		}
	}

	public void finish() {
		scanner.close();
	}

	@Override
	protected void finalize() throws Throwable {
		this.scanner.close();
	}

}
