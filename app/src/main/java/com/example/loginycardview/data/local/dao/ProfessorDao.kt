package com.example.loginycardview.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loginycardview.data.local.entities.ProfessorEntity

@Dao
interface ProfessorDao {
    @Query("SELECT * FROM professors")
    suspend fun getAllProfessors(): List<ProfessorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfessor(professor: ProfessorEntity)
}
