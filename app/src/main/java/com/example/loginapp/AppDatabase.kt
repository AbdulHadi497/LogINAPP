package com.example.loginapp

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDatabase? = null
        val DB_NAME = "/hadi/login_db.db"
        val DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "$DB_PATH$DB_NAME"
        ).setJournalMode(JournalMode.TRUNCATE)
            .build()
      /*  @Synchronized
        fun getInstance(ctx: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ctx, AppDatabase::class.java, "$DB_PATH$DB_NAME"
                ).setJournalMode(JournalMode.TRUNCATE)
                    .build()
            }
            return instance as AppDatabase
        }*/
    }
}