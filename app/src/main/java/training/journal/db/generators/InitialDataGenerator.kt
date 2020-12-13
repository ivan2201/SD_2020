package training.journal.db.generators

import android.content.Context
import training.journal.R
import training.journal.db.entity.ExerciseTypeEntity
import training.journal.db.entity.MeasureUnitEntity
import training.journal.db.entity.ParameterEntity

class InitialDataGenerator {
    companion object {

        fun getExerciseTypes(context: Context): List<ExerciseTypeEntity> {
            return listOf(
                ExerciseTypeEntity(context.resources.getString(R.string.strength)),
                ExerciseTypeEntity(context.resources.getString(R.string.dexterity)),
                ExerciseTypeEntity(context.resources.getString(R.string.coordination)),
                ExerciseTypeEntity(context.resources.getString(R.string.endurance)),
                ExerciseTypeEntity(context.resources.getString(R.string.speed)),
                ExerciseTypeEntity(context.resources.getString(R.string.flexibility)),
                ExerciseTypeEntity(context.resources.getString(R.string.concentration)),
                ExerciseTypeEntity(context.resources.getString(R.string.other_type))
            )
        }
        fun getMeasureUnits(context: Context): List<MeasureUnitEntity> {
            return listOf(
                MeasureUnitEntity(context.resources.getString(R.string.mm)),
                MeasureUnitEntity(context.resources.getString(R.string.sm)),
                MeasureUnitEntity(context.resources.getString(R.string.m)),
                MeasureUnitEntity(context.resources.getString(R.string.km)),
                MeasureUnitEntity(context.resources.getString(R.string.ue)),
                MeasureUnitEntity(context.resources.getString(R.string.sec)),
                MeasureUnitEntity(context.resources.getString(R.string.min)),
                MeasureUnitEntity(context.resources.getString(R.string.h)),
                MeasureUnitEntity(context.resources.getString(R.string.kg))
            )
        }
        fun getParameters(context: Context): List<ParameterEntity> {
            return listOf(
                ParameterEntity(context.resources.getString(R.string.time), 6),
                ParameterEntity(context.resources.getString(R.string.distance), 3),
                ParameterEntity(context.resources.getString(R.string.weight), 9)
            )
        }
    }
}