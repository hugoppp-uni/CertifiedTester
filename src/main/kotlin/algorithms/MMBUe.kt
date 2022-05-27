package algorithms

import DecisionTable
import DecisionTableRow

class MMBUe : Algorithm {
    override fun run(decisionTable: DecisionTable): Set<Int> {

        val relevantIndices = mutableSetOf<Int>()

        for (i in decisionTable.Rows.indices) {
            for (k in i + 1 until decisionTable.Rows.size) {
                if (decisionTable.Rows[i].evaluated != decisionTable.Rows[k].evaluated &&
                    isNeighbour(decisionTable.Rows[i], decisionTable.Rows[k])
                ) {
                    relevantIndices.add(i)
                    relevantIndices.add(k)
                    break //go to the next row
                }
            }
        }

        return relevantIndices
    }

    /**
     * @return True, if exactly one condition from row1 differs in row2
     */
    private fun isNeighbour(row1: DecisionTableRow, row2: DecisionTableRow): Boolean {
        return row1.subExpressions
            .zip(row2.subExpressions)
            .count { it.first != it.second } == 1
    }

}
