package com.guo.news.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.model.CommentModel;
import com.guo.news.data.remote.ResultTransformer;
import com.guo.news.data.remote.ServiceHost;
import com.guo.news.ui.adapter.CommentAdapter;
import com.guo.news.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String KEY_CONTENT_ID = "contentId";
    private static final int NEWS_LOADER = 100;
    private static final int COMMENTS_LOADER = 200;
    private static final String TAG = NewsActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.news_image)
    ImageView news_image;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.byline)
    TextView byline;
    @Bind(R.id.body)
    TextView body;
    @Bind(R.id.add_comment)
    FloatingActionButton add_comment;
    @Bind(R.id.comment_list)
    RecyclerView comment_list;

    private String mContentId;
    private CommentAdapter mCommentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        mContentId = getIntent().getStringExtra(KEY_CONTENT_ID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        comment_list.setLayoutManager(new LinearLayoutManager(this));
        mCommentAdapter = new CommentAdapter(this, null);
        comment_list.setAdapter(mCommentAdapter);


        if (mContentId != null) {
            getSupportLoaderManager().initLoader(NEWS_LOADER, null, this);
            getSupportLoaderManager().initLoader(COMMENTS_LOADER, null, this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NEWS_LOADER:
                return new CursorLoader(this, ContentEntity.buildContentWithIdUri(mContentId),
                        null,
                        null,
                        null,
                        null);
            case COMMENTS_LOADER:
                return new CursorLoader(this,
                        NewsContract.CommentEntity.buildWithContentIDUrl(mContentId),
                        null,
                        null,
                        null,
                        null);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case NEWS_LOADER:
                if (data.moveToFirst()) {
                    title.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_HEADLINE)));
                    date.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE)));
                    byline.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_BYLINE)));
                    body.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_BODY)));
                    Picasso.with(this)
                            .load(data.getString(data.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL)))
                            .into(news_image);
//                    Palette palette = Palette.from(((BitmapDrawable) news_image.getDrawable()).getBitmap())
//                            .generate();
//                    Palette.Swatch vibrant = palette.getVibrantSwatch();
//                    if (vibrant != null) {
//                        toolbar.setBackgroundColor(vibrant.getRgb());
//                        toolbar.setTitleTextColor(vibrant.getTitleTextColor());
//                    }


                }
                break;
            case COMMENTS_LOADER:
                if (data == null || data.getCount() == 0) {
                    ServiceHost.getService().getCommentList(mContentId, 1)
                            .subscribeOn(Schedulers.io())
                            .map(new ResultTransformer<List<CommentModel>>())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<CommentModel>>() {
                                @Override
                                public void call(List<CommentModel> commentModels) {
                                    if (commentModels == null) {
                                        Log.d(TAG, "get comment fail");
                                    }
                                    Utility.insertComments(getApplicationContext(), commentModels);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            });
                } else {
                    mCommentAdapter.setCursor(data);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCommentAdapter.setCursor(null);
    }

    @OnClick(R.id.add_comment)
    public void click() {
        Intent intent = new Intent(NewsActivity.this, CommentActivity.class);
        intent.putExtra(CommentActivity.KEY_CONTENT_ID, mContentId);
        startActivity(intent);
    }
}