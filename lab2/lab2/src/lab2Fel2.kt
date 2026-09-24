fun String.monogram(): String {
    return this.split(" ")
        .map { it.first().uppercase() }
        .joinToString("")
}

fun List<String>.joinWith(separator: String): String {
    return this.joinToString(separator)
}

fun List<String>.longest(): String {
    return this.maxByOrNull { it.length } ?: ""
}

fun main() {
    val name = "John Smith"
    println(name.monogram())
    val fruits = listOf("apple", "pear", "melon")
    println(fruits.joinWith("#"))
    val fruits2 = listOf("apple", "pear", "strawberry", "melon")
    println(fruits2.longest())
}