package com.example.loginapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("Select ifnull(max(surveyId),0)+1 from User")
    fun getId(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(user: User)
}