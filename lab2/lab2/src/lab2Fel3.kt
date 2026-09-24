import java.time.LocalDate
import kotlin.random.Random

data class Date(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
    val day: Int = LocalDate.now().dayOfMonth
) : Comparable<Date> {

    // Natural ordering: year -> month -> day
    override fun compareTo(other: Date): Int {
        return when {
            year != other.year -> year - other.year
            month != other.month -> month - other.month
            else -> day - other.day
        }
    }
}

// Checks if the year is a leap year
fun Date.isLeapYear(): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

// Checks if the date is valid
fun Date.isValid(): Boolean {
    if (month !in 1..12 || day < 1) {
        return false
    }

    val daysInMonth = when (month) {
        2 -> if (isLeapYear()) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

    return day <= daysInMonth
}

fun main() {

    val validDates = mutableListOf<Date>()

    // Generate dates until we have 10 valid ones
    while (validDates.size < 10) {

        val date = Date(
            year = Random.nextInt(2000, 2031),
            month = Random.nextInt(1, 13),
            day = Random.nextInt(1, 32)
        )

        if (date.isValid()) {
            validDates.add(date)
        } else {
            println("Invalid date: $date")
        }
    }

    // Print valid dates
    println("\nValid dates:")
    validDates.forEach {
        println(it)
    }

    // Sort using natural ordering
    validDates.sort()

    println("\nSorted dates:")
    validDates.forEach {
        println(it)
    }

    // Reverse the sorted list
    validDates.reverse()

    println("\nReversed dates:")
    validDates.forEach {
        println(it)
    }

    // Sort using a custom Comparator
    validDates.sortWith(
        Comparator { date1, date2 ->
            when {
                date1.day != date2.day -> date1.day - date2.day
                date1.month != date2.month -> date1.month - date2.month
                else -> date1.year - date2.year
            }
        }
    )

    println("\nCustom sorted dates (based on days):")
    validDates.forEach {
        println(it)
    }
}