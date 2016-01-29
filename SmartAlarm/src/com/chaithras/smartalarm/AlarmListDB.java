package com.chaithras.smartalarm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;


public class AlarmListDB extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "smartalarm.db";
	private static final int DATABASE_VERSION = 1;

	public AlarmListDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i("alarmdb", "alarmDB constructor");
	}

	@Override
	public void onCreate(SQLiteDatabase smartAlarmDb) {
		Log.i("alarmdb", "alarmDB Adding tables");



		smartAlarmDb.execSQL("CREATE TABLE IF NOT EXISTS " + "ALARMLIST"
				+ " (" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "HOURS" + " INTEGER,"
				+ "MINUTES " + " INTEGER" + ");");
		Log.i("alarmdb", "alarmDB Adding tables done ");
	}

	
	public void addAlarm(AlarmDescriptor alarmDesc) {
		SQLiteDatabase smartAlarmDb = getWritableDatabase();

		Log.i("alarmdb", "alarmDB Adding alarm:"
				+ alarmDesc.hours + alarmDesc.minutes );
		ContentValues newAlarm = new ContentValues(1);
		newAlarm.put("HOURS", alarmDesc.hours);
		newAlarm.put("MINUTES", alarmDesc.minutes);
		smartAlarmDb.insert("ALARMLIST", null, newAlarm);
	}

	public void editAlarm(AlarmDescriptor alarmDesc) {
		SQLiteDatabase smartAlarmDb = getWritableDatabase();

		Log.i("alarmdb", "alarmDB editing alarm:"
				+ alarmDesc.hours + alarmDesc.minutes );
		ContentValues editAlarm = new ContentValues();
		editAlarm.put("HOURS", alarmDesc.hours);
		editAlarm.put("MINUTES", alarmDesc.minutes);
		editAlarm.put("ID", alarmDesc.id);
		String queryFilter = "ID=" + alarmDesc.id;
		smartAlarmDb.update("ALARMLIST", editAlarm, queryFilter, null);	
	}

	public void deleteAlarm(AlarmDescriptor alarmDesc) {
		SQLiteDatabase smartAlarmDb = getWritableDatabase();

		Log.i("alarmdb", "alarmDB deleting alarm:"
				+ alarmDesc.hours + alarmDesc.minutes );
		String queryFilter = "ID=" + alarmDesc.id;
		smartAlarmDb.delete("ALARMLIST", queryFilter, null);	
	}

	public int getAlarmCount() {
		SQLiteDatabase smartAlarmDb = getWritableDatabase();
		
			Cursor result = smartAlarmDb.rawQuery("select * from ALARMLIST", null);
			return(result.getCount());
	}

	public void getAlarms(AlarmDescriptor alarmDesc[]) {
			SQLiteDatabase smartAlarmDb = getWritableDatabase();
			Integer i=0;
			
				Cursor result = smartAlarmDb.rawQuery("select ID, HOURS, MINUTES from ALARMLIST", null);
				result.moveToFirst();
				while (result.isAfterLast() == false) 
				{
					alarmDesc[i].id  = result.getInt(0);
					alarmDesc[i].hours  = result.getInt(1);
					alarmDesc[i].minutes  = result.getInt(2);
					Log.i("alarmdb", "alarmDB getting alarm:"
							+ alarmDesc[i].hours + ":" + alarmDesc[i].minutes );
					i++;
				    result.moveToNext();
				}
		}
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
	}
	

}
