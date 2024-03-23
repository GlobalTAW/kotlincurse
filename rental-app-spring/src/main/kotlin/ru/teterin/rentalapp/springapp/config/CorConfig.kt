package ru.teterin.rentalapp.springapp.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.logging.RentalLoggerProvider
import ru.teterin.rentalapp.logging.logback.rentalLoggerLogback
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.repo.IAdRepository
import ru.teterin.rentalapp.repo.inmemory.AdRepoInMemory
import ru.teterin.rentalapp.repo.sql.RepoAdSQL
import ru.teterin.rentalapp.repo.sql.SqlProperties
import ru.teterin.rentalapp.repo.stub.AdRepoStub
import ru.teterin.rentalapp.springapp.models.RentalAppSettings
import javax.management.loading.ClassLoaderRepository

@Suppress("unused")
@Configuration
@EnableConfigurationProperties(SqlPropertiesEx::class)
class CorConfig {

    @Bean
    fun processor(corSettings: RentalCorSettings) = RentalAdProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): RentalLoggerProvider = RentalLoggerProvider { rentalLoggerLogback(it) }

    @Bean
    fun prodRepository(props: SqlPropertiesEx) = RepoAdSQL(SqlProperties(
        url = props.url,
        user = props.user,
        password = props.password,
        schema = props.schema,
        table = props.table ?: "ad",
    ))

    @Bean
    fun testRepository() = AdRepoInMemory()

    @Bean
    fun stubRepository() = AdRepoStub()

    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: IAdRepository,
        @Qualifier("testRepository") testRepository: IAdRepository,
        @Qualifier("stubRepository") stubRepository: IAdRepository,
    ): RentalCorSettings = RentalCorSettings(
        loggerProvider = loggerProvider(),
        repoProd = prodRepository,
        repoTest = testRepository,
        repoStub = stubRepository,
    )

    @Bean
    fun appSettings(corSettings: RentalCorSettings, processor: RentalAdProcessor) = RentalAppSettings(
        corSettings = corSettings,
        processor = processor,
    )

}
