package ru.donspb.ticker

interface TimestampProvider {
    fun getMilliseconds(): Long
}