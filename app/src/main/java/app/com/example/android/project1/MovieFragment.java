package app.com.example.android.project1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
    private ArrayList<Movie> movies;

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
        adapter = new MovieArrayAdapter(getActivity(), new ArrayList<Movie>());

        //initialize gridview, and pass the adapter to the gridview
        gridView = (GridView) rootView.findViewById(R.id.gridview_layout);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = adapter.getItem(position);
                Intent detailActivity = new Intent(getActivity(), MovieDetailActivity.class);
                detailActivity.putExtra("movie", movie);
                startActivity(detailActivity);
            }
        });

        return rootView;
    }

    private void updateMovies()
    {
        //FetchMovies moviesTask = new FetchMovies();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortPref = prefs.getString(getString(R.string.sort_key), getString(R.string.sort_default));
        //moviesTask.execute(sortPref);


        Retrofit retroFit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDbService.TMDbAPI tmDbAPI = retroFit.create(TMDbService.TMDbAPI.class);

        //passes sort preference and api key into the network call
        Call<Movie> call = tmDbAPI.loadMovies(sortPref, BuildConfig.TMDB_API_KEY);
        //equivalent to onPostExecute of AsyncTask
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movies = response.body().getItems();
                for (Movie movie : movies) {
                    adapter.add(movie);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t)
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

    /*public class FetchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String LOG_TAG = FetchMovies.class.getSimpleName();

        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER = "poster_path";
            final String TMDB_TITLE = "title";
            final String TMDB_PLOT = "overview";
            final String TMDB_RATING = "vote_average";
            final String TMDB_DATE = "release_date";


            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

            ArrayList<Movie> resultStrs = new ArrayList<Movie>();

            for (int i = 0; i < movieArray.length(); i++) {
                String poster;
                String title;
                String overView;
                String rating;
                String date;

                JSONObject JSONmovie = movieArray.getJSONObject(i);

                poster = JSONmovie.getString(TMDB_POSTER);
                title = JSONmovie.getString(TMDB_TITLE);
                overView = JSONmovie.getString(TMDB_PLOT);
                rating = JSONmovie.getString(TMDB_RATING);
                date = JSONmovie.getString(TMDB_DATE);

                Movie movie = new Movie(poster, title, overView, rating, date);

                movie.setMoviePoster(poster);
                movie.setMovieTitle(title);
                movie.setMoviePlot(overView);
                movie.setMovieRating(rating);
                movie.setMovieRelease(date);


                resultStrs.add(movie);
            }
            return resultStrs;
        }

        //runs this first
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0)
            {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortType = sharedPref.getString(getString(R.string.sort_key),
                    getString(R.string.sort_default));

            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(sortType)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error " + e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                    return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        //runs last
        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {
                adapter.clear();
                for (Movie movie : result) {
                    String path = movie.getMoviePoster();
                    String url = "http://image.tmdb.org/t/p/w342/" + path;
                    movie.setMoviePoster(url);
                    adapter.add(movie);
                }
            }
        }
    }*/
}




