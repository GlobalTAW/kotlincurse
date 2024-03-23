package ru.teterin.rentalapp.repo.sql

import org.testcontainers.containers.PostgreSQLContainer
import ru.teterin.rentalapp.model.models.RentalAd
import java.time.*
import java.util.UUID.*

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "postgres"
    private const val SCHEMA = "rentalapp"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        test: String,
        initObjects: Collection<RentalAd> = emptyList(),
        randomUuid: () -> String = { randomUUID().toString() },
    ): RepoAdSQL {
        return RepoAdSQL(
            SqlProperties(
                url = url,
                user = USER,
                password = PASS,
                schema = SCHEMA,
                table = "ad-$test",
            ),
            initObjects,
            randomUuid = randomUuid,
        )
    }

}
