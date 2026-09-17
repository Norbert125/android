
fun main() {
//    val num1 = 3;
//    val num2 = 2;
//    println("$num1 + $num2 = ${num1 + num2}");
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
//    for (day in days) {
//        println(day)
//    }
    println(days.filter { it.startsWith("T") })
    println( days.filter { it.contains('e') })
}