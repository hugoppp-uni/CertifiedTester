import algorithms.MCDC
import algorithms.MMBUe
import kotlinx.cli.*
import java.io.File


data class Args(val Files: List<String>, val OutputDir: String, val CoverageTypes: List<CoverageType>) {
    fun validate() {
        Files.forEach {
            val file = File(it)
            if (!file.isFile) throw Exception("'$it' doesnt exist")
            if (file.extension != "md") throw Exception("${file.extension} files are not supported")
        }
        if (!File(OutputDir).isDirectory)
            throw Exception("Output directory '${OutputDir}' does not exist")
        if (CoverageTypes.count() != Files.count())
            throw Exception("Count of input files does not match count of coverage types")
    }
}

enum class CoverageType {
    MMBUe, //condition coverage
    MCDC,
}

fun main(args: Array<String>) {
    val typedArgs = parseArgs(args)

    val tasks = typedArgs.CoverageTypes.zip(typedArgs.Files)
    println(
        "Creating coverage into directory '${typedArgs.OutputDir}:\n\n" +
                tasks.joinToString(separator = "\n") { "${it.first}: ${it.second}" }
    )

    val outputDir = File(typedArgs.OutputDir)

    for ((coverageType, filename) in tasks) {

        val inputFile = File(filename)
        val inputText = inputFile.readLines()
        val (decisionTable, headerLineCount) = DecisionTable.createFromMarkdown(inputText)

        val testCasesToInclude = when (coverageType) {
            CoverageType.MCDC -> MCDC().run(decisionTable)
            CoverageType.MMBUe -> MMBUe().run(decisionTable)
            else -> throw Exception("'$coverageType' is  unknown")
        }

        val outputFileName = "${coverageType}_${File(filename).name}"

        val linesToWrite = (inputText.take(headerLineCount) +
                inputText.filterIndexed { index, _ -> testCasesToInclude.contains(index + headerLineCount) })

        File(outputDir, outputFileName).writeText(linesToWrite.joinToString("\n"))
    }

}

private fun parseArgs(args: Array<String>): Args {
    val parser = ArgParser("Testcase selector")
    val inputs by parser.option(ArgType.String, shortName = "i", description = "Input files (1..n)")
        .multiple()
        .required()
    val outputDir by parser.option(ArgType.String, shortName = "o", description = "Output directory")
        .required()
    val coverageTypes by parser.option(ArgType.Choice<CoverageType>(), shortName = "c", description = "Coverage type")
        .multiple()
        .required()

    parser.parse(args)
    val typedArgs = Args(inputs, outputDir, coverageTypes)
    typedArgs.validate()

    return typedArgs
}

