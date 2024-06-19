package cl.jpinodev.virtualwalletapidb.data.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: Transactions)

    @Query("SELECT * FROM transactions WHERE accountId = :accountId OR toAccountId = :accountId")
    suspend fun getTransactionsByAccountId(accountId: Int): List<Transactions>
}