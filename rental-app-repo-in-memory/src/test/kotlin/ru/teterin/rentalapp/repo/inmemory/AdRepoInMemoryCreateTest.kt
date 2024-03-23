package ru.teterin.rentalapp.repo.inmemory

import ru.teterin.rentalapp.repo.test.RepoAdCreateTest

class AdRepoInMemoryCreateTest : RepoAdCreateTest() {
    override val repo = AdRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}
