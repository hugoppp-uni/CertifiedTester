package algorithms

import filterInvalidCases
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MCDCTest {
    @Test
    fun test_exc1() {

        val actualLines =
            filterInvalidCases(getResourceLines("exercise1.md"), CoverageType.MCDC)
        val expectedLines = getResourceLines("exercise1_mcdc_solution.md")

        assertEquals(expectedLines, actualLines)
    }

    @Test
    fun test_exc2() {

        val actualLines =
            filterInvalidCases(getResourceLines("exercise2.md"), CoverageType.MCDC)
        val expectedLines = getResourceLines("exercise2_mcdc_solution.md")
        val expectedLines2 = getResourceLines("exercise2_mcdc_solution2.md")
        val expectedLines3 = getResourceLines("exercise2_mcdc_solution3.md")
        val expectedLines4 = getResourceLines("exercise2_mcdc_solution4.md")

    assertTrue { actualLines.equals(expectedLines)||actualLines.equals(expectedLines2)||actualLines.equals(expectedLines3)||actualLines.equals(expectedLines4)}



    }

    private fun getResourceLines(path: String): List<String> {
        return File(this::class.java.classLoader.getResource(path)?.path!!).readLines();
    }


}