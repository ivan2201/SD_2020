package training.journal.db.model

import training.journal.db.entity.MeasureUnitEntity
import training.journal.db.entity.ParameterEntity
import training.journal.db.entity.ParameterResultEntity

data class ParameterResultModel(
    var parameter: ParameterEntity,
    var resultsParameterList: MutableList<ParameterResultEntity>,
    var measureUnitChoices: MutableList<MeasureUnitEntity>
)
