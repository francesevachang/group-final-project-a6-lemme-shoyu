/**
 * Pacy Wu: I wrote the FolderDatabase to build the database in companion object,
 * so there will only be one folder database throughout the app.
 **/

package edu.uw.peihsi5.lemmeshoyu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Create an instance of folder database **/
@Database(entities = arrayOf(Folder::class), version = 1, exportSchema = false)
abstract class FolderDatabase : RoomDatabase() {
    abstract fun getFoldersDao(): FolderDao

    companion object {
        @Volatile
        private var INSTANCE: FolderDatabase? = null

        fun getDatabase(context: Context): FolderDatabase? {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FolderDatabase::class.java, "FolderTable"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance

            }
        }


    }
}