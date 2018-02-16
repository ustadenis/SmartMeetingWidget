package com.ssa.startmeetingwidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchContactsAdapter extends RecyclerView.Adapter<SearchContactsAdapter.SearchContactsViewHolder> {

    private List<ContactModel> mItems;

    private Context mContext;

    private View.OnClickListener mListener;

    public SearchContactsAdapter(Context context, @NonNull List<ContactModel> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public final SearchContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_contact, parent, false);
        return new SearchContactsViewHolder(view, viewType);
    }

    @Override
    public final void onBindViewHolder(SearchContactsViewHolder holder, int position) {
        ContactModel contact = mItems.get(position);

        holder.mName.setText(contact.getName());
        holder.mEmail.setText(contact.getEmail());

        if (contact.getIcon() != null) {
            Picasso.with(mContext)
                   .load(contact.getIcon())
                   .into(holder.mPhoto);
        } else {
            Picasso.with(mContext)
                   .load(R.drawable.placeholderuser)
                   .into(holder.mPhoto);
        }

        holder.itemView.setOnClickListener(mListener);
    }

    @Override
    public final int getItemCount() {
        return mItems.size();
    }

    public final ContactModel getItem(int position) {
        return mItems.get(position);
    }

    public void update(List<ContactModel> newContacts) {
        mItems = newContacts;
        notifyDataSetChanged();
    }

    public final void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    static class SearchContactsViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.name)
        AppCompatTextView mName;
        @Nullable
        @BindView(R.id.email)
        AppCompatTextView mEmail;
        @Nullable
        @BindView(R.id.photo)
        ImageView mPhoto;

        public SearchContactsViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
