package ru.teterin.rentalapp.common

class ControllerTest {

    private val request = AdCreateRequest(
        requestId = "88a17ae8-5511-4f00-b225-56f7f1c7deb8",
        ad = AdCreateObject(
            title = "Прокат Сап Борда",
            description = "Прокат Сап Борда Exegol 3.2x0.8м",
            timeParam = TimeParam(
                rentDates = arrayListOf("2023-12-21"),
                issueTimes = arrayListOf("1703170800")
            ),
            productId = "9cc5e324-fc21-4e1e-860a-fe5cf87e17ea",
        ),
        debug = AdDebug(
            mode = AdRequestDebugMode.STUB,
            stub = AdRequestDebugStubs.SUCCESS
        ),
    )

    private val appSettings: IRentalAppSettings = object : IRentalAppSettings {
        override val processor: RentalAdProcessor = RentalAdProcessor()
        override val corSettings: RentalCorSettings = RentalCorSettings()
    }

    private suspend fun createAd(request: AdCreateRequest): AdCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportAd() as AdCreateResponse },
            ControllerTest::class,
            "controller-test"
        )

    @Test
    fun springHelperTest() = runTest {
        val res = createAd(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

}
