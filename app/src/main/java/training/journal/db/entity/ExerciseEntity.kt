package training.journal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = ExerciseTypeEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["typeId"]
            )
        ]
)
data class ExerciseEntity(
        @ColumnInfo var name: String,
        @ColumnInfo var description: String?,
        @ColumnInfo var typeId: Long,
        @ColumnInfo var serverId: Long = -1,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
) {
    override fun toString(): String {
        return name
    }
}

