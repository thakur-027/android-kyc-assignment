package com.example.kycbank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kycbank.ui.accounts.AccountsScreen
import com.example.kycbank.ui.camera.CameraKycScreen
import com.example.kycbank.ui.details.DetailsScreen
import com.example.kycbank.ui.theme.ThemeViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    themeViewModel: ThemeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "accounts",
        modifier = modifier
    ) {
        composable("accounts") {
            AccountsScreen(
                themeViewModel = themeViewModel,
                onCustomerClick = { customerId ->
                    navController.navigate("details/$customerId")
                }
            )
        }
        composable(
            route = "details/{customerId}",
            arguments = listOf(navArgument("customerId") { type = NavType.StringType })
        ) {
            DetailsScreen(navController = navController)
        }

        composable(
            route = "camera/{customerId}",
            arguments = listOf(navArgument("customerId") { type = NavType.StringType })
        ) {
            CameraKycScreen(
                onSelfieCaptured = { selfiePath ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selfiePath", selfiePath)
                    navController.popBackStack()
                }
            )
        }
    }
}