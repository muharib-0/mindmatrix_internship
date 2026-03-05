// Function to calculate the grade based on marks
fun calculateGrade(marks: Int): String {
    return when {
        marks >= 90 -> "A"
        marks >= 75 -> "B"
        marks >= 60 -> "C"
        marks >= 50 -> "D"
        else -> "Fail"
    }
}

// Function to print the student performance report
fun generateReport(name: String, marksList: List<Int>) {

    println("Student Performance Report")
    println("Student Name: $name")
    println("---------------------------")

    var total = 0

    for (i in marksList.indices) {
        val marks = marksList[i]
        val grade = calculateGrade(marks)

        println("Subject ${i + 1}: Marks = $marks, Grade = $grade")
        total += marks
    }

    val average = total / marksList.size

    println("---------------------------")
    println("Total Marks: $total")
    println("Average Marks: $average")

    // Condition to determine overall result
    if (average >= 50) {
        println("Overall Result: Pass")
    } else {
        println("Overall Result: Fail")
    }
}

fun main() {

    val studentName = "John"

    // List of marks for subjects
    val marks = listOf(85, 72, 90, 65, 50)

    // Generate report
    generateReport(studentName, marks)
}