import kotlinx.cli.*
import java.io.File


data class Args(val Files: List<String>, val OutputDir: String?, val CoverageTypes: List<CoverageType>) {
    fun validate() {
        Files.forEach { if (!File(it).isFile) throw Exception("'$it' doesnt exist") }
        if (OutputDir?.let { File(it).isDirectory } == false)
            throw Exception("Output directory '${OutputDir}' does not exist")
        if (CoverageTypes.count() != Files.count())
            throw Exception("Count of input files does not match count of coverage types")
    }
}

enum class CoverageType {
    MMBUe,
    MCDC,
}

fun main(args: Array<String>) {
    val typedArgs = parseArgs(args)

    println(
        "Creating coverage into directory '${typedArgs.OutputDir}:\n\n" +
                typedArgs.CoverageTypes.zip(typedArgs.Files)
                    .joinToString(separator = "\n") { "${it.first}: ${it.second}" }
    )

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

