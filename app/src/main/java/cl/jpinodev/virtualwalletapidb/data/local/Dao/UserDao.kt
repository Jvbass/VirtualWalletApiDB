package cl.jpinodev.virtualwalletapidb.data.local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: Users)

    @Query("SELECT * FROM users Where id = :userId")
    suspend fun getUserById(userId: Int): Users?

}