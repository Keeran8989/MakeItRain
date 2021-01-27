package com.keeran.makeitrain.weatherdb

import android.content.Context
import android.os.Build
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

@Database(entities = arrayOf(WeatherData::class), version = 1, exportSchema = false)
abstract class WeatherDataRoomDB : RoomDatabase() {
    //val auth = FirebaseAuth.getInstance()
    //abstract fun userDAO(): UserDAO
    abstract fun weatherDataDAO(): WeatherDataDao

    private class UserDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val weatherDataDAO = database.weatherDataDAO()

                    // Delete all content here.
                    weatherDataDAO.deleteAll()

                    // Add sample words.
                }
            }
        }
    }


    companion object {
        @Volatile

        private var INSTANCE2: WeatherDataRoomDB? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun getDatabase(context: Context, scope: CoroutineScope): WeatherDataRoomDB {
            val tempInstance = INSTANCE2
            if (tempInstance != null) {
                return tempInstance
            }

            val masterKeyAlias = "928-4-f998hfkajui37$%^@hdbdh397487bd"
            val dbKey = getCharKey(masterKeyAlias.toCharArray(), context)
            val supportFactory = SupportFactory(getBytes(dbKey))
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    WeatherDataRoomDB::class.java,
                    "WeatherData"
                ).openHelperFactory(supportFactory).build()
                INSTANCE2 = instance
                return instance
            }
        }

        private var INSTANCE: WeatherDataRoomDB? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun getInstance(context: Context): WeatherDataRoomDB {
            val passcode = "928-4-f998hfkajui37\$%^@hdbdh397487bd"

            val dbKey = getCharKey(passcode.toCharArray(), context)
            val supportFactory = SupportFactory(getBytes(dbKey))
            return Room.databaseBuilder(
                context, WeatherDataRoomDB::class.java,
                "WeatherData"
            ).openHelperFactory(supportFactory).build()
        }

    }

}