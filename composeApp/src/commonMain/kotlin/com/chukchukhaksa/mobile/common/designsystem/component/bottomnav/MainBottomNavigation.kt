package com.chukchukhaksa.mobile.common.designsystem.component.bottomnav

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import chukchukhaksa.composeapp.generated.resources.Res
import chukchukhaksa.composeapp.generated.resources.ic_home
import chukchukhaksa.composeapp.generated.resources.ic_web
import com.chukchukhaksa.mobile.common.designsystem.theme.Gray6A
import com.chukchukhaksa.mobile.common.designsystem.theme.Primary
import com.chukchukhaksa.mobile.common.designsystem.theme.SuwikiTheme
import com.chukchukhaksa.mobile.common.designsystem.theme.White
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainBottomNavigation(
  navController: NavController,
  onNavigateToTimetable: () -> Unit,
  onNavigateToWebView: () -> Unit,
) {
  val currentBackStackEntry = navController.currentBackStackEntryAsState()
  val currentRoute = currentBackStackEntry.value?.destination?.route

  // 메인 탭 화면에서만 바텀 네비게이션 표시
  val isMainTab = currentRoute == "timetable" || currentRoute == "webview"

  if (isMainTab) {
    NavigationBar(
      containerColor = White,
      tonalElevation = 8.dp,
    ) {
      NavigationBarItem(
        icon = {
          Icon(
            painter = painterResource(Res.drawable.ic_home),
            contentDescription = "시간표",
            modifier = Modifier.size(24.dp),
          )
        },
        label = { Text("시간표") },
        selected = currentRoute == "timetable",
        onClick = onNavigateToTimetable,
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = Primary,
          selectedTextColor = Primary,
          unselectedIconColor = Gray6A,
          unselectedTextColor = Gray6A,
          indicatorColor = Primary.copy(alpha = 0.1f),
        ),
      )

      NavigationBarItem(
        icon = {
          Icon(
            painter = painterResource(Res.drawable.ic_web),
            contentDescription = "웹",
            modifier = Modifier.size(24.dp),
          )
        },
        label = { Text("웹") },
        selected = currentRoute == "webview",
        onClick = onNavigateToWebView,
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = Primary,
          selectedTextColor = Primary,
          unselectedIconColor = Gray6A,
          unselectedTextColor = Gray6A,
          indicatorColor = Primary.copy(alpha = 0.1f),
        ),
      )
    }
  }
}
