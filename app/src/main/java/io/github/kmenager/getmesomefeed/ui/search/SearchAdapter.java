package io.github.kmenager.getmesomefeed.ui.search;

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
import butterknife.OnClick;
import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.local.model.Result;
import io.github.kmenager.getmesomefeed.injection.ActivityContext;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Result> mResults;
    private final Context mContext;



    public interface CallbackSearchAdapter {
        void onClickSearchFeed(Result result);
        void onSubscribeFeed(Result result);
        void onUnSubscribeFeed(Result result);
    }

    private CallbackSearchAdapter mCallbackSearchAdapter;

    public void setCallbackSearchAdapter(CallbackSearchAdapter callbackSearchAdapter) {
        mCallbackSearchAdapter = callbackSearchAdapter;
    }

    @Inject
    public SearchAdapter(@ActivityContext Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder) {
            ((SearchViewHolder) holder).bind(mResults.get(position),
                    mCallbackSearchAdapter,
                    mContext);
        }
    }

    @Override
    public int getItemCount() {
        return mResults != null ? mResults.size() : 0;
    }


    public void setResults(List<Result> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    public void updateResult(Result result) {
        for (int i = 0; i < mResults.size(); i++) {
            if (result.feedId.equals(mResults.get(i).feedId)) {
                mResults.set(i, result);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Bind(R.id.icon_feed)
        ImageView mIconFeed;

        @Bind(R.id.title_search)
        TextView mTitleFeed;

        @Bind(R.id.readers_search)
        TextView mReadersFeed;

        @Bind(R.id.icon_save_favorite)
        ImageView mSave;

        private Result mResult;
        private CallbackSearchAdapter mCallbackSearchAdapter;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Result result,
                         CallbackSearchAdapter callbackSearchAdapter,
                         Context context) {
            mResult = result;
            mCallbackSearchAdapter = callbackSearchAdapter;
            mTitleFeed.setText(result.title);
            int reader = result.subscribers;
            String readers = reader + " readers";
            reader = reader / 1000;
            if (reader > 0) {
                readers = reader + "K readers";
            }
            mSave.setImageResource(result.isSaved ? R.drawable.ic_favorite_black_24dp :
                    R.drawable.ic_favorite_border_black_24dp);

            mReadersFeed.setText(readers);
            Picasso.with(context)
                    .load(result.visualUrl)
                    .placeholder(R.drawable.background_cover_protection)
                    .into(mIconFeed);
        }

        @OnClick(R.id.icon_save_favorite)
        public void saveFavorite() {
            if (mCallbackSearchAdapter != null) {
                if (mResult.isSaved)
                    mCallbackSearchAdapter.onUnSubscribeFeed(mResult);
                else
                    mCallbackSearchAdapter.onSubscribeFeed(mResult);
            }
        }

        @Override
        public void onClick(View v) {
            if (mCallbackSearchAdapter != null) {
                mCallbackSearchAdapter.onClickSearchFeed(mResult);
            }
        }
    }
}