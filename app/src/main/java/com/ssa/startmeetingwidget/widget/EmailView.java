package com.ssa.startmeetingwidget.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Patterns;

import com.ssa.startmeetingwidget.R;

import java.util.ArrayList;
import java.util.List;

final class EmailView extends AppCompatEditText implements TextWatcher, CurrentEmailListener {

    private List<Email> mEmails = new ArrayList<>();
    private int mLastSelection = 0;

    private CurrentEmailListener mListener = this;

    public EmailView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public EmailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public EmailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setSingleLine(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addTextChangedListener(this);
    }

    private List<Email> getSplitEmails(String text) {
        String[] split = text.split("( )|(,)");
        ArrayList<Email> emails = new ArrayList<>();

        int start = 0;
        int end = 0;

        for (String emailString : split) {
            start = text.indexOf(emailString, end);
            end = start + emailString.length();
            boolean valid = isValidEmail(emailString);

            emails.add(new Email(emailString, valid, start, end));
        }

        return emails;
    }

    private boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mEmails = getSplitEmails(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        updateEditableSpan(editable, getSelectionStart());
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mLastSelection == selStart) return;
        mLastSelection = selStart;
        mListener.onSelectionChanged();
        updateEditableSpan(getEditableText(), selStart);
    }

    @Override
    public void onChangeEditingEmail(String email) {
        // Just stub
    }

    @Override
    public void onSelectionChanged() {
        // Just stub
    }

    void setListener(CurrentEmailListener listener) {
        mListener = listener != null ? listener : this;
    }

    void updateChangingText(String foundEmail) {
        int selStart = getSelectionStart();
        for (Email email : mEmails) {
            if (selStart >= email.mStart && selStart <= email.mEnd) {
                getEditableText().replace(email.mStart, email.mEnd, foundEmail);
                return;
            }
        }
    }

    private void updateEditableSpan(Editable editable, int selStart) {
        removeSpan(editable, 0, editable.length());
        for (Email email : mEmails) {
            boolean needSpan = selStart < email.mStart || selStart > email.mEnd;
            if (needSpan) {
                editable.setSpan(new BackgroundColorSpan(getResources()
                                .getColor(email.mValid ? R.color.colorGray : R.color.colorRed)),
                        email.mStart,
                        email.mEnd,
                        0);
            } else {
                mListener.onChangeEditingEmail(email.mEmail);
            }
        }
    }

    private void removeSpan(Editable editable, int start, int end) {
        BackgroundColorSpan[] spannable = editable.getSpans(start, end, BackgroundColorSpan.class);
        if (spannable != null && spannable.length > 0) {
            for (int i = 0; i < spannable.length; i++) {
                editable.removeSpan(spannable[i]);
            }
        }
    }

    List<String> getValidEmails() {
        List<String> validEmails = new ArrayList<>(mEmails.size());
        for (Email email : mEmails) {
            if (email.mValid) {
                validEmails.add(email.mEmail);
            }
        }
        return validEmails;
    }

    void appendEmail(String email) {
        getEditableText().append(email);
    }

    private static class Email {
        private String mEmail;
        private boolean mValid;
        private int mStart;
        private int mEnd;

        public Email(String email, boolean valid, int start, int end) {
            mEmail = email;
            mValid = valid;
            mStart = start;
            mEnd = end;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Email{");
            sb.append("mEmail='").append(mEmail).append('\'');
            sb.append(", mValid=").append(mValid);
            sb.append(", mStart=").append(mStart);
            sb.append(", mEnd=").append(mEnd);
            sb.append('}');
            return sb.toString();
        }
    }
}
