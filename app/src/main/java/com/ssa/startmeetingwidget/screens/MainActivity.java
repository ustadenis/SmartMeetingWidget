package com.ssa.startmeetingwidget.screens;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ssa.startmeetingwidget.R;
import com.ssa.startmeetingwidget.common.BasePermissionActivity;
import com.ssa.startmeetingwidget.screens.contacts.ContactsActivity;
import com.ssa.startmeetingwidget.widget.EmailViewWithPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BasePermissionActivity {

    private final static int REQUEST_CONTACT_CODE = 101;

    @BindView(R.id.email_view)
    EmailViewWithPopup mEmailView;
    @BindView(R.id.email_list)
    ListView mEmailList;

    ArrayAdapter<String> mAdapter;

    @Override
    protected String[] getDesiredPermissions() {
        return new String[] {Manifest.permission.READ_CONTACTS};
    }

    @Override
    protected void onPermissionDenied() {}

    @Override
    protected void onPermissionGranted() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mEmailView.setOnClickFindUser(view -> startActivityForResult(ContactsActivity.createIntent(MainActivity.this), REQUEST_CONTACT_CODE));
    }

    @OnClick(R.id.get_valid_emails_button)
    void onClickGetValidEmails() {
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mEmailView.getValidEmails());
        mEmailList.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CONTACT_CODE: {
                String email = data.getStringExtra(ContactsActivity.EXTRA_EMAIL);
                mEmailView.appendEmail(email);
                break;
            }
        }
    }
}
