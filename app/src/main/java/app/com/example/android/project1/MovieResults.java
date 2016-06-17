package app.com.example.android.project1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by typ_ex on 5/24/2016.
 */

//basic setup of this class was taken from the Udacity Google+ webcast
public class MovieResults {
    public ArrayList<Movie> getItems() {
        return movies;
    }
    @SerializedName("results")
    private ArrayList<Movie> movies;

public static class Movie implements Parcelable {
    @SerializedName("original_title")
    String movieTitle;

    @SerializedName("overview")
    String moviePlot;

    @SerializedName("vote_average")
    String movieRating;

    @SerializedName("release_date")
    String movieRelease;

    @SerializedName("poster_path")
    String moviePoster;

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePlot() {
        return moviePlot;
    }


    public String getMovieRating() {
        return movieRating;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }
    
    private Movie(Parcel in) {
        movieTitle = in.readString();
        moviePlot = in.readString();
        movieRating = in.readString();
        movieRelease = in.readString();
        moviePoster = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(moviePlot);
        parcel.writeString(movieRating);
        parcel.writeString(movieRelease);
        parcel.writeString(moviePoster);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
}
