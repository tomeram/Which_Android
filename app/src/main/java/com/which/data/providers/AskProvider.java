package com.which.data.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.which.data.db.WhichContract;
import com.which.data.db.WhichDbHelper;

/**
 * Created by tomeramir on 01/09/2016.
 */
public class AskProvider extends ContentProvider {
    private WhichDbHelper dbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int ASK = 100;
    private static final int ASK_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WhichContract.UserEntry.CONTENT_AUTHORITY;

        matcher.addURI(authority, WhichContract.PATH_ASK, ASK);
        matcher.addURI(authority, WhichContract.PATH_ASK + "/#", ASK_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WhichDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] table, String selection, String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor resCursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case ASK:
                resCursor = db.query(WhichContract.AskEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            case ASK_ID:
                String[] args = new String[1];
                args[0] = "" + uri.getLastPathSegment();

                resCursor = db.query(WhichContract.AskEntry.TABLE_NAME, null,
                        WhichContract.Answer._ID + " = ?", args, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + uri);
        }

        return resCursor;
    }

    @Override
    public String getType(Uri uri) {
        if (uri.compareTo(WhichContract.AskEntry.CONTENT_URI) == 0) {
            return WhichContract.AskEntry.CONTENT_ITEM_TYPE;
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri resUri;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case ASK:
                long id = db.insert(WhichContract.AskEntry.TABLE_NAME, null, contentValues);
                resUri = WhichContract.AskEntry.buildAskUri(id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return resUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (uri.compareTo(WhichContract.AskEntry.CONTENT_URI) == 0) {
            db.beginTransaction();
            int returnCount = 0;

            try {
                long _id;
                for (ContentValues value : values) {
                    _id = db.replace(WhichContract.AskEntry.TABLE_NAME, null, value);
                    if (_id != -1) {
                        returnCount++;
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }


            if (returnCount > 0)

                getContext().getContentResolver().notifyChange(uri, null);

            return returnCount;
        } else {
            return super.bulkInsert(uri, values);
        }
    }
}