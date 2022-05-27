import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.multiple
import kotlinx.cli.required
import java.io.File

enum class CoverageType {
    MMBUe, //condition coverage
    MCDC,
}


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

    companion object {
        fun create(args: Array<String>): Args {
            val parser = ArgParser("Testcase selector")
            val inputs by parser.option(ArgType.String, shortName = "i", description = "Input files (1..n)")
                .multiple()
                .required()
            val outputDir by parser.option(ArgType.String, shortName = "o", description = "Output directory")
                .required()
            val coverageTypes by parser.option(
                ArgType.Choice<CoverageType>(),
                shortName = "c",
                description = "Coverage type"
            )
                .multiple()
                .required()

            parser.parse(args)
            val typedArgs = Args(inputs, outputDir, coverageTypes)
            typedArgs.validate()

            return typedArgs
        }
    }

}