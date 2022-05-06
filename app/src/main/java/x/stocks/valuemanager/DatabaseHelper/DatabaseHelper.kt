package x.stocks.valuemanager.DatabaseHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import x.stocks.valuemanager.Models.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

//Tabla BankSummaries
val table_banksummaries = "BankSummaries"

val columnid_banksummaries = "id"
val columnbankId_banksummaries = "BankId"
val columnYSpences_banksummaries = "YSpences"
val columnbalance_banksummaries = "Balance"
val columnmonthname_banksummaries = "MonthName"
val columnmonthnumber_banksummaries = "MonthNumber"

//Tabla MonthNames
val table_monthNames = "MonthNames"

val columnid_monthNames = "id"
val columnLanguageId_monthnames = "LanguageId"
val columnMonthNumber_monthnames = "MonthNumber"
val columnMonthName_monthnames = "MonthName"

//Tabla CarModels
val table_carmodels = "CarModels"

val columnid_carmodels = "Id"
val columnbrand_carmodels = "Brand"
val columnMileage = "CurrentMilleage"
val columnEditDateCarModels = "LastEditDate"

//Tabla MaintenanceRecords
val table_maintenancerecords = "MaintenanceRecords"

val columnid_maintenancerecords = "Id"
val columnMaintenanceName_maintenancerecords = "MaintenanceName"
val columnComment_maintenancerecords = "Comment"
val columnModelId_maintenancerecords = "ModelId"
val columnStartKm_maintenancerecords = "startKM"
val columnEndKm_maintenancerecords = "endKm"
val columnNextAfter_maintenancerecords = "NextAfter"
val columnRecordDate_maintenancerecords = "RecordDate"
val columnLastEditDate_maintenanceRecords = "LastEditDate"
val columnFulfilled_maintenancerecords = "Fulfilled"
val columnCreatedInApp_maintenanceRecords = "CreatedInApp"
val columnUpdatedInApp_maintenanceRecords = "UpdatedInApp"
val columnDeletedInApp_maintenanceRecords = "Deleted"

//Tabla BankInformation
val table_bankinformation = "BankInformation"

val columnId_bankinformation = "Id"
val columnBankName_bankinformation = "BankName"
val columnInitialBalance_bankinformation = "InitialBalance"

//Tabla BankBalance
val table_bankbalance = "BankBalance"

val columnId_bankbalance = "Id"
val columnBankId_bankbalance = "BankId"
val columnOperation_bankbalance = "Operation"
val columnValue_bankbalance = "Value"
val columnComment_bankbalance = "Comment"
val columnRecordDate_bankbalance = "RecordDate"
val columnLastEditDate_bankbalance = "LastEditDate"
val columnCreatedInApp_bankbalance = "CreatedInApp"
val columnMSpences_BankBalance = "MSpences"
val columnMonthNumber_BankBalance = "MonthNumber"
val columnYSpences_BankBalance = "YSpences"
val columnBalance_BankBalance = "Balance"
val columnDebitUniqueIdentifier_BankBalance = "DebitUniqueIdentifier"

// Tabla SelfLoans
val table_SelfLoans = "SelfLoans"

val columnInstallmentPayedDate_SelfLoans = "DInstallment"
val columnAmount_SelfLoans = "Amount"
val columnAlreadyPayed_SelfLoans = "AlreadyPayed"
val columnYourCurrentDebt_SelfLoans = "YourCurrentDebt"
val columnEachInstallment_SelfLoans = "EachInstallment"

//Tabla BSpences
val table_spences = "Spences"

val columnId_Spences = "id"
val columnSpenceId_Spences = "SpenceId"
val columnSpence_Spences = "Spence"
val columnDspences_Spences = "DSpences"
val columnDia_Spences = "dia"
val columnDiaNumber_Spences = "DiaNumber"
val columnDiaSpences_Spences = "DiaSpences"
val columnMSpences_Spences = "MSpences"
val columnMonthNumber_Spences = "MonthNumber"
val columnYSpences_Spences = "YSpence"
val columnCreatedInApp_Spences = "CreatedInApp"
val columnBankDebit_Spences = "BankDebit"
val columnDebitUniqueIdentifier_Spences = "DebitUniqueIdentifier"

//Tabla SpenceCategory
val table_spencecategory = "SpenceCategory"

val columnSpenceID_spencecategory = "SpenceID"
val columnSpenceDescription_spencecategory = "SpenceDescription"
val columnCreatedinApp_spencecategory = "CreatedInApp"

//Tabla Companies
val table_companies = "Companies"

val columnCompanyId_companies = "CompanyId"
val columnCompany_companies = "Company"
val columnRealPrice_companies = "RealPrice"
val columnPonderated_companies = "Ponderated"
val columnRanking_companies = "Ranking"
val columnMarketPrice_companies = "MarketPrice"
val columnExclude_companies = "Exclude"
val columnImage_companies = "Image"
val columnCreatedInApp_companies = "CreatedInApp"

//Tabla Investment
val table_investment = "Investment"

val columnCompanyId_investment = "CompanyId"
val columnTotalOwned_investment = "TotalOwned"
val columnPrecioPromedioCompra_investment = "PrecioPromedioCompra"
val columnDinero_investment = "Dinero"
val columnSoloCompradas_investment = "SoloCompradas"
val columnSoloVendidas_investment = "SoloVendidas"
val columnUtilidadBancaria_investment = "UtilidadBancaria"
val columnUtilidadAccion_investment = "UtilidadAccion"
val columnComisionCompra_investment = "ComisionCompra"
val columnComisionVenta_investment = "ComisionVenta"

//Tabla Dividendos
val table_dividendos = "Dividendos"

val columnId_dividendos = "id"
val columnCompanyId_dividendos = "CompanyID"
val columnYear_dividendos = "Year"
val columnDividendosEfectivo_dividendos= "DividendosEfectivo"
val columnDividendosAccion_dividendos = "DividendosAccion"
val columnAccionesPrecedentes_dividendos = "AccionesPrecedentes"
val columnAccionesPorAccion_dividendos = "AccionesPorAccion"
val columnEfectivoPorAccion_dividendos = "EfectivoPorAccion"
val columnDistribute_dividendos = "Distribute"
val columnGraph_dividendos = "Graph"
val columnAvSalePrice_dividendos = "AvSalePrice"

//Tabla Parameters
val table_parameters = "Parameters"

val columnId_parameters = "id"
val columnYear_parameters = "Year"
val columnMonth_parameters = "Month"
val columnMonthNumber_parameters= "MonthNumber"
val columnMonthlySavings_parameters = "MonthlySavings"
val columnMonthlyIncome_parameters = "MonthlyIncome"
val columnCreatedInApp_parameters = "CreatedInApp"

//Tabla MarketPrices
val table_marketPrices = "MarketPrices"

val columnCompanyId_marketPrices = "CompanyID"
val columnMarketPrice_marketPrices = "MarketPrice"
val columnFechaMP_marketPrices = "FechaMP"
val columnCreatedInApp_marketprices = "CreatedInApp"

//Tabla MarketPriceSeguimiento
val table_marketPriceSeguimiento = "MarketPriceSeguimiento"

val columnid_marketPriceSeguimiento = "id"
val columnCompanyId_marketPriceSeguimiento = "CompanyId"
val columnDay_marketPriceSeguimiento = "Day"
val columnMarketPrice_marketPriceSeguimiento = "MarketPrice"
val columnRecordedDateAndTime_marketPriceSeguimiento = "RecordedDateAndTime"
val columnCreatedInApp_marketPriceSeguimiento = "CreatedInApp"

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Value", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        //Creo la tabla SelfLoans
        val createTableSelfLoansStatement = ("CREATE TABLE " + table_SelfLoans + "(" + columnInstallmentPayedDate_SelfLoans + " TEXT, "
                + columnAmount_SelfLoans + " DOUBLE, " + columnAlreadyPayed_SelfLoans + " DOUBLE, "
                + columnYourCurrentDebt_SelfLoans + " DOUBLE, " + columnEachInstallment_SelfLoans + " DOUBLE)")
        db?.execSQL(createTableSelfLoansStatement)

        //Creo la tabla BankSummaries
        val createTableBankSummariesStatement = ("CREATE TABLE " + table_banksummaries + "(" + columnid_banksummaries + " INTEGER, "
                + columnbankId_banksummaries + " INTEGER, " + columnYSpences_banksummaries + " INTEGER, "
                + columnbalance_banksummaries + " DOUBLE, " + columnmonthname_banksummaries + " TEXT, "
                + columnmonthnumber_banksummaries + " INTEGER)")
        db?.execSQL(createTableBankSummariesStatement)

        //Creo la tabla CarModels
        val createTableCarModelsStatement = ("CREATE TABLE " + table_carmodels + "(" + columnid_carmodels + " INTEGER, "
                + columnbrand_carmodels + " TEXT, " + columnMileage + " INTEGER, '" + columnEditDateCarModels + "' TEXT)")
        db?.execSQL(createTableCarModelsStatement)

        //Creo la tabla MonthNames
        val createTableMonthnames = ("CREATE TABLE " + table_monthNames + "(" + columnid_monthNames + " INTEGER, "
                + columnLanguageId_monthnames + " INTEGER, " + columnMonthNumber_monthnames + " INTEGER, " + columnMonthName_monthnames + " TEXT)")
        db?.execSQL(createTableMonthnames)

        //Creo la tabla MaintenanceRecords
        val createTableMaintenanceRecordsStatement = ("CREATE TABLE " + table_maintenancerecords + "(" + columnid_maintenancerecords + " INTEGER, "
                + columnMaintenanceName_maintenancerecords + " TEXT," + columnComment_maintenancerecords + " TEXT, " + columnModelId_maintenancerecords + " INTEGER, "
                + columnStartKm_maintenancerecords + " INTEGER," + columnEndKm_maintenancerecords + " INTEGER, " + columnNextAfter_maintenancerecords + " INTEGER, "
                + columnRecordDate_maintenancerecords + " TEXT, " + columnLastEditDate_maintenanceRecords + " TEXT, " + columnFulfilled_maintenancerecords + " BOOLEAN, "
                + columnCreatedInApp_maintenanceRecords + " BOOLEAN, " + columnUpdatedInApp_maintenanceRecords + " BOOLEAN, " + columnDeletedInApp_maintenanceRecords + " BOOLEAN)")
        db?.execSQL(createTableMaintenanceRecordsStatement)

        //Creo la tabla Parameters
        val createTableParametersStatement = ("CREATE TABLE " + table_parameters + "(" + columnId_parameters + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + columnYear_parameters + " INTEGER," + columnMonth_parameters + " TEXT, " + columnMonthNumber_parameters + " INTEGER, "
                + columnMonthlySavings_parameters + " DOUBLE," + columnMonthlyIncome_parameters + " DOUBLE, "
                + columnCreatedInApp_parameters + " BOOLEAN)")
        db?.execSQL(createTableParametersStatement)

        //Creo la tabla SpenceCategory
        val createTableSpenceCategoryStatement = ("CREATE TABLE " + table_spencecategory + "(" + columnSpenceID_spencecategory + " INTEGER, "
                + columnSpenceDescription_spencecategory + " TEXT, " + columnCreatedinApp_spencecategory + " BOOLEAN)")
        db?.execSQL(createTableSpenceCategoryStatement)

        //Creo la tabla Spences
        val createTableSpencesStatement = ("CREATE TABLE " + table_spences + "(" + columnId_Spences + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + columnSpenceId_Spences + " INTEGER, " + columnSpence_Spences + " DOUBLE, " + columnDspences_Spences + " TEXT, " + columnDia_Spences +
                " TEXT, " + columnDiaNumber_Spences + " INTEGER, " + columnDiaSpences_Spences + " INTEGER, " + columnMSpences_Spences + " TEXT, "
                + columnMonthNumber_Spences + " INTEGER, " + columnYSpences_Spences + " INTEGER, " + columnCreatedInApp_Spences + " BOOLEAN, "
                + columnBankDebit_Spences + " BOOLEAN, " + columnDebitUniqueIdentifier_Spences + " TEXT)")
        db?.execSQL(createTableSpencesStatement)

        //Creo la tabla PMarketPrices
        val createTableMarketPricesStatement = ("CREATE TABLE " + table_marketPrices + "(" + columnCompanyId_marketPrices + " INTEGER, "
                + columnMarketPrice_marketPrices + " DOUBLE, " + columnFechaMP_marketPrices + " TEXT, " + columnCreatedInApp_marketprices +  " BOOLEAN)")
        db?.execSQL(createTableMarketPricesStatement)

        //Creo la tabla MarketPriceSeguimiento
        val createTableMarketPriceSeguimientoStatement = ("CREATE TABLE " + table_marketPriceSeguimiento + "(" + columnid_marketPriceSeguimiento + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + columnCompanyId_marketPriceSeguimiento + " INTEGER, " + columnDay_marketPriceSeguimiento + " TEXT, " + columnMarketPrice_marketPriceSeguimiento +  " DOUBLE, "
                + columnRecordedDateAndTime_marketPriceSeguimiento + " TEXT, " + columnCreatedInApp_marketPriceSeguimiento + " BOOLEAN)")
        db?.execSQL(createTableMarketPriceSeguimientoStatement)
        
        //Creo la tabla Companies
        val createTableCompaniesStatement = "CREATE TABLE " + table_companies + "(" + columnCompanyId_companies + " INTEGER, " + columnCompany_companies +
                " TEXT, " + columnRealPrice_companies + " DOUBLE, " + columnPonderated_companies + " DOUBLE, " + columnRanking_companies + " INTEGER, " + columnMarketPrice_companies + " DOUBLE, " +
                columnExclude_companies + " TEXT, " + columnImage_companies + " BLOB, " + columnCreatedInApp_companies + " BOOLEAN)"
        db?.execSQL(createTableCompaniesStatement)

        //Creo la tabla Investment
        val createTableInvestmentListsStatement = "CREATE TABLE " + table_investment + "(" + columnCompanyId_investment + " INTEGER, " + columnTotalOwned_investment +
                " INTEGER, " + columnPrecioPromedioCompra_investment + " DOUBLE, " + columnDinero_investment +
                " DOUBLE, " + columnSoloCompradas_investment + " INTEGER, " + columnSoloVendidas_investment + " INTEGER, " + columnUtilidadBancaria_investment + " DOUBLE, " + columnUtilidadAccion_investment +
                " DOUBLE, " + columnComisionCompra_investment + " DOUBLE, " + columnComisionVenta_investment + " DOUBLE)"
        db?.execSQL(createTableInvestmentListsStatement)

        //Creo la tabla Dividendos
        val createTableDividendosStatement = "CREATE TABLE " + table_dividendos + "(" + columnId_dividendos + " INTEGER, " + columnCompanyId_dividendos +
                " INTEGER, " + columnYear_dividendos + " INTEGER, " + columnDividendosEfectivo_dividendos +
                " DOUBLE, " + columnDividendosAccion_dividendos + " DOUBLE, " + columnAccionesPrecedentes_dividendos + " INTEGER, " + columnAccionesPorAccion_dividendos + " DOUBLE, " + columnEfectivoPorAccion_dividendos +
                " DOUBLE, " + columnDistribute_dividendos + " TEXT, " + columnGraph_dividendos + " TEXT, " + columnAvSalePrice_dividendos + " DOUBLE)"
        db?.execSQL(createTableDividendosStatement)

        //Creo la tabla BankInformation
        val createTableBankInformationStatement = "CREATE TABLE " + table_bankinformation + "(" + columnId_bankinformation + " INTEGER, " + columnBankName_bankinformation +
                " TEXT, " + columnInitialBalance_bankinformation + " DOUBLE)"
        db?.execSQL(createTableBankInformationStatement)

        //Creo la tabla BankBalance
        val createTableBankBalanceStatement = "CREATE TABLE " + table_bankbalance + "(" + columnId_bankbalance + " INTEGER PRIMARY KEY AUTOINCREMENT, " + columnBankId_bankbalance + " INTEGER, " + columnOperation_bankbalance +
                " TEXT, " + columnValue_bankbalance + " DOUBLE, " + columnComment_bankbalance +
                " TEXT, " + columnRecordDate_bankbalance + " TEXT, " + columnLastEditDate_bankbalance + " TEXT, " + columnCreatedInApp_bankbalance + " BOOLEAN, " + columnMSpences_BankBalance + " TEXT, " +
                columnMonthNumber_BankBalance + " INTEGER, " + columnYSpences_BankBalance + " INTEGER, " + columnBalance_BankBalance + " DOUBLE, " + columnDebitUniqueIdentifier_BankBalance + " TEXT)"
        db?.execSQL(createTableBankBalanceStatement)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun delete(tableName: String?, que: String? = null) {
        try {
            val db = this.readableDatabase
            db.delete(tableName, que, null)
            db.close()
        } catch (ex: Exception) {

        }
    }

    fun updateDeleteMaintenance(maintenanceId: Int): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnDeletedInApp_maintenanceRecords, true)

        val update = db.update(table_maintenancerecords, cv, "Id = " + maintenanceId, null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateMaintenanceIdBasedOnMax(maintenanceId: Int, newMaintenanceId: Int?): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnid_maintenancerecords, newMaintenanceId)

        val update = db.update(table_maintenancerecords, cv, "Id = " + maintenanceId, null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateBankBalanceWhenSyncConfirmed(bankId: Int, recordDate: String, value: Double): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnCreatedInApp_bankbalance, false)

        val update = db.update(table_bankbalance, cv, "BankId = " + bankId.toString() + " AND RecordDate = '" + recordDate + "' AND Value = " + value.toString(), null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateSpencesWhenSyncConfirmed(spenceid: Int, dspences: String, spence: Double): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnCreatedInApp_Spences, false)

        val update = db.update(table_spences, cv, "SpenceId = " + spenceid.toString() + " AND DSpences = '" + dspences + "' AND Spence = " + spence.toString(), null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateCarMaintenancesWhenSyncConfirmed(modelId: Int, maintenanceName: String, lastEditDate: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnCreatedInApp_maintenanceRecords, false)

        val update = db.update(table_maintenancerecords, cv, "ModelId = " + modelId.toString() + " AND MaintenanceName = '" + maintenanceName + "' AND LastEditDate = '" + lastEditDate + "'", null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun addOneBankInformation(bankInformationModel: BankInformationModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnId_bankinformation, bankInformationModel.Id)
        cv.put(columnBankName_bankinformation, bankInformationModel.BankName)
        cv.put(columnInitialBalance_bankinformation, bankInformationModel.InitialBalance)
        val insert = db.insert(table_bankinformation, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun addOneMonthNames(monthNamesModel: MonthNamesModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnid_monthNames, monthNamesModel.Id)
        cv.put(columnLanguageId_monthnames, monthNamesModel.LanguageId)
        cv.put(columnMonthNumber_monthnames, monthNamesModel.MonthNumber)
        cv.put(columnMonthName_monthnames, monthNamesModel.MonthName)
        val insert = db.insert(table_monthNames, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun addOneBankSummaries(bankSummariesModel: BankSummariesModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnbankId_banksummaries, bankSummariesModel.Id)
        cv.put(columnbankId_banksummaries, bankSummariesModel.BankId)
        cv.put(columnYSpences_banksummaries, bankSummariesModel.YSpences)
        cv.put(columnbalance_banksummaries, bankSummariesModel.Balance)
        cv.put(columnmonthname_banksummaries, bankSummariesModel.MonthName)
        cv.put(columnmonthnumber_banksummaries, bankSummariesModel.MonthNumber)
        val insert = db.insert(table_banksummaries, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun addOneBankBalance(bankBalanceModel: BankBalanceModel?): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(columnId_bankbalance, bankBalanceModel?.Id)
        cv.put(columnBankId_bankbalance, bankBalanceModel?.BankId)
        cv.put(columnOperation_bankbalance, bankBalanceModel?.Operation)
        cv.put(columnValue_bankbalance, bankBalanceModel?.Value)
        cv.put(columnComment_bankbalance, bankBalanceModel?.Comment)
        cv.put(columnRecordDate_bankbalance, bankBalanceModel?.RecordDate)
        cv.put(columnLastEditDate_bankbalance, bankBalanceModel?.LastEditDate)
        cv.put(columnCreatedInApp_bankbalance, bankBalanceModel?.CreatedInApp)
        cv.put(columnMSpences_BankBalance, bankBalanceModel?.MSpences)
        cv.put(columnMonthNumber_BankBalance, bankBalanceModel?.MonthNumber)
        cv.put(columnYSpences_BankBalance, bankBalanceModel?.YSpences)
        cv.put(columnBalance_BankBalance, bankBalanceModel?.Balance)
        cv.put(columnDebitUniqueIdentifier_BankBalance, bankBalanceModel?.DebitUniqueIdentifier)
        val insert = db.insert(table_bankbalance, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun addOneCarModel(carModelsModel: CarModelsModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnid_carmodels, carModelsModel.Id)
        cv.put(columnbrand_carmodels, carModelsModel.Brand)
        cv.put(columnMileage, carModelsModel.Mileage)
        cv.put(columnEditDateCarModels, carModelsModel.LastEditDate)
        val insert = db.insert(table_carmodels, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun addOneMaintenanceRecords(maintenanceRecordsModel: MaintenanceRecordsModel?): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnid_maintenancerecords, maintenanceRecordsModel?.Id)
        cv.put(columnMaintenanceName_maintenancerecords, maintenanceRecordsModel?.MaintenanceName)
        cv.put(columnComment_maintenancerecords, maintenanceRecordsModel?.Comment)
        cv.put(columnModelId_maintenancerecords, maintenanceRecordsModel?.ModelId)
        cv.put(columnStartKm_maintenancerecords, maintenanceRecordsModel?.startKm)
        cv.put(columnEndKm_maintenancerecords, maintenanceRecordsModel?.endKm)
        cv.put(columnNextAfter_maintenancerecords, maintenanceRecordsModel?.NextAfter)
        cv.put(columnRecordDate_maintenancerecords, maintenanceRecordsModel?.RecordDate)
        cv.put(columnLastEditDate_maintenanceRecords, maintenanceRecordsModel?.LastEditDate)
        cv.put(columnFulfilled_maintenancerecords, maintenanceRecordsModel?.Fulfilled)
        cv.put(columnCreatedInApp_maintenanceRecords, maintenanceRecordsModel?.CreatedInApp)
        cv.put(columnUpdatedInApp_maintenanceRecords, false)
        cv.put(columnDeletedInApp_maintenanceRecords, false)
        val insert = db.insert(table_maintenancerecords, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun updateOneMaintenanceRecords(
        maintenanceRecordsModel: MaintenanceRecordsModel?,
        maintenanceName: String,
        fulfilled: Int
    ): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnMaintenanceName_maintenancerecords, maintenanceRecordsModel?.MaintenanceName)
        cv.put(columnComment_maintenancerecords, maintenanceRecordsModel?.Comment)
        cv.put(columnStartKm_maintenancerecords, maintenanceRecordsModel?.startKm)
        cv.put(columnEndKm_maintenancerecords, maintenanceRecordsModel?.endKm)
        cv.put(columnNextAfter_maintenancerecords, maintenanceRecordsModel?.NextAfter)
        cv.put(columnRecordDate_maintenancerecords, maintenanceRecordsModel?.RecordDate)
        cv.put(columnLastEditDate_maintenanceRecords, maintenanceRecordsModel?.LastEditDate)
        cv.put(columnFulfilled_maintenancerecords, maintenanceRecordsModel?.Fulfilled)
       // cv.put(columnCreatedInApp_maintenanceRecords, maintenanceRecordsModel?.CreatedInApp)
        cv.put(columnUpdatedInApp_maintenanceRecords, true)
        val update = db.update(
            table_maintenancerecords,
            cv,
            "MaintenanceName = '" + maintenanceName + "' AND Fulfilled = " + fulfilled,
            null
        )
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun addOneSpenceCategory(spenceCategoryModel: SpenceCategoryModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnSpenceID_spencecategory, spenceCategoryModel.SpenceId)
        cv.put(columnSpenceDescription_spencecategory, spenceCategoryModel.SpenceDescription)
        cv.put(columnCreatedinApp_spencecategory, spenceCategoryModel.CreatedInApp)
        val insert = db.insert(table_spencecategory, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun addOneSelfLoans(selfLoansModel: SelfLoansModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnInstallmentPayedDate_SelfLoans, selfLoansModel.DateInstallment)
        cv.put(columnAmount_SelfLoans, selfLoansModel.Amount)
        cv.put(columnAlreadyPayed_SelfLoans, selfLoansModel.AlreadyPayed)
        cv.put(columnYourCurrentDebt_SelfLoans, selfLoansModel.YourCurrentDebt)
        cv.put(columnEachInstallment_SelfLoans, selfLoansModel.EachInstallment)
        val insert = db.insert(table_SelfLoans, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }
    fun addOneSpences(spencesModel: SpencesModel?): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        //  cv.put(columnId_Spences, spencesModel.id)
        cv.put(columnSpenceId_Spences, spencesModel?.SpenceId)
        cv.put(columnSpence_Spences, spencesModel?.Spence)
        cv.put(columnDspences_Spences, spencesModel?.DSpences)
        cv.put(columnDia_Spences, spencesModel?.dia)
        cv.put(columnDiaNumber_Spences, spencesModel?.diaNumber)
        cv.put(columnDiaSpences_Spences, spencesModel?.diaSpences)
        cv.put(columnMSpences_Spences, spencesModel?.MSpences)
        cv.put(columnMonthNumber_Spences, spencesModel?.monthNumber)
        cv.put(columnYSpences_Spences, spencesModel?.YSpences)
        cv.put(columnCreatedInApp_Spences, spencesModel?.createdInApp)
        cv.put(columnBankDebit_Spences, spencesModel?.BankDebit)
        cv.put(columnDebitUniqueIdentifier_Spences,spencesModel?.DebitUniqueIdentifier)
        val insert = db.insert(table_spences, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun updateOneSpences(spencesModel: SpencesModel?, fecha: String, spenceId: Int, debit: Boolean, updateUniqueIdentifier: Boolean): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnSpence_Spences, spencesModel?.Spence)
        cv.put(columnCreatedInApp_Spences, spencesModel?.createdInApp)
        if(updateUniqueIdentifier == true){
            cv.put(columnDebitUniqueIdentifier_Spences, spencesModel?.DebitUniqueIdentifier)
        }
        if(debit == true){
            cv.put(columnBankDebit_Spences, spencesModel?.BankDebit)
        }else{
              if(spencesModel?.Spence == 0.0){   //if debit is false and spence is 0.0, I assure that bankDebit is 0 so spinnerBankDebit does not show it as bank debit
                  cv.put(columnBankDebit_Spences, spencesModel?.BankDebit)
               }
        }
        val update = db.update(
            table_spences,
            cv,
            "DSpences = '" + fecha + "' AND SpenceId = " + spenceId,
            null
        )
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateOneSpencesCreatedInApp(spencesModel: SpencesModel?, spenceId: Int): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(columnId_bankbalance, bankBalanceModel?.Id)
        cv.put(columnCreatedInApp_Spences, spencesModel?.createdInApp)

        val update = db.update(table_spences, cv, "id = " + spenceId.toString(), null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateOneMaintenancesCreatedInApp(
        maintenanceRecordsModel: MaintenanceRecordsModel?,
        maintenanceId: Int
    ): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(columnId_bankbalance, bankBalanceModel?.Id)
        cv.put(columnCreatedInApp_maintenanceRecords, maintenanceRecordsModel?.CreatedInApp)
       // cv.put(columnUpdatedInApp_maintenanceRecords, maintenanceRecordsModel?.UpdatedInApp)
        val update = db.update(
            table_maintenancerecords,
            cv,
            "Id = " + maintenanceId.toString(),
            null
        )
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateOneMarketPriceCreatedInApp(marketPricesModel: MarketPricesModel?, companyId: Int): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(columnId_bankbalance, bankBalanceModel?.Id)
        cv.put(columnCreatedInApp_marketprices, marketPricesModel?.CreatedInApp)

        val update = db.update(table_marketPrices, cv, "CompanyID = " + companyId.toString(), null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateCarModel(carModelsModel: CarModelsModel?, modelName: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnMileage, carModelsModel?.Mileage)
        cv.put(columnEditDateCarModels, carModelsModel?.LastEditDate)

        val update = db.update(table_carmodels, cv, "Brand = '" + modelName + "'", null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateOneBankBalance(
        bankBalanceModel: BankBalanceModel?,
        fecha: String,
        bankId: Int,
        valor: String
    ): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(columnId_bankbalance, bankBalanceModel?.Id)
        cv.put(columnValue_bankbalance, bankBalanceModel?.Value)
        cv.put(columnComment_bankbalance, bankBalanceModel?.Comment)
        cv.put(columnLastEditDate_bankbalance, bankBalanceModel?.LastEditDate)
        cv.put(columnCreatedInApp_bankbalance, bankBalanceModel?.CreatedInApp)
        cv.put(columnBalance_BankBalance, bankBalanceModel?.Balance)

        val update = db.update(
            table_bankbalance,
            cv,
            "RecordDate = '" + fecha + "' AND BankId = " + bankId.toString() + " AND Value = " + valor,
            null
        )
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateOneBankBalanceCreatedInApp(bankBalanceModel: BankBalanceModel?, bankBalanceId: Int): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(columnId_bankbalance, bankBalanceModel?.Id)
        cv.put(columnCreatedInApp_bankbalance, bankBalanceModel?.CreatedInApp)

        val update = db.update(table_bankbalance, cv, "Id = " + bankBalanceId.toString(), null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }
    fun deleteOneBankBalance(fecha: String, bankId: Int, valor: String): Boolean {
        val db = this.writableDatabase
        val delete = db.delete(
            table_bankbalance,
            "RecordDate = '" + fecha + "' AND BankId = " + bankId.toString() + " AND Value = " + valor,
            null
        )
        return if (delete == -1) {
            false
        } else {
            true
        }
    }

    fun deleteOneBankBalanceBasedOnUniqueIdentifier(uniqueIdentifier: String): Boolean {
        val db = this.writableDatabase
        val delete = db.delete(
                table_bankbalance,
                "DebitUniqueIdentifier = '" + uniqueIdentifier + "'",
                null
        )
        return if (delete == -1) {
            false
        } else {
            true
        }
    }

        fun addOneParameters(parametersModel: ParametersModel?): Boolean {
            val db = this.writableDatabase
            val cv = ContentValues()
           // cv.put(columnId_parameters, parametersModel?.Id)
            cv.put(columnYear_parameters, parametersModel?.Year)
            cv.put(columnMonth_parameters, parametersModel?.Month)
            cv.put(columnMonthNumber_parameters, parametersModel?.MonthNumber)
            cv.put(columnMonthlySavings_parameters, parametersModel?.MonthlySavings)
            cv.put(columnMonthlyIncome_parameters, parametersModel?.MonthlyIncome)
            cv.put(columnCreatedInApp_parameters, parametersModel?.CreatedInApp)
            val insert = db.insert(table_parameters, null, cv)
            return if (insert == -1L) {
                false
            } else {
                true
            }
        }

    fun updateOneParameters(parametersModel: ParametersModel?, year: Int?, monthNumber: Int?): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        // cv.put(columnId_parameters, parametersModel?.Id)
        cv.put(columnMonthlyIncome_parameters, parametersModel?.MonthlyIncome)
        cv.put(columnCreatedInApp_parameters, parametersModel?.CreatedInApp)

        val update = db.update(
            table_parameters,
            cv,
            "Year = " + year + " AND MonthNumber = " + monthNumber,
            null
        )
        return if (update == -1) {
            false
        } else {
            true
        }
    }
        fun addOneDividendos(dividendosModel: DividendosModel): Boolean {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(columnId_dividendos, dividendosModel.id)
            cv.put(columnCompanyId_dividendos, dividendosModel.CompanyId)
            cv.put(columnYear_dividendos, dividendosModel.Year)
            cv.put(columnDividendosEfectivo_dividendos, dividendosModel.DividendosEfectivo)
            cv.put(columnDividendosAccion_dividendos, dividendosModel.DividendosAccion)
            cv.put(columnAccionesPrecedentes_dividendos, dividendosModel.AccionesPrecedentes)
            cv.put(columnAccionesPorAccion_dividendos, dividendosModel.AccionesPorAccion)
            cv.put(columnEfectivoPorAccion_dividendos, dividendosModel.EfectivoPorAccion)
            cv.put(columnDistribute_dividendos, dividendosModel.Distribute)
            cv.put(columnGraph_dividendos, dividendosModel.Graph)
            cv.put(columnAvSalePrice_dividendos, dividendosModel.AvSalePrice)
            val insert = db.insert(table_dividendos, null, cv)
            return if (insert == -1L) {
                false
            } else {
                true
            }
        }

        fun addOneCompanies(companiesModel: CompaniesModel): Boolean {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(columnCompanyId_companies, companiesModel.CompanyId)
            cv.put(columnCompany_companies, companiesModel.Company)
            cv.put(columnRealPrice_companies, companiesModel.RealPrice)
            cv.put(columnPonderated_companies, companiesModel.Ponderated)
            cv.put(columnRanking_companies, companiesModel.Ranking)
            cv.put(columnMarketPrice_companies, companiesModel.MarketPrice)
            cv.put(columnExclude_companies, companiesModel.Exclude)
            cv.put(columnImage_companies, companiesModel.Image)
            cv.put(columnCreatedInApp_companies, companiesModel.CreatedInApp)
            val insert = db.insert(table_companies, null, cv)
            return if (insert == -1L) {
                false
            } else {
                true
            }
        }

        fun addOneInvestment(investmentModel: InvestmentModel): Boolean {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(columnCompanyId_investment, investmentModel.CompanyId)
            cv.put(columnTotalOwned_investment, investmentModel.TotalOwned)
            cv.put(columnPrecioPromedioCompra_investment, investmentModel.PrecioPromedioCompra)
            cv.put(columnDinero_investment, investmentModel.Dinero)
            cv.put(columnSoloCompradas_investment, investmentModel.SoloCompradas)
            cv.put(columnSoloVendidas_investment, investmentModel.SoloVendidas)
            cv.put(columnUtilidadBancaria_investment, investmentModel.UtilidadBancaria)
            cv.put(columnUtilidadAccion_investment, investmentModel.UtilidadAccion)
            cv.put(columnComisionCompra_investment, investmentModel.ComisionCompra)
            cv.put(columnComisionVenta_investment, investmentModel.ComisionVenta)
            val insert = db.insert(table_investment, null, cv)
            return if (insert == -1L) {
                false
            } else {
                true
            }
        }

        fun addOneMarketPriceSeguimiento(marketPriceSeguimientoModel: MarketPriceSeguimientoModel?): Boolean {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(columnCompanyId_marketPriceSeguimiento, marketPriceSeguimientoModel?.CompanyId)
            cv.put(columnDay_marketPriceSeguimiento, marketPriceSeguimientoModel?.Day)
            cv.put(
                columnMarketPrice_marketPriceSeguimiento,
                marketPriceSeguimientoModel?.MarketPrice
            )
            cv.put(
                columnRecordedDateAndTime_marketPriceSeguimiento,
                marketPriceSeguimientoModel?.RecordedDateAndTime
            )
            cv.put(
                columnCreatedInApp_marketPriceSeguimiento,
                marketPriceSeguimientoModel?.CreatedInApp
            )
            val insert = db.insert(table_marketPriceSeguimiento, null, cv)
            return if (insert == -1L) {
                false
            } else {
                true
            }
        }

    fun addOneMarketPrices(marketPricesModel: MarketPricesModel?): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnCompanyId_marketPrices, marketPricesModel?.CompanyId)
        cv.put(columnMarketPrice_marketPrices, marketPricesModel?.MarketPrice)
        cv.put(columnFechaMP_marketPrices, marketPricesModel?.FechaMP)
        cv.put(columnCreatedInApp_marketprices, marketPricesModel?.CreatedInApp)
        val insert = db.insert(table_marketPrices, null, cv)
        return if (insert == -1L) {
            false
        } else {
            true
        }
    }

    fun updateOneMarketPrices(marketPricesModel: MarketPricesModel?, companyId: Int): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnMarketPrice_marketPrices, marketPricesModel?.MarketPrice)
       cv.put(columnFechaMP_marketPrices, marketPricesModel?.FechaMP)
       cv.put(columnCreatedInApp_marketprices, marketPricesModel?.CreatedInApp)
        val update = db.update(table_marketPrices, cv, "CompanyID = " + companyId.toString(), null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

    fun updateOneCompanies(companiesModel: CompaniesModel?, companyId: Int, success: Boolean): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        if(success == true){
            cv.put(columnMarketPrice_marketPrices, companiesModel?.MarketPrice)
        }else{
            cv.put(columnExclude_companies, companiesModel?.Exclude)
        }
        cv.put(columnCreatedInApp_companies, companiesModel?.CreatedInApp)
        val update = db.update(table_companies, cv, "CompanyId = " + companyId.toString(), null)
        return if (update == -1) {
            false
        } else {
            true
        }
    }

        fun getAllBanks(que: Int): List<String> {
            val labels: MutableList<String> = ArrayList()
            val labelIds: MutableList<String> = ArrayList()
            labels.clear()
            labelIds.clear()
            var noBank = true

            // Select All Query from CatMap
            val selectQuery = "SELECT  * FROM " + table_bankinformation + " ORDER BY " + columnBankName_bankinformation
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    noBank = false
                    labels.add(cursor.getString(1))
                    labelIds.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }

            if (noBank == true) {
                labels.add("NoBank")
                labelIds.add("0")
            }
            // closing connection
            cursor.close()
            db.close()
            return if (que == 1) {
                // returning lables
                labels
            } else {
                labelIds
            }
        }

    fun getIfAnyCarmaintenanceWasRecordedInApp(filtro: String): Boolean? {

          var anymaintenanceAddedInApp: Boolean? = false

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_maintenancerecords + filtro
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                anymaintenanceAddedInApp = true
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()

        return anymaintenanceAddedInApp
    }

    fun RecalculateCarmaintenanceIdBasedOnMaxFromMainDatabase(filtro: String, newId: Int?) {

        var recalculateNewId: Int? = newId
        var oldId: Int = 0
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_maintenancerecords + filtro
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               oldId = cursor.getInt(0)

                if (recalculateNewId != null) {
                    recalculateNewId = recalculateNewId + 1
                }
                updateMaintenanceIdBasedOnMax(oldId, recalculateNewId)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
    }

    fun getIfAnyCompanyEditedInApp(filtro: String): Boolean? {

        var anyCompanyEditedInApp: Boolean? = false

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_companies + filtro
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                anyCompanyEditedInApp = true
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()

        return anyCompanyEditedInApp
    }

    fun getIfAnyMarketPriceEditedInApp(filtro: String): Boolean? {

        var anyCompanyEditedInApp: Boolean? = false

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_marketPrices + filtro
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                anyCompanyEditedInApp = true
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()

        return anyCompanyEditedInApp
    }

    fun getIfAnyMarketPriceSeguiminetoRecord(): Boolean? {

        var anyMarketPriceSeguimientoRecord: Boolean? = false

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_marketPriceSeguimiento + " Where CreatedInApp = 1"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                anyMarketPriceSeguimientoRecord = true
                break;
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()

        return anyMarketPriceSeguimientoRecord
    }

    fun getMaxMaintenanceId(filtro: String, que: String): Int? {

        var maxMaintenanceId: Int = 0

        // Select All Query from CatMap
        val selectQuery = "SELECT  " + que + "(Id) FROM " + table_maintenancerecords + filtro
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                maxMaintenanceId = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()

        return maxMaintenanceId
    }

        fun getAllCars(que: Int): List<String> {
            val labels: MutableList<String> = ArrayList()
            val labelIds: MutableList<String> = ArrayList()
            labels.clear()
            labelIds.clear()
            var noBank = true

            // Select All Query from CatMap
            val selectQuery = "SELECT  * FROM " + table_carmodels + " ORDER BY " + columnbrand_carmodels
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    noBank = false
                    labels.add(cursor.getString(1))
                    labelIds.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }

            if (noBank == true) {
                labels.add("NoCar")
                labelIds.add("0")
            }
            // closing connection
            cursor.close()
            db.close()
            return if (que == 1) {
                // returning lables
                labels
            } else {
                labelIds
            }
        }

    fun getMileageANDModelId(brandName: String, que: Int): Int {

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_carmodels + " WHERE Brand = '" + brandName + "'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var mileage: Int = 0
        var modelId: Int = 0
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mileage =  cursor.getInt(2)
                modelId = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        if(que== 1){
            return mileage
        }else{
            return modelId
        }

        // closing connection
        cursor.close()
        db.close()
    }


        fun getCompanies(que: Int): List<String> {
            val labels: MutableList<String> = ArrayList()
            val labelIds: MutableList<String> = ArrayList()
            labels.clear()
            labelIds.clear()
            var noBank = true

            // Select All Query from CatMap
            val selectQuery = "SELECT  * FROM " + table_companies + " ORDER BY " + columnCompany_companies
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    noBank = false
                    labels.add(cursor.getString(1))
                    labelIds.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }

            if (noBank == true) {
                labels.add("NoCompany")
                labelIds.add("0")
            }
            // closing connection
            cursor.close()
            db.close()
            return if (que == 1) {
                // returning lables
                labels
            } else {
                labelIds
            }
        }

        fun getYears(que: Int): List<String> {
            val labels: MutableList<String> = ArrayList()
            val labelIds: MutableList<String> = ArrayList()
            labels.clear()
            labelIds.clear()
            var noBank = true

            // Select All Query from CatMap
            val selectQuery = "SELECT id, Year FROM Dividendos Group By Year"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var lastYear: Int = 0
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    noBank = false
                    labels.add(cursor.getString(1))
                    lastYear = cursor.getInt(1)
                    labelIds.add(cursor.getString(0))
                } while (cursor.moveToNext())

                var yearAdded: Int = 2020
                yearAdded = getLastYear()
                var yearMasUno: Int = 1 + yearAdded

                for (i in yearMasUno..yearMasUno + 5) {
                    labels.add(i.toString())
                }
            }

            if (noBank == true) {
                labels.add("NoYears")
                labelIds.add("0")
            }
            // closing connection
            cursor.close()
            db.close()
            return if (que == 1) {
                // returning lables
                labels
            } else {
                labelIds
            }
        }

        fun getLastYear(): Int {
            var maxYear: Int = 0

            // Select All Query from CatMap
            val selectQuery = "SELECT id, Max(Year)AS Year FROM Dividendos"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    maxYear = cursor.getInt(1)

                } while (cursor.moveToNext())
            }
            // closing connection
            cursor.close()
            db.close()

            return maxYear
        }

        fun getSpenceCategories(que: Int): List<String> {
            val labels: MutableList<String> = ArrayList()
            val labelIds: MutableList<String> = ArrayList()
            labels.clear()
            labelIds.clear()
            var noBank = true

            // Select All Query from CatMap
            val selectQuery = "SELECT  * FROM " + table_spencecategory + " ORDER BY " + columnSpenceDescription_spencecategory
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    noBank = false
                    labels.add(cursor.getString(1))
                    labelIds.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }

            if (noBank == true) {
                labels.add("NoSpences")
                labelIds.add("0")
            }
            // closing connection
            cursor.close()
            db.close()
            return if (que == 1) {
                // returning lables
                labels
            } else {
                labelIds
            }
        }

    fun getMonthBudget(month: String, year: Int?): Double {
        var presupuesto: Double = 0.00

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_parameters + " WHERE Month = '" + month + "' AND Year = " + year.toString()
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                presupuesto = cursor.getDouble(5)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
       return presupuesto
    }


    fun getAvailableBudget(): Double {
        var yourCurrentDebt: Double = 0.00
        var alreadyPayed: Double = 0.0
        var availableBudget: Double = 0.0

        // Select All Query from CatMap
        val selectQuery = "SELECT SUM(YourCurrentDebt) AS YourCurrentDebt, SUM(AlreadyPayed) AS AlreadyPayed FROM " + table_SelfLoans
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                yourCurrentDebt = cursor.getDouble(0)
                alreadyPayed = cursor.getDouble(1)
            } while (cursor.moveToNext())
        }

        availableBudget = yourCurrentDebt - alreadyPayed
        // closing connection
        cursor.close()
        db.close()
        return availableBudget
    }

        fun getAllMaintenances(que: Int, brand: String, fulfilled: Int, endKm: Int): List<String> {
            val labels: MutableList<String> = ArrayList()
            val labelIds: MutableList<String> = ArrayList()
            labels.clear()
            labelIds.clear()
            var fulfilledCreatedInApp: String? = null
            if(fulfilled == 1){
                fulfilledCreatedInApp = " AND UpdatedInApp = 1 AND Deleted = 0"
            }else{
                fulfilledCreatedInApp = " AND Deleted = 0"
            }
            var noBank = true
            var selectQuery: String? = null
            // Select All Query from CatMap
            if (que == 1) {
                selectQuery = "SELECT MaintenanceRecords.Id, MaintenanceRecords.MaintenanceName" +
                        " FROM CarModels INNER JOIN" +
                        " MaintenanceRecords ON CarModels.Id = MaintenanceRecords.ModelId WHERE MaintenanceRecords.Deleted = 0 AND CarModels.Brand = '" + brand +
                        "' AND MaintenanceRecords.Fulfilled = " + fulfilled.toString() + fulfilledCreatedInApp
            } else {
                selectQuery = "SELECT MaintenanceRecords.Id, MaintenanceRecords.MaintenanceName" +
                        " FROM CarModels INNER JOIN" +
                        " MaintenanceRecords ON CarModels.Id = MaintenanceRecords.ModelId WHERE MaintenanceRecords.Deleted = 0 AND CarModels.Brand = '" + brand +
                        "' AND MaintenanceRecords.Fulfilled = 0 AND MaintenanceRecords.endKm <= " + (endKm) + fulfilledCreatedInApp
            }


            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    noBank = false
                    labels.add(cursor.getString(1))
                    labelIds.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }

            if (noBank == true) {
                labels.add("NoMaintenance")
                labelIds.add("0")
            }
            // closing connection
            cursor.close()
            db.close()
            return if (que == 1) {
                // returning lables
                labels
            } else {
                labels
                // labelIds
            }
        }

    fun getMaintenanceId(brand: String, maintenanceName: String): Int {

        var selectQuery: String = "SELECT MaintenanceRecords.Id, MaintenanceRecords.MaintenanceName" +
                    " FROM CarModels INNER JOIN" +
                    " MaintenanceRecords ON CarModels.Id = MaintenanceRecords.ModelId WHERE CarModels.Brand = '" +
                brand + "' AND MaintenanceRecords.MaintenanceName = '" + maintenanceName + "'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var maintenanceId: Int = 0
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                maintenanceId = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()

        return maintenanceId
    }

        fun getAllSavedRecords(que: Int, bankName: String?, recordDate: String?, mas: String = ""): List<String> {
            val labels: MutableList<String> = ArrayList()
            val labelIds: MutableList<String> = ArrayList()
            labels.clear()
            labelIds.clear()
            var noBank = true

            var queMas: String? = null
            if (que == 1) {
                queMas = ""
            } else {
                queMas = " AND BankBalance.Value = " + mas
            }


            // Select All Query from CatMap
            val selectQuery = "SELECT BankInformation.BankName, BankBalance.Operation, " +
                    " BankBalance.Value,  BankBalance.Comment FROM  BankBalance INNER JOIN " +
                    "  BankInformation ON  BankBalance.BankId =  BankInformation.Id " +
                    " Where BankInformation.BankName = '" + bankName + "' AND BankBalance.RecordDate = '" + recordDate + "T00:00:00'" + queMas + " ORDER BY BankBalance.Id DESC"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    noBank = false
                    labels.add(cursor.getString(2))
                    labelIds.add(cursor.getString(3))
                } while (cursor.moveToNext())
            }

            if (noBank == true) {
                labels.add("NoBankExpense")
                labelIds.add("No comment")
            }else{

            }
            // closing connection
            cursor.close()
            db.close()
            return if (que == 1) {
                // returning lables
                labels
            } else {
                labelIds
            }
        }

        fun getTotalAllBanks(): Double {

            // Select All Query from CatMap
            val selectQuery = "SELECT Id, Sum(Value) AS Value FROM BankBalance"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: Double = 0.00
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    total = cursor.getDouble(1)
                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

        fun getTotalInitialBalanceAllBanks(): Double {

            // Select All Query from CatMap
            val selectQuery = "SELECT Id, Sum(InitialBalance) AS InitialBalace FROM BankInformation"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: Double = 0.00
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    total = cursor.getDouble(1)
                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

        fun getTotalOneBank(bankName: String): Double {

            // Select All Query from CatMap
            val selectQuery = "SELECT BankBalance.Id, Sum(BankBalance.Value) AS Value" +
                    " FROM BankBalance INNER JOIN BankInformation ON BankBalance.BankId = BankInformation.Id WHERE BankInformation.BankName = '" + bankName + "'"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: Double = 0.00
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    total = cursor.getDouble(1)
                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

        fun getTotalInitialBalanceOneBank(bankName: String): Double {

            // Select All Query from CatMap
            val selectQuery = "SELECT Id, Sum(InitialBalance) AS InitialBalace FROM BankInformation Where BankName = '" + bankName + "'"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: Double = 0.00
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    total = cursor.getDouble(1)
                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

        fun getFromCompanies(que: Int, companyName: String): Double {

            // Select All Query from CatMap
            val selectQuery = "SELECT * FROM Companies Where Company = '" + companyName + "'"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: Double = 0.00
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    when (que) {
                        1 -> total = cursor.getDouble(4)
                        2 -> total = cursor.getDouble(2)
                        3 -> total = cursor.getDouble(5)
                        else -> { // Note the block
                            total = cursor.getDouble(6)
                        }
                    }

                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

        fun getFromCompaniesExclude(companyName: String): String {

            // Select All Query from CatMap
            val selectQuery = "SELECT * FROM Companies Where Company = '" + companyName + "'"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: String = ""
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    total = cursor.getString(6)

                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

        fun getFromMaintenances(
            que: Int,
            brandName: String,
            maintenanceName: String,
            fulfilled: Int
        ): String? {

            // Select All Query from CatMap
            val selectQuery = "SELECT CarModels.Brand, MaintenanceRecords.MaintenanceName, " +
                    "MaintenanceRecords.Comment, MaintenanceRecords.startKm, MaintenanceRecords.endKm, " +
                    "MaintenanceRecords.NextAfter, MaintenanceRecords.RecordDate, MaintenanceRecords.Fulfilled " +
                    "FROM CarModels INNER JOIN MaintenanceRecords ON CarModels.Id = MaintenanceRecords.ModelId " +
                    "WHERE CarModels.Brand = '" + brandName + "' AND MaintenanceRecords.MaintenanceName = '" +
                    maintenanceName + "' AND MaintenanceRecords.Fulfilled = " + fulfilled
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: String? = null
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    when (que) {
                        1 -> total = cursor.getString(2)
                        2 -> total = cursor.getString(3)
                        3 -> total = cursor.getString(4)
                        4 -> total = cursor.getString(5)
                        5 -> total = cursor.getString(6)
                        else -> { // Note the block
                            total = cursor.getString(7)
                        }
                    }
                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

        fun getFromSpences(
            que: Int,
            spenceName: String,
            spenceDate: String,
            monthNumber: Int?,
            year: String
        ): Double {

            var selectQuery: String? = null
            // Select All Query from CatMap
            when (que) {
                1 -> selectQuery =
                    "SELECT SpenceCategory.SpenceDescription, Spences.Spence, Spences.DSpences, Spences.MSpences, Spences.YSpence" +
                            " FROM Spences INNER JOIN" +
                            " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID Where SpenceCategory.SpenceDescription = '" +
                            spenceName + "' And DSpences = '" + spenceDate + "T12:00:00'"
                2 -> selectQuery = "SELECT Sum(Spences.Spence) AS Spence" +
                        " FROM Spences INNER JOIN" +
                        " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID Where SpenceCategory.SpenceDescription = '" +
                        spenceName + "' And MonthNumber = " + monthNumber + " AND Spences.YSpence = " + year
                3 -> selectQuery = "SELECT Sum(Spences.Spence) AS Spence" +
                        " FROM Spences INNER JOIN" +
                        " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID Where MonthNumber = " + monthNumber + " AND Spences.YSpence = " + year
                4 -> selectQuery = "SELECT Spences.CreatedInApp" +
                        " FROM Spences INNER JOIN" +
                        " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID Where SpenceCategory.SpenceDescription = '" +
                        spenceName + "' And DSpences = '" + spenceDate + "T12:00:00'"
                else -> { // Note the block
                    selectQuery = "SELECT MonthlyIncome FROM Parameters WHERE MonthNumber = " + monthNumber + " AND Year = " + year
                }
            }

            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var total: Double = 0.00
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    when (que) {
                        1 -> total = cursor.getDouble(1)
                        2 -> total = cursor.getDouble(0)
                        3 -> total = cursor.getDouble(0)
                        4 -> total = cursor.getDouble(0)
                        else -> { // Note the block
                            total = cursor.getDouble(0)
                        }
                    }

                } while (cursor.moveToNext())
            }
            db.close()
            return total
            // closing connection
        }

    fun getFromSpencesBankDebit(
            spenceName: String,
            spenceDate: String
    ): Int {

        var selectQuery: String? = null
        selectQuery =
                "SELECT SpenceCategory.SpenceDescription, Spences.BankDebit" +
                        " FROM Spences INNER JOIN" +
                        " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID Where SpenceCategory.SpenceDescription = '" +
                        spenceName + "' And DSpences = '" + spenceDate + "T12:00:00'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var total: Int = 0
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                total = cursor.getInt(1)
            } while (cursor.moveToNext())
        }
        db.close()
        return total
        // closing connection
    }

    fun getSpencesCreatedInApp(spenceName: String, spenceDate: String): String? {

        var selectQuery: String? = null

          selectQuery = "SELECT Spences.CreatedInApp" +
                    " FROM Spences INNER JOIN" +
                    " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID Where SpenceCategory.SpenceDescription = '" +
                    spenceName + "' And DSpences = '" + spenceDate + "T12:00:00'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var enApp: String? = null
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                enApp = cursor.getString(0)

            } while (cursor.moveToNext())
        }
        db.close()
        return enApp
        // closing connection
    }

    fun getFromSpencesSpenceIdAndDay(id: Int, que: Int): String? {

        var selectQuery: String? = null

        selectQuery = "SELECT Spences.SpenceId, Spences.DSpences" +
                " FROM Spences INNER JOIN" +
                " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID Where Spences.id = " + id.toString()

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var spenceId: String? = null
        var diaSpence: String? = null
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                spenceId = cursor.getString(0)
                diaSpence = cursor.getString(1)
            } while (cursor.moveToNext())
        }
        db.close()
        if(que==0){
            return spenceId
        }else{
            return diaSpence
        }

        // closing connection
    }

    fun getFromMarketPRiceSeguimientoDay(id: Int, que: Int): String? {

        var selectQuery: String? = null

        selectQuery = "SELECT * From MarketPriceSeguimiento WHERE id = " + id.toString()

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var dia: String? = null
        var companyId: String? = null
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dia = cursor.getString(2)
                companyId = cursor.getString(1)
            } while (cursor.moveToNext())
        }
        db.close()

        if(que == 1){
            return dia
        }else{
            return companyId
        }


        // closing connection
    }
    fun getFromBankBalanceBankIdAndDay(id: Int, que: Int): String? {

        var selectQuery: String? = null

        selectQuery = "SELECT BankBalance.BankId, BankBalance.RecordDate, Operation" +
                " FROM BankBalance Where Id = " + id.toString()

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var bankId: String? = null
        var dia: String? = null
        var operation: String? = null
        var outcome: String? = null
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                when(que){
                    0 -> bankId = cursor.getString(0)
                    1 -> dia = cursor.getString(1)
                    2 -> operation = cursor.getString(2)
                }
            } while (cursor.moveToNext())
        }
        db.close()

        when(que){
            0 -> outcome = bankId
            1 -> outcome = dia
            2 -> outcome = operation
        }
        return outcome
    }

    fun getParametersCreatedInApp(year: Int, monthNumber: Int): String? {

        var selectQuery: String? = null

        selectQuery = "SELECT CreatedInApp " +
                " FROM Parameters Where Year = " + year + " AND MonthNumber = " + monthNumber

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var enApp: String? = null
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                enApp = cursor.getString(0)

            } while (cursor.moveToNext())
        }
        db.close()
        return enApp
        // closing connection
    }

        fun getSpenceId(spenceName: String): Int {

            // Select All Query from CatMap
            val selectQuery = "SELECT  * FROM " + table_spencecategory + " WHERE " + columnSpenceDescription_spencecategory + " = '" + spenceName + "'"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var spenceId: Int = 0
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    spenceId = cursor.getInt(0)
                } while (cursor.moveToNext())
            }

            // closing connection
            cursor.close()
            db.close()
            return spenceId
        }

    fun getCompanyId(companyName: String): Int {

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_companies + " WHERE " + columnCompany_companies + " = '" + companyName + "'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var companyId: Int = 0
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                companyId = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return companyId
    }


    fun getBankId(bankName: String): Int {

            // Select All Query from CatMap
            val selectQuery = "SELECT  * FROM " + table_bankinformation + " WHERE " + columnBankName_bankinformation + " = '" + bankName + "'"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var bankId: Int = 0
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    bankId = cursor.getInt(0)
                } while (cursor.moveToNext())
            }

            // closing connection
            cursor.close()
            db.close()
            return bankId
        }

        fun getSpence(date: String, spenceId: Int): Boolean {

            // Select All Query from CatMap
            val selectQuery = "SELECT  * FROM " + table_spences + " WHERE " + columnDspences_Spences + " LIKE '" + date + "%' AND " + columnSpenceId_Spences + " = " + spenceId.toString()
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            var tieneSpence: Boolean = false
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    tieneSpence = true
                } while (cursor.moveToNext())
            }

            // closing connection
            cursor.close()
            db.close()
            return tieneSpence
        }

    fun getOnlyBankTransactionsCreatedInApp(): List<String>? {
        val labelTransactionsCreatedinAppIds: MutableList<String> = ArrayList()
        labelTransactionsCreatedinAppIds.clear()
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM BankBalance WHERE CreatedInApp = 1"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelTransactionsCreatedinAppIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return labelTransactionsCreatedinAppIds
    }

    fun getOnlyExpensesCreatedInApp(): List<String>? {
        val labelExpensesCreatedinAppIds: MutableList<String> = ArrayList()
        labelExpensesCreatedinAppIds.clear()
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM Spences WHERE CreatedInApp = 1"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelExpensesCreatedinAppIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return labelExpensesCreatedinAppIds
    }

    fun getAllCarMaintenances(que: String): List<String>? {
        val labelallMaintenancesIds: MutableList<String> = ArrayList()
        labelallMaintenancesIds.clear()
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM MaintenanceRecords" +  que
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelallMaintenancesIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return labelallMaintenancesIds
    }

    fun getImage(position: Int?): Bitmap? {
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_companies + " WHERE CompanyId = " + position
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var imag: ByteArray? = null
        var bt: Bitmap? = null
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()
            imag = cursor.getBlob(7)
            bt = BitmapFactory.decodeByteArray(imag, 0, imag.size)
        } catch (e: java.lang.Exception) {
        }

        // closing connection
        cursor.close()
        db.close()
        return bt
    }
    fun getAllCompanyIds(): List<String> {

        val labelIds: MutableList<String> = ArrayList()
        labelIds.clear()

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_companies + " ORDER BY " + columnCompany_companies
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()

       return  labelIds
    }

    fun getAllCompaniesEditedInApp(): List<String>? {
        val labelCompanyIds: MutableList<String> = ArrayList()
        labelCompanyIds.clear()
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_companies + " Where CreatedInApp = 1"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelCompanyIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return labelCompanyIds
    }

    fun getAllMarketPricesEditedInApp(): List<String>? {
        val labelCompanyIds: MutableList<String> = ArrayList()
        labelCompanyIds.clear()
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_marketPrices + " Where CreatedInApp = 1"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelCompanyIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return labelCompanyIds
    }

    fun getAllCarModels(que: Int): List<String>? {
        val labelCarModelIds: MutableList<String> = ArrayList()
        labelCarModelIds.clear()

        val labelCarModelLastEditDates: MutableList<String> = ArrayList()
        labelCarModelLastEditDates.clear()

        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_carmodels
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelCarModelIds.add(cursor.getString(0))
                labelCarModelLastEditDates.add(cursor.getString(3))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        if(que == 1){
            return labelCarModelIds
        }else{
            return labelCarModelLastEditDates
        }

    }

    fun getAllMarketPriceSeguimiento(): List<String>? {
        val labelMarketPriceSeguimientoIds: MutableList<String> = ArrayList()
        labelMarketPriceSeguimientoIds.clear()
        // Select All Query from CatMap
        val selectQuery = "SELECT  * FROM " + table_marketPriceSeguimiento + " Where CreatedInApp = 1"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labelMarketPriceSeguimientoIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return labelMarketPriceSeguimientoIds
    }

    fun getTransactionstoUpSync(Id: Int): String? {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        val selectQuery = "SELECT  * FROM BankBalance WHERE CreatedInApp = 1 And Id = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonTransactionstoUpSync = ""
        try {
            // looping through all rows and adding to list
           cursor.moveToFirst()

            jsonTransactionstoUpSync = """{
  "Id": ${cursor.getInt(0)},
  "BankId": ${cursor.getInt(1)},
  "Operation": "${cursor.getString(2)}",
  "Value": ${cursor.getDouble(3)},
  "Comment": "${cursor.getString(4)}",
  "RecordDate": "${cursor.getString(5)}",
  "LastEditDale": "${cursor.getString(6)}",
  "CreatedInApp": 1,
  "MSpences": "${cursor.getString(8)}",
  "MonthNumber": ${cursor.getInt(9)},
  "YSpences": ${cursor.getInt(10)},
  "GetDebitNumber_AfterSync": 0,
  "FechaSync": "${currentDate.toString()}", 
  "DebitUniqueIdentifier": "${cursor.getString(12)}"
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonTransactionstoUpSync
    }

    fun getCompaniestoUpSync(Id: Int?): String? {
        val selectQuery = "SELECT * FROM Companies WHERE CreatedInApp = 1 And CompanyId = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonTransactionstoUpSync = ""
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()

            //serializo la imagen a base64

            //serializo la imagen a base64
            val img = cursor.getBlob(7)
            val imgString = Base64.encodeToString(img, Base64.NO_WRAP)

            jsonTransactionstoUpSync = """{
  "CompanyId": ${cursor.getInt(0)},
  "Company": "${cursor.getString(1)}",
  "RealPrice": ${cursor.getDouble(2)},
  "Ponderated": ${cursor.getDouble(3)},
  "Ranking": ${cursor.getInt(4)},
  "MarketPrice": ${cursor.getDouble(5)},
  "Exclude": "${cursor.getString(6)}",
  "Image": "${imgString}",
  "CreatedInApp": 1
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonTransactionstoUpSync
    }

    fun getCarModelstoUpSync(Id: Int?): String? {
        val selectQuery = "SELECT * FROM CarModels WHERE id = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonTransactionstoUpSync = ""
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()

            jsonTransactionstoUpSync = """{
  "Id": ${cursor.getInt(0)},
  "Brand": "${cursor.getString(1)}",
  "CurrentMilleage": ${cursor.getInt(2)}, 
  "LastEditDate": "${cursor.getString(3)}" 
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonTransactionstoUpSync
    }

    fun getMarketPriceSeguimientoToUpSync(Id: Int?): String? {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        val selectQuery = "SELECT * FROM MarketPriceSeguimiento WHERE id = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonTransactionstoUpSync = ""
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()

            //serializo la imagen a base64
            jsonTransactionstoUpSync = """{
  "CompanyId": ${cursor.getInt(1)},
  "Day": "${cursor.getString(2)}",
  "MarketPrice": ${cursor.getDouble(3)},
  "RecordedDateAndTime": "${cursor.getString(4)}",    
  "FechaSync": "${currentDate.toString()}" 
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonTransactionstoUpSync
    }

    fun getMaintenanceRecordsUpdateToUpSync(Id: Int?): String? {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        val selectQuery = "SELECT * FROM MaintenanceRecords WHERE CreatedInApp = 0 AND UpdatedInApp = 1 And Id = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonTransactionstoUpSync = ""
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()

            //serializo la imagen a base64

            //serializo la imagen a base64
            val img = cursor.getBlob(7)
            val imgString = Base64.encodeToString(img, Base64.NO_WRAP)

            jsonTransactionstoUpSync = """{
  "Id": ${cursor.getInt(0)},
  "MaintenanceName": "${cursor.getString(1)}",
  "Comment": "${cursor.getString(2)}",
  "ModelId": ${cursor.getInt(3)},  
  "startKm": ${cursor.getInt(4)},
  "endKm": ${cursor.getInt(5)},
  "NextAfter": ${cursor.getInt(6)},
  "RecordDate": "${cursor.getString(7)}",
  "LastEditDate": "${cursor.getString(8)}",
  "Fulfilled": ${cursor.getInt(9)},
  "CreatedInApp": 0,
  "Deleted": 0,  
  "FechaSync": "${currentDate.toString()}" 
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonTransactionstoUpSync
    }

    fun getMarketPricestoUpSync(Id: Int?): String? {
        val selectQuery = "SELECT * FROM " + table_marketPrices + " WHERE CreatedInApp = 1 And CompanyID = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonTransactionstoUpSync = ""
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()

            jsonTransactionstoUpSync = """{
  "CompanyID": ${cursor.getInt(0)}, 
  "MarketPrice": ${cursor.getDouble(1)},
  "FechaMP": "${cursor.getString(2)}",  
  "CreatedInApp": 1
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonTransactionstoUpSync
    }

    fun getSpencestoUpSync(Id: Int): String? {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        val selectQuery = "SELECT  * FROM Spences WHERE CreatedInApp = 1 And id = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonSpencestoUpSync = ""
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()

            jsonSpencestoUpSync = """{
  "id": ${cursor.getInt(0)},
  "SpenceId": ${cursor.getInt(1)},
  "Spence": ${cursor.getDouble(2)},
  "DSpences": "${cursor.getString(3)}",
  "dia": "${cursor.getString(4)}",
  "DiaNumber": "${cursor.getInt(5)}",
  "DiaSpences": ${cursor.getInt(6)},
  "MSpences": "${cursor.getString(7)}",
  "MonthNumber": ${cursor.getInt(8)},
  "YSpences": ${cursor.getInt(9)},
  "CreatedInApp": 1,
  "SelfLoans_AfterSync": 0,
  "FechaSync": "${currentDate.toString()}",  
  "DebitUniqueIdentifier": "${cursor.getString(12)}"
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonSpencestoUpSync
    }

    fun getAllCarmaintenancestoUpSync(Id: Int): String? {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        val selectQuery = "SELECT  * FROM MaintenanceRecords WHERE CreatedInApp = 1 AND Id = $Id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var jsonMaintenancestoUpSync = ""
        try {
            // looping through all rows and adding to list
            cursor.moveToFirst()

            jsonMaintenancestoUpSync = """{
  "Id": ${cursor.getInt(0)},
  "MaintenanceName": "${cursor.getString(1)}",
  "Comment": "${cursor.getString(2)}",
  "ModelId": ${cursor.getInt(3)},
  "startKm": ${cursor.getInt(4)},
  "endKm": ${cursor.getInt(5)},
  "NextAfter": ${cursor.getInt(6)},
  "RecordDate": "${cursor.getString(7)}",
  "LastEditDate": "${cursor.getString(8)}",
  "Fulfilled": ${cursor.getInt(9)},
  "CreatedInApp": ${cursor.getInt(10)},
  "Deleted": ${cursor.getInt(12)},
  "FechaSync": "${currentDate.toString()}"  
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        // closing connection
        cursor.close()
        db.close()
        return jsonMaintenancestoUpSync
    }

    fun getSyncAudittoUpSync(id: Int?, name: String): String? {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        var jsonTransactionstoUpSync = ""
        try {
            jsonTransactionstoUpSync = """{
  "id": ${id.toString()},
  "Name": "${name}",
  "SyncDate": "${currentDate.toString()}"  
  }"""
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return jsonTransactionstoUpSync
    }

    fun LoadCompanies(): ArrayList<CompaniesModel> {
        val companiesModel: ArrayList<CompaniesModel> = ArrayList<CompaniesModel>()
        var selectQuery = ""
        selectQuery = "SELECT CompanyId, Company, RealPrice, Ponderated, Ranking, MarketPrice, Exclude, Image, CreatedInApp FROM Companies WHERE Exclude = 'no' Order By Ranking"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var companyId: Int?
        var company: String?
        var realPrice: Double?
        var ponderated: Double?
        var ranking: Int?
        var marketPrice: Double?
        var exclude: String?
        var image: ByteArray?
        var createdInApp: Boolean?

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                companyId = cursor.getInt(0)
                company = cursor.getString(1)
                realPrice = cursor.getDouble(2)
                ponderated = cursor.getDouble(3)
                ranking = cursor.getInt(4)
                marketPrice = cursor.getDouble(5)
                exclude = cursor.getString(6)
                image = cursor.getBlob(7)
                createdInApp = false
                companiesModel.add(
                    CompaniesModel(
                        companyId,
                        company,
                        realPrice,
                        ponderated,
                        ranking,
                        marketPrice,
                        exclude,
                        image,
                        createdInApp
                    )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return companiesModel
    }

    fun LoadSpences(query: String?, year: String?): ArrayList<SpencesModel> {
        val spencesModel: ArrayList<SpencesModel> = ArrayList<SpencesModel>()
        var selectQuery = ""
        selectQuery = "SELECT SpenceCategory.SpenceDescription, SUM(Spences.Spence) AS Spence FROM Spences INNER JOIN\n" +
                " SpenceCategory ON Spences.SpenceId = SpenceCategory.SpenceID " + year + " Group By SpenceCategory.SpenceDescription" + query

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var spencename: String?
        var spence: Double?

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                spencename = cursor.getString(0)
                spence = cursor.getDouble(1)

                spencesModel.add(
                        SpencesModel(
                                1,
                                1,
                                spencename,
                                spence,
                                "Dummy",
                                "Dummy",
                                1,
                                1,
                                "Dummy",
                                1,
                                2000,
                                false,
                            false,
                                "0"
                        )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return spencesModel
    }

    fun LoadMarketPriceSeguimiento(companyId: Int): ArrayList<MarketPriceSeguimientoModel> {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -2)
        val date = calendar.time
        val format = SimpleDateFormat("yyyy MM dd")
        val dateOutput: String = format.format(date)

        val marketPriceSeguimientoModel: ArrayList<MarketPriceSeguimientoModel> = ArrayList<MarketPriceSeguimientoModel>()
        var selectQuery = ""
        selectQuery = "SELECT CompanyId, Day, MarketPrice, RecordedDateAndTime, CreatedInApp FROM MarketPriceSeguimiento WHERE CompanyId = " + companyId.toString() + " AND Day >= '" + dateOutput + "' Order By Day"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var companyId: Int?
        var day: String?
        var marketPrice: Double?
        var recordedDateAndTime: String?
        var createdInApp: Boolean?

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                companyId = cursor.getInt(0)
                day = cursor.getString(1)
                marketPrice = cursor.getDouble(2)
                recordedDateAndTime = cursor.getString(3)
                createdInApp = false
                marketPriceSeguimientoModel.add(
                    MarketPriceSeguimientoModel(
                        companyId,
                        day,
                        marketPrice,
                        recordedDateAndTime,
                        createdInApp
                    )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return marketPriceSeguimientoModel
    }

    fun RetrieveCompanyId(companyName: String): Int {

         var selectQuery = ""
        selectQuery = "SELECT CompanyId FROM Companies WHERE Company = '" + companyName + "'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var companyId: Int = 1

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                companyId = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return companyId
    }

    fun RetrieveSpenceId(spenceName: String): Int {

        var selectQuery = ""
        selectQuery = "SELECT SpenceId FROM SpenceCategory WHERE SpenceDescription = '" + spenceName + "'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var spenceId: Int = 1

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                spenceId = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return spenceId
    }

    fun RetrieveDebitUniqueIdentifier_Spences(spenceid: Int, dspences: String): String {

        var selectQuery = ""
        selectQuery = "SELECT DebitUniqueIdentifier FROM Spences WHERE SpenceId = " + spenceid.toString() + " AND DSpences LIKE '" + dspences + "%'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var uniqueIdentifier: String = "0"

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                uniqueIdentifier = cursor.getString(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return uniqueIdentifier
    }

    fun RetrieveDebitUniqueIdentifier_Banks(bankId: Int, recordDate: String, value: Double): String {

        var selectQuery = ""
        selectQuery = "SELECT DebitUniqueIdentifier FROM BankBalance WHERE BankId = " + bankId.toString() + " AND RecordDate = '" + recordDate + "' AND Value = " + value

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var uniqueIdentifier: String = "0"

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                uniqueIdentifier = cursor.getString(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return uniqueIdentifier
    }

    fun RetrievePayedInstallment(InstallmentDate: String): Boolean {

        var selectQuery = ""
        selectQuery = "SELECT * FROM SelfLoans WHERE DInstallment = '" + InstallmentDate + "'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var installmentExists: Boolean = false

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                installmentExists = true
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return installmentExists
    }

    fun RetrieveBankId(bankName: String): Int {

        var selectQuery = ""
        selectQuery = "SELECT Id FROM BankInformation WHERE BankName = '" + bankName + "'"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var bankId: Int = 1

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                bankId = cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return bankId
    }

    fun LoadDividendos(filtro: String, que: Int): ArrayList<DividendosModel> {
        val dividendosModel: ArrayList<DividendosModel> = ArrayList<DividendosModel>()
        var selectQuery = ""
        if(que == 1){
            selectQuery = "SELECT DISTINCT Dividendos.CompanyID, MIN(Dividendos.Year) AS Year, Companies.Company, AVG(Dividendos.AccionesPorAccion) AS AccionesPorAccion, AVG(Dividendos.EfectivoPorAccion) AS EfectivoPorAccion\n" +
                    "FROM            Companies LEFT OUTER JOIN\n" +
                    "                         Dividendos ON Companies.CompanyId = Dividendos.CompanyID\n" +
                    filtro + " GROUP BY Dividendos.CompanyID, Companies.Company ORDER BY Dividendos.Year DESC limit 10"

        }else{
            selectQuery = "SELECT Dividendos.CompanyID, Dividendos.Year, Companies.Company, Dividendos.AccionesPorAccion, Dividendos.EfectivoPorAccion\n" +
                    "FROM            Companies LEFT OUTER JOIN\n" +
                    "                         Dividendos ON Companies.CompanyId = Dividendos.CompanyID\n" +
                     filtro + " ORDER BY Dividendos.Year DESC limit 10"
        }

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var company: String?
        var year: Int?
        var accionesPorAccion: Double?
        var efectivoPorAccion: Double?

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                year = cursor.getInt(1)
                company = cursor.getString(2)
                accionesPorAccion = cursor.getDouble(3)
                efectivoPorAccion = cursor.getDouble(4)

                dividendosModel.add(
                    DividendosModel(
                        0,
                        0,
                        company,
                        year,
                        0.0,
                        0.0,
                        0,
                        accionesPorAccion,
                        efectivoPorAccion,
                        "si",
                        "si",
                        0.0
                    )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return dividendosModel
    }

    fun LoadSpences(filtro: String): ArrayList<SpencesModel> {
        val spencesModel: ArrayList<SpencesModel> = ArrayList<SpencesModel>()
        var selectQuery = ""
        selectQuery = "SELECT SpenceCategory.SpenceDescription, SUM(Spences.Spence) AS Spence, MonthNames.MonthName\n" +
                "FROM SpenceCategory INNER JOIN\n" +
                "  Spences ON SpenceCategory.SpenceID = Spences.SpenceId INNER JOIN\n" +
                "   MonthNames ON Spences.MonthNumber = MonthNames.MonthNumber" + filtro + " GROUP BY MonthNames.MonthName ORDER BY Spences.MonthNumber"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var spenceName: String?
        var spence: Double?
        var month: String = ""

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                spenceName = cursor.getString(0)
                spence = cursor.getDouble(1)
                month = cursor.getString(2)

                spencesModel.add(
                        SpencesModel(
                                0,
                                0,
                                spenceName,
                                spence,
                                "null",
                                "Dummy",
                                1,
                                1,
                                month,
                                1,
                                2000,
                                true,
                            false,
                                "0"
                        )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return spencesModel
    }

    fun LoadBankBalancesPerMonth(filtro: String): ArrayList<BankSummariesModel> {
        val bankSummariesModel: ArrayList<BankSummariesModel> = ArrayList<BankSummariesModel>()
        var selectQuery = ""
        selectQuery = "SELECT SUM(Balance) AS Balance, MonthName, MonthNumber FROM BankSummaries " + filtro + " GROUP BY MonthName, MonthNumber ORDER BY MonthNumber"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var balance: Double?
        var month: String = ""

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                balance = cursor.getDouble(0)
                month = cursor.getString(1)

                bankSummariesModel.add(
                        BankSummariesModel(
                                0,
                                1,
                                2020,
                                balance,
                                month,
                                1
                        )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return bankSummariesModel
    }

    fun LoadInvestment(filtro: String): ArrayList<InvestmentModel> {
        val investmentModel: ArrayList<InvestmentModel> = ArrayList<InvestmentModel>()
        var selectQuery = ""
        selectQuery = "SELECT  Investment.CompanyId, Companies.Company, Investment.TotalOwned, Investment.PrecioPromedioCompra, Investment.Dinero, Investment.SoloCompradas, \n" +
                "                         Investment.SoloVendidas, Investment.UtilidadBancaria, Investment.UtilidadAccion, Investment.ComisionCompra, Investment.ComisionVenta\n" +
                "FROM           Companies INNER JOIN\n" +
                "                         Investment ON Companies.CompanyId = Investment.CompanyId WHERE Companies.Exclude = 'no'" + filtro

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var companyID: Int?
        var company: String?
        var totalOwned: Int?
        var precioPromedioCompra: Double?
        var dinero: Double?
        var soloCompradas: Int?
        var soloVendidas: Int?
        var utilidadBancaria: Double?
        var utilidadAccion: Double?
        var comisionCompra: Double?
        var comisionVenta: Double?

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                companyID = cursor.getInt(0)
                company = cursor.getString(1)
                totalOwned = cursor.getInt(2)
                precioPromedioCompra = cursor.getDouble(3)
                dinero = cursor.getDouble(4)
                soloCompradas = cursor.getInt(5)
                soloVendidas = cursor.getInt(6)
                utilidadBancaria = cursor.getDouble(7)
                utilidadAccion = cursor.getDouble(8)
                comisionCompra= cursor.getDouble(9)
                comisionVenta = cursor.getDouble((10))

                investmentModel.add(
                    InvestmentModel(
                        companyID,
                        company,
                        totalOwned,
                        precioPromedioCompra,
                        dinero,
                        soloCompradas,
                        soloVendidas,
                        utilidadBancaria,
                        utilidadAccion,
                        comisionCompra,
                        comisionVenta
                    )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return investmentModel
    }

    fun LoadBankBalanceAllAccounts(year: String?): ArrayList<BankBalanceModel> {
        val bankBalanceModel: ArrayList<BankBalanceModel> = ArrayList<BankBalanceModel>()
        var selectQuery = ""
        selectQuery = "SELECT BankInformation.BankName, SUM(BankBalance.Value) + BankInformation.InitialBalance AS Value FROM   BankBalance INNER JOIN\n" +
                " BankInformation ON BankBalance.BankId = BankInformation.Id " + year + " Group By BankInformation.BankName"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var bank: String?
        var value: Double?

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                bank = cursor.getString(0)
                value = cursor.getDouble(1)

                bankBalanceModel.add(
                        BankBalanceModel(
                                1,
                                1,
                                bank,
                                "Dummy",
                                value,
                                "Dummy",
                                "Dummy",
                                "Dummy",
                                false,
                                "january",
                                1,
                                2000,
                                value,
                                "0"
                        )
                )
            } while (cursor.moveToNext())
        }

        // closing connection
        cursor.close()
        db.close()
        return bankBalanceModel
    }

    }
