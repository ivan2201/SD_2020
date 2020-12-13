package training.journal.viewholders

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import kotlinx.android.synthetic.main.item_exercise_element.view.*
import training.journal.R
import training.journal.db.entity.MeasureUnitEntity
import training.journal.db.entity.ParameterEntity.Companion.EQUALS_BETTER
import training.journal.db.entity.ParameterEntity.Companion.GREATER_BETTER
import training.journal.db.entity.ParameterEntity.Companion.LESS_BETTER
import training.journal.db.model.ParameterModel

class ExerciseElementViewHolder(
    itemView: View
) : BaseViewHolder<ParameterModel>(itemView) {

    private var title: EditText = itemView.title
    private var value: EditText = itemView.value
    private var units: Spinner = itemView.units
    private var type: ImageView = itemView.result_type
    private var delete: ImageView = itemView.delete_parameter_button
    private var item: ParameterModel? = null

    override fun bind(item: ParameterModel) {
        this.item = item
        this.item?.let { newItem ->
            title.setText(newItem.parameter.name)
            value.setText(newItem.parameter.value.toString())
            units.adapter = ArrayAdapter<MeasureUnitEntity>(
                this.itemView.context,
                R.layout.spinner_item,
                newItem.measureUnitChoices
            )
            units.setSelection(newItem.parameter.measureUnitId.toInt() - 1)
            type.setOnCreateContextMenuListener { menu, _, _ ->
                menu?.add(0, GREATER_BETTER, 0, R.string.greater_better)?.setOnMenuItemClickListener {
                    typeMenuItemClicked(it.itemId)
                    true
                }
                menu?.add(0, LESS_BETTER, 0, R.string.less_better)?.setOnMenuItemClickListener {
                    typeMenuItemClicked(it.itemId)
                    true
                }
                menu?.add(0, EQUALS_BETTER, 0, R.string.equals_better)?.setOnMenuItemClickListener {
                    typeMenuItemClicked(it.itemId)
                    true
                }
            }
            typeMenuItemClicked(newItem.parameter.resultType)
            type.setOnClickListener {
                it.showContextMenu()
            }
            title.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty()) {
                        newItem.parameter.name = s.toString()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            value.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty()) {
                        val newValue: Float? = s.toString().toFloatOrNull()
                        if (newValue != null) {
                            newItem.parameter.value = newValue
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            units.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    newItem.parameter.measureUnitId = position.toLong() + 1
                }
            }
        }
    }

    fun setClickListener(onDeleteClick: (View) -> Unit) {
        delete.setOnClickListener(onDeleteClick)
    }

    private fun typeMenuItemClicked(id: Int) {
        item?.parameter?.resultType = id
        when (id) {
            GREATER_BETTER -> {
                type.setImageResource(R.drawable.ic_arrow_up)
                type.rotation = 0f
            }
            LESS_BETTER -> {
                type.setImageResource(R.drawable.ic_arrow_up)
                type.rotation = 180f
            }
            EQUALS_BETTER -> {
                type.setImageResource(R.drawable.equalsign)
                type.rotation = 0f
            }
        }
    }
}
