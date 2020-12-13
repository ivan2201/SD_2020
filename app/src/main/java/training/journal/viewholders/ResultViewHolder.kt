package training.journal.viewholders

import android.view.View
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.item_result.view.*
import training.journal.db.model.ParameterResultModel

class ResultViewHolder(
        itemView: View
) : BaseViewHolder<ParameterResultModel>(itemView) {

    private var graphTitle: TextView = itemView.graph_title
    private var graph: GraphView = itemView.graph_layout

    override fun bind(item: ParameterResultModel) {
        graph.series.clear()
        val unitId = item.parameter.measureUnitId
        val unitText = item.measureUnitChoices[unitId.toInt() - 1]
        graphTitle.text = String.format("%s (%s)", item.parameter.name, unitText)
        var x = 1.0
        val data = item.resultsParameterList.map {
            println(it.value)
            x += 1.0
            DataPoint(x, it.value.toDouble())
        }.toTypedArray()
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(data)
        graph.addSeries(series)
    }
}
