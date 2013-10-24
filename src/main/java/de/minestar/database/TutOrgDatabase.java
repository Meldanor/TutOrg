package de.minestar.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TutOrgDatabase {

    private static final String ALL_DATA = "allData";
    private static final String ALL_STUDENT_NAME = "allStudentName";
    private static final String ALL_TUTORIUM = "allTutorium";
    private static final String STUDENT_BY_NAME = "studentByName";
    private static final String ADD_STUDENT = "addStudent";
    private static final String ALL_DATES = "allWeeks";

    private Database database;

    public TutOrgDatabase() {
        init();
    }

    private void init() {
        database = new SQLiteDatabase("data");
        database.createStructureIfNeeded(getClass().getResourceAsStream("/structure.sql"));
        this.prepareStatements();
    }

    // Date to text conversion because of the sqllite datase, which does not
    // support any date datatypes
    private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

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
            this.database.prepareStatement(ADD_STUDENT, "INSERT INTO students (name, surname, tutorium, date) VALUES (?,?,?,?);");
            this.database.prepareStatement(ALL_TUTORIUM, "SELECT DISTINCT(tutorium) FROM students;");
            this.database.prepareStatement(ALL_STUDENT_NAME, "SELECT DISTINCT(name) FROM students;");
            this.database.prepareStatement(ALL_DATA, "SELECT * FROM students ORDER BY date;");
            this.database.prepareStatement(STUDENT_BY_NAME, "SELECT DISTINCT(surname) FROM students WHERE name = ?;");
            this.database.prepareStatement(ALL_DATES, "SELECT DISTINCT(date) FROM students;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(String name, String surname, String tutorium, Date date) {
        PreparedStatement statement = this.database.getQuery(ADD_STUDENT);
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

        PreparedStatement statement = this.database.getQuery(ALL_TUTORIUM);
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

    public List<String> getAllNames() {

        PreparedStatement statement = this.database.getQuery(ALL_STUDENT_NAME);
        List<String> tutoriums = new ArrayList<String>();
        try {

            ResultSet res = statement.executeQuery();
            while (res.next()) {
                tutoriums.add(res.getString("name"));
            }
            return tutoriums;
        } catch (Exception e) {

            e.printStackTrace();
            return Collections.<String> emptyList();
        }
    }

    public List<TutOrgData> getAll() {
        PreparedStatement statement = this.database.getQuery(ALL_DATA);
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

    public List<String> getSurnameByName(String name) {

        PreparedStatement statement = this.database.getQuery(STUDENT_BY_NAME);
        List<String> tutoriums = new ArrayList<String>();
        try {
            statement.setString(1, name);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                tutoriums.add(res.getString("surname"));
            }
            return tutoriums;
        } catch (Exception e) {

            e.printStackTrace();
            return Collections.<String> emptyList();
        }
    }

    private static final DateTimeFormatter NEW_DATE_FORMAT = DateTimeFormat.mediumDate().withLocale(Locale.GERMAN);

    public int getTotalWeeks() {
        PreparedStatement statement = this.database.getQuery(ALL_DATES);
        List<LocalDate> weeks = new ArrayList<LocalDate>();
        try {
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                String d = res.getString(1);
                weeks.add(NEW_DATE_FORMAT.parseLocalDate(d));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        if (weeks.size() == 0)
            return 0;

        int weekOfYear = 0;

        for (Iterator<LocalDate> iterator = weeks.iterator(); iterator.hasNext();) {
            LocalDate localDate = iterator.next();
            weekOfYear = localDate.getWeekOfWeekyear();
            while (iterator.hasNext()) {
                LocalDate cur = iterator.next();
                if (cur.getWeekOfWeekyear() == weekOfYear) {
                    iterator.remove();
                } else {

                    break;
                }
            }
        }
        return weeks.size();
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
