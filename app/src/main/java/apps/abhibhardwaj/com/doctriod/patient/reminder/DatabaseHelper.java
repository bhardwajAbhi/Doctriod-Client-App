package apps.abhibhardwaj.com.doctriod.patient.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String TAG = "DBHelper";


  private static String DATABASE_NAME = "DOCTROID";
  private static int DATABASE_VERSION = 1;

  private Context context;

  private SQLiteDatabase db;

  //table
  public static final String TABLE_NAME = "PILLREMINDER";
  public static final String KEY_ID = "ID";
  public static final String KEY_NAME = "NAME";
  public static final String KEY_DAYS = "DAYS";
  public static final String KEY_DOSAGE = "DOSAGE";
  public static final String KEY_TIME = "TIME";
  public static final String KEY_MESSAGE = "MESSAGE";

  private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
      "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + KEY_NAME + " TEXT NOT NULL, "
      + KEY_DAYS + " TEXT NOT NULL, "
      + KEY_DOSAGE + " TEXT NOT NULL, "
      + KEY_TIME + " TEXT NOT NULL, "
      + KEY_MESSAGE + " TEXT NOT NULL);";





  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
    Log.d(TAG, "database Created successfully");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
    onCreate(db);
  }

  // -------------------------------------- custom methods for data manipulation ----------------------------------------------

  public long saveReminder(PillReminder reminder)
  {
    db = getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(KEY_NAME, reminder.getName());
    values.put(KEY_DAYS, reminder.getDays());
    values.put(KEY_DOSAGE, reminder.getDosage());
    values.put(KEY_TIME, reminder.getTime());
    values.put(KEY_MESSAGE, reminder.getMessage());

    long rowCount = db.insert(TABLE_NAME, null, values);
    db.close();
    return rowCount;

  }

  public Boolean deleteReminder(long rowID)
  {
    db = getWritableDatabase();
    return db.delete(TABLE_NAME, KEY_ID + "=" + rowID, null) > 0;
  }


  public ArrayList<PillReminder> fetchAllReminders()
  {
    ArrayList<PillReminder> reminderList = new ArrayList<>();

    String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

    db  = getWritableDatabase();
    Cursor cursor = db.rawQuery(SELECT_QUERY, null);

    if (cursor != null)
    {
      if (cursor.moveToFirst())
      {
        do {
          PillReminder reminder = new PillReminder();

          reminder.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
          reminder.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
          reminder.setDays(cursor.getString(cursor.getColumnIndex(KEY_DAYS)));
          reminder.setDosage(cursor.getString(cursor.getColumnIndex(KEY_DOSAGE)));
          reminder.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
          reminder.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));

          reminderList.add(reminder);
        }
        while (cursor.moveToNext());
      }
    }
    return reminderList;
  }

  public PillReminder fetchReminder(long rowID)
  {

    String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "=" + rowID;

    db  = getWritableDatabase();
    Cursor cursor = db.rawQuery(SELECT_QUERY, null);

    PillReminder reminder = new PillReminder();

    if (cursor != null)
    {
      if (cursor.moveToFirst())
      {
        do {

          reminder.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
          reminder.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
          reminder.setDays(cursor.getString(cursor.getColumnIndex(KEY_DAYS)));
          reminder.setDosage(cursor.getString(cursor.getColumnIndex(KEY_DOSAGE)));
          reminder.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
          reminder.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));

        }
        while (cursor.moveToNext());
      }
    }
    return reminder;
  }



  public boolean updateReminder(long rowID, PillReminder reminder)
  {
    db = getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(KEY_NAME, reminder.getName());
    values.put(KEY_DAYS, reminder.getDays());
    values.put(KEY_DOSAGE, reminder.getDosage());
    values.put(KEY_TIME, reminder.getTime());
    values.put(KEY_MESSAGE, reminder.getMessage());

    return db.update(TABLE_NAME, values, KEY_ID + "=" + rowID, null) > 0;
  }

  public void closeDB ()
  {
    db.close();
  }







}

