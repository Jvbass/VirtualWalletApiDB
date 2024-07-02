package cl.jpinodev.virtualwalletapidb.data.network.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://wallet-main.eba-ccwdurgr.us-east-1.elasticbeanstal.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}