package com.example.expirydate.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideWordsDataBase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(context, ProductDatabase::class.java, "words.db").build()
    }

    @Provides
    fun provideWordsDao(wordsDatabase: ProductDatabase): ProductDAO {
        return wordsDatabase.wordDao()
    }
}