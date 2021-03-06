package de.meldanor.tutorg;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleHandler implements InteractionHandler {

    private Scanner scanner;

    private PrintStream output;

    public ConsoleHandler(InputStream input, PrintStream output) {
        this.scanner = new Scanner(input);
        this.output = output;
    }

    @Override
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
    private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+$");

    private void addNewStudent() {
        Date date = askDate();
        if (date == null) {
            return;
        }

        output.println("Tag = " + DATE_FORMAT.format(date));

        String tutorium = askTutorium();
        if (tutorium == null) {
            return;
        }
        output.println("Tutorium = " + tutorium);

        int counter = askStudents(date, tutorium);
        output.println("Es wurden " + counter + " Personen eingetragen!");
    }

    private int askStudents(Date date, String tutorium) {

        output.println("Tragen Sie einzeln Namen ein. Sind Sie fertig, geben Sie 'X' ein.");

        int counter = 0;
        while (true) {
            String name = readName();
            if (name == null)
                break;
            String surname = readSurname(name);
            if (surname == null)
                break;
            Core.database.addStudent(name, surname, tutorium, date);
            output.println(surname + " " + name + " eingetragen!");
            ++counter;
        }
        return counter;
    }

    private String readSurname(String name) {

        List<String> surnames = Core.database.getSurnameByName(name);
        System.out.println(surnames);
        if (surnames.size() != 0) {
            output.println("Meinen Sie vielleicht? (Sonst Name eingeben)");
            int nameOption = 1;
            // print options
            for (String surname : surnames) {
                output.print("(" + nameOption + ") " + surname + " ");
                ++nameOption;
            }
            output.println();

        }
        output.print("Vorname : ");
        String surname = scanner.nextLine();
        if (name.equals("X"))
            return null;
        if (NUMBER_PATTERN.matcher(surname).matches()) {
            int option = Integer.parseInt(surname) - 1;
            surname = surnames.get(option);
        }
        return surname;
    }

    private String readName() {
        output.print("Nachname : ");
        String name = scanner.nextLine();
        if (name.equals("X"))
            return null;

        EditDistance distance = new LevenshteinDistance(name);
        List<String> names = Core.database.getAllNames();
        List<String> similarNames = distance.similarNames(names);

        // Possible matches
        if (!similarNames.isEmpty()) {
            // Name is existing
            if (similarNames.size() == 1 && similarNames.get(0).equals(name)) {
                // Do nothing - we have a 100% match

            } else {
                // Let the user decide, what name is correct
                output.println("Meinten Sie vielleicht einen der folgende Namen? (Leer lassen fuer " + name + ")");
                int nameOption = 1;
                // print options
                for (String string : similarNames) {
                    output.print("(" + nameOption + ") " + string + " ");
                    ++nameOption;
                }
                output.println();

                // Read answer
                try {
                    nameOption = scanner.nextInt();
                    scanner.nextLine();
                    name = similarNames.get(nameOption - 1);
                } catch (InputMismatchException e) {
                    // Not a number
                } catch (Exception e) {
                    // Do nothing
                }

            }
        }
        return name;
    }

    private String askTutorium() {
        output.print("Tutorium: ");
        List<String> tutoriums = Core.database.getAllTutorium();
        if (tutoriums.size() != 0) {
            output.print(" (Zahl fuer vorhandenes, sonst den Namen)");
            int i = 1;
            for (String string : tutoriums) {
                output.print("(" + i + ") " + string + " ");
                ++i;
            }
            output.println();
        }
        String tutorium = scanner.nextLine();
        if (NUMBER_PATTERN.matcher(tutorium).matches()) {
            int option = Integer.parseInt(tutorium) - 1;
            tutorium = tutoriums.get(option);
        }
        return tutorium;
    }

    private Date askDate() {
        Date date = null;
        output.println("Tag eingeben (\"Heute\" fuer Heute, Format: Tag.Monat.Jahr. Beispiel ist 24.03.1991)");
        String day = scanner.nextLine();

        if (day.equalsIgnoreCase("Heute")) {
            date = new Date();
        } else {
            try {
                date = DATE_FORMAT.parse(day);
            } catch (ParseException e) {
                output.println("Wrong format!");
                return null;
            }
        }
        return date;
    }

    private void showStatistic() {

        StatisticHandler handler = new ConsoleStatistics(output);

        output.println("Was moechten Sie wissen?");
        output.println("(1) Alle Daten (2) �ber einen Student (3) �ber ein Tutorium");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1 :
                handler.printAll();
                break;
            case 2 :
                // TODO: Implement
                handler.printStudent("", "");
                break;
            case 3 :
                // TODO: Implement
                handler.printTutorium("");
                break;
            default :
                break;
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
