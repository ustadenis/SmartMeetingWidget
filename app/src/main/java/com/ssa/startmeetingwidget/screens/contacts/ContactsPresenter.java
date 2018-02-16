package com.ssa.startmeetingwidget.screens.contacts;

import android.util.Log;

import com.ssa.startmeetingwidget.common.ContactResolver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class ContactsPresenter implements IContactsPresenter {

    private Disposable mGetContactsDisposable = Disposables.disposed();

    private ContactsView mView;

    public ContactsPresenter(ContactsView view) {
        mView = view;
    }

    @Override
    public void onResolveContacts() {
        mGetContactsDisposable.dispose();
        mGetContactsDisposable = ContactResolver.findContactsByEmail(mView.getContext(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactModels -> mView.onContactsResolved(contactModels),
                        throwable -> Log.e("Error", throwable.getMessage()));
    }

    @Override
    public void onDestroy() {
        mGetContactsDisposable.dispose();
    }
}
