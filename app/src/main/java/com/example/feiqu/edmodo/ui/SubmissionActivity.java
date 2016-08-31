package com.example.feiqu.edmodo.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feiqu.edmodo.R;
import com.squareup.picasso.Picasso;

public class SubmissionActivity extends AppCompatActivity {
    public static final String EXTRA_AVATAR = "creator_avatars_large";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_SUBMITTED_AT = "submitted_at";
    public static final String EXTRA_CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        Intent intent = getIntent();
        String title = intent.getStringExtra(AssignmentActivity.EXTRA_TITLE);
        String avatar = intent.getStringExtra(EXTRA_AVATAR);
        String name = intent.getStringExtra(EXTRA_NAME);
        String submittedAt = intent.getStringExtra(EXTRA_SUBMITTED_AT);
        String content = intent.getStringExtra(EXTRA_CONTENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(title);

        ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);
        Picasso.with(this)
                .load(avatar)
                .into(profilePic);

        TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(name);

        TextView submittedAtView = (TextView) findViewById(R.id.submitted_at);
        submittedAtView.setText(submittedAt);

        TextView contentView = (TextView) findViewById(R.id.content);
        contentView.setText(content);
    }
}
