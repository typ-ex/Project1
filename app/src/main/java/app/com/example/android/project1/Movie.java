package app.com.example.android.project1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by typ_ex on 5/24/2016.
 */
public class Movie implements Parcelable
{
    String movieTitle;
    String moviePlot;
    String movieRating;
    String movieRelease;
    String moviePoster;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public void setMoviePlot(String moviePlot) {
        this.moviePlot = moviePlot;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public void setMovieRelease(String movieRelease) {
        this.movieRelease = movieRelease;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public Movie(String title, String plot, String rating, String release, String poster)
    {
        this.movieTitle = title;
        this.moviePlot = plot;
        this.movieRating = rating;
        this.movieRelease = release;
        this.moviePoster = poster;
    }

    private Movie(Parcel in)
    {
        movieTitle = in.readString();
        moviePlot = in.readString();
        movieRating = in.readString();
        movieRelease = in.readString();
        moviePoster = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(movieTitle);
        parcel.writeString(moviePlot);
        parcel.writeString(movieRating);
        parcel.writeString(movieRelease);
        parcel.writeString(moviePoster);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        public Movie[] newArray(int i)
        {
            return new Movie[i];
        }
    };


}
