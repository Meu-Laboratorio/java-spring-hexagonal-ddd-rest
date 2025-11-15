package com.gusrubin.lab.taskmanager.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // --- IMPORTANTE ---
    // O endereço "10.0.2.2" é um alias especial do emulador Android
    // para o localhost (127.0.0.1) do seu computador.
    private const val EMULATOR_HOST = "10.0.2.2"

    // Se estiver usando um DISPOSITIVO FÍSICO, comente a linha acima e descomente a abaixo,
    // substituindo pelo IP da sua máquina na rede Wi-Fi.
    // private const val PHYSICAL_DEVICE_HOST = "SEU_IP_DA_REDE_WIFI"

    private const val BASE_URL = "http://$EMULATOR_HOST:8080/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}