package com.example.flexie.services

import com.example.flexie.models.notification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OneSignalService {
    @POST("notifications")
    @Headers(
        "Authorization: Basic Njk2MDA2MWEtNzY5NC00ZWIwLWIwMmUtNmJjOGYzMWU1ZTk0",
        "Content-Type: application/json; charset=utf-8"
    )
    suspend fun sendNotification(@Body notification: notification) : Response<Unit>
}