package x.stocks.valuemanager.Models

class MarketPriceSeguimientoModel(companyId: Int, day: String, marketPrice: Double, recordedDateAndTime: String, createdInApp: Boolean) {
    var CompanyId = companyId

    var Day = day

    var MarketPrice = marketPrice

    var RecordedDateAndTime = recordedDateAndTime

    var CreatedInApp = createdInApp
}