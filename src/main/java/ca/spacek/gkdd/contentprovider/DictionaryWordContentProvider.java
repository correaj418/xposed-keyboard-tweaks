package ca.spacek.gkdd.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import ca.spacek.gkdd.data.DictionaryWordDatabaseHelper;
import ca.spacek.gkdd.data.DictionaryWordTable;

/**
 * 
 * @author Nick Spacek Lots of code taken from:
 *         http://www.vogella.com/articles/AndroidSQLite/article.html
 * 
 */
public class DictionaryWordContentProvider extends ContentProvider {
	private DictionaryWordDatabaseHelper database;

	private static final int DICTIONARY_WORDS = 10;
	private static final int DICTIONARY_WORD_ID = 20;

	private static final String AUTHORITY = "ca.spacek.gkdd.contentprovider";

	private static final String BASE_PATH = "dictionarywords";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/dictionarywords";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/dictionaryword";

	private static final UriMatcher MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		MATCHER.addURI(AUTHORITY, BASE_PATH, DICTIONARY_WORDS);
		MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", DICTIONARY_WORD_ID);
	}

	@Override
	public boolean onCreate() {
		database = new DictionaryWordDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(DictionaryWordTable.TABLE_DICTIONARY_WORD);

		int uriType = MATCHER.match(uri);
		switch (uriType) {
		case DICTIONARY_WORDS:
			break;
		case DICTIONARY_WORD_ID:
			queryBuilder.appendWhere(DictionaryWordTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContentResolver(), uri);

		return cursor;
	}

    private ContentResolver getContentResolver() {
        Context context = getContext();
        if (context == null) {
            throw new NullPointerException("Context was null, shouldn't happen");
        }
        return context.getContentResolver();
    }

    @Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = MATCHER.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id;
		switch (uriType) {
		case DICTIONARY_WORDS:
			id = sqlDB.insert(DictionaryWordTable.TABLE_DICTIONARY_WORD, null,
					values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = MATCHER.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated;

        switch (uriType) {
		case DICTIONARY_WORDS:
			rowsUpdated = sqlDB.update(
					DictionaryWordTable.TABLE_DICTIONARY_WORD, values,
					selection, selectionArgs);
			break;
		case DICTIONARY_WORD_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(
						DictionaryWordTable.TABLE_DICTIONARY_WORD, values,
						DictionaryWordTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(
						DictionaryWordTable.TABLE_DICTIONARY_WORD, values,
						DictionaryWordTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = MATCHER.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();

        int rowsDeleted;
		switch (uriType) {
		case DICTIONARY_WORDS:
			rowsDeleted = sqlDB.delete(
					DictionaryWordTable.TABLE_DICTIONARY_WORD, selection,
					selectionArgs);
			break;
		case DICTIONARY_WORD_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						DictionaryWordTable.TABLE_DICTIONARY_WORD,
						DictionaryWordTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(
						DictionaryWordTable.TABLE_DICTIONARY_WORD,
						DictionaryWordTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	private void checkColumns(String[] projection) {
		if (projection != null) {
			for (String column : projection) {
				if (!DictionaryWordTable.COLUMNS.contains(column)) {
					throw new IllegalArgumentException(
							"Unknown column in projection: " + column);
				}
			}
		}
	}
}
