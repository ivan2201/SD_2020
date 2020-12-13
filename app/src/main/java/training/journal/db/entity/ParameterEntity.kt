package training.journal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MeasureUnitEntity::class,
            parentColumns = ["id"],
            childColumns = ["measureUnitId"]
        )
    ]
)
data class ParameterEntity(
    @ColumnInfo var name: String,
    @ColumnInfo var measureUnitId: Long,
    @ColumnInfo var resultType: Int = GREATER_BETTER,
    @ColumnInfo var value: Float = 0.0f,
    @ColumnInfo var serverId: Long = -1,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) {
    companion object {
        const val GREATER_BETTER = 1
        const val EQUALS_BETTER = 2
        const val LESS_BETTER = 3
    }
}
