import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import kotlin.reflect.typeOf


internal class DecisionTableTest {
    @Test
    fun test_invalid_markdown() {

        assertThrows(Exception::class.java) {
            DecisionTable.createFromMarkdown(
                listOf(
                    "| A0 | A1 | A2 | B  |",
                    "| -- | -- | -- | -- |",
                    "|  0 |  0 |  0 |  1 |",
                    "|  0 |  0 |  0 |  1 | 2 |"
                )
            )
        }

        assertThrows(Exception::class.java) {
            DecisionTable.createFromMarkdown(
                listOf(
                    "| A0 | A1 | A2 | B  |",
                    "| -- | -- | -- | -- |",
                    "|  0 |  0 |  0 |  1 |",
                    "|  0 |  0 |  0 |  A |"
                )
            )
        }
    }

}