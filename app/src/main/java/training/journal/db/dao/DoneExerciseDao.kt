package training.journal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import training.journal.db.entity.DoneExerciseEntity
import training.journal.db.entity.ParameterEntity

@Dao
interface DoneExerciseDao {
    @Query("SELECT * FROM DoneExerciseEntity")
    fun getAll(): List<DoneExerciseEntity>

    @Query("SELECT * FROM DoneExerciseEntity WHERE userId=:userId")
    fun getAllByUser(userId: Long): List<DoneExerciseEntity>

    @Query("SELECT * FROM DoneExerciseEntity WHERE id = :id")
    fun getById(id: Long): DoneExerciseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(doneExerciseEntity: DoneExerciseEntity): Long

    @Insert
    fun insert(doneExerciseEntityList: List<DoneExerciseEntity>): List<Long>

    @Update
    fun update(doneExerciseEntity: DoneExerciseEntity): Int

    @Update
    fun update(doneExerciseEntity: List<DoneExerciseEntity>): Int

    @Delete
    fun delete(doneExerciseEntity: DoneExerciseEntity): Int
}