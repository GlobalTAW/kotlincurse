package ru.teterin.rentalapp.springapp

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import ru.teterin.rentalapp.repo.sql.RepoAdSQL

@SpringBootTest
class ApplicationTest {

    @Suppress("unused")
    @MockBean
    private lateinit var repo: RepoAdSQL

    @Test
    fun contextLoads() {
    }

}
