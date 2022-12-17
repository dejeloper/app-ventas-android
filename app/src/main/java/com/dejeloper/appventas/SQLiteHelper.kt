package com.dejeloper.appventas

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, database_name, null, database_version) {

    companion object {
        private const val database_version = 1
        private const val database_name = "ventas.db"
        private const val table_name_user = "ventas_usuarios"
        private const val ID = "rowid"
        private const val NAME = "name"
        private const val STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblUsuarios = ("CREATE TABLE " + table_name_user + " ("
                + NAME + " TEXT NOT NULL,"
                + STATUS + " INTEGER NOT NULL DEFAULT 1"
                + ");")

        db?.execSQL(createTblUsuarios)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $table_name_user")
        onCreate(db)
    }

    fun insertUser(model: UserModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME, model.name)
        contentValues.put(STATUS, model.status)

        val success = db.insert(table_name_user, null, contentValues)
        db.close()

        return success
    }

    @SuppressLint("Range")
    fun getAllUsers(): ArrayList<UserModel> {
        val modelList: ArrayList<UserModel> = ArrayList()
        val selectQuery = "SELECT rowid, name, status FROM $table_name_user"

        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var status: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("rowid"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                status = cursor.getInt(cursor.getColumnIndex("status"))

                val model = UserModel(id = id, name = name, status = status)
                modelList.add(model)
            } while (cursor.moveToNext())
        }

        return modelList
    }

    fun updateUser(model: UserModel): Int {
        try {
            val db = this.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(NAME, model.name)
            contentValues.put(STATUS, model.status)

            val success = db.update(table_name_user, contentValues, "rowid = " + model.id, null)
            db.close()
            return success
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

    fun deleteUser(id: Int): Int {
        try {
            val db = this.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(ID, id)

            val success = db.delete(table_name_user, "rowid = $id", null)
            db.close()
            return success
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }


}