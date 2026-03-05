fun main() {
    // 1. Variables: 'val' for read-only (constants), 'var' for values that can change
    val userName = "Alex"
    var tasksCompleted = 0

    // 2. Lists: Creating a mutable list so we can add or remove items later
    val dailyTasks = mutableListOf("Write Kotlin code", "Review pull requests", "Attend team meeting")

    // Adding a new item to our list
    dailyTasks.add("Update documentation")

    // 3. String Templates: Using $ to insert variables and ${} to insert expressions
    println("Hello, $userName!")
    println("You currently have ${dailyTasks.size} tasks on your agenda.")
    println("Tasks completed today: $tasksCompleted\n")

    // 4. Calling our custom function
    displayTasks(dailyTasks)
    
    // Simulating completing a task
    tasksCompleted++
    println("\nGreat job! You just completed a task. Total completed: $tasksCompleted")
}

// 4. Functions: Defining a function that takes a List of Strings as a parameter
fun displayTasks(tasks: List<String>) {
    println("--- Your To-Do List ---")
    
    // Looping through the list and using a string template for formatting
    for ((index, task) in tasks.withIndex()) {
        println("${index + 1}. $task")
    }
}