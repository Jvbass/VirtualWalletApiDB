package cl.jpinodev.virtualwalletapidb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transactions)

    @Query("SELECT * FROM transactions WHERE accountId = :accountId OR toAccountId = :accountId")
    suspend fun getTransactionsByAccountId(accountId: Int): List<Transactions>
}