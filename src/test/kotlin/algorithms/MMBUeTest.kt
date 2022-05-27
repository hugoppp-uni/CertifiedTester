package algorithms

import filterInvalidCases
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class MMBUeTest {

    @Test
    fun test_exc1() {

        val actualLines =
            filterInvalidCases(getResourceLines("exercise1.md"), CoverageType.MMBUe)
        val expectedLines = getResourceLines("exercise1_mmbue_solution.md")

        assertEquals(expectedLines, actualLines)
    }

    @Test
    fun test_exc2() {

        val actualLines =
            filterInvalidCases(getResourceLines("exercise2.md"), CoverageType.MMBUe)
        val expectedLines = getResourceLines("exercise2_mmbue_solution.md")

        assertEquals(expectedLines, actualLines)
    }

    private fun getResourceLines(path: String): List<String> {
        return File(this::class.java.classLoader.getResource(path)?.path!!).readLines();
    }
}