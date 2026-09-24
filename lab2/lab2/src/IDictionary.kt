interface IDictionary {
    fun add(s: String): Boolean
    fun find(s: String): Boolean
    fun size(): Int
}

enum class DictionaryType {
    ARRAY_LIST, TREE_SET, HASH_SET
}