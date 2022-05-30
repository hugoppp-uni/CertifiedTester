package algorithms

import DecisionTable

class MCDC : Algorithm {
    override fun run(decisionTable: DecisionTable): Set<Int> {
        // find testcase pairs for each column/condition
        // select appropriate testcases, and try to minimize their number
        return setOf()
    }

    /**
     *
     * @param table decision table
     * @param columnIndex index of the column, for which the significant testcases will be searched
     * @return a set of testcase pairs, that are needed for MC/DC coverage
     */
    private fun testPairsOfColumns(table: DecisionTable, columnIndex: Int): Set<Pair<Int, Int>> {
        val indicesTodo = (0 until table.Rows.size).toMutableList()
        val pairs = mutableSetOf<Pair<Int, Int>>()

        // generating pairs (first, second)
        while (indicesTodo.isNotEmpty()) {
            val firstIndex = indicesTodo.removeFirst()
            val firstEntry = table.Rows[firstIndex]
            val firstEntryWithoutCol = firstEntry.withoutConditionAt(columnIndex)

            // search through remaining rows, to find partner test
            for (secondIndex in indicesTodo) {
                val secondEntry = table.Rows[secondIndex]

                // partner was found with significant change
                if (firstEntryWithoutCol == secondEntry.withoutConditionAt(columnIndex)
                    && firstEntry.evaluated != secondEntry.evaluated
                ) {
                    indicesTodo.remove(secondIndex)
                    pairs.add(Pair(firstIndex, secondIndex))
                    break
                }
            }
            // todo throw exception if no partner test was ever found
            //  it shows that the input decision table was not complete
        }
        return pairs
    }
}