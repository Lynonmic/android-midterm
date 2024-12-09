package vn.edu.student.leyennhi_dh52113344.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vn.edu.student.leyennhi_dh52113344.model.NhanVien;
import vn.edu.student.leyennhi_dh52113344.model.PhongBan;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QlyNhanVien";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PHONGBAN = "phongban";
    private static final String TABLE_NHANVIEN = "nhanvien";

    private static final String KEY_PHONGBAN_ID = "maphongban";
    private static final String KEY_PHONGBAN_NAME = "tenphongban";

    private static final String KEY_NHANVIEN_ID = "manv";
    private static final String KEY_NHANVIEN_NAME = "tennv";
    private static final String KEY_NHANVIEN_PHONGBAN_ID = "maphongban";
    private static final String KEY_NHANVIEN_IMAGE = "image";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_PHONGBAN + "("
                + KEY_PHONGBAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_PHONGBAN_NAME + " TEXT)";
        db.execSQL(CREATE_CATEGORY_TABLE);

 
        String CREATE_NhanVien_TABLE = "CREATE TABLE " + TABLE_NHANVIEN + "("
                + KEY_NHANVIEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NHANVIEN_NAME + " TEXT, "
                + KEY_NHANVIEN_PHONGBAN_ID + " INTEGER, "
                + KEY_NHANVIEN_IMAGE + " TEXT, "
                + "FOREIGN KEY(" + KEY_NHANVIEN_PHONGBAN_ID + ") REFERENCES "
                + TABLE_PHONGBAN + "(" + KEY_PHONGBAN_ID + "))";
        db.execSQL(CREATE_NhanVien_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHANVIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONGBAN);

        onCreate(db);
    }
    public long addCategory(PhongBan phongBan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PHONGBAN_NAME, phongBan.getTenPhongBan());
        return db.insert(TABLE_PHONGBAN, null, values);
    }

    public PhongBan getPhongBanById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHONGBAN,
                new String[]{KEY_PHONGBAN_ID, KEY_PHONGBAN_NAME},
                KEY_PHONGBAN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            PhongBan phongBan = new PhongBan(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PHONGBAN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONGBAN_NAME))
            );
            cursor.close();
            return phongBan;
        }

        return null;
    }

    public List<PhongBan> getAllPhongBan() {
        List<PhongBan> phongBanList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PHONGBAN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PhongBan phongBan = new PhongBan(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PHONGBAN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONGBAN_NAME))
                );
                phongBanList.add(phongBan);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return phongBanList;
    }

    public int updatePhongBan(PhongBan phongBan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PHONGBAN_NAME, phongBan.getTenPhongBan());

        return db.update(TABLE_PHONGBAN, values,
                KEY_PHONGBAN_ID + " = ?",
                new String[]{String.valueOf(phongBan.getMaphongban())});
    }

    public long deletePhongBan(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PHONGBAN, KEY_PHONGBAN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public long addNhanVien(NhanVien nhanVien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NHANVIEN_NAME, nhanVien.getTennv());
        values.put(KEY_NHANVIEN_PHONGBAN_ID, nhanVien.getMaphongban());
        values.put(KEY_NHANVIEN_IMAGE, nhanVien.getHinhanh());
        return db.insert(TABLE_NHANVIEN, null, values);
    }

    public NhanVien getNhanVienById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NHANVIEN,
                new String[]{KEY_NHANVIEN_ID, KEY_NHANVIEN_NAME, KEY_NHANVIEN_PHONGBAN_ID,
                        KEY_NHANVIEN_IMAGE},
                KEY_NHANVIEN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            NhanVien nhanVien = new NhanVien(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_PHONGBAN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_IMAGE))
            );
            cursor.close();
            return nhanVien;
        }

        return null;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> nhanVienList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NHANVIEN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                NhanVien nhanVien = new NhanVien(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_PHONGBAN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NHANVIEN_IMAGE))
                );
                nhanVienList.add(nhanVien);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return nhanVienList;
    }


    public int updateNhanVien(NhanVien nhanVien) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NHANVIEN_NAME, nhanVien.getTennv());
        values.put(KEY_NHANVIEN_PHONGBAN_ID, nhanVien.getMaphongban());
        values.put(KEY_NHANVIEN_IMAGE, nhanVien.getHinhanh());

        return db.update(TABLE_NHANVIEN, values,
                KEY_NHANVIEN_ID + " = ?",
                new String[]{String.valueOf(nhanVien.getManv())});
    }

    public long deleteNhanVien(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NHANVIEN, KEY_NHANVIEN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

}
