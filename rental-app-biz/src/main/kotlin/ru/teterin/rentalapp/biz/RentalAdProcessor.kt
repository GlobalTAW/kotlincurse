package ru.teterin.rentalapp.biz

import ru.otus.otuskotlin.marketplace.cor.chain
import ru.otus.otuskotlin.marketplace.cor.rootChain
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.biz.general.initRepo
import ru.teterin.rentalapp.biz.general.prepareResult
import ru.teterin.rentalapp.biz.groups.operation
import ru.teterin.rentalapp.biz.groups.stubs
import ru.teterin.rentalapp.biz.repo.repoCreate
import ru.teterin.rentalapp.biz.repo.repoDelete
import ru.teterin.rentalapp.biz.repo.repoPrepareBook
import ru.teterin.rentalapp.biz.repo.repoPrepareCreate
import ru.teterin.rentalapp.biz.repo.repoPrepareDelete
import ru.teterin.rentalapp.biz.repo.repoPrepareUpdate
import ru.teterin.rentalapp.biz.repo.repoRead
import ru.teterin.rentalapp.biz.repo.repoSearch
import ru.teterin.rentalapp.biz.repo.repoUpdate
import ru.teterin.rentalapp.biz.validation.*
import ru.teterin.rentalapp.biz.workers.*
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState

class RentalAdProcessor(
    @Suppress("unused")
    private val corSettings: RentalCorSettings = RentalCorSettings.NONE
) {

    @Suppress("RedundantSuspendModifier")
    suspend fun exec(ctx: RentalContext) = BusinessChain.exec(ctx.also { it.corSettings = corSettings })

    companion object {
        private val BusinessChain = rootChain<RentalContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание объявления", RentalCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = RentalAdId.NONE }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    validateTitleHasContent("Проверка на непустой заголовок")
                    validateDescriptionHasContent("Проверка на непустое описание")
                    validateTimeParamRentDatesNotEmpty("Проверяем наличие дат получения")
                    validateTimeParamIssueTimesNotEmpty("Проверяем наличие времени получения")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объявления для сохранения")
                    repoCreate("Сохранение объявления в БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Получить объявление", RentalCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = RentalAdId(adValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение объявления из БД")
                    worker {
                        title = "Подготовка ответа для чтения"
                        on { state == RentalState.RUNNING }
                        handle { adRepoDone = adRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
            }
            operation("Изменить объявление", RentalCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = RentalAdId(adValidating.id.asString().trim()) }
                    worker("Очистка lock") { adValidating.lock = RentalAdLock(adValidating.lock.asString().trim()) }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateTitleHasContent("Проверка на непустой заголовок")
                    validateDescriptionHasContent("Проверка на непустое описание")
                    validateTimeParamRentDatesNotEmpty("Проверяем наличие дат получения")
                    validateTimeParamIssueTimesNotEmpty("Проверяем наличие времени получения")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика обновления"
                    repoRead("Чтение объявления из БД")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление объявления в БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Удалить объявление", RentalCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = RentalAdId(adValidating.id.asString().trim()) }
                    worker("Очистка lock") { adValidating.lock = RentalAdLock(adValidating.lock.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение обхъявления из БД")
                    repoPrepareDelete("Подготовка объявления для удаления")
                    repoDelete("Удаление объявления из БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Поиск объявлений", RentalCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adFilterValidating") { adFilterValidating = adFilterRequest.copy() }

                    finishAdFilterValidation("Успешное завершение процедуры валидации")
                }
                repoSearch("Поиск объявления в БД по фильтру")
                prepareResult("Подготовка ответа")
            }
            operation("Забронировать услугу", RentalCommand.BOOK) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = RentalAdId(adValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTimeParamRentDatesNotEmpty("Проверяем наличие дат получения")
                    validateTimeParamIssueTimesNotEmpty("Проверяем наличие времени получения")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика бронирования"
                    repoRead("Чтение объявления из БД")
                    repoPrepareBook("Подготовка объекта для бронирования")
                    repoUpdate("Обновление объявления в БД")
                }
                prepareResult("Подготовка ответа")
            }
        }.build()
    }

}
