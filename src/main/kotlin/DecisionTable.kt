class DecisionTable(val Table: List<List<Boolean>>) {
    companion object {
        data class MdResult(val DecisionTable: DecisionTable, val HeaderCount: Int)

        fun createFromMarkdown(mdLines: List<String>): MdResult {

            var headerCount = 0
            var columnCount: Int? = null
            val decisionTable = mutableListOf<List<Boolean>>()

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

                decisionTable += integerFields.map { (it != 0) }
            }

            return MdResult(DecisionTable(decisionTable), headerCount)
        }
    }

}
