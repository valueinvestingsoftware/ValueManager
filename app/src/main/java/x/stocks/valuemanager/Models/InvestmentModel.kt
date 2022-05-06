package x.stocks.valuemanager.Models

class InvestmentModel(companyId: Int, company: String, totalOwned: Int, precioPromedioCompra: Double, dinero: Double, soloCompradas: Int, soloVendidas: Int, utilidadBancaria: Double, utilidadAccion: Double, comisionCompra: Double, comisionVenta: Double) {

    var CompanyId = companyId

    var Company = company

    var TotalOwned = totalOwned

    var PrecioPromedioCompra = precioPromedioCompra

    var Dinero = dinero

    var SoloCompradas = soloCompradas

    var SoloVendidas = soloVendidas

    var UtilidadBancaria = utilidadBancaria

    var UtilidadAccion = utilidadAccion

    var ComisionCompra = comisionCompra

    var ComisionVenta = comisionVenta

}