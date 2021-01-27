package com.keeran.makeitrain.userdb

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.keeran.makeitrain.getCharKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.sqlcipher.database.SQLiteDatabase.getBytes
import net.sqlcipher.database.SupportFactory

@Database(entities = arrayOf(MainData::class), version = 1, exportSchema = false)
abstract class MainDataRoomDB : RoomDatabase() {
    abstract fun mainDataDAO(): MainDataDao

    private class UserDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val mainDataDAO = database.mainDataDAO()
                    mainDataDAO.deleteAll()
                }
            }
        }
    }


    companion object {
        @Volatile

        private var INSTANCE2: MainDataRoomDB? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun getDatabase(context: Context, scope: CoroutineScope): MainDataRoomDB {
            val tempInstance = INSTANCE2
            if (tempInstance != null) {
                return tempInstance
            }
            val masterKeyAlias = "8998fhf98j577h07h*&H*&H4h47482874&&^#%"
            val dbKey = getCharKey(masterKeyAlias.toCharArray(), context)
            val supportFactory = SupportFactory(getBytes(dbKey))
            Log.i("CRYPTCheck", dbKey.toString())
            return Room.databaseBuilder(
                context, MainDataRoomDB::class.java,
                "MainData"
            ).openHelperFactory(supportFactory).build()
        }

        private var INSTANCE: MainDataRoomDB? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun getInstance(context: Context): MainDataRoomDB {
            val passcode = "8998fhf98j577h07h*&H*&H4h47482874&&^#%"

            val dbKey = getCharKey(passcode.toCharArray(), context)
            val supportFactory = SupportFactory(getBytes(dbKey))
            return Room.databaseBuilder(
                context, MainDataRoomDB::class.java,
                "MainData"
            ).openHelperFactory(supportFactory).build()
        }
    }

}