package cl.jpinodev.virtualwalletapidb.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.jpinodev.virtualwalletapidb.data.local.dao.AccountDao
import cl.jpinodev.virtualwalletapidb.data.local.dao.TransactionDao
import cl.jpinodev.virtualwalletapidb.data.local.dao.UserDao
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users

@Database(
    entities = [Users::class, Accounts::class, Transactions::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AccountDao(): AccountDao
    abstract fun UserDao(): UserDao
    abstract fun TransactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wallet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}