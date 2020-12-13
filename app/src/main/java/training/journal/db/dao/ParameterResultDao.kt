package training.journal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import training.journal.db.entity.ParameterResultEntity


@Dao
interface ParameterResultDao {
    @Query("SELECT * FROM ParameterResultEntity")
    fun getAll(): List<ParameterResultEntity>

    @Query("SELECT * FROM ParameterResultEntity WHERE doneExerciseId=:doneExerciseId")
    fun getAllByDoneExerciseId(doneExerciseId: Long): List<ParameterResultEntity>

    @Query("SELECT * FROM ParameterResultEntity WHERE id = :id")
    fun getById(id: Long): ParameterResultEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parameterResultEntity: ParameterResultEntity): Long

    @Insert
    fun insert(parameterResultEntityList: List<ParameterResultEntity>): List<Long>

    @Update
    fun update(parameterResultEntity: ParameterResultEntity): Int

    @Update
    fun update(parameterResultEntityList: List<ParameterResultEntity>): Int

    @Delete
    fun delete(parameterResultEntity: ParameterResultEntity): Int
}