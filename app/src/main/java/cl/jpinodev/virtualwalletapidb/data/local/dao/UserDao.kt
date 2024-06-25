package cl.jpinodev.virtualwalletapidb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): Users?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): Users?
}