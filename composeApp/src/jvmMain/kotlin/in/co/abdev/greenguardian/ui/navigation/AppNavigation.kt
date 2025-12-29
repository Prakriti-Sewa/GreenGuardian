package `in`.co.abdev.greenguardian.ui.navigation

import `in`.co.abdev.greenguardian.ui.screens.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToReport = {
                    navController.navigate(Screen.Report.route)
                },
                onNavigateToMap = {
                    navController.navigate(Screen.Map.route)
                },
                onNavigateToIssue = { issueId ->
                    navController.navigate(Screen.IssueDetail.createRoute(issueId))
                }
            )
        }
        
        composable(Screen.Report.route) {
            ReportIssueScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onIssueCreated = { issueId ->
                    navController.popBackStack()
                    navController.navigate(Screen.IssueDetail.createRoute(issueId))
                }
            )
        }
        
        composable(Screen.Map.route) {
            MapScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToIssue = { issueId ->
                    navController.navigate(Screen.IssueDetail.createRoute(issueId))
                }
            )
        }
        
        composable(
            route = Screen.IssueDetail.route,
            arguments = listOf(navArgument("issueId") { type = NavType.StringType })
        ) { backStackEntry ->
            val issueId = backStackEntry.arguments?.getString("issueId") ?: return@composable
            IssueDetailScreen(
                issueId = issueId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
