import java.util.TreeSet

class ListDictionary(val items: List<String>) : IDictionary {
    val words = items.toMutableList()

    override fun add(s:String): Boolean {
        this.words.add(s)
        return true
    }
    override fun find(s: String): Boolean {
        return  this.words.contains(s)
    }
    override fun size(): Int{
        return this.words.size
    }
}

class TreeSetDictionary(val items: List<String>) : IDictionary {
    val words: TreeSet<String> = sortedSetOf(*items.toTypedArray())
    val dictType = DictionaryType.TREE_SET
    override fun add(s:String): Boolean {
        this.words.add(s)
        return true
    }
    override fun find(s: String): Boolean {
        return  this.words.contains(s)
    }
    override fun size(): Int{
        return this.words.size
    }
    fun get_dict_type(): DictionaryType{
        return  dictType
    }
}