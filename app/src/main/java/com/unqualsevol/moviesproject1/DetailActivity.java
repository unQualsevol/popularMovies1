package com.unqualsevol.moviesproject1;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.unqualsevol.moviesproject1.data.MoviesContract;
import com.unqualsevol.moviesproject1.model.Movie;
import com.unqualsevol.moviesproject1.utils.MoviesUtils;
import com.unqualsevol.moviesproject1.utils.NetworkUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.unqualsevol.moviesproject1.model.ApplicationContract.INTENT_EXTRA_MOVIE_DATA;
import static com.unqualsevol.moviesproject1.model.ApplicationContract.INTENT_EXTRA_MOVIE_ID;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {



    public static final String[] MOVIE_PROJECTION = {
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.MovieEntry.COLUMN_POSTER,
            MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
    };

    public static final int INDEX_TITLE = 0;
    public static final int INDEX_ORIGINAL_TITLE = 1;
    public static final int INDEX_POSTER_IMAGE = 2;
    public static final int INDEX_OVERVIEW = 3;
    public static final int INDEX_VOTE_AVERAGE = 4;
    public static final int INDEX_RELEASE_DATE = 5;
    public static final int INDEX_POSTER_PATH = 6;
    public static final int INDEX_MOVIE_ID = 7;

    public static final int ID_MOVIES_LOADER = 499;
    public static final int ID_MOVIES_INSERT_TOKEN = 503;

    //TODO use binding!
    @BindView(R.id.tv_movie_title) TextView mTitleTextView;

    @BindView(R.id.iv_great_poster) ImageView mGreatPosterImageView;

    @BindView(R.id.tv_original_movie_title) TextView mOriginalTitleTextView;

    @BindView(R.id.tv_movie_overview) TextView mOverviewTextView;

    @BindView(R.id.tv_user_rating) TextView mUserRatingTextView;

    @BindView(R.id.rb_movie_rating) RatingBar mUserRatingRatingBar;

    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;

    private Movie mMovie;

    private Bitmap mPoster;

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mGreatPosterImageView.setImageBitmap(bitmap);
            mPoster = bitmap;
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if(intentThatStartedThisActivity.hasExtra(INTENT_EXTRA_MOVIE_DATA)) {
                mMovie = intentThatStartedThisActivity.getParcelableExtra(INTENT_EXTRA_MOVIE_DATA);
                updateView(mMovie, null);
            } else if(intentThatStartedThisActivity.hasExtra(INTENT_EXTRA_MOVIE_ID)) {
                int movieId = intentThatStartedThisActivity.getIntExtra(INTENT_EXTRA_MOVIE_ID, 0);
                Bundle bundle = new Bundle();
                bundle.putInt(INTENT_EXTRA_MOVIE_ID, movieId);
                getSupportLoaderManager().initLoader(ID_MOVIES_LOADER, bundle, this);
            }
        }
    }

    public void onClickAddMovie(final View view) {
        ContentValues contentValues = MoviesUtils.buildMovieContentValues(mMovie, mPoster);

        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                if(uri != null) {
                    Snackbar.make(view, "Movie added to favorites", Snackbar.LENGTH_SHORT)
                            .show();
                    //notify something?
                }
            }
        };
        handler.startInsert(ID_MOVIES_INSERT_TOKEN, null, MoviesContract.MovieEntry.CONTENT_URI, contentValues);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case ID_MOVIES_LOADER:
                int movieId = bundle.getInt(INTENT_EXTRA_MOVIE_ID);
                Uri movieQueryUri = MoviesContract.MovieEntry.buildMovieUriWithId(movieId);

                return new CursorLoader(this,
                        movieQueryUri,
                        MOVIE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        updateView(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void updateView(Movie movie, Bitmap image) {
        mTitleTextView.setText(movie.getTitle());
        if(image != null) {
            mGreatPosterImageView.setImageBitmap(image);
        } else {
            Picasso.with(this)
                    .load(NetworkUtils.buildImageUrl(movie.getPosterPath()))
                    .into(mTarget);
        }
        mOriginalTitleTextView.setText(getString(R.string.detail_original_title) + movie.getOriginalTitle());
        mOverviewTextView.setText(getString(R.string.detail_synopsis) + movie.getOverview());
        mUserRatingRatingBar.setRating(movie.getVoteAverage().floatValue()/2);
        mUserRatingTextView.setText("(" + movie.getVoteAverage() + ")");
        mReleaseDateTextView.setText(getString(R.string.detail_release_date) + movie.getReleaseDate());
    }

    private void updateView(Cursor cursor) {
        mMovie = new Movie();
        mMovie.setTitle(cursor.getString(INDEX_TITLE));
        mMovie.setOriginalTitle(cursor.getString(INDEX_ORIGINAL_TITLE));
        mMovie.setId(cursor.getInt(INDEX_MOVIE_ID));
        mMovie.setOverview(cursor.getString(INDEX_OVERVIEW));
        mMovie.setPosterPath(cursor.getString(INDEX_POSTER_PATH));
        mMovie.setReleaseDate(cursor.getString(INDEX_RELEASE_DATE));
        mMovie.setVoteAverage(new BigDecimal(cursor.getFloat(INDEX_VOTE_AVERAGE)));
        byte[] posterInBytes = cursor.getBlob(INDEX_POSTER_IMAGE);
        mPoster = MoviesUtils.getImage(posterInBytes);
        updateView(mMovie, mPoster);
    }
}
