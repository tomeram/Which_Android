package com.which.data.dao;

import android.content.Context;
import android.database.Cursor;

import com.which.data.db.WhichContract;
import com.which.data.entitties.User;

/**
 * Created by tomeramir on 29/08/2016.
 */
public class UserDao {
    private static final String LOG_TAG = UserDao.class.getSimpleName();

    public static User isLoggedIn(Context context) {
        Cursor cursor = null;
        User res = null;

        try {
            cursor = context.getContentResolver().query(
                    WhichContract.UserEntry.CONTENT_URI,
                    null,
                    WhichContract.UserEntry.COLUMN_ACCESS_TOKEN + "!= NULL",
                    null, null);

            if (cursor != null && cursor.moveToNext()) {
                User user = new User();
                user.fromCursor(cursor);

                res = user;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return res;
    }
}