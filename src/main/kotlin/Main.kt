import kotlinx.cli.*
import java.io.File


data class Args(val Files: List<String>, val OutputDir: String?, val coverageTypes: List<CoverageType>)

enum class CoverageType {
    MMBUe,
    MCDC,
}

fun main(args: Array<String>) {
    val typedArgs = parseArgs(args)

    println {
        "Creating ${typedArgs.Files.joinToString(", ")} Coverage " +
                "from files ${typedArgs.coverageTypes.joinToString(separator = ", ")} " +
                "into directory ${typedArgs.OutputDir}"
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
        .default(CoverageType.MMBUe)
        .multiple()

    fun validateArgs(args: Args) {
        args.Files.forEach { if (!File(it).isFile) throw Exception("'$it' doesnt exist") }
        if (args.OutputDir?.let { File(it).isDirectory } == false)
            throw Exception("Output directory '${args.OutputDir}' does not exist")
    }

    parser.parse(args)
    val typedArgs = Args(inputs, outputDir, coverageTypes)
    validateArgs(typedArgs)

    return typedArgs

}

