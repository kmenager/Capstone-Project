package io.github.kmenager.getmesomefeed.ui.menu;


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
import io.github.kmenager.getmesomefeed.data.model.MenuHeader;
import io.github.kmenager.getmesomefeed.data.model.MenuItemView;
import io.github.kmenager.getmesomefeed.data.model.MenuRowItem;
import io.github.kmenager.getmesomefeed.injection.ActivityContext;
import io.github.kmenager.getmesomefeed.util.CircleTransform;
import io.github.kmenager.getmesomefeed.util.Constants;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MenuItemView> mMenuRowItems;

    private MenuAdapterCallback mMenuAdapterCallback;

    public void setMenuAdapterCallback(MenuAdapterCallback menuAdapterCallback) {
        mMenuAdapterCallback = menuAdapterCallback;
    }

    public void setMenuItems(List<MenuItemView> menuItemInterfaces) {
        if (mMenuRowItems != null) {
            int size = getItemCount();
            mMenuRowItems.addAll(menuItemInterfaces);
            notifyItemRangeInserted(size, menuItemInterfaces.size() - 1);
        } else {
            mMenuRowItems = menuItemInterfaces;
            notifyDataSetChanged();
        }
    }

    public void removeMenuItems() {
        if (mMenuRowItems != null) {
            int size = getItemCount();
            if (size > 0) {
                if (mMenuRowItems.get(0) instanceof MenuHeader) {
                    MenuItemView menuItemView = mMenuRowItems.get(0);
                    mMenuRowItems.clear();
                    mMenuRowItems.add(menuItemView);
                    notifyItemRangeRemoved(1, size - 1);
                } else {
                    mMenuRowItems.clear();
                    notifyItemRangeRemoved(0, size - 1);
                }
            }
        }
    }

    public void setHeader(MenuHeader menuHeader) {
        if (mMenuRowItems != null) {
            if (mMenuRowItems.size() > 0) {
                if (mMenuRowItems.get(0) instanceof MenuHeader) {
                    mMenuRowItems.set(0, menuHeader);
                    notifyItemChanged(0);
                } else {
                    mMenuRowItems.add(0, menuHeader);
                    notifyItemInserted(0);
                }
            } else {
                mMenuRowItems.add(menuHeader);
                notifyItemInserted(0);
            }
        }
    }

    public void removeHeader() {
        if (mMenuRowItems != null) {
            if (mMenuRowItems.size() > 0) {
                if (mMenuRowItems.get(0) instanceof MenuHeader) {
                    mMenuRowItems.remove(0);
                    notifyItemRemoved(0);
                }
            }
        }
    }


    public interface MenuAdapterCallback {
        void onItemClicked(MenuRowItem menuItemAdapter);
    }

    private final Context mContext;

    @Inject
    public MenuAdapter(@ActivityContext Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Constants.VIEW_TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.nav_header_main, parent, false);
                return new MenuViewHeaderHolder(view);
            case Constants.VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_menu_feed_subscribe, parent, false);
                return new MenuViewItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MenuViewItemHolder) {
            MenuRowItem menuRowItem = (MenuRowItem) mMenuRowItems.get(position);
            ((MenuViewItemHolder) holder).bind(mContext, menuRowItem, mMenuAdapterCallback);
        } else if (holder instanceof MenuViewHeaderHolder) {
            MenuHeader menuHeader = (MenuHeader) mMenuRowItems.get(position);
            ((MenuViewHeaderHolder) holder).bind(mContext, menuHeader);
        }
    }

    @Override
    public int getItemCount() {
        return mMenuRowItems != null ? mMenuRowItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMenuRowItems != null && mMenuRowItems.size() > position) {
            return mMenuRowItems.get(position).getType();
        }
        return super.getItemViewType(position);
    }


    protected static class MenuViewItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Bind(R.id.menu_icon_feed)
        ImageView mImageView;

        @Bind(R.id.menu_label)
        TextView mLabel;

        private MenuRowItem mMenuRowItem;
        private MenuAdapterCallback mMenuAdapterCallback;

        public MenuViewItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Context context, MenuRowItem menuRowItem, MenuAdapterCallback menuAdapterCallback) {
            mMenuRowItem = menuRowItem;
            mMenuAdapterCallback = menuAdapterCallback;
            mLabel.setText(menuRowItem.title);
            Picasso.with(context)
                    .load(menuRowItem.coverUrl)
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            if (mMenuAdapterCallback != null) {
                mMenuAdapterCallback.onItemClicked(mMenuRowItem);
            }
        }
    }

    protected static class MenuViewHeaderHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.nav_header_name)
        TextView mTvName;

        @Bind(R.id.nav_header_email)
        TextView mTvEmail;

        @Bind(R.id.nav_header_profile)
        ImageView mProfile;

        public MenuViewHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, MenuHeader menuHeader) {
            mTvName.setText(menuHeader.displayName);
            mTvEmail.setText(menuHeader.email);
            Picasso.with(context)
                    .load(menuHeader.urlProfile)
                    .transform(new CircleTransform())
                    .into(mProfile);
        }
    }
}
