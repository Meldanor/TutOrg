package de.meldanor;

import java.io.File;
import java.io.InputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.minestar.database.Database;
import de.minestar.database.SQLiteDatabase;

public class DatabaseTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File f = new File("data.db");
		f.delete();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File f = new File("data.db");
		f.delete();
	}

	@Test
	public void test() {
		Database db = new SQLiteDatabase("data");
		InputStream is = getClass().getResourceAsStream("src/test/resources/structure.sql");
		System.out.println(is);
		db.createStructureIfNeeded(is);

	}

}
