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

        return pairs
    }
}