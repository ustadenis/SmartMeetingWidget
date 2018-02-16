package com.ssa.startmeetingwidget.screens.contacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ssa.startmeetingwidget.ContactModel;
import com.ssa.startmeetingwidget.R;
import com.ssa.startmeetingwidget.SearchContactsAdapter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends AppCompatActivity implements ContactsView, View.OnClickListener {

    public static final String EXTRA_EMAIL = "extra_email";

    @BindView(R.id.email_list)
    RecyclerView mRecyclerView;

    private SearchContactsAdapter mAdapter;

    private ContactsPresenter mContactsPresenter = new ContactsPresenter(this);

    public static Intent createIntent(Context context) {
        return new Intent(context, ContactsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SearchContactsAdapter(this, Collections.emptyList());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);

        mContactsPresenter.onResolveContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContactsPresenter.onDestroy();

    }

    @Override
    public void onContactsResolved(List<ContactModel> contacts) {
        mAdapter.update(contacts);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        if (position != -1) {
            ContactModel contactModel = mAdapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_EMAIL, contactModel.getEmail());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
