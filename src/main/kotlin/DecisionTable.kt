class DecisionTableRow(decisionList: List<Boolean>) {
    val evaluated: Boolean = decisionList.last()
    val subExpressions = decisionList.dropLast(1)

    /**
     * Excludes the boolean at [columnIndex] from this subexpression.
     * It's used to determine the rows for MC/DC,
     * where only [columnIndex] and [evaluated] differ.
     * @param columnIndex Index of condition column, to exclude.
     * @return Subexpression list without the condition at index [columnIndex].
     */
    fun withoutConditionAt(columnIndex: Int): List<Boolean> {
        // a bit verbose, but more readable than:
        //return subExpressions.filterIndexed { i, _ -> i != columnIndex}
        val list = subExpressions.toMutableList()
        list.removeAt(columnIndex)
        return list
    }
}

class DecisionTable(val Rows: List<DecisionTableRow>, val columnCount: Int) {
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
                    if (decisionTableRows.isEmpty()) {
                        headerCount++
                        continue
                    } else
                        throw Exception("Invalid markdown")
                }

                decisionTableRows += DecisionTableRow(integerFields.map { (it != 0) })
            }

            // columnCount will never be null here
            return MdResult(DecisionTable(decisionTableRows, columnCount!!), headerCount)
        }
    }


}
