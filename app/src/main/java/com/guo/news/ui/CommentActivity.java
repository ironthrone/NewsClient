package com.guo.news.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.guo.news.R;
import com.guo.news.data.model.CommentModel;
import com.guo.news.data.remote.ResultTransformer;
import com.guo.news.data.remote.ServiceHost;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity {

    public static final String KEY_CONTENT_ID = "contentId";
    private static final String TAG = CommentActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinator;
    @Bind(R.id.comment)
    EditText comment;
    private String mContentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        mContentId = getIntent().getStringExtra(KEY_CONTENT_ID);
        if (mContentId == null) {
            Log.d(TAG, "Not receive contentId");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        if (comment.requestFocus()) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(comment, InputMethodManager.SHOW_IMPLICIT);
//        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            checkThenFinish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        checkThenFinish();
    }


    private void checkThenFinish() {
        if (!TextUtils.isEmpty(comment.getText().toString().trim())) {

            new AlertDialog.Builder(CommentActivity.this)
                    .setMessage("Scratch content has not saved!")
                    .setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Abandon", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
        } else {
            finish();
        }
    }

    @OnClick(R.id.publish)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish:
                String commentStr = comment.getText().toString().trim();
                if (TextUtils.isEmpty(commentStr)) {
                    Toast.makeText(CommentActivity.this, "comment is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                CommentModel commentModel = new CommentModel();
                commentModel.content = commentStr;
                commentModel.news_id = mContentId;

                ServiceHost.getService().addComment(commentModel)
                        .subscribeOn(Schedulers.io())
                        .map(new ResultTransformer<String>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Toast.makeText(CommentActivity.this, s, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                break;
        }
    }
}
