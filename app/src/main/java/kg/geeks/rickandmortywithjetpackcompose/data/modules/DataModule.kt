package kg.geeks.rickandmortywithjetpackcompose.data.modules

import androidx.room.Room
import kg.geeks.rickandmortywithjetpackcompose.BuildConfig // Добавляем импорт
import kg.geeks.rickandmortywithjetpackcompose.data.api.CharacterApiService
import kg.geeks.rickandmortywithjetpackcompose.data.api.LocationApiService
import kg.geeks.rickandmortywithjetpackcompose.data.local.AppDatabase
import kg.geeks.rickandmortywithjetpackcompose.data.repository.CharacterRepository
import kg.geeks.rickandmortywithjetpackcompose.data.repository.EpisodeRepository
import kg.geeks.rickandmortywithjetpackcompose.data.repository.LocationRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule: Module = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { get<Retrofit>().create(CharacterApiService::class.java) }
    single { get<Retrofit>().create(LocationApiService::class.java) }
    single { get<AppDatabase>().favoriteCharacterDao() }
    single { CharacterRepository(get()) }
    single { LocationRepository(get()) }
    single { EpisodeRepository(get()) }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}

private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL) // Используем BuildConfig.BASE_URL
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}