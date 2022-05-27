import algorithms.MCDC
import algorithms.MMBUe
import java.io.File


fun main(args: Array<String>) {
    val typedArgs = Args.create(args)

    val tasks = typedArgs.CoverageTypes.zip(typedArgs.Files)
    println(
        "Creating coverage into directory '${typedArgs.OutputDir}:\n\n" +
                tasks.joinToString(separator = "\n") { "${it.first}: ${it.second}" }
    )

    val outputDir = File(typedArgs.OutputDir)

    for ((coverageType, filename) in tasks) {

        val linesToWrite = filterInvalidCasesFromFile(filename, coverageType)

        val outputFileName = "${coverageType}_${File(filename).name}"
        File(outputDir, outputFileName).writeText(linesToWrite.joinToString("\n"))
    }

}

fun filterInvalidCasesFromFile(
    filename: String,
    coverageType: CoverageType
): List<String> {
    val inputFile = File(filename)
    val inputText = inputFile.readLines()
    return filterInvalidCases(inputText, coverageType)
}


fun filterInvalidCases(
    inputText: List<String>,
    coverageType: CoverageType
): List<String> {
    val (decisionTable, headerLineCount) = DecisionTable.createFromMarkdown(inputText)

    val testCasesToInclude = when (coverageType) {
        CoverageType.MCDC -> MCDC().run(decisionTable)
        CoverageType.MMBUe -> MMBUe().run(decisionTable)
        else -> throw Exception("'$coverageType' is  unknown")
    }

    return (inputText.take(headerLineCount) +
            inputText.filterIndexed { index, _ -> testCasesToInclude.contains(index - headerLineCount) })
}

