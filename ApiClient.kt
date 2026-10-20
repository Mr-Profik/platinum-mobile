package com.platinum.mobile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {
    // Change this if your HTTP API runs on another port/domain.
    // This assumes the API is HTTP on the same IP and port as the game server.
    private const val API_BASE = "http://80.242.59.112:3993"

    suspend fun login(nickname: String, password: String): String = request(
        "/api/login", JSONObject().put("nickname", nickname).put("password", password)
    )

    suspend fun register(nickname: String, password: String): String = request(
        "/api/register", JSONObject().put("nickname", nickname).put("password", password)
    )

    suspend fun checkServer(): String = withContext(Dispatchers.IO) {
        try {
            val conn = (URL("$API_BASE/").openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"; connectTimeout = 5000; readTimeout = 5000
            }
            val code = conn.responseCode
            conn.disconnect()
            if (code in 200..499) "Сервер отвечает (HTTP $code). Это ещё не подтверждает работу API."
            else "Сервер ответил с ошибкой HTTP $code"
        } catch (e: Exception) {
            "Нет ответа от HTTP API. Проверь адрес, порт и включён ли HTTP-сервис."
        }
    }

    private suspend fun request(path: String, body: JSONObject): String = withContext(Dispatchers.IO) {
        try {
            val conn = (URL("$API_BASE$path").openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 7000
                readTimeout = 7000
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
            }
            conn.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val response = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
            conn.disconnect()
            val json = try { JSONObject(response) } catch (_: Exception) { JSONObject() }
            when {
                code !in 200..299 -> "API вернул HTTP $code: ${json.optString("message", response.take(120))}"
                json.optBoolean("success", false) -> json.optString("message", "Запрос выполнен")
                json.has("message") -> json.optString("message")
                else -> "Ответ API получен, но формат ответа нужно согласовать с сервером"
            }
        } catch (e: Exception) {
            "Не удалось связаться с API. Убедись, что маршрут $path существует и порт открыт."
        }
    }
}
