package ca.spacek.gkdd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryWordDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "dictionarywordtable.db";
	private static final int DATABASE_VERSION = 1;
	
	public DictionaryWordDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		DictionaryWordTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		DictionaryWordTable.onUpgrade(database, oldVersion, newVersion);
	}
}
