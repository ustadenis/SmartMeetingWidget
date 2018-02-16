package com.ssa.startmeetingwidget;

public final class ContactModel {
    String mName;
    String mEmail;
    String mIcon;

    public ContactModel(String name, String email, String icon) {
        mName = name;
        mEmail = email;
        mIcon = icon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContactModel{");
        sb.append("mName='").append(mName).append('\'');
        sb.append(", mEmail='").append(mEmail).append('\'');
        sb.append(", mIcon='").append(mIcon).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getIcon() {
        return mIcon;
    }
}
