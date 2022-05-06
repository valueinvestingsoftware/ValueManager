package x.stocks.valuemanager.Models

import org.w3c.dom.Text

class BankBalanceModel(id: Int, bankId: Int, bank: String, operation: String, value: Double?, comment: String, recordDate: String, lastEditDate: String, createdInApp: Boolean, mspences: String, monthNumber: Int, yspences: Int, balance: Double, debitUniqueIdentifier: String) {
    var Id: Int? = id

    var BankId: Int? = bankId

    var Bank: String = bank

    var Operation: String? = operation

    var Value: Double? = value

    var Comment: String? = comment

    var RecordDate: String? = recordDate

    var LastEditDate: String? = lastEditDate

    var CreatedInApp: Boolean? = createdInApp

    var MSpences: String? = mspences

    var MonthNumber: Int? = monthNumber

    var YSpences: Int? = yspences

    var Balance: Double? = balance

    var DebitUniqueIdentifier: String? = debitUniqueIdentifier
}