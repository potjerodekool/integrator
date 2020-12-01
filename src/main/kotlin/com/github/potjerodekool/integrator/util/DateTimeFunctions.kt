package com.github.potjerodekool.integrator.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDateTime.format() = this.format(DateTimeFormatter.ISO_DATE_TIME)

fun Date?.toLocalDateTime() =
        if (this == null) null else LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())

fun ZonedDateTime?.toDate() =
    if (this == null) null else Date.from(this.toInstant());


