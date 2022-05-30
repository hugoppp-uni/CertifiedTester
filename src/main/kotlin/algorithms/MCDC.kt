package algorithms

import DecisionTable
import java.util.Random

class MCDC : Algorithm {
    override fun run(decisionTable: DecisionTable): Set<Int> {
        val numOfColumns = decisionTable.columnCount -1

        // find testcase pairs for each column/condition
        // construct map: column -> set<pair>
        // Iterable.associateWith = make a map using the elements as keys and the function result as value
        val testPairsOfColumn = (0 until numOfColumns).associateWith { col ->
            testPairsOfColumns(decisionTable, col)
        }

        // select appropriate testcases, and try to minimize their number
        val indices = selectPairs(testPairsOfColumn).toSet()

        println(testPairsOfColumn)
        println("indices: $indices")
        return indices
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

    /**
     * Not guaranteed, that they represent the minimal number of testcases!
     * But the testcases will cover the decision table
     */
    private fun selectPairs(pairsMapped: Map<Int, Set<Pair<Int, Int>>>): List<Int> {
        val remainingPairs = mutableMapOf<Int, Set<Pair<Int, Int>>>()
        val chosenIndices = mutableListOf<Int>()

        // partition 'pairsMapped' into to 2 collections
        for (entry in pairsMapped) {
            // from a condition with only 1 testPair, exactly that pair must be chosen
            if (entry.value.size == 1) {
                chosenIndices.addPair(entry.value.elementAt(0))
            }
            // the rest remain to be processed
            else {
                remainingPairs[entry.key] = entry.value
            }
        }

        // in following code, only 1 method/heuristics will be used to try to minimize testcases:
        // pairs that share indices with chosenIndices will be preferred

        if (chosenIndices.isEmpty()) {
            // you could calculate which index appears in most condition
            // but we just chose to select a random entry
            val randomEntry = pairsMapped.random()

            // move randomEntry from remaining Pairs to chosenIndices
            chosenIndices.addPair(randomEntry.value.random())
            remainingPairs.remove(randomEntry.key)
        }

        for (pairs in remainingPairs.values) {
            val similarPairs = pairs.filter { it.containsAnyOf(chosenIndices) }

            // take the first pair, or if there arent any, just take a random one
            // you could use more sophisticated heuristics
            chosenIndices.addPair(similarPairs.getOrElse(0) { pairs.random() })
        }

        return chosenIndices
    }

    /**
     * add all components (first and second) of [pair] to this list
     */
    private fun <T> MutableList<T>.addPair(pair: Pair<T, T>): Boolean = addAll(pair.toList())

    /**
     * choose a random entry
     */
    private fun <T,U> Map<T,U>.random(): Map.Entry<T,U> = entries.elementAt(Random().nextInt(size))

    /**
     * @return true if [collection] contains any of this components
     */
    private fun <T> Pair<T, T>.containsAnyOf(collection: Collection<T>): Boolean = first in collection || second in collection
}