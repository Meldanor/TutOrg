package de.meldanor.tutorg;

import de.minestar.database.TutOrgDatabase;

public class Core {

    public static void main(String[] args) {
        new Core();
    }

    public static TutOrgDatabase database;

    public Core() {
        init();
    }

    private void init() {
        database = new TutOrgDatabase();

        InteractionHandler handler = new ConsoleHandler(System.in, System.out);
        handler.run();

    }

}
