package me.tadej.joker.di

import dagger.Binds
import dagger.Module
import me.tadej.joker.parser.JsonParser
import me.tadej.joker.parser.Parser
import javax.inject.Singleton

@Module
interface ParserModule {
    @Binds @Singleton fun provideParser(parser: JsonParser): Parser<String, String>
}
