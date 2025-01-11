package edu.pmdm.monforte_danielimdbapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.monforte_danielimdbapp.models.Movie;

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="favorites.db";
    private SQLiteDatabase database;
    private final String MOVIES_TABLE_NAME="MOVIES";
    private final String FAVORITES_TABLE_NAME="FAVORITES";
    //Version 1: MOVIES(id,title,image) FAVORITES(userId,movieId)
    //Version 2: Added insertionTime column to FAVORITES
    //Version 3: Added movieDate column to MOVIES
    private static int databaseVersion=3;

    private ContentValues values; //This variable is used to insert and delete in the database

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,databaseVersion);
        database=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableMovies="CREATE TABLE "+MOVIES_TABLE_NAME+" (movieId TEXT PRIMARY KEY, movieTitle TEXT NOT NULL, movieImage TEXT, movieDate TEXT)";
        db.execSQL(createTableMovies);
        String createTableFavorites="CREATE TABLE "+FAVORITES_TABLE_NAME+" (userId TEXT NOT NULL, movieId TEXT NOT NULL, insertionTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,PRIMARY KEY(userId, movieId), FOREIGN KEY(movieId) REFERENCES MOVIES(movieId))";
        db.execSQL(createTableFavorites);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FAVORITES");
        db.execSQL("DROP TABLE IF EXISTS MOVIES");
        onCreate(db);
    }

    public void addMovie(Movie movie){
        values = new ContentValues();
        values.put("movieId", movie.getId());
        values.put("movieTitle", movie.getTitulo());
        values.put("movieImage", movie.getPortada());
        values.put("movieDate", movie.getFecha());
        database.insert(MOVIES_TABLE_NAME,null,values);
    }
    public void addFavorite(String userId, String movieId){
        values = new ContentValues();
        values.put("userId", userId);
        values.put("movieId", movieId);
        database.insert(FAVORITES_TABLE_NAME,null,values);
    }
    public void removeFavorite(String userId, String movieId){
        String condition = "userId = ? AND movieId = ?";
        String conditionArgs[] = { userId, movieId };
        database.delete(FAVORITES_TABLE_NAME, condition,conditionArgs);
    }

    /*
    All of the previous methods about inserting and deleting used to use the execSQL method
    For example: database.execSQL("INSERT INTO "+MOVIES_TABLE_NAME+" VALUES('"+movie.getId()+"','"+movie.getTitulo()+"','"+movie.getPortada()+"','"+movie.getFecha()+"')");
    This, apart from being less intuitive, had a problem: if the movie's title had a ' symbol, the statement would think that the title ended there and this would break the SQL syntax
    Using ContentValues and its methods to insert and delete we can avoid this
     */


    public boolean movieExists(String movieId){
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + MOVIES_TABLE_NAME + " WHERE movieId = ?", new String[]{movieId});
        boolean exists = false;
        if(cursor.moveToFirst()) {
            int count=cursor.getInt(0);
            if(count>0) exists=true;
        }
        cursor.close();
        return exists;
    }
    public boolean movieIsFavorite(String userId, String movieId){
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + FAVORITES_TABLE_NAME + " WHERE userId = ? AND movieId=?", new String[]{userId,movieId});
        boolean isFavorite = false;
        if(cursor.moveToFirst()) {
            int count=cursor.getInt(0);
            if(count>0)isFavorite=true;
        }
        cursor.close();
        return isFavorite;
    }
    public List<Movie> getUserFavorites(String userId) {
        List<Movie> favorites = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT M.movieId, M.movieTitle, M.movieImage, M.movieDate FROM " +MOVIES_TABLE_NAME+" M JOIN "+FAVORITES_TABLE_NAME+" F ON M.movieId=F.movieId WHERE userID LIKE ? ORDER BY F.insertionTime ASC",new String[]{userId});
        while (cursor.moveToNext()) {
            String movieId = cursor.getString(0);
            String movieTitle = cursor.getString(1);
            String movieImage = cursor.getString(2);
            String movieDate = cursor.getString(3);
            favorites.add(new Movie(movieId, movieTitle,movieImage,movieDate));
        }
        cursor.close();
        return favorites;
    }
}
