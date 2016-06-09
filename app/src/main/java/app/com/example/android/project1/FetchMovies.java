package app.com.example.android.project1;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by typ_ex on 6/8/2016.
 */
public class FetchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String LOG_TAG = FetchMovies.class.getSimpleName();

    private String sortType;
    private MovieArrayAdapter adapter;

    public FetchMovies(String sortType, MovieArrayAdapter adapter)
    {
        super();
        this.sortType = sortType;
        this.adapter = adapter;
    }

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
}
