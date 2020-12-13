package training.journal.viewholders

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.item_active_exercise_element.view.*
import training.journal.R
import training.journal.db.entity.ParameterEntity.Companion.EQUALS_BETTER
import training.journal.db.entity.ParameterEntity.Companion.GREATER_BETTER
import training.journal.db.entity.ParameterEntity.Companion.LESS_BETTER
import training.journal.db.model.ParameterModel
import kotlin.math.abs

class ActiveExerciseViewHolder(
    itemView: View
) : BaseViewHolder<ParameterModel>(itemView) {

    private var name: TextView = itemView.parameter_name
    private var goal: EditText = itemView.parameter_value
    private var value: EditText = itemView.parameter_value_result
    private var unit1: TextView = itemView.parameter_unit
    private var unit2: TextView = itemView.parameter_unit_result
    private var progress: TextView = itemView.parameter_progress

    private var goalValue: Float = 0f

    override fun bind(item: ParameterModel) {
        name.text = item.parameter.name

        val measureUnit = item.measureUnitChoices[item.parameter.measureUnitId.toInt() - 1]
        unit1.text = measureUnit.acronym
        unit2.text = measureUnit.acronym
        goalValue = item.parameter.value
        value.setText("")
        goal.setText(item.parameter.value.toString())
        goal.isEnabled = false
        progress.text = ""

        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val newValue: Float? = s.toString().toFloatOrNull()
                    if (newValue != null) {
                        item.parameter.value = newValue
                        if (goalValue > 0.000001) {
                            val percents = when (item.parameter.resultType) {
                                LESS_BETTER -> {
                                    goalValue * 100f / item.parameter.value
                                }
                                EQUALS_BETTER -> {
                                    100f - abs(item.parameter.value - goalValue) * 100f / goalValue
                                }
                                GREATER_BETTER -> {
                                    item.parameter.value * 100f / goalValue
                                }
                                else -> {
                                    0f
                                }
                            }
                            progress.text = String.format(itemView.resources.getString(R.string.parameter_progress), percents)
                            if (percents > 75f) {
                                progress.setTextColor(Color.GREEN)
                            } else {
                                progress.setTextColor(Color.BLACK)
                            }
                        } else {
                            progress.text = ""
                        }
                    }
                } else {
                    progress.text = ""
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
