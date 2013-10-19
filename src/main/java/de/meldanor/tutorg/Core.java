package de.meldanor.tutorg;

import de.minestar.database.TutOrgDatabase;

public class Core {

	public static void main(String[] args) {
		Core core = new Core();
	}

	private TutOrgDatabase database;

	public Core() {
		init();
	}

	private void init() {
		this.database = new TutOrgDatabase();
	}

}
