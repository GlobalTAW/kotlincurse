package ru.teterin.rentalapp.repo.sql

import ru.teterin.rentalapp.model.repo.IAdRepository
import ru.teterin.rentalapp.repo.test.RepoAdCreateTest
import ru.teterin.rentalapp.repo.test.RepoAdDeleteTest
import ru.teterin.rentalapp.repo.test.RepoAdReadTest
import ru.teterin.rentalapp.repo.test.RepoAdSearchTest
import ru.teterin.rentalapp.repo.test.RepoAdUpdateTest
import kotlin.random.Random

val random = Random(System.currentTimeMillis())
class RepoAdSQLCreateTest: RepoAdCreateTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "create-" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoAdSqlDeleteTest: RepoAdDeleteTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "delete-" + random.nextInt(),
        initObjects,
    )
}

class RepoAdSqlReadTest: RepoAdReadTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "read-" + random.nextInt(),
        initObjects,
    )
}

class RepoAdSqlUpdateTest: RepoAdUpdateTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "update-" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoAdSqlSearchTest: RepoAdSearchTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "search-" + random.nextInt(),
        initObjects,
    )

}
