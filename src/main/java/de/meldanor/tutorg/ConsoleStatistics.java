package de.meldanor.tutorg;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import de.minestar.database.TutOrgDatabase.TutOrgData;

public class ConsoleStatistics implements StatisticHandler {

    private PrintStream output;

    public ConsoleStatistics(PrintStream output) {
        this.output = output;
    }

    private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

//    private static final DateTimeFormatter NEW_DATE_FORMAT = DateTimeFormat.mediumDate().withLocale(Locale.GERMAN);

    @Override
    public void printAll() {
        List<TutOrgData> data = Core.database.getAll();
        for (TutOrgData student : data) {
            output.print(student.getName() + "\t");
            output.print(student.getSurname() + "\t");
            output.print(student.getTutor() + "\t");
            output.print(DATE_FORMAT.format(student.getDate()));
            output.println();
        }
    }

    @Override
    public void printTutorium(String tutorium) {
        System.out.println("NOT IMPLEMENTED YET");
    }

    @Override
    public void printStudent(String name, String surname) {
        System.out.println("NOT IMPLEMENTED YET");
    }

}
