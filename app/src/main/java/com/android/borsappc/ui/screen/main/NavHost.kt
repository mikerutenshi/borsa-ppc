package com.android.borsappc.ui.screen.main

//@Composable
//fun BorsaPPCNavHost(
//    navController: NavHostController,
//    modifier: Modifier = Modifier,
//    initialScreen: String
//) {
//    NavHost(
//        navController = navController,
//        startDestination = initialScreen,
//        modifier = modifier
//    ) {
//        composable(route = DrawerScreens.Main.route) {
//            MainScreen(navController)
//        }
//
//        composable(route = DrawerScreens.Auth.route) {
////            val parentEntry = remember(backStackEntry) {
////                navController.getBackStackEntry(DrawerScreens.Main.route)
////            }
////            val parentViewModel = hiltViewModel<MainViewModel>(parentEntry)
//            AuthScreen(navController)
//        }
//
//        composable(route = DrawerScreens.Work.route) {
//            WorkScreen()
//        }
//
//        composable(route = DrawerScreens.Worker.route) {
//            WorkerScreen()
//        }
//
//        composable(route = DrawerScreens.Product.route) {
//            ProductScreen()
//        }
//        composable(route = DrawerScreens.Report.route) {
//            ReportScreen()
//        }
//    }
//}