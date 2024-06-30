package cl.jpinodev.virtualwalletapidb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import retrofit2.Response

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Accounts)

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    suspend fun getAccountsByUserId(userId: Int): List<Accounts>
}