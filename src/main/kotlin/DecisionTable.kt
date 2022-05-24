class DecisionTableRow (decisionList: List<Boolean>){
    val evaluated : Boolean = decisionList.last()
    val subExpressions = decisionList.dropLast(1)
}
class DecisionTable(val Rows: List<DecisionTableRow>) {
    companion object {
        data class MdResult(val DecisionTable: DecisionTable, val HeaderCount: Int)

        fun createFromMarkdown(mdLines: List<String>): MdResult {

            var headerCount = 0
            var columnCount: Int? = null
            val decisionTableRows = mutableListOf<DecisionTableRow>()

            for (line in mdLines) {
                val fields = line
                    .split("|")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                when {
                    columnCount == null -> columnCount = fields.count()
                    columnCount != fields.count() -> throw Exception("Markdown table has varying amount of columns")
                }

                val integerFields = fields.map { it.toIntOrNull() }

                if (integerFields.any { it == null }) {
                    headerCount++
                    continue
                }
                else if(integerFields.count() != columnCount)
                    throw Exception("Invalid markdown")

                decisionTableRows += DecisionTableRow(integerFields.map { (it != 0) })
            }

            return MdResult(DecisionTable(decisionTableRows), headerCount)
        }
    }


}
