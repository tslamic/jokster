package me.tadej.joker.parser

interface Parser<T, U> {
    fun parse(data: T): U
}
