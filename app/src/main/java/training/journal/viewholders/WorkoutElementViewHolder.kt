package training.journal.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_workout_element.view.*
import training.journal.items.WorkoutItem

class WorkoutElementViewHolder(
    itemView: View
) : BaseViewHolder<WorkoutItem>(itemView) {

    private var icon: ImageView = itemView.icon
    private var title: TextView = itemView.title
    private var description: TextView = itemView.description

    override fun bind(item: WorkoutItem) {
        icon.setImageResource(item.iconId.toInt())
        title.text = item.title
        description.text = item.description
    }
}