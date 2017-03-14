package com.unqualsevol.moviesproject1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unqualsevol.moviesproject1.BuildConfig;
import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.interfaces.DataReceiver;
import com.unqualsevol.moviesproject1.interfaces.OnRefreshCompleteListener;
import com.unqualsevol.moviesproject1.model.DetailsMode;
import com.unqualsevol.moviesproject1.model.Page;
import com.unqualsevol.moviesproject1.model.ReviewsPage;
import com.unqualsevol.moviesproject1.model.Trailer;
import com.unqualsevol.moviesproject1.model.TrailersPage;
import com.unqualsevol.moviesproject1.tasks.FetchMoviesTask;
import com.unqualsevol.moviesproject1.viewholders.DetailItemViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.unqualsevol.moviesproject1.tasks.FetchMoviesTask.FIRST_PAGE;

public class DetailsAdapter extends RecyclerView.Adapter<DetailItemViewHolder> implements DataReceiver<Page> {

    private static final int VIEW_TYPE_TRAILER = 0;
    private static final int VIEW_TYPE_REVIEW = 1;
    private static final int VIEW_TYPE_LABELED_TRAILER = 2;
    private static final int VIEW_TYPE_LABELED_REVIEW = 3;

    private int mCountTrailers = 0;
    private int mCountReviews = 0;
    private String mMovieId;
    private DetailsMode mDetailsMode;

    private TrailersPage mTrailersPage;
    private SparseArray<ReviewsPage> mReviewsPageMap = new SparseArray<>();
    private List<Integer> loadingPages = new ArrayList<>();
    private Set<OnRefreshCompleteListener> listeners = new HashSet<>();

    public DetailsAdapter(int movieId, DetailsMode mode) {
        this.mMovieId = String.valueOf(movieId);
        this.mDetailsMode = mode;
        start(mode);
    }

    public void restart() {
        start(mDetailsMode);
    }

    private void start(DetailsMode mode) {
        switch (mode) {
            case TRAILERS:
                loadTrailers();
                break;
            case REVIEWS:
                loadReviews(FIRST_PAGE);
                break;
            case BOTH:
                loadTrailers();
                loadReviews(FIRST_PAGE);
                break;
        }
    }

    public void registerOnRefreshCompleteListener(OnRefreshCompleteListener listener) {
        listeners.add(listener);
    }

    private void loadTrailers() {
        new FetchMoviesTask(this).execute(FetchMoviesTask.TRAILERS, mMovieId, BuildConfig.themoviedb_api_key);
    }

    private void loadReviews(int page) {
        loadingPages.add(page);
        new FetchMoviesTask(this).execute(FetchMoviesTask.REVIEWS, mMovieId, BuildConfig.themoviedb_api_key, String.valueOf(page));
    }

    @Override
    public DetailItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId;

        switch (viewType) {
            case VIEW_TYPE_TRAILER: {
                layoutId = R.layout.trailer_list_item;
                break;
            }
            case VIEW_TYPE_REVIEW: {
                layoutId = R.layout.review_list_tem;
                break;
            }
            case VIEW_TYPE_LABELED_TRAILER: {
                layoutId = R.layout.labeled_trailer_list_item;
                break;
            }
            case VIEW_TYPE_LABELED_REVIEW: {
                layoutId = R.layout.labeled_review_list_item;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        return new DetailItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailItemViewHolder holder, int position) {
        if (position < mCountTrailers) {
            holder.updateViewHolder(mTrailersPage.getTrailers().get(position));
        } else {
            int reviewPosition = position - mCountTrailers;
            int page = 1 + reviewPosition / 20;
            int posInPage = reviewPosition % 20;
            ReviewsPage reviewsPage = mReviewsPageMap.get(page);
            if (reviewsPage == null) {
                holder.showLoading();
                if (!loadingPages.contains(page)) {
                    loadReviews(page);
                }
            } else {
                holder.showData();
                holder.updateViewHolder(reviewsPage.getReviews().get(posInPage));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mCountTrailers > 0) {
            return VIEW_TYPE_LABELED_TRAILER;
        } else if (position < mCountTrailers) {
            return VIEW_TYPE_TRAILER;
        } else if (position == mCountTrailers) {
            return VIEW_TYPE_LABELED_REVIEW;
        } else {
            return VIEW_TYPE_REVIEW;
        }
    }

    @Override
    public int getItemCount() {
        return mCountTrailers + mCountReviews;
    }

    @Override
    public void setData(Page data) {
        if (data == null) {
            for (OnRefreshCompleteListener listener : listeners) {
                listener.onFailedRefresh();
            }
        } else if (data instanceof ReviewsPage) {
            savePage((ReviewsPage) data);
        } else if (data instanceof TrailersPage) {
            savePage((TrailersPage) data);
        }
        notifyDataSetChanged();
    }

    private void savePage(ReviewsPage reviewsPage) {
        if (reviewsPage != null) {
            mReviewsPageMap.put(reviewsPage.getPage(), reviewsPage);
            loadingPages.remove(Integer.valueOf(reviewsPage.getPage()));
            mCountReviews = reviewsPage.getTotalResults();
        }
    }

    private void savePage(TrailersPage trailersPage) {
        mTrailersPage = trailersPage;
        if (trailersPage != null) {
            mCountTrailers = trailersPage.getTotalResults();
        }
        notifyDataSetChanged();
    }

    public Trailer getTrailer(int index) {
        if (mCountTrailers < index) {
            return null;
        } else {
            return mTrailersPage.getTrailers().get(index);
        }
    }
}
