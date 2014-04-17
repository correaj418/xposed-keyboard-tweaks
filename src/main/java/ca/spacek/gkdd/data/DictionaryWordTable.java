package ca.spacek.gkdd.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.database.sqlite.SQLiteDatabase;

public class DictionaryWordTable {
	public static final String TABLE_DICTIONARY_WORD = "dictionaryword";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WORD = "word";
	public static final Set<String> COLUMNS;
	static {
		HashSet<String> columns = new HashSet<String>();
		columns.add(COLUMN_ID);
		columns.add(COLUMN_WORD);
		COLUMNS = Collections.unmodifiableSet(columns);
	}
	
	private static final String SQL_CREATE = "create table " +
			TABLE_DICTIONARY_WORD + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_WORD  + " text not null" + ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(SQL_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// handle this gracefully
		database.execSQL("drop table if exists " + TABLE_DICTIONARY_WORD);
		onCreate(database);
	}
}
