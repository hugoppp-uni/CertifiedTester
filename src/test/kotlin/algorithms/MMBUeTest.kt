package algorithms

import filterInvalidCases
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class MMBUeTest {

    @Test
    fun run() {

        val actualLines =
            filterInvalidCases(getResourceLines("exercise1.md"), CoverageType.MMBUe)
        val expectedLines = getResourceLines("exercise1_mmbue_solution.md")

        assertEquals(expectedLines, actualLines)
    }

    private fun getResourceLines(path: String): List<String> {
        return File(this::class.java.classLoader.getResource(path)?.path!!).readLines();
    }
}