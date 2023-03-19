package com.example.cropcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CropDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "crops.db";
    private static final int VERSION = 4;

    private static CropDatabase cropDb;

    /**
     * CropDatabase is a singleton so only allow one instance to exist
     *
     * @param context Application context
     * @return The single database instance
     */
    public static CropDatabase getInstance(Context context) {
        return cropDb == null ? new CropDatabase(context) : cropDb;
    }

    private CropDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class SettingsTable {
        private static final String TABLE = "settings";
        private static final String COL_FROSTDATE = "frostdate";
        private static final String COL_SUNICON = "sunicon";
    }

    private static final class CropTable {
        private static final String TABLE = "crops";
        private static final String COL_NAME = "name";
        private static final String COL_TYPE = "type";
        // Dates are stored as text in YYYY-MM-DD format
        private static final String COL_DAYS_TO_PLANT_BEFORE_FROST = "plantdays";
        private static final String COL_DAYS_UNTIL_GERM = "germdays";
        private static final String COL_DAYS_UNTIL_HARVEST = "harvestdays";
        private static final String COL_SUN = "sun";
        private static final String COL_NOTES = "notes";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create SettingsTable
        db.execSQL("create table " + SettingsTable.TABLE + " ("
                + SettingsTable.COL_FROSTDATE + " text, "
                + SettingsTable.COL_SUNICON + " text)");
        // Create CropTable
        db.execSQL("create table " + CropTable.TABLE + " ("
                + CropTable.COL_NAME + " text, "
                + CropTable.COL_TYPE + " integer, "
                + CropTable.COL_DAYS_TO_PLANT_BEFORE_FROST + " integer, "
                + CropTable.COL_DAYS_UNTIL_GERM + " integer, "
                + CropTable.COL_DAYS_UNTIL_HARVEST + " integer, "
                + CropTable.COL_SUN + " integer, "
                + CropTable.COL_NOTES + " text)");
    }

    /**
     * When database VERSION is updated, clear table
     * @param db Database
     * @param oldVersion Previous database
     * @param newVersion Current database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        clearDatabase(db);
    }

    /**
     * Clear database
     * @param db Database
     */
    public void clearDatabase(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + SettingsTable.TABLE);
        db.execSQL("drop table if exists " + CropTable .TABLE);
        onCreate(db);
    }

    /**
     * Adds a crop to table
     *
     * @param crop Crop to add
     * @return True if successful
     */
    public boolean addCrop(Crop crop) {
        if (cropExists(crop.getName())) return false; // Crop already exists
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CropTable.COL_NAME, crop.getName());
        values.put(CropTable.COL_TYPE, crop.getType().ordinal());
        values.put(CropTable.COL_DAYS_TO_PLANT_BEFORE_FROST, crop.getDaysToPlantBeforeLastFrost());
        values.put(CropTable.COL_DAYS_UNTIL_GERM, crop.getDaysToHarvest());
        values.put(CropTable.COL_DAYS_UNTIL_HARVEST, crop.getDaysToGerm());
        values.put(CropTable.COL_SUN, crop.getSunText());
        values.put(CropTable.COL_NOTES, crop.getNotes());
        long id = db.insert(CropTable.TABLE, null, values);
        return id != -1;
    }

    /**
     * Read all crops
     *
     * @return List containing all crops
     */
    public ArrayList<Crop> readAllCrops() {
        ArrayList<Crop> crops = new ArrayList<>(); // List to store crops
        SQLiteDatabase db = getReadableDatabase();
        // Read all crops from database ordered alphabetically by name
        String query = "select * from " + CropTable.TABLE + " order by "
                + CropTable.COL_NAME + " asc";
        Cursor cursor = db.rawQuery(query, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                crops.add(new Crop(cursor.getString(0), // name
                        Crop.cropType.values()[cursor.getInt(1)], // type
                        cursor.getInt(2), // days to plant before frost
                        cursor.getInt(3), // days until germ
                        cursor.getInt(4), // days until harvest
                        Crop.getSunLevelFromText(cursor.getString(5)), // sun
                        cursor.getString(6)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return crops;
    }

    /**
     *
     * @param search Text to search by
     * @return List containing results
     */
    public ArrayList<Crop> searchCropsByName(String search) {
        ArrayList<Crop> crops = new ArrayList<>(); // List to store crops
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + CropTable.TABLE + " where " + CropTable.COL_NAME
                + " like " + "'%=?%'" + " --case-insensitive";
        Cursor cursor = db.rawQuery(query, new String[]{search});
        if (cursor.moveToFirst()) {
            do {
                crops.add(new Crop(cursor.getString(0), // name
                        Crop.cropType.values()[cursor.getInt(1)], // type
                        cursor.getInt(2), // days to plant before frost
                        cursor.getInt(3), // days until germ
                        cursor.getInt(4), // days until harvest
                        Crop.sunLevel.values()[cursor.getInt(5)], // sun
                        cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return crops;
    }

    /**
     * Delete a Crop based on name
     *
     * @param cropName Crop to delete
     * @return True if deletion is successful
     */
    public boolean deleteCrop(String cropName) {
        if (!cropExists(cropName)) return false; // Crop doesn't exist
        SQLiteDatabase db = getWritableDatabase();
        String query = CropTable.COL_NAME + " = ?";
        db.delete(CropTable.TABLE, query, new String[] {cropName});
        return true;
    }

    /**
     * Checks if a crop exists in the database based on name
     *
     * @param cropName Crop to check
     * @return True if crop exists
     */
    public boolean cropExists(String cropName) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query to get everything from column with matching username
        String query = "select * from " + CropTable.TABLE +
                " where " + CropTable.COL_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cropName});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists; // If cursor moves to something, the crop exists
    }
}
