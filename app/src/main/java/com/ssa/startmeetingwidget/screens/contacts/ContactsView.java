package com.ssa.startmeetingwidget.screens.contacts;

import android.content.Context;

import com.ssa.startmeetingwidget.ContactModel;

import java.util.List;

public interface ContactsView {
    void onContactsResolved(List<ContactModel> cont);
    Context getContext();
}
