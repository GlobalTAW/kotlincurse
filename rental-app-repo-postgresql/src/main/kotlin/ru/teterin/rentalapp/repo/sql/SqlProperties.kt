package ru.teterin.rentalapp.repo.sql

data class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/rental",
    val user: String = "postgres",
    val password: String = "postgres",
    val schema: String = "rental",
    val table: String = "ad",
)
