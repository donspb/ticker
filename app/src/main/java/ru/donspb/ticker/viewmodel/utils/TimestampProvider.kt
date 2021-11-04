package ru.donspb.ticker.viewmodel.utils

interface TimestampProvider {
    fun getMilliseconds(): Long
}