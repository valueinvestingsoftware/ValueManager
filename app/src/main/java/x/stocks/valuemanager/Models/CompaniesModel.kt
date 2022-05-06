package x.stocks.valuemanager.Models

class CompaniesModel(companyId: Int, company: String, realPrice: Double, ponderated: Double, ranking: Int, marketPrice: Double, exclude: String, image: ByteArray?, createdInApp: Boolean) {
    var CompanyId = companyId

    var Company = company

    var RealPrice = realPrice

    var Ponderated = ponderated

    var Ranking = ranking

    var MarketPrice = marketPrice

    var Exclude = exclude

    var Image = image

    var CreatedInApp = createdInApp

}