package cl.jpinodev.virtualwalletapidb.data.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts

@Dao
interface AccountDao {
    @Insert
    suspend fun insertAccount(account: Accounts)

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    suspend fun getAccountsByUserId(userId: Int): List<Accounts>
}