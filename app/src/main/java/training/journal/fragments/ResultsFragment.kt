package training.journal.fragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_results.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import training.journal.R
import training.journal.db.entity.DoneExerciseEntity
import training.journal.db.entity.ExerciseEntity
import training.journal.db.entity.MeasureUnitEntity
import training.journal.db.entity.ParameterResultEntity
import training.journal.db.model.ParameterResultModel
import training.journal.items.ItemsList
import training.journal.repository.CurrentUserRepository
import training.journal.utils.recycler.adapters.BaseListAdapter
import training.journal.viewholders.ResultViewHolder

class ResultsFragment : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private var exerciseButton: Button? = null

    private var map: MutableMap<ExerciseEntity, MutableList<ParameterResultEntity>> = mutableMapOf()
    private var doneExercisesList: MutableList<DoneExerciseEntity>? = null
    private var elements: ItemsList<ParameterResultModel> = ItemsList(mutableListOf())

    private var measureUnitChoices: MutableList<MeasureUnitEntity>? = null

    private var userId: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = elements_list
        exerciseButton = exercise_button

        val activeExerciseAdapter = BaseListAdapter(
            holderType = ResultViewHolder::class,
            layoutId = R.layout.item_result,
            dataSource = elements,
            onClick = {}
        )
        recyclerView?.adapter = activeExerciseAdapter
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

        GlobalScope.launch(Dispatchers.IO) {
            database?.let { appDatabase ->
                val email = CurrentUserRepository.currentUser.value?.email
                val user = appDatabase.userDao().getByEmail(email!!)
                userId = user.id

                doneExercisesList = appDatabase.doneExerciseDao().getAllByUser(userId!!).toMutableList()

                for (doneExercise in doneExercisesList!!) {
                    val exercise = appDatabase.exerciseDao().getById(doneExercise.exerciseId)
                    if (!map.containsKey(exercise)) {
                        map[exercise] = mutableListOf()
                    }
                    val list = map[exercise]
                    list!!.addAll(appDatabase.parameterResultDao().getAllByDoneExerciseId(doneExercise.id))
                }
                measureUnitChoices = appDatabase.measureUnitDao().getAll().toMutableList()
            }
            withContext(Dispatchers.Main) {
                exerciseButton?.setOnClickListener {
                    it.showContextMenu()
                }
                registerForContextMenu(exerciseButton!!)
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == exercise_button.id) {
            for (exercise in map.keys) {
                menu.add(0, exercise.id.toInt(), 0, exercise.name).setOnMenuItemClickListener {
                    loadExerciseResult(exercise.id)
                    true
                }
            }
        }
    }

    private fun loadExerciseResult(exerciseId: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val exercise = database?.exerciseDao()?.getById(exerciseId)!!
            val resultsList = map[exercise]
            val parametersList = database?.exerciseParameterDao()?.getParametersForExercise(exerciseId)!!
            val parameterModelList = parametersList.map {
                ParameterResultModel(
                    it,
                    resultsList!!.filter { result -> result.parameterId == it.id }.toMutableList(),
                    measureUnitChoices!!
                )
            }.toMutableList()
            withContext(Dispatchers.Main) {
                exerciseButton?.text = exercise.name
                elements.setData(parameterModelList)
            }
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_results
}
