package com.platinum.mobile

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Black = Color(0xFF07090E)
private val Panel = Color(0xE80A0D13)
private val Orange = Color(0xFFFF7900)
private val TextMuted = Color(0xFF9CAABD)
private val Line = Color(0xFF303947)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        setContent { PlatinumLauncher() }
    }
}

@Composable
private fun PlatinumLauncher() {
    val scope = rememberCoroutineScope()
    var page by remember { mutableStateOf("Вход") }
    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(true) }
    var showPassword by remember { mutableStateOf(false) }
    var busy by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var status by remember { mutableStateOf("Готов к подключению") }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Orange, background = Black, surface = Panel,
        onPrimary = Color.White, onBackground = Color.White, onSurface = Color.White
    )) {
        Box(Modifier.fillMaxSize().background(Black)) {
            Image(
                painter = painterResource(R.drawable.platinum_city),
                contentDescription = "Platinum Mobile city artwork",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(Modifier.fillMaxSize().background(
                Brush.horizontalGradient(listOf(Color(0xFF05070B), Color(0xDD05070B), Color(0x5505070B)))
            ))
            Row(Modifier.fillMaxSize().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                // Sidebar
                Column(
                    Modifier.width(180.dp).fillMaxHeight()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xF0070A10))
                        .border(1.dp, Color(0x442D3744), RoundedCornerShape(22.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text("♛", color = Orange, fontSize = 48.sp, lineHeight = 48.sp)
                            Text("PLATINUM", fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Text("M O B I L E", color = Orange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(2.dp))
                        SidebarItem("Главная", Icons.Default.Home, page == "Главная") { page = "Главная" }
                        SidebarItem("Вход", Icons.Default.Person, page == "Вход") { page = "Вход" }
                        SidebarItem("Регистрация", Icons.Default.Add, page == "Регистрация") { page = "Регистрация" }
                        SidebarItem("Настройки", Icons.Default.Settings, page == "Настройки") { page = "Настройки" }
                    }
                    Column {
                        Text("CRMP LAUNCHER", color = TextMuted, fontSize = 10.sp)
                        Text("Version 1.0.0", color = TextMuted, fontSize = 11.sp)
                    }
                }

                // Main area
                Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xCC111722), shape = RoundedCornerShape(30.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Line)) {
                            Row(Modifier.padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountCircle, null, tint = Orange, modifier = Modifier.size(23.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(nickname.ifBlank { "Гость" }, fontSize = 12.sp)
                                Spacer(Modifier.width(8.dp))
                                Icon(Icons.Default.ExpandMore, null, tint = TextMuted)
                            }
                        }
                    }

                    Row(Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Login panel
                        Column(
                            Modifier.weight(1.35f).fillMaxHeight()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Panel)
                                .border(1.dp, Orange.copy(alpha = .9f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 22.dp, vertical = 18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                when (page) {
                                    "Регистрация" -> "Создать аккаунт"
                                    "Настройки" -> "Настройки лаунчера"
                                    "Главная" -> "Добро пожаловать!"
                                    else -> "Вход в аккаунт"
                                },
                                fontSize = 23.sp, fontWeight = FontWeight.Bold
                            )
                            Text(
                                when (page) {
                                    "Регистрация" -> "Создай аккаунт Platinum Mobile"
                                    "Настройки" -> "Настройки запуска и интерфейса"
                                    "Главная" -> "Всё готово к следующему приключению"
                                    else -> "Войдите в свой аккаунт, чтобы начать играть"
                                }, color = TextMuted, fontSize = 12.sp
                            )

                            if (page == "Настройки") {
                                Text("Ориентация: альбомная", color = TextMuted)
                                Text("Игровой сервер: 80.242.59.112:3993", color = TextMuted)
                                Text("Версия лаунчера: 1.0.0", color = TextMuted)
                            } else {
                                OutlinedTextField(
                                    value = nickname, onValueChange = { nickname = it },
                                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                                    label = { Text("Никнейм") },
                                    leadingIcon = { Icon(Icons.Default.Person, null) },
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    colors = fieldColors()
                                )
                                OutlinedTextField(
                                    value = password, onValueChange = { password = it },
                                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                                    label = { Text("Пароль") },
                                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                                    trailingIcon = {
                                        IconButton(onClick = { showPassword = !showPassword }) {
                                            Icon(if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                                        }
                                    },
                                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = fieldColors()
                                )
                                if (page != "Регистрация") {
                                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(rememberMe, { rememberMe = it }, colors = CheckboxDefaults.colors(checkedColor = Orange))
                                        Text("Запомнить меня", color = TextMuted, fontSize = 12.sp)
                                        Spacer(Modifier.weight(1f))
                                        TextButton(onClick = { status = "Восстановление пароля нужно подключить к API" }) {
                                            Text("Забыли пароль?", color = Orange, fontSize = 11.sp)
                                        }
                                    }
                                }
                                Button(
                                    onClick = {
                                        if (nickname.isBlank() || password.isBlank()) {
                                            status = "Заполни никнейм и пароль"
                                        } else {
                                            busy = true; progress = .15f
                                            status = if (page == "Регистрация") "Создаём аккаунт…" else "Входим в аккаунт…"
                                            scope.launch {
                                                status = if (page == "Регистрация")
                                                    ApiClient.register(nickname.trim(), password)
                                                else ApiClient.login(nickname.trim(), password)
                                                progress = 1f; busy = false
                                            }
                                        }
                                    },
                                    enabled = !busy && page != "Главная",
                                    modifier = Modifier.fillMaxWidth().height(46.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.White)
                                ) {
                                    Icon(if (page == "Регистрация") Icons.Default.PersonAdd else Icons.Default.Login, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text(if (page == "Регистрация") "Создать аккаунт" else "Войти", fontWeight = FontWeight.Bold)
                                }
                                if (page != "Регистрация") {
                                    OutlinedButton(
                                        onClick = { page = "Регистрация" },
                                        modifier = Modifier.fillMaxWidth().height(42.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                                    ) {
                                        Icon(Icons.Default.Add, null)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Создать аккаунт")
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { page = "Вход" }, modifier = Modifier.fillMaxWidth().height(42.dp),
                                        shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                                    ) { Text("Уже есть аккаунт? Войти") }
                                }
                            }
                            Spacer(Modifier.weight(1f, fill = false))
                            if (busy) LinearProgressIndicator(
                                progress = { progress }, modifier = Modifier.fillMaxWidth(),
                                color = Orange, trackColor = Line
                            )
                            Text(status, color = TextMuted, fontSize = 10.sp, maxLines = 2)
                        }

                        // Server status card
                        Column(
                            Modifier.width(230.dp).fillMaxHeight()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xF0080B11))
                                .border(1.dp, Color(0xFF273140), RoundedCornerShape(20.dp))
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(Color(0xFF16D98B)))
                                Spacer(Modifier.width(8.dp))
                                Text("СЕРВЕР", color = Color(0xFF61E8B1), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Image(
                                painter = painterResource(R.drawable.platinum_city),
                                contentDescription = "Город Platinum Mobile",
                                modifier = Modifier.fillMaxWidth().height(78.dp).clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Text("Platinum Mobile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("♛", color = Orange, fontSize = 18.sp)
                                Spacer(Modifier.width(7.dp))
                                Text("CRMP • RolePlay", color = TextMuted, fontSize = 11.sp)
                            }
                            HorizontalDivider(color = Line)
                            ServerRow("IP адрес", "80.242.59.112")
                            ServerRow("Порт", "3993")
                            ServerRow("Статус", "Не проверен")
                            ServerRow("API", "HTTP")
                            Spacer(Modifier.weight(1f))
                            OutlinedButton(
                                onClick = {
                                    busy = true; progress = .1f; status = "Проверяем HTTP API…"
                                    scope.launch {
                                        status = ApiClient.checkServer()
                                        progress = 1f; busy = false
                                    }
                                },
                                enabled = !busy,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange)
                            ) { Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(6.dp)); Text("Проверить") }
                        }
                    }

                    // News footer
                    Row(
                        Modifier.fillMaxWidth().height(82.dp).clip(RoundedCornerShape(16.dp))
                            .background(Color(0xE9070A10)).border(1.dp, Color(0xAA74350C), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Campaign, null, tint = Orange, modifier = Modifier.size(27.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("ПОСЛЕДНИЕ НОВОСТИ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Добро пожаловать на Platinum Mobile!", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            Text("Мы рады видеть тебя на проекте. Приятной игры!", color = TextMuted, fontSize = 10.sp)
                        }
                        Text("PLATINUM\nMOBILE", color = Orange, fontSize = 13.sp, fontWeight = FontWeight.Black, lineHeight = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SidebarItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Brush.horizontalGradient(listOf(Color(0xFFFF7900).copy(alpha = .35f), Color.Transparent)) else Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
    Row(
        Modifier.fillMaxWidth().height(42.dp).clip(RoundedCornerShape(10.dp)).background(bg)
            .then(if (selected) Modifier.border(1.dp, Orange.copy(alpha = .45f), RoundedCornerShape(10.dp)) else Modifier)
            .padding(horizontal = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onClick, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(0.dp)) {
            Icon(icon, null, tint = if (selected) Orange else TextMuted)
            Spacer(Modifier.width(10.dp))
            Text(label, color = if (selected) Orange else TextMuted, fontSize = 12.sp, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ServerRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = TextMuted, fontSize = 11.sp)
        Spacer(Modifier.weight(1f))
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Orange,
    focusedLabelColor = Orange,
    cursorColor = Orange,
    unfocusedBorderColor = Line,
    unfocusedLabelColor = TextMuted,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)
