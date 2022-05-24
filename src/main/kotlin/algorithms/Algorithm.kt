package algorithms

import DecisionTable

/**
 * @returns A list of valid test case indices
 */
interface Algorithm {
    fun run(input: DecisionTable): Set<Int>
}
