package app.com.example.android.project1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {


    public MovieFragment() {
        // Required empty public constructor
    }

    private MovieArrayAdapter adapter;
    private GridView gridView;
    private ArrayList<MovieResults.Movie> movies;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        // Initialize new adapter
        adapter = new MovieArrayAdapter(getActivity(), new ArrayList<MovieResults.Movie>());

        //initialize gridview, and pass the adapter to the gridview
        gridView = (GridView) rootView.findViewById(R.id.gridview_layout);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieResults.Movie movie = adapter.getItem(position);
                Intent detailActivity = new Intent(getActivity(), MovieDetailActivity.class);
                detailActivity.putExtra("movie", movie);
                startActivity(detailActivity);
            }
        });

        return rootView;
    }

    private void updateMovies()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortPref = prefs.getString(getString(R.string.sort_key), getString(R.string.sort_default));


        Retrofit retroFit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDbService.TMDbAPI tmDbAPI = retroFit.create(TMDbService.TMDbAPI.class);

        //passes sort preference and api key into the network call
        Call<MovieResults> call = tmDbAPI.loadMovies(sortPref, BuildConfig.TMDB_API_KEY);
        //equivalent to onPostExecute of AsyncTask
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                movies = response.body().getItems();
                for (MovieResults.Movie movie : movies) {
                    adapter.add(movie);
                }

                Log.i("response",response.raw().toString());
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t)
            {
                System.out.print(call);
            }

        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        updateMovies();
    }
}




