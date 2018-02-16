package com.ssa.startmeetingwidget.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ssa.startmeetingwidget.ContactModel;
import com.ssa.startmeetingwidget.R;
import com.ssa.startmeetingwidget.SearchContactsAdapter;
import com.ssa.startmeetingwidget.common.ContactResolver;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public final class EmailViewWithPopup extends RelativeLayout implements CurrentEmailListener, View.OnClickListener {

    private View mRootView;

    @BindView(R.id.search_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_card)
    CardView mSearchCardView;
    @BindView(R.id.email_edit_text)
    EmailView mEmailView;

    private CompositeDisposable mDisposables = new CompositeDisposable();
    private Disposable mSearchContactDisposable = Disposables.disposed();

    private SearchContactsAdapter mContactsAdapter;

    private OnClickListener mOnClickFindUser;

    public EmailViewWithPopup(Context context) {
        super(context);
        init(context, null, 0);
    }

    public EmailViewWithPopup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public EmailViewWithPopup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmailViewWithPopup(Context context, AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mRootView = inflate(context, R.layout.layout_email_edit_text, this);
        ButterKnife.bind(this, mRootView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEmailView.setListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mContactsAdapter = new SearchContactsAdapter(getContext(), Collections.emptyList());
        mRecyclerView.setAdapter(mContactsAdapter);
        mContactsAdapter.setOnClickListener(this);
    }

    @Override
    public void onChangeEditingEmail(String email) {
        mSearchContactDisposable.dispose();
        mSearchContactDisposable = ContactResolver.findContactsByEmail(getContext(), email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateSearchResults,
                throwable -> Log.e("Error", throwable.getMessage()));
    }

    @Override
    public void onSelectionChanged() {
        updateSearchResults(Collections.emptyList());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mSearchContactDisposable.dispose();
        mDisposables.dispose();
    }

    private void updateSearchResults(List<ContactModel> contacts) {
        mSearchCardView.setVisibility(contacts.isEmpty() ? GONE : VISIBLE);
        mContactsAdapter.update(contacts);
    }

    @Override
    public void onClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        if (position != -1) {
            ContactModel contactModel = mContactsAdapter.getItem(position);
            mEmailView.updateChangingText(contactModel.getEmail());
        }
    }

    @OnClick(R.id.find_user)
    void onClickFindUser(View view) {
        if (mOnClickFindUser != null) {
            mOnClickFindUser.onClick(view);
        }
    }

    public void setOnClickFindUser(OnClickListener onClickFindUser) {
        mOnClickFindUser = onClickFindUser;
    }

    public List<String> getValidEmails() {
        return mEmailView.getValidEmails();
    }

    public void appendEmail(String email) {
        mEmailView.appendEmail(email);
    }
}
