package io.github.kmenager.getmesomefeed.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.injection.ActivityContext;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<ItemView> mItems;
    private String mPlaceHolderUrl;
    private final Context mContext;
    private OnItemClickListener mOnItemClickListener;

    @Inject
    public MainAdapter(@ActivityContext Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mItems.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public void setItems(List<ItemView> items, String placeHolder) {
        mItems = items;
        mPlaceHolderUrl = placeHolder;
        notifyDataSetChanged();
    }

    public List<ItemView> getItems() {
        return mItems;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        @Bind(R.id.image_view_row_main)
        ImageView mImageView;

        @Bind(R.id.title_row_main)
        TextView mTitle;

        private ItemView mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(ItemView item, Context context) {
            mItem = item;
            mTitle.setText(item.getTitle());
            if (item.getUrlImage() != null) {
                Picasso.with(context)
                        .load(item.getUrlImage())
                        .placeholder(R.drawable.background_cover_protection)
                        .into(mImageView);
            } else if (mPlaceHolderUrl != null) {
                Picasso.with(context)
                        .load(mPlaceHolderUrl)
                        .placeholder(R.drawable.background_cover_protection)
                        .into(mImageView);
            } else {
                mImageView.setImageResource(R.drawable.background_cover_protection);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mItem);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ItemView item);
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
