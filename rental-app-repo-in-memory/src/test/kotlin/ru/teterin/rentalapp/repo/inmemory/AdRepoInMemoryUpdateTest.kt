package ru.teterin.rentalapp.repo.inmemory

import ru.teterin.rentalapp.model.repo.IAdRepository
import ru.teterin.rentalapp.repo.test.RepoAdSearchTest

class AdRepoInMemoryUpdateTest : RepoAdSearchTest() {
    override val repo : IAdRepository = AdRepoInMemory(
        initObjects = initObjects
    )
}
