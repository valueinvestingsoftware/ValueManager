package x.stocks.valuemanager.Models

class BankInformationModel(id: Int, bankName: String, initialBalance: Double) {
    var Id = id

    var BankName: String? = bankName

    var InitialBalance: Double? = initialBalance


    override fun toString(): String {
        return "BankInformationModel(Id=$Id, BankName=$BankName, InitialBalance=$InitialBalance)"
    }

}