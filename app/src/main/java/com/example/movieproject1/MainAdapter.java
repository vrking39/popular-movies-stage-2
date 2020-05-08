package com.example.movieproject1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.PostersViewHolder> {

    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    private MovieModel[] mMovies = null;
    private ClickListener mClickListener;

    class PostersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mPosterViewHolder;

        PostersViewHolder(View v){
            super(v);
            mPosterViewHolder = v.findViewById(R.id.iv_list_item_poster);
            mPosterViewHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mClickListener.onClick(clickPosition);
        }
    }

    public MainAdapter(MovieModel[] movies, ClickListener clickListener) {
        mMovies = movies;
        mClickListener = clickListener;

    }

    public MovieModel[] getmMovie() {
        return mMovies;
    }

    @Override
    public PostersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_movies;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new PostersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostersViewHolder holder, int position) {
        Picasso.get()
                .load(IMAGE_URL.concat(mMovies[position].getPoster()))
                .fit()
                .into(holder.mPosterViewHolder);
    }

    @Override
    public int getItemCount() { return mMovies.length; }

    public interface ClickListener {
        void onClick(int position);
    }



}
