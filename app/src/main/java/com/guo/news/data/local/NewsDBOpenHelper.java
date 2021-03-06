package com.guo.news.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.guo.news.data.local.NewsContract.CommentEntity;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.local.NewsContract.SectionEntity;

/**
 * Created by Administrator on 2016/9/24.
 */
public class NewsDBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DB_NAME = "news.db";


    public NewsDBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_CONTENT_TABLE = "create table " + ContentEntity.TABLE_NAME + "(" +
                ContentEntity._ID + " integer primary key autoincrement," +
                ContentEntity.COLUMN_ID + " text unique not null," +
                ContentEntity.COLUMN_HEADLINE + " text not null," +
                ContentEntity.COLUMN_SECTION_ID + " text not null," +
                ContentEntity.COLUMN_WEB_PUBLICATION_DATE + " integer," +
                ContentEntity.COLUMN_WEB_URL + " text not null," +
                ContentEntity.COLUMN_BYLINE + " text," +
                ContentEntity.COLUMN_BODY + " text not null," +
                ContentEntity.COLUMN_THUMBNAIL + " text," +
                ContentEntity.COLUMN_TRAIL_TEXT + " text not null," +
                ContentEntity.COLUMN_WORD_COUNT + " text," +
                "unique(" + ContentEntity.COLUMN_ID + ") on conflict ignore" +
                ");";
        final String CREATE_COMMENT_TABLE = "create table " + CommentEntity.TABLE_NAME + "(" +
                CommentEntity._ID + " integer primary key autoincrement," +
                CommentEntity.COLUMN_ID + " text unique not null," +
                CommentEntity.COLUMN_CONTENT_ID + " text not null," +
                CommentEntity.COLUMN_CONTENT + " text not null," +
                CommentEntity.COLUMN_ADD_TIME + " integer not null," +
                "unique (" + CommentEntity.COLUMN_ID + ") on conflict ignore" +
                ");";

        final String CREATE_SECTION_TABLE = "create table " + SectionEntity.TABLE_NAME + "(" +
                SectionEntity._ID + " integer primary key," +
                SectionEntity.COLUMN_ID + " text unique not null," +
                SectionEntity.COLUMN_WEB_TITLE + " text not null," +
                SectionEntity.COLUMN_INSTERTED + " integer default 0," +
                "unique("+ SectionEntity.COLUMN_ID +")on conflict ignore" +
                ");";
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_CONTENT_TABLE);
        db.execSQL(CREATE_SECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + ContentEntity.TABLE_NAME + " if exit;");
        db.execSQL("drop table " + SectionEntity.TABLE_NAME + " if exit;");
        db.execSQL("drop table " + CommentEntity.TABLE_NAME + " if exit;");
        onCreate(db);
    }
}
