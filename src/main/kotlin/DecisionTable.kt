class DecisionTable(val Table: List<List<Boolean>>) {
    companion object {
        public fun createFromMarkdown(mdText: List<String>): DecisionTable {
            //todo parse markdown
            val decisions: List<List<Boolean>> = emptyList()
            return DecisionTable(decisions)
        }
    }

}