package com.wngud.oneminutestart.presentation.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wngud.oneminutestart.MainViewModel
import com.wngud.oneminutestart.presentation.components.AppBar

@Composable
fun SettingScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    var nickName by remember { mutableStateOf("쭉가") }

    BackHandler {
        onBackPressed()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = "설정", hasBackButton = false, action = {}
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileSection(
                    nickName = nickName,
                    onChange = { new -> nickName = new }
                )
            }
            item {
                ThemeSection(mainViewModel)
            }
            item {
                NotificationSection()
            }
            item {
                TimerSection()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSection(
    nickName: String,
    onChange: (String) -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "프로필 설정",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Unspecified
                        ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = isClicked,
                    value = nickName,
                    onValueChange = onChange,
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            isClicked = !isClicked
                        }) {
                            if (isClicked) {
                                Icon(
                                    modifier = Modifier.size(30.dp),
                                    imageVector = Icons.Default.Check, contentDescription = null
                                )
                            } else {
                                Icon(
                                    modifier = Modifier.size(30.dp),
                                    imageVector = Icons.Default.Edit, contentDescription = null
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = if (isClicked) MaterialTheme.colorScheme.background else Color.Transparent
                    ),
                )
            }
        }
    }
}

@Composable
fun ThemeSection(mainViewModel: MainViewModel) {
    val isDarkMode by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
    var modeString by remember { mutableStateOf(if (isDarkMode) "다크 모드" else "라이트 모드") }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("테마 설정", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(modeString, fontSize = 16.sp, modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { darkMode ->
                        mainViewModel.toggleTheme(darkMode)
                        modeString = if (darkMode) "다크 모드" else "라이트 모드"
                    })
            }
        }
    }
}

@Composable
fun NotificationSection() {
    var pushNotificationEnabled by remember { mutableStateOf(false) }
    var vibrationEnabled by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("알림 설정", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("푸시 알림", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Switch(
                    checked = pushNotificationEnabled,
                    onCheckedChange = { pushNotificationEnabled = it })
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("알림음", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                DropdownMenuWithItems(listOf("기본음", "중소리", "무음"))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("진동", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Switch(checked = vibrationEnabled, onCheckedChange = { vibrationEnabled = it })
            }
        }
    }
}

@Composable
fun TimerSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("타이머 설정", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("타이머 알림음", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                DropdownMenuWithItems(listOf("기본음", "중소리", "무음"))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("기본 시간", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                DropdownMenuWithItems(listOf("25분", "30분", "45분", "60분"))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("쉬는 시간", fontSize = 16.sp, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                DropdownMenuWithItems(listOf("5분", "10분", "15분"))
            }
        }
    }
}

@Composable
fun DropdownMenuWithItems(items: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(items[0]) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(selectedItem)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item
                    expanded = false
                }) {
                    Text(item)
                }
            }
        }
    }
}