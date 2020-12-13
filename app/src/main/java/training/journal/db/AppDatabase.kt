package training.journal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import training.journal.db.converter.DateConverter
import training.journal.db.dao.DoneExerciseDao
import training.journal.db.dao.ExerciseDao
import training.journal.db.dao.ExerciseParameterDao
import training.journal.db.dao.ExerciseTypeDao
import training.journal.db.dao.MeasureUnitDao
import training.journal.db.dao.ParameterDao
import training.journal.db.dao.ParameterResultDao
import training.journal.db.dao.UserDao
import training.journal.db.dao.UserWorkoutDao
import training.journal.db.dao.WorkoutDao
import training.journal.db.dao.WorkoutExerciseDao
import training.journal.db.entity.DoneExerciseEntity
import training.journal.db.entity.ExerciseEntity
import training.journal.db.entity.ExerciseParameterEntity
import training.journal.db.entity.ExerciseTypeEntity
import training.journal.db.entity.MeasureUnitEntity
import training.journal.db.entity.ParameterEntity
import training.journal.db.entity.ParameterResultEntity
import training.journal.db.entity.UserEntity
import training.journal.db.entity.UserWorkoutEntity
import training.journal.db.entity.WorkoutEntity
import training.journal.db.entity.WorkoutExerciseEntity
import training.journal.db.generators.InitialDataGenerator

@Database(
    entities = [
        ExerciseEntity::class,
        ExerciseParameterEntity::class,
        ExerciseTypeEntity::class,
        MeasureUnitEntity::class,
        ParameterEntity::class,
        UserEntity::class,
        UserWorkoutEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        DoneExerciseEntity::class,
        ParameterResultEntity::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME: String = "workoutJournalDB"

        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (instance != null) {
                return instance
            }

            return synchronized(this) {
                if (instance != null) {
                    instance
                } else {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            GlobalScope.launch(Dispatchers.IO) {
                                instance?.let {
                                    it.exerciseTypeDao().insert(InitialDataGenerator.getExerciseTypes(context))
                                    it.measureUnitDao().insert(InitialDataGenerator.getMeasureUnits(context))
                                    it.parameterDao().insert(InitialDataGenerator.getParameters(context))
                                }
                            }
                        }
                    })
                        .build()
                    instance
                }
            }
        }
    }

    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseParameterDao(): ExerciseParameterDao
    abstract fun exerciseTypeDao(): ExerciseTypeDao
    abstract fun measureUnitDao(): MeasureUnitDao
    abstract fun parameterDao(): ParameterDao
    abstract fun userDao(): UserDao
    abstract fun userWorkoutDao(): UserWorkoutDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun doneExerciseDao(): DoneExerciseDao
    abstract fun parameterResultDao(): ParameterResultDao
}
