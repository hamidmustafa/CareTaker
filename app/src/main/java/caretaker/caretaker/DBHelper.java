package caretaker.caretaker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
public class DBHelper extends SQLiteOpenHelper  {
    private static String db_name="CareTakerData";
    private static int version=1;
    public static String DATABASE_TABLE_Patient="patientdata";
    public static String DATABASE_TABLE_Doctor="doctordata";


    public static final String FIELD_Patient_ID="id_email";
    public static final String FIELD_EmailPatient="email_patient";
    public static final String FIELD_NamePatient="name_patient";


    public static final String FIELD_Doctor_ID="id_email";
    public static final String FIELD_EmailDoctor="email_doctor";
    public static final String FIELD_NameDoctor="name_doctor";


    public static SQLiteDatabase DBB_Readable;
    public static SQLiteDatabase DB_Writable;
    public static Cursor MyCursor_Patient;
    public static Cursor MyCursor_Doctor;
    public static ArrayList<String> ArrayList_Email_Patient;
    public static ArrayList<String> ArrayList_Email_Doctor;
    public static ArrayList<String> ArrayList_Name_Patient;
    public static ArrayList<String> ArrayList_Name_Doctor;

    private static String Dummy_String_Patient;
    private static String Dummy_String_Doctor;

    private static String Dummy_Name_Patient;
    private static String Dummy_Name_Doctor;






    public DBHelper(Context context)
    {
        super(context, db_name, null, version);
        this.DB_Writable=getWritableDatabase();
        this.DBB_Readable=getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String Create_SQL_Patient=" CREATE TABLE " + DBHelper.DATABASE_TABLE_Patient + "(" + DBHelper.FIELD_Patient_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBHelper.FIELD_EmailPatient
                + " TEXT, " +  DBHelper.FIELD_NamePatient + " TEXT " +  ")";
        String Create_SQL_Doctor=" CREATE TABLE " + DBHelper.DATABASE_TABLE_Doctor + "(" + DBHelper.FIELD_Doctor_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBHelper.FIELD_EmailDoctor
                + " TEXT, " +  DBHelper.FIELD_NameDoctor + " TEXT " +   ")";
        db.execSQL(Create_SQL_Patient);
        db.execSQL(Create_SQL_Doctor);


    }

    public long insertPatientData(ContentValues  contentValues)
    {
        long rowID= DB_Writable.insert(DATABASE_TABLE_Patient, null, contentValues);
        return  rowID;
    }
    public long insertDoctorData(ContentValues  contentValues)
    {
        long rowID= DB_Writable.insert(DATABASE_TABLE_Doctor, null, contentValues);
        return  rowID;
    }
    public  int delPatientData(Uri uri, String selection, String[] selectionArgs)
    {
        int cnt=DB_Writable.delete(DATABASE_TABLE_Patient, selection, selectionArgs);
        return cnt;
    }
    public  int delDoctorData(Uri uri, String selection, String[] selectionArgs)
    {
        int cnt=DB_Writable.delete(DATABASE_TABLE_Doctor, selection, selectionArgs);
        return cnt;
    }

    public  Cursor getAllData_Patient()
    {
        return  DB_Writable.query(DATABASE_TABLE_Patient, new String[]{DBHelper.FIELD_Patient_ID, DBHelper.FIELD_EmailPatient, DBHelper.FIELD_NamePatient}, null,null,null,null, null);
    }
    public  Cursor getAllData_Doctor()
    {
        return  DB_Writable.query(DATABASE_TABLE_Doctor, new String[]{DBHelper.FIELD_Patient_ID, DBHelper.FIELD_EmailPatient, DBHelper.FIELD_NameDoctor}, null,null,null,null, null);
    }
    public static Cursor getCursorData_Patient()
    {
        String[] silentcols = {DBHelper.FIELD_Patient_ID, DBHelper.FIELD_EmailPatient,DBHelper.FIELD_NamePatient };
        MyCursor_Patient = DBB_Readable.query(DBHelper.DATABASE_TABLE_Patient, silentcols, null, null, null, null, null);
        if (MyCursor_Patient!=null)
            MyCursor_Patient.moveToFirst();
        return MyCursor_Patient;
    }
    public static Cursor getCursorData_Doctor()
    {
        String[] silentcols = {DBHelper.FIELD_Doctor_ID, DBHelper.FIELD_EmailDoctor,DBHelper.FIELD_NameDoctor};
        MyCursor_Doctor = DBB_Readable.query(DBHelper.DATABASE_TABLE_Doctor, silentcols, null, null, null, null, null);
        if (MyCursor_Doctor!=null)
            MyCursor_Doctor.moveToFirst();
        return  MyCursor_Doctor;
    }
    public static void LoopingData_Patient()
    {
       ArrayList_Email_Patient = new ArrayList<String>();
        ArrayList_Name_Patient = new ArrayList<String>();


        for(MyCursor_Patient.moveToFirst(); !MyCursor_Patient.isAfterLast(); MyCursor_Patient.moveToNext()) {
            Dummy_String_Patient = MyCursor_Patient.getString(MyCursor_Patient.getColumnIndex(DBHelper.FIELD_EmailPatient));
            Dummy_Name_Patient = MyCursor_Patient.getString(MyCursor_Patient.getColumnIndex(DBHelper.FIELD_NamePatient));


            ArrayList_Email_Patient.add(Dummy_String_Patient);
            ArrayList_Name_Patient.add(Dummy_Name_Patient);


        }
        MyCursor_Patient.close();

        }
    public static void LoopingData_Doctor()
    {
        ArrayList_Email_Doctor = new ArrayList<String>();
        ArrayList_Name_Doctor = new ArrayList<String>();


        for(MyCursor_Doctor.moveToFirst(); !MyCursor_Doctor.isAfterLast(); MyCursor_Doctor.moveToNext()) {
            Dummy_String_Doctor = MyCursor_Doctor.getString(MyCursor_Doctor.getColumnIndex(DBHelper.FIELD_EmailDoctor));
            Dummy_Name_Doctor = MyCursor_Doctor.getString(MyCursor_Doctor.getColumnIndex(DBHelper.FIELD_NameDoctor));

            ArrayList_Email_Doctor.add(Dummy_String_Doctor);
            ArrayList_Name_Doctor.add(Dummy_Name_Doctor);


        }
        MyCursor_Doctor.close();

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
