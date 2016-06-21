package app.com.example.android.project1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    //pre associate the views
    @BindView(R.id.title) TextView textViewTitle;
    @BindView(R.id.poster) ImageView imageViewPoster;
    @BindView(R.id.plot) TextView textViewPlot;
    @BindView(R.id.rating) TextView textViewRating;
    @BindView(R.id.release) TextView textViewRelease;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(rootView);
        Intent intent = getActivity().getIntent();
        if (intent != null)
        {
            MovieResults.Movie movie = intent.getParcelableExtra("movie");
            /*TextView textViewTitle = (TextView) rootView.findViewById(R.id.title);
            ImageView imageViewPoster = (ImageView) rootView.findViewById(R.id.poster);
            TextView textViewPlot = (TextView) rootView.findViewById(R.id.plot);
            TextView textViewRating = (TextView) rootView.findViewById(R.id.rating);
            TextView textViewRelease = (TextView) rootView.findViewById(R.id.release);*/

            ButterKnife.setDebug(true);

            textViewTitle.setText(movie.getMovieTitle());

            String url = movie.getMoviePoster();
            Picasso.with(getContext()).load(url).into(imageViewPoster);

            String plot = "Plot" + "\n" + movie.getMoviePlot() + "\n";
            textViewPlot.setText(plot);

            String rating = "Rating: " + movie.getMovieRating() + "/10" + "\n";
            textViewRating.setText(rating);

            String date = "Released: " + movie.getMovieRelease();

            textViewRelease.setText(date);


        }
        return rootView;
    }

}
