package training.journal.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import training.journal.BuildConfig

object RetrofitApiUtils {

    // todo server url
    var TRAINING_JOURNAL_URL = "http://devdomain.chickenkiller.com"
        get() = field
        set(value) {
            field = value
        }

    @JvmStatic
    fun createApi(): ApiInterface {
        val okHttpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            okHttpClient.addInterceptor(httpLoggingInterceptor)
        }

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(TRAINING_JOURNAL_URL)
                .client(okHttpClient.build())
                .build()
                .create(ApiInterface::class.java)
    }
}