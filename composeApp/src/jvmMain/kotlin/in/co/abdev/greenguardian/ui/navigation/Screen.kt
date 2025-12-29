package `in`.co.abdev.greenguardian.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Report : Screen("report")
    data object Map : Screen("map")
    data object IssueDetail : Screen("issue/{issueId}") {
        fun createRoute(issueId: String) = "issue/$issueId"
    }
    data object Profile : Screen("profile")
}
