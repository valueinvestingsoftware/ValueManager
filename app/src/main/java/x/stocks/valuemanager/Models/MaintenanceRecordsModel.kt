package x.stocks.valuemanager.Models

class MaintenanceRecordsModel(id: Int, maintenanceName: String, comment: String, modelId: Int, startKm: Int, endKm: Int, nextAfter: Int, recordDate: String, lastEditDate: String, fulfilled: Boolean, createdInApp: Boolean, updatedInApp: Boolean, deletedInApp: Boolean) {

    var Id = id

    var MaintenanceName = maintenanceName

    var Comment = comment

    var ModelId = modelId

    var startKm = startKm

    var endKm = endKm

    var NextAfter = nextAfter

    var RecordDate = recordDate

    var LastEditDate = lastEditDate

    var Fulfilled = fulfilled

    var CreatedInApp = createdInApp

    var UpdatedInApp = updatedInApp

    var DeletedinApp = deletedInApp

}