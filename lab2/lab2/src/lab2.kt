import ListDictionary
import java.net.URL

fun main(){
    //println("Hello World")
    val words = mutableListOf("apple", "orange", "pineapple")
    val dict =  ListDictionary(words)
    println(dict.find("apple"))
    println(dict.size())
    println(dict.add("banana"))
    println(dict.size())
    println(dict.words)
}