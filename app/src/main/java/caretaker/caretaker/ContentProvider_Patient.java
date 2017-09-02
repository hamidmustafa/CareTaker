package caretaker.caretaker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.sql.SQLException;

/**
 * Created by Fusion on 01-Oct-16.
 */
public class ContentProvider_Patient extends ContentProvider {
    public  static  final String PROVIDER_NAME_DATA_Patient="caretaker.caretaker.patientdata";
    /*  Every provider will be identified by its URI*/
    public static final Uri CONTENT_URI_Patient = Uri.parse("content://" + PROVIDER_NAME_DATA_Patient + "/patientdata" );
    // Constant to indentify the requested operation
    private static  final int LOCATIONS_DATA=1;
    private static final UriMatcher uriMatcherdata;

    static {

        uriMatcherdata= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcherdata.addURI(PROVIDER_NAME_DATA_Patient,"patientdata",LOCATIONS_DATA);
    }
    public DBHelper mDBdata;
    @Override
    public boolean onCreate()
    {

        mDBdata=new DBHelper(getContext()) {
            @Override
            public void onCreate(SQLiteDatabase dbbdata) {
                super.onCreate(dbbdata);
            }
        };

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcherdata.match(uri)==LOCATIONS_DATA)
        {return mDBdata.getAllData_Patient();}
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID_data=mDBdata.insertPatientData(values)    ;
        Uri _uridata=null;
        if (rowID_data>0)
        {_uridata= ContentUris.withAppendedId(CONTENT_URI_Patient, rowID_data);}
        else {
            try
            {throw  new SQLException("Failed to insert:"+ uri)  ;}
            catch (SQLException e)
            {e.printStackTrace();}
        }


        return _uridata;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt4=0;
        cnt4=mDBdata.delPatientData(uri,selection,selectionArgs);
        return cnt4;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
