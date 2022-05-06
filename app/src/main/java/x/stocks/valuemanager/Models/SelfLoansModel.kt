package x.stocks.valuemanager.Models

class SelfLoansModel(dateInstallment:String, amount: Double, alreadyPayed: Double, yourCurrentDebt: Double, eachInstallment: Double) {

    var DateInstallment = dateInstallment

    var Amount = amount

    var AlreadyPayed = alreadyPayed

    var YourCurrentDebt = yourCurrentDebt

    var EachInstallment = eachInstallment
}