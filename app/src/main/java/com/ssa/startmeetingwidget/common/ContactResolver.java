package com.ssa.startmeetingwidget.common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.ssa.startmeetingwidget.ContactModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class ContactResolver {
    public static Single<List<ContactModel>> findContactsByEmail(Context context, String email) {
        return Single.create(e -> {
            List<ContactModel> emailList = new ArrayList<>();

            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            if (cur != null && cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String photo = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                    Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId +
                                    " AND " + ContactsContract.CommonDataKinds.Email.DATA + " LIKE '%" + email + "%'",
                            null,
                            null);

                    if (emails != null) {
                        while (emails.moveToNext()) {
                            String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            emailList.add(new ContactModel(name, emailAddress, photo));
                        }
                        emails.close();
                    }
                }
                cur.close();
            }

            e.onSuccess(emailList);
        });
    }
}
