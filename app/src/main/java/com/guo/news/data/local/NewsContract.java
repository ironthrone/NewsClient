package com.guo.news.data.local;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/9/24.
 */
public class NewsContract {

    public static final String CONTENT_AUTHORITY = "com.guo.news";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SECTION = "section";
    public static final String COMMENT_PATH = "comment";
    public static final String PATH_CONTENT = "content";


    private static String getContentType(String path) {
        return ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + path;
    }

    private static String getContentItemType(String path) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd." + CONTENT_AUTHORITY + "." + path;
    }



    public static class ContentEntity implements BaseColumns {

        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SECTION);
        public static final String CONTENT_TYPE = getContentType(PATH_SECTION);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(PATH_SECTION);

        public static final String TABLE_NAME = "content";


        public static final String COLUMN_ID = "id";
        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_WEB_PUBLICATION_DATE = "webPublicationDate";
        public static final String COLUMN_SECTION_ID = "sectionId";
        public static final String COLUMN_WEB_URL = "webUrl";
        public static final String COLUMN_STANDFIRST = "standfirst";
        public static final String COLUMN_BYLINE = "byline";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_WORD_COUNT = "wordCount";

        public static final String CREATE_NEWS_TABLE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement," +
                COLUMN_ID + " text unique not null," +
                COLUMN_HEADLINE + " text not null," +
                COLUMN_SECTION_ID + "text not null," +
                COLUMN_WEB_PUBLICATION_DATE + " text not null," +
                COLUMN_WEB_URL + " text not null," +
                COLUMN_BYLINE + " text not null," +
                COLUMN_BODY + " integer," +
                COLUMN_WORD_COUNT + " text," +
                COLUMN_THUMBNAIL + " integer not null," +
                COLUMN_STANDFIRST + "text," +
                " on conflict ignore" +
                " foreign key(" + COLUMN_SECTION_ID + ") references " +
                SectionEntity.TABLE_NAME + "(" + SectionEntity._ID + ")" +
                ");";

        public static String getContentId(Uri uri) {
            return uri.getLastPathSegment();
        }


        public static Uri buildContentWithSectionUri(String sectionId){
            return CONTENT_URI.buildUpon().appendPath(sectionId).build();
        }
        public static Uri buildContentWithIdUri(int id){
            return CONTENT_URI.buildUpon().appendPath("item").appendPath(String.valueOf(id)).build();
        }

    }

    public static class SectionEntity implements BaseColumns {
        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CONTENT);

        public static final String CONTENT_TYPE = getContentType(PATH_SECTION);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(PATH_SECTION);

        public static final String TABLE_NAME = "section";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_WEB_TITLE = "webTitle";


        public static final String CREATE_CHANNEL_TABLE = "create table " + TABLE_NAME + "(" +
                _ID + " text primary key," +
                COLUMN_ID + " text unique not null," +
                COLUMN_WEB_TITLE + " text not null," +
                "on conflict ignore" +
                ");";
    }

    public static class CommentEntity implements BaseColumns {
        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, COMMENT_PATH);

        public static final String CONTENT_TYPE = getContentType(COMMENT_PATH);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(COMMENT_PATH);

        public static final String TABLE_NAME = "comment";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CONTENT_ID = "contentId";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_ADD_TIME = "addTime";


        public static final String CREATE_COMMENT_TABLE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement," +
                COLUMN_ID + "integer unique not null" +
                COLUMN_CONTENT_ID + " integer unique not null," +
                COLUMN_CONTENT + " text not null," +
                COLUMN_ADD_TIME + " integer not null," +
                "on conflict ignore " +
                "foreign key(" + COLUMN_CONTENT_ID + ") references " +
                ContentEntity.TABLE_NAME + "(" + ContentEntity._ID + ")" +
                ");";
        public static Uri buildWithContentIDUrl(String contentId){
            return CONTENT_URI.buildUpon().appendPath(contentId).build();
        }

    }

}