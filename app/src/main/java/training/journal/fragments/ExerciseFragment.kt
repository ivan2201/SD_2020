package training.journal.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_exercise.*
import kotlinx.android.synthetic.main.item_exercise_element.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import training.journal.R
import training.journal.db.entity.ExerciseEntity
import training.journal.db.entity.ExerciseParameterEntity
import training.journal.db.entity.ExerciseTypeEntity
import training.journal.db.entity.MeasureUnitEntity
import training.journal.db.entity.ParameterEntity
import training.journal.db.model.ParameterModel
import training.journal.items.ItemsList
import training.journal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import training.journal.lifecycle.Page.Companion.WORKOUT_ID_KEY
import training.journal.utils.logger.Logger
import training.journal.utils.recycler.adapters.ParameterListAdapter
import training.journal.viewholders.ExerciseElementViewHolder

class ExerciseFragment : BaseFragment() {

    private var exerciseNameEditText: EditText? = null
    private var exerciseTypeSpinner: Spinner? = null
    private var recyclerView: RecyclerView? = null
    private var addParameterButton: ImageView? = null
    private var removeParameterButton: ImageView? = null
    private var exercise: ExerciseEntity? = null
    private var workoutId: Long? = null
    private var exerciseId: Long? = null
    private var listAdapter: ParameterListAdapter? = null
    private var elements: ItemsList<ParameterModel>? = null
    private var measureUnitChoices: MutableList<MeasureUnitEntity>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = elements_list
        removeParameterButton = delete_parameter_button
        exerciseNameEditText = exercise_name
        exerciseTypeSpinner = exercise_type_spinner

        addParameterButton = add_parameter_button
        addParameterButton?.let { registerForContextMenu(it) }

        GlobalScope.launch(Dispatchers.IO) {
            activity?.let {
                workoutId = (it.intent.extras?.get(WORKOUT_ID_KEY)) as Long
                exerciseId = (it.intent.extras?.get(EXERCISE_ID_KEY)) as Long
            }
            exercise = database?.exerciseDao()?.getById(exerciseId!!)
            val parametersList = database?.exerciseParameterDao()?.getParametersForExercise(exerciseId!!)!!
            measureUnitChoices = database?.measureUnitDao()?.getAll()?.toMutableList()!!
            val exerciseTypeChoices = database?.exerciseTypeDao()?.getAll()?.toMutableList()!!

            val parameterModelList = parametersList.map {
                ParameterModel(it, measureUnitChoices!!)
            }.toMutableList()

            withContext(Dispatchers.Main) {

                exerciseTypeSpinner?.adapter = ArrayAdapter<ExerciseTypeEntity>(
                    requireContext(), R.layout.spinner_item, exerciseTypeChoices
                )
                exerciseTypeSpinner?.setSelection(exercise!!.typeId.toInt() - 1)

                exerciseNameEditText?.setText(exercise?.name)

                elements = ItemsList(parameterModelList)

                listAdapter = ParameterListAdapter(
                    holderType = ExerciseElementViewHolder::class,
                    layoutId = R.layout.item_exercise_element,
                    dataSource = elements!!,
                    onDeleteParameterClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            database?.exerciseParameterDao()?.delete(exerciseId!!, it.parameter.id)
                            withContext(Dispatchers.Main) {
                                elements!!.remove(it)
                            }
                        }
                    }
                )
                recyclerView?.adapter = listAdapter
                recyclerView?.layoutManager = LinearLayoutManager(activity)

                addParameterButton?.setOnClickListener {
                    it.showContextMenu()
                }
            }
        }

        setHasOptionsMenu(true)
    }

    private fun createNewParameter(parameter: ParameterEntity? = null) {
        GlobalScope.launch(Dispatchers.IO) {
            val parameterEntity = parameter?.copy() ?: ParameterEntity("", 1)
            parameterEntity.id = 0
            parameterEntity.id = database?.parameterDao()?.insert(parameterEntity)!!
            database?.exerciseParameterDao()?.insert(ExerciseParameterEntity(
                exerciseId!!,
                parameterEntity.id
            ))
            withContext(Dispatchers.Main) {
                elements?.add(
                    ParameterModel(parameterEntity, measureUnitChoices!!)
                )
            }
        }
    }

    private fun addParameter(parameter: ParameterEntity) {
        createNewParameter(parameter)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == add_parameter_button.id) {
            menu.add(0, 0, 0, v.resources.getString(R.string.create_new)).setOnMenuItemClickListener {
                createNewParameter()
                true
            }
            GlobalScope.launch(Dispatchers.IO) {
                database?.let {
                    val parameters = it.parameterDao().getAll().distinctBy { param -> param.name }
                    withContext(Dispatchers.Main) {
                        for (parameter in parameters) {
                            menu.add(1, parameter.id.toInt(), 0, parameter.name).setOnMenuItemClickListener {
                                addParameter(parameter)
                                true
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Logger.d(this, "onCreateOptionsMenu")
        inflater.inflate(R.menu.apply_reject_menu, menu)
        val saveButton: MenuItem = menu.findItem(R.id.apply_changes)
        saveButton.setOnMenuItemClickListener {
            val exerciseName = exerciseNameEditText!!.text.toString()
            val typeId = exerciseTypeSpinner!!.selectedItemId
            if (exerciseName == "") {
                exerciseNameEditText?.setBackgroundColor(Color.RED)
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    exercise?.name = exerciseName
                    exercise?.typeId = typeId + 1
                    database?.exerciseDao()?.update(exercise!!)

                    listAdapter!!.data.forEach {
                        val parameter = it.parameter
                        database?.parameterDao()?.update(parameter)
                    }

                    withContext(Dispatchers.Main) {
                        router?.goToPrevFragment()
                    }
                }
            }
            true
        }
        val cancelButton: MenuItem = menu.findItem(R.id.reject_changes)
        cancelButton.setOnMenuItemClickListener {
            val exerciseName = exerciseNameEditText?.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                if (exerciseName == "") {
                    database?.exerciseDao()?.delete(exercise!!)
                }
                withContext(Dispatchers.Main) {
                    router?.goToPrevFragment()
                }
            }

            true
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_exercise
}
