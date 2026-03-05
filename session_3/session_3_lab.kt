// Data class to store user profile information
data class UserProfile(
    val name: String,
    val age: Int,
    val role: String,
    val showAvatar: Boolean
)

// Function to print an avatar box
fun printAvatar(name: String) {
    println("+-----------+")
    println("|  Avatar   |")
    println("|   ${name.first()}       |")
    println("+-----------+")
}

// Function to display a user profile
fun displayProfile(user: UserProfile) {
    println("================================")
    
    if (user.showAvatar) {
        printAvatar(user.name)
    }

    println("Name : ${user.name}")
    println("Age  : ${user.age}")
    println("Role : ${user.role}")
    
    println("================================")
    println()
}

fun main() {

    // List of user profiles
    val users = listOf(
        UserProfile("Alice", 22, "Student", true),
        UserProfile("Bob", 25, "Developer", false),
        UserProfile("Charlie", 21, "Designer", true)
    )

    println("USER PROFILE DIRECTORY")
    println()

    // Loop through profiles and display them
    for (user in users) {
        displayProfile(user)
    }
}