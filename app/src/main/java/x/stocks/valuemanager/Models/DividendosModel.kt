package x.stocks.valuemanager.Models

class DividendosModel(id: Int, companyId: Int, company: String, year: Int, dividendosEfectivo: Double, dividendosAccion: Double, accionesPrecedentes: Int, accionesPorAccion: Double, efectivoPorAccion: Double, distribute: String, graph: String, avSalePrice: Double) {

    var id = id

    var CompanyId = companyId

    var Company = company

    var Year = year

    var DividendosEfectivo = dividendosEfectivo

    var DividendosAccion = dividendosAccion

    var AccionesPrecedentes = accionesPrecedentes

    var AccionesPorAccion = accionesPorAccion

    var EfectivoPorAccion = efectivoPorAccion

    var Distribute = distribute

    var Graph = graph

    var AvSalePrice = avSalePrice

}