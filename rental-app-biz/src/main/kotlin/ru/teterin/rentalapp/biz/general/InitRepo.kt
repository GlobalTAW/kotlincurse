package ru.teterin.rentalapp.biz.general

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.helpers.errorAdministration
import ru.teterin.rentalapp.model.helpers.fail
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.repo.IAdRepository

fun ICorChainDsl<RentalContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы
    """.trimIndent()
    handle {
        adRepo = when {
            workMode == RentalWorkMode.TEST -> corSettings.repoTest
            workMode == RentalWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if(workMode != RentalWorkMode.STUB && adRepo == IAdRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfugured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
