package x.stocks.valuemanager

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.*
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*


class SyncFragment : Fragment() {

    var spinnerYearExpenses: Spinner? = null
    var db: DatabaseHelper? = null
    var btSyncDownCars: Button? = null;
    var btSyncDownBanks: Button? = null;
    var btSyncDownExpenses: Button? = null;
    var btSyncDownPortfolio: Button? = null
    var btSyncUpCars: Button? = null;
    var btSyncUpBanks: Button? = null;
    var btSyncUpExpenses: Button? = null;
    var btSyncUpPortfolio: Button? = null
    var btUpdateIp: Button? = null
    var textIp: TextView? = null
    lateinit var sharedPreferences: SharedPreferences
    var retrievedIP: String? = null
    var retrievedInstallment: String? = "0"
    var retrievedMilleageLastEditDate: String? = null
    var totalArrayLength = 0
    var a = 0
    var percentaje = 0
    var maxIdMaintenanceInMainDB: Int? = null
    var minIdMaintenanceLocally: Int? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "SYNCHRONIZATION"
        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_sync, container, false)
        db = DatabaseHelper(inflater.context)
        //Assign variable
        spinnerYearExpenses = view?.findViewById<Spinner>(R.id.spYearExpenses)
        loadYears(requireContext())
        spinnerYearExpenses?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        btSyncDownCars = view?.findViewById<Button>(R.id.btnSyncDownCars)
        btSyncDownBanks = view?.findViewById<Button>(R.id.btnSyncDownBanks)
        btSyncDownExpenses = view?.findViewById<Button>(R.id.btnSyncDownExpenses)
        btSyncDownPortfolio = view?.findViewById<Button>(R.id.btnSyncDownPortfolio)
        btSyncUpCars = view?.findViewById<Button>(R.id.btnSyncUpCars)
        btSyncUpBanks = view?.findViewById<Button>(R.id.btnSyncUpBanks)
        btSyncUpExpenses = view?.findViewById<Button>(R.id.btnSyncUpExpenses)
        btSyncUpPortfolio = view?.findViewById<Button>(R.id.btnSyncUpPortfolio)
        btUpdateIp = view?.findViewById<Button>(R.id.btnUpdateIp)
        textIp = view?.findViewById<TextView>(R.id.txtIp)

        val spRetrieve = inflater.context.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        retrievedIP = spRetrieve.getString("ip", "")
        retrievedInstallment = spRetrieve.getString("installment", "0")
        retrievedMilleageLastEditDate = spRetrieve.getString("milleageDate", "1999-01-01 00:00:00")

        textIp?.text = retrievedIP
        //To assign into shared preferences
        sharedPreferences = inflater.context.getSharedPreferences(
                "MyUserPrefs",
                Context.MODE_PRIVATE
        )

        btUpdateIp?.setOnClickListener {
            SaveSharedPreferencesIP()
        }

       EnableButtons(false) //I enable buttons once the verifications are complete
       CheckIfAlreadySync()  //The idea is to avoid buttons to be enable before the app checks if records are already in the main database

        GetMaxMaintenanceRecord() //I get the max maintenance record in main app

        //With the below I check if I have recorded new maintenances in the app or deleted
        var filtro: String = " WHERE CreatedInApp = 1 OR Deleted = 1"
        var doExistMaintenancesRecordedOrDeletedInApp: Boolean? = db!!.getIfAnyCarmaintenanceWasRecordedInApp(filtro)
        if(doExistMaintenancesRecordedOrDeletedInApp == true){
            minIdMaintenanceLocally = db!!.getMaxMaintenanceId(filtro, "Min")
        }else{
            minIdMaintenanceLocally = 0
        }

        btSyncDownBanks?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        var year: String? = null
                        year = spinnerYearExpenses?.selectedItem.toString()
                        db = DatabaseHelper(inflater.context)
                        db?.delete("MonthNames")
                        db?.delete("BankSummaries")
                        db?.delete("BankInformation")
                        db?.delete("BankBalance", "YSpences = " + year)
                        BajarMonthNames()
                        BajarBankInformation()
                        BajarBankSummaries()
                        BajarBankBalance(year)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Banks and bank balances will be deleted before downloading them, do you want to proceed?")
                .setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSyncDownCars?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        db = DatabaseHelper(inflater.context)
                        db?.delete("CarModels")
                        db?.delete("MaintenanceRecords")
                        BajarCarModels(true)
                        BajarMaintenanceRecords()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Cars and Maintenance records will be deleted before downloading them, do you want to proceed?")
                .setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSyncDownExpenses?.setOnClickListener {
            var year: String = spinnerYearExpenses?.selectedItem.toString()

            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        db = DatabaseHelper(inflater.context)
                        db?.delete("MonthNames")
                        db?.delete("SelfLoans")
                        db?.delete("SpenceCategory")
                        db?.delete("Spences", "YSpence = " + year)
                        db?.delete("Parameters", "Year = " + year)
                        BajarMonthNames()
                        BajarSpenceCategory()
                        BajarSelfLoans()
                        BajarSpences(year.toInt())
                        BajarParameters(year.toInt())
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Expenses of " + year.toString() + " will be deleted before downloading them back, do you want to proceed?")
                .setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSyncDownPortfolio?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        var year: String? = null
                        year = spinnerYearExpenses?.selectedItem.toString()
                        db = DatabaseHelper(inflater.context)
                        db?.delete("MarketPrices")
                        db?.delete("MarketPriceSeguimiento")
                        db?.delete("Companies")
                        db?.delete("Investment")
                        db?.delete("Dividendos")
                        BajarMarketPrices()
                        BajarMarketPriceSeguimiento()
                        BajarCompanies()
                        BajarInvestment()
                        BajarDividendos()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("All stock market data will be deleted before downloading it, do you want to proceed?")
                .setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSyncUpBanks?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        LoopThroughBankBalance()
                        UpdateSyncAuditUpSync(7, "Bank transactions")
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.ic_menu_save)
            builder.setCancelable(false)
            builder.setMessage("Do you want to upload bank transactions recorded in the app?")
                .setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSyncUpExpenses?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        LoopThroughExpenses()
                        UpdateSyncAuditUpSync(6, "Expenses")
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.ic_menu_save)
            builder.setCancelable(false)
            builder.setMessage("Do you want to upload expenses recorded in the app?")
                .setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSyncUpCars?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (maxIdMaintenanceInMainDB!! < minIdMaintenanceLocally!!) {
                        } else { //if needed I recalculate ids of recorded and deleted maintenances that were done in the app before sync
                            var filtro: String = " WHERE CreatedInApp = 1 OR Deleted = 1"
                            db!!.RecalculateCarmaintenanceIdBasedOnMaxFromMainDatabase(filtro, maxIdMaintenanceInMainDB)
                        }

                        LoopThroughAllCarModels()
                        LoopThroughAllCarMaintenances()
                        UpdateSyncAuditUpSync(8, "Car maintenances")
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.ic_menu_save)
            builder.setCancelable(false)
            builder.setMessage("Do you want to upload your car maintenances?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSyncUpPortfolio?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        LoopThroughAllCompanies()
                        LoopThroughAllMarketPrices()
                        LoopThroughAllMarketPriceSeguimientoRecords()
                        UpdateSyncAuditUpSync(9, "Market prices")
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.ic_menu_save)
            builder.setCancelable(false)
            builder.setMessage("Do you want to upload the market prices updated in the app?")
                .setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                .setNegativeButton("No", dialogOnClickListener).show()
        }

        return view
    }

    private fun BajarDividendos() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/Dividendos"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var companyId: Int? = 0
                    var year: Int? = 0
                    var dividendosEfectivo: Double? = 0.00
                    var dividendosAccion: Double? = 0.00
                    var accionesPrecedentes: Int? = 0
                    var accionesPorAccion: Double? = 0.00
                    var efectivoPorAccion: Double? = 0.00
                    var distribute: String? = ""
                    var graph: String? = ""
                    var avSalePrice: Double? = 0.00
                    var item: JSONObject? = null
                    try {
                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("id")
                            companyId = item.getInt("CompanyID")
                            year = item.getInt("Year")
                            dividendosEfectivo = item.getDouble("DividendosEfectivo")
                            dividendosAccion = item.getDouble("DividendosAccion")
                            accionesPrecedentes = item.getInt("AccionesPrecedentes")
                            accionesPorAccion = item.getDouble("AccionesPorAccion")
                            efectivoPorAccion = item.getDouble("EfectivoPorAccion")
                            distribute = item.getString("Distribute")
                            graph = item.getString("Graph")
                            avSalePrice = item.getDouble("AvSalePrice")

                            Toast.makeText(
                                    layoutInflater.context,
                                    "Downloading dividends: $percentage%", Toast.LENGTH_SHORT
                            ).show()

                            val dividendosModel = DividendosModel(
                                    id,
                                    companyId,
                                    "fuzzy",
                                    year,
                                    dividendosEfectivo,
                                    dividendosAccion,
                                    accionesPrecedentes,
                                    accionesPorAccion,
                                    efectivoPorAccion,
                                    distribute,
                                    graph,
                                    avSalePrice
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneDividendos(
                                    dividendosModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Dividends were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                    btSyncDownPortfolio?.isEnabled = false
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarInvestment() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/Investment"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var companyId: Int? = 0
                    var totalOwned: Int? = 0
                    var precioPromedioCompra: Double? = 0.00
                    var dinero: Double? = 0.00
                    var soloCompradas: Int? = 0
                    var soloVendidas: Int? = 0
                    var utilidadBancaria: Double? = 0.00
                    var utilidadAccion: Double? = 0.00
                    var comisionCompra: Double? = 0.00
                    var comisionVenta: Double? = 0.00
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            companyId = item.getInt("CompanyId")
                            totalOwned = item.getInt("TotalOwned")
                            precioPromedioCompra = item.getDouble("PrecioPromedioCompra")
                            dinero = item.getDouble("Dinero")
                            soloCompradas = item.getInt("SoloCompradas")
                            soloVendidas = item.getInt("SoloVendidas")
                            utilidadBancaria = item.getDouble("UtilidadBancaria")
                            utilidadAccion = item.getDouble("UtilidadAccion")
                            comisionCompra = item.getDouble("ComisionCompra")
                            comisionVenta = item.getDouble("ComisionVenta")

                            // Toast.makeText(layoutInflater.context, "Downloading Investment information: $percentage%", Toast.LENGTH_SHORT).show()

                            val investmentModel = InvestmentModel(
                                    companyId,
                                    "Fuzzy",
                                    totalOwned,
                                    precioPromedioCompra,
                                    dinero,
                                    soloCompradas,
                                    soloVendidas,
                                    utilidadBancaria,
                                    utilidadAccion,
                                    comisionCompra,
                                    comisionVenta,
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneInvestment(
                                    investmentModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Investment information were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarCompanies() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/Companies"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var companyId: Int? = 0
                    var company: String? = ""
                    var realPrice: Double? = 0.00
                    var ponderated: Double? = 0.00
                    var ranking: Int? = 0
                    var marketPrice: Double? = 0.00
                    var exclude: String? = ""
                    var image: Object? = null
                    var createdInApp: Boolean = false
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            companyId = item.getInt("CompanyId")
                            company = item.getString("Company")
                            realPrice = item.getDouble("RealPrice")
                            ponderated = item.getDouble("Ponderated")
                            ranking = item.getInt("Ranking")
                            marketPrice = item.getDouble("MarketPrice")
                            exclude = item.getString("Exclude")
                            image = item.get("Image") as Object?

                            var imageByte: ByteArray? = null

                            if (image != null) {
                                imageByte = Base64.decode(image.toString(), Base64.NO_WRAP)

                            } else {
                                imageByte = null
                            }
                            Toast.makeText(
                                    layoutInflater.context,
                                    "Downloading companies: $percentage%", Toast.LENGTH_SHORT
                            ).show()

                            val companiesModel = CompaniesModel(
                                    companyId,
                                    company,
                                    realPrice,
                                    ponderated,
                                    ranking,
                                    marketPrice,
                                    exclude,
                                    imageByte,
                                    createdInApp
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneCompanies(
                                    companiesModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Companies were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarMarketPrices() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/MarketPrices"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var companyId: Int? = 0
                    var marketPrice: Double? = 0.00
                    var fechaMP: String? = ""
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            companyId = item.getInt("CompanyID")
                            marketPrice = item.getDouble("MarketPrice")
                            fechaMP = item.getString("FechaMP")

                            //  Toast.makeText(layoutInflater.context, "Downloading Market prices: $percentage%", Toast.LENGTH_SHORT).show()

                            val marketPricesModel = MarketPricesModel(
                                    companyId,
                                    marketPrice,
                                    fechaMP,
                                    false
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneMarketPrices(
                                    marketPricesModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Market prices were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarMarketPriceSeguimiento() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/MarketPriceSeguimiento"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var companyId: Int? = 0
                    var day: String? = ""
                    var marketPrice: Double? = 0.00
                    var recordedDate: String? = ""
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            companyId = item.getInt("CompanyId")
                            day = item.getString("Day")
                            marketPrice = item.getDouble("MarketPrice")
                            recordedDate = item.getString("RecordedDateAndTime")

                            //  Toast.makeText(layoutInflater.context, "Downloading follow-up Market prices: $percentage%", Toast.LENGTH_SHORT).show()

                            val marketPriceSeguimientoModel = MarketPriceSeguimientoModel(
                                    companyId,
                                    day,
                                    marketPrice,
                                    recordedDate,
                                    false
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneMarketPriceSeguimiento(
                                    marketPriceSeguimientoModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Follow-up market prices were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarSpences(year: Int) {
        // Instantiate the RequestQueue.

        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/Spences?year=" + year

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var spenceId: Int? = 0
                    var spence: Double? = 0.00
                    var DSpenses: String? = ""
                    var dia: String? = ""
                    var diaNumber: Int? = 0
                    var diaSpences: Int? = 0
                    var MSpences: String? = ""
                    var monthNumber: Int? = 0
                    var YSpences: Int? = 0
                    var createdInApp: Boolean? = false
                    var bankDebit: Boolean? = false
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("id")
                            spenceId = item.getInt("SpenceId")
                            spence = item.getDouble("Spence")
                            DSpenses = item.getString("DSpences")
                            dia = item.getString("dia")
                            diaNumber = item.getInt("DiaNumber")
                            diaSpences = item.getInt("DiaSpences")
                            MSpences = item.getString("MSpences")
                            monthNumber = item.getInt("MonthNumber")
                            YSpences = item.getInt("YSpences")
                            createdInApp = item.getBoolean("CreatedInApp")
                            bankDebit = item.getBoolean("BankDebit")

                            Toast.makeText(
                                    layoutInflater.context,
                                    "Downloading " + year.toString() + "'s expenses: $percentage%",
                                    Toast.LENGTH_SHORT
                            ).show()

                            val spencesModel = SpencesModel(
                                    id,
                                    spenceId,
                                    "Dummy",
                                    spence,
                                    DSpenses,
                                    dia,
                                    diaNumber,
                                    diaSpences,
                                    MSpences,
                                    monthNumber,
                                    YSpences,
                                    false,
                                    bankDebit,
                                    "0"
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneSpences(
                                    spencesModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Expenses were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }

    private fun BajarParameters(year: Int) {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/Parameters?year=" + year

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var year: Int? = 0
                    var month: String? = null
                    var monthNumber: Int? = 0
                    var monthlySavings: Double? = 0.00
                    var monthlyIncome: Double? = 0.00
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("id")
                            year = item.getInt("Year")
                            month = item.getString("Month")
                            monthNumber = item.getInt("MonthNumber")
                            monthlySavings = item.getDouble("MonthlySavings")
                            monthlyIncome = item.getDouble("MonthlyIncome")

                            // Toast.makeText(layoutInflater.context, "Downloading " + year.toString() + "'s incomes: $percentage%", Toast.LENGTH_SHORT).show()

                            val parametersModel = ParametersModel(
                                    id,
                                    year,
                                    month,
                                    monthNumber,
                                    monthlySavings,
                                    monthlyIncome,
                                    false
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneParameters(
                                    parametersModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Incomes were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarSpenceCategory() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/SpenceCategory"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var spenceId: Int? = 0
                    var spenceDescription: String? = ""
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            spenceId = item.getInt("SpenceID")
                            spenceDescription = item.getString("SpenceDescription")

                            //  Toast.makeText(layoutInflater.context, "Downloading expense categories: $percentage%", Toast.LENGTH_SHORT).show()

                            val spenceCategoryModel = SpenceCategoryModel(
                                    spenceId,
                                    spenceDescription,
                                    false
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneSpenceCategory(
                                    spenceCategoryModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                  //  Toast.makeText(
                   //         layoutInflater.context,
                   //        "Expense categories were downloaded successfully!",
                   //         Toast.LENGTH_LONG
                  //  ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarSelfLoans() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/SelfLoans"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var installmentDate: String? = ""
                    var amount: Double? = 0.0
                    var alreadyPayed: Double? = 0.0
                    var yourCurrentDebt: Double? = 0.0
                    var eachInstallment: Double? = 0.0
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            installmentDate = item.getString("LoanDate")
                            amount = item.getDouble("Amount")
                            alreadyPayed = item.getDouble("AlreadyPayed")
                            yourCurrentDebt = item.getDouble("YourCurrentDebt")
                            eachInstallment = item.getDouble("EachInstallment")

                            //  Toast.makeText(layoutInflater.context, "Downloading expense categories: $percentage%", Toast.LENGTH_SHORT).show()

                            val selfLoansModel = SelfLoansModel(
                                    installmentDate,
                                    amount,
                                    alreadyPayed,
                                    yourCurrentDebt,
                                    eachInstallment
                            )

                            //I save as shared preference to be used when saving expenses
                            retrievedInstallment = eachInstallment.toString()
                            val editor: Editor = sharedPreferences.edit()
                            editor.putString("installment", retrievedInstallment)
                            editor.commit()

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneSelfLoans(
                                    selfLoansModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    // Toast.makeText(
                    //      layoutInflater.context,
                    //      "Self loans were downloaded successfully!",
                    //      Toast.LENGTH_LONG
                    // ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarMaintenanceRecords() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/MaintenanceRecords"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var maintenanceName: String? = ""
                    var comment: String? = ""
                    var modelId: Int? = 0
                    var startKm: Int? = 0
                    var endKm: Int? = 0
                    var nextAfter: Int? = 0
                    var recordDate: String? = ""
                    var lastEditDate: String? = ""
                    var fulfilled: Boolean? = false
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("Id")
                            maintenanceName = item.getString("MaintenanceName")
                            comment = item.getString("Comment")
                            modelId = item.getInt("ModelId")
                            startKm = item.getInt("startKm")
                            endKm = item.getInt("endKm")
                            nextAfter = item.getInt("NextAfter")
                            recordDate = item.getString("RecordDate")
                            lastEditDate = item.getString("LastEditDate")
                            fulfilled = item.getBoolean("Fulfilled")

                            Toast.makeText(
                                    layoutInflater.context,
                                    "Downloading cars and maintenance records: $percentage%",
                                    Toast.LENGTH_SHORT
                            ).show()

                            val maintenanceRecordsModel = MaintenanceRecordsModel(
                                    id,
                                    maintenanceName,
                                    comment,
                                    modelId,
                                    startKm,
                                    endKm,
                                    nextAfter,
                                    recordDate,
                                    lastEditDate,
                                    fulfilled,
                                    false,
                                    false,
                                    false
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneMaintenanceRecords(
                                    maintenanceRecordsModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Car maintenance records were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                    btSyncDownCars?.isEnabled = false
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun GetMaxMaintenanceRecord() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)

        val url = "http://" + retrievedIP + "/api/MaintenanceRecords?max=hola"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0

                    var item: JSONObject? = null
                    try {
                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            item = response.getJSONObject(i)
                            id = item.getInt("Id")
                            // Toast.makeText(
                            //   layoutInflater.context,
                            //    "Downloading max car maintenance record: $percentage%",
                            //     Toast.LENGTH_SHORT
                            // ).show()

                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    // Toast.makeText(
                    //   layoutInflater.context,
                    //   "Max maintenance record is " + id.toString(),
                    //    Toast.LENGTH_LONG
                    // ).show()
                    maxIdMaintenanceInMainDB = id
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )
        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarCarModels(guardo: Boolean) {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/CarModels"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var brand: String? = ""
                    var mileage: Int? = 0
                    var currentDate: String = ""
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("Id")
                            brand = item.getString("Brand")
                            mileage = item.getInt("CurrentMilleage")
                            currentDate = item.getString("LastEditDate")

                            if (guardo == true) {
                                Toast.makeText(
                                        layoutInflater.context,
                                        "Downloading cars and maintenance records: $percentage%",
                                        Toast.LENGTH_SHORT
                                ).show()
                            }

                            if (guardo == true) {

                                val carModelsModel = CarModelsModel(
                                        id,
                                        brand,
                                        mileage,
                                        currentDate
                                )

                                val dataBasehelper = DatabaseHelper(layoutInflater.context)
                                val success: Boolean = dataBasehelper.addOneCarModel(
                                        carModelsModel)
                            } else {
                                val editor: Editor = sharedPreferences.edit()
                                editor.putString("milleageDate", currentDate)
                                editor.commit()
                            }


                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    if (guardo == true) {
                        Toast.makeText(
                                layoutInflater.context,
                                "Car models were downloaded successfully!",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    private fun BajarBankBalance(year: String) {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/BankBalance?year=" + year

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var bankId: Int? = 0
                    var operation: String? = ""
                    var value: Double? = 0.00
                    var comment: String? = "0"
                    var recordDate: String? = "0"
                    var lastEditDate = ""
                    var createdInApp: Boolean? = false
                    var mspences: String? = null
                    var monthNumber: Int? = null
                    var yspences: Int? = null
                    var balance: Double? = null
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("Id")
                            bankId = item.getInt("BankId")
                            operation = item.getString("Operation")
                            value = item.getDouble("Value")
                            comment = item.getString("Comment")
                            recordDate = item.getString("RecordDate")
                            lastEditDate = item.getString("LastEditDale")
                            createdInApp = item.getBoolean("CreatedInApp")
                            mspences = item.getString("MSpences")
                            monthNumber = item.getInt("MonthNumber")
                            yspences = item.getInt("YSpences")
                            balance = item.getDouble("Balance")
                            Toast.makeText(
                                    layoutInflater.context,
                                    "Downloading " + year.toString() + "'s bank balances: $percentage%",
                                    Toast.LENGTH_SHORT
                            ).show()

                            val bankBalanceModel = BankBalanceModel(
                                    id,
                                    bankId,
                                    "Dummy",
                                    operation,
                                    value,
                                    comment,
                                    recordDate,
                                    lastEditDate,
                                    false,
                                    mspences,
                                    monthNumber,
                                    yspences,
                                    balance,
                                    "0"
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneBankBalance(
                                    bankBalanceModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Bank balances were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }

    private fun UpdateBankBalanceCreatedInApp(Id: Int) {


        var id: Int = 0
        var bankId: Int = 0
        var operation: String = "Dummy"
        var value: Double = 0.00
        var comment: String = "Dummy"
        var recordDate: String = "Dummy"
        var lastEditDate = "Dummy"
        var createdInApp: Boolean = false
        var mspences: String = "Dummy"
        var monthNumber: Int = 0
        var yspences: Int = 1999

        try {

            val bankBalanceModel = BankBalanceModel(
                    id,
                    bankId,
                    "Dummy",
                    operation,
                    value,
                    comment,
                    recordDate,
                    lastEditDate,
                    createdInApp,
                    mspences,
                    monthNumber,
                    yspences,
                    0.00,
                    "0"
            )

            val dataBasehelper = DatabaseHelper(layoutInflater.context)
            val success: Boolean = dataBasehelper.updateOneBankBalanceCreatedInApp(
                    bankBalanceModel, Id
            )


        } catch (e: JSONException) {

            //  btSyncDownClients.setEnabled(false);
            //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
            e.printStackTrace()
        }
    }


    private fun UpdateSpencesCreatedInApp(Id: Int) {


        var id: Int = 0
        var spenceId: Int = 0
        var spence: Double = 0.00
        var DSpences: String = "Dummy"
        var dia: String = "Dummy"
        var diaNumber: Int = 0
        var diaSpences: Int = 0
        var MSpences: String = "Dummy"
        var monthNumber: Int = 0
        var YSpences: Int = 0

        try {

            val spencesModel = SpencesModel(
                    id,
                    spenceId,
                    "Dummy",
                    spence,
                    DSpences,
                    dia,
                    diaNumber,
                    diaSpences,
                    MSpences,
                    monthNumber,
                    YSpences,
                    false,
                    false,
                    "0"
            )

            val dataBasehelper = DatabaseHelper(layoutInflater.context)
            val success: Boolean = dataBasehelper.updateOneSpencesCreatedInApp(
                    spencesModel, Id
            )


        } catch (e: JSONException) {

            //  btSyncDownClients.setEnabled(false);
            //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
            e.printStackTrace()
        }
    }

    private fun UpdateMaintenancesCreatedInApp(Id: Int) {


        var id: Int = 0
        var maintenanceName: String = "Dummy"
        var comment: String = "Dummy"
        var modelId: Int = 0
        var startKm: Int = 0
        var endKm: Int = 0
        var nextAfter: Int = 0
        var recordDate: String = "Dummy"
        var lastEditDate: String = "Dummy"
        var fulfilled: Boolean = false
        try {

            val maintenanceRecordsModel = MaintenanceRecordsModel(
                    id,
                    maintenanceName,
                    comment,
                    modelId,
                    startKm,
                    endKm,
                    nextAfter,
                    recordDate,
                    lastEditDate,
                    fulfilled,
                    false,
                    false,
                    false
            )

            val dataBasehelper = DatabaseHelper(layoutInflater.context)
            val success: Boolean = dataBasehelper.updateOneMaintenancesCreatedInApp(
                    maintenanceRecordsModel, Id
            )


        } catch (e: JSONException) {

            //  btSyncDownClients.setEnabled(false);
            //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
            e.printStackTrace()
        }
    }

    private fun UpdateMarketPriceCreatedInApp(Id: Int) {


        var companyId: Int = 0
        var marketPrice: Double = 0.00
        var fechaMP: String = "Dummy"
        var createdInApp: Boolean = false
        try {

            val marketPricesModel = MarketPricesModel(
                    companyId,
                    marketPrice,
                    fechaMP,
                    createdInApp
            )

            val dataBasehelper = DatabaseHelper(layoutInflater.context)
            val success: Boolean = dataBasehelper.updateOneMarketPriceCreatedInApp(
                    marketPricesModel, Id
            )


        } catch (e: JSONException) {

            //  btSyncDownClients.setEnabled(false);
            //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
            e.printStackTrace()
        }
    }

    private fun BajarBankInformation() {

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/BankInformation"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var bankName: String? = ""
                    var initialBalance = 0.00
                    var item: JSONObject? = null

                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("Id")
                            bankName = item.getString("BankName")
                            initialBalance = item.getDouble("InitialBalance")
                            //   Toast.makeText(layoutInflater.context, "Downloading banks: $percentage%", Toast.LENGTH_SHORT).show()

                            val bankInformationModel = BankInformationModel(
                                    id,
                                    bankName,
                                    initialBalance
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneBankInformation(
                                    bankInformationModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Banks were downloaded successfully!",
                            Toast.LENGTH_LONG
                    ).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }

    private fun BajarMonthNames() {

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/MonthNames"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var languageId: Int? = 0
                    var monthNumber = 0
                    var monthName: String? = null
                    var item: JSONObject? = null

                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("id")
                            languageId = item.getInt("LanguageId")
                            monthNumber = item.getInt("MonthNumber")
                            monthName = item.getString("MonthName")

                            //  Toast.makeText(layoutInflater.context, "Downloading banks: $percentage%", Toast.LENGTH_SHORT).show()

                            val monthNamesModel = MonthNamesModel(
                                    id,
                                    languageId,
                                    monthNumber,
                                    monthName
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneMonthNames(
                                    monthNamesModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    //  Toast.makeText(layoutInflater.context, "Banks were downloaded successfully!", Toast.LENGTH_LONG).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }


    private fun BajarBankSummaries() {

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/BankSummaries"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var id: Int? = 0
                    var bankid: Int? = 0
                    var yspences: Int? = 0
                    var balance: Double? = 0.0
                    var monthname: String? = null
                    var monthnumber: Int? = 1
                    var item: JSONObject? = null

                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            id = item.getInt("id")
                            bankid = item.getInt("bankId")
                            yspences = item.getInt("YSpences")
                            balance = item.getDouble("balance")
                            monthname = item.getString("MonthName")
                            monthnumber = item.getInt("MonthNumber")

                            //   Toast.makeText(layoutInflater.context, "Downloading banks: $percentage%", Toast.LENGTH_SHORT).show()

                            val bankSummariesModel = BankSummariesModel(
                                    id,
                                    bankid,
                                    yspences,
                                    balance,
                                    monthname,
                                    monthnumber
                            )

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.addOneBankSummaries(
                                    bankSummariesModel
                            )
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    //  Toast.makeText(layoutInflater.context, "Banks were downloaded successfully!", Toast.LENGTH_LONG).show()
                },
                { error -> // error listener
                    Toast.makeText(
                            layoutInflater.context,
                            error.toString() + "_onResponse",
                            Toast.LENGTH_LONG
                    ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }

    private fun SaveSharedPreferencesIP() {

        val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    retrievedIP = textIp?.getText().toString()
                    val editor: Editor = sharedPreferences.edit()
                    editor.putString("ip", retrievedIP)
                    editor.commit()
                    Toast.makeText(activity, "IP has been saved!", Toast.LENGTH_LONG).show()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }
        }
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Confirmation")
        builder.setIcon(android.R.drawable.ic_menu_save)
        builder.setCancelable(false)
        builder.setMessage("Do you want to update the server's ip?").setPositiveButton(
                "Yes",
                dialogOnClickListener
        )
            .setNegativeButton("No", dialogOnClickListener).show()
    }

    private fun loadYears(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)
            val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
            // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: MutableList<String> = ArrayList()
            lables?.add("2020")
            lables?.add("2021")
            lables?.add("2022")
            lables?.add("2023")
            lables?.add("2024")
            lables?.add("2025")
            lables?.add("2026")
            lables?.add("2027")
            lables?.add("2028")
            lables?.add("2029")
            lables?.add("2030")
            // Creating adapter for spinner

            // Creating adapter for spinner
            val dataAdapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_item, lables
            )

            // Drop down layout style - list view with radio button

            // Drop down layout style - list view with radio button
            // dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // attaching data adapter to spinner

            // attaching data adapter to spinner
            spinnerYearExpenses?.adapter = dataAdapter
            spinnerYearExpenses?.setSelection(
                    getIndex(
                            spinnerYearExpenses!!,
                            currentYear.toString()
                    )
            )
            // Spinner click listener
            exito = true

        } catch (e: Exception) {
            exito = false
            exito
        }
        return exito
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == myString) {
                index = i
            }
        }
        return index
    }

    fun LoopThroughBankBalance() = runBlocking { // this: CoroutineScope
        launch{ DeleteBankBalanceWhileUploading()}
        launch {PostBankBalanceWhileUploading()}
    }

    suspend fun DeleteBankBalanceWhileUploading(){
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String
        val transactionsRecordedEnApp: List<String>? = db!!.getOnlyBankTransactionsCreatedInApp()
        transactionsRecordedEnApp?.indices?.forEach { i ->
            id = transactionsRecordedEnApp[i]
            DeleteBankBalanceUpSync(id.toInt())
            //Toast.makeText(context,"Transaction $id was uploaded to the server!", Toast.LENGTH_SHORT).show()
            exito = true
            //  UpdateBankBalanceCreatedInApp(id.toInt())
        }
    }

    suspend fun PostBankBalanceWhileUploading(){
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String
        val transactionsRecordedEnApp: List<String>? = db!!.getOnlyBankTransactionsCreatedInApp()
        transactionsRecordedEnApp?.indices?.forEach { i ->
            id = transactionsRecordedEnApp[i]
            PostBankBalanceUpSync(id.toInt())
            Toast.makeText(
                    context,
                    "Transaction $id was uploaded to the server!",
                    Toast.LENGTH_SHORT
            ).show()

            exito = true
            //  UpdateBankBalanceCreatedInApp(id.toInt())
        }
    }

    //IO, Main, Default
//CouroutineScope(IO).launch{}
    fun LoopThroughExpenses() = runBlocking {
        launch{DeleteSpencesWhileUploading()}
        launch{PostSpencesWhileUploading()}
    }

    suspend fun DeleteSpencesWhileUploading(){
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String
        val expensesRecordedEnApp: List<String>? = db!!.getOnlyExpensesCreatedInApp()
        expensesRecordedEnApp?.indices?.forEach { i ->
            id = expensesRecordedEnApp[i]
            DeleteSpencesUpSync(id.toInt())
           // Toast.makeText(context, "Expense $id was uploaded to the server!", Toast.LENGTH_SHORT).show()

            exito = true
            //  UpdateSpencesCreatedInApp(id.toInt())
        }
    }

    suspend fun PostSpencesWhileUploading(){
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String
        val expensesRecordedEnApp: List<String>? = db!!.getOnlyExpensesCreatedInApp()
        expensesRecordedEnApp?.indices?.forEach { i ->
            id = expensesRecordedEnApp[i]
            PostSpencesUpSync(id.toInt())
            Toast.makeText(context, "Expense $id was uploaded to the server!", Toast.LENGTH_SHORT)
                    .show()
            exito = true
            //  UpdateSpencesCreatedInApp(id.toInt())
        }
    }

    //IO, Main, Default
//CouroutineScope(IO).launch{}
    fun LoopThroughAllCarMaintenances() = runBlocking {
       launch {deleteCarsWhileUploading()}
        launch {PostCarsWhileUploading()}
        launch {UpdateCarsWhileUploading()}
        }

        suspend fun deleteCarsWhileUploading() {
            var exito = false
            db = DatabaseHelper(requireContext())
            var id: String
            var filtro = " Where CreatedInApp = 1 OR Deleted = 1"
            var anyMaintenanceCreatedInApp = db!!.getIfAnyCarmaintenanceWasRecordedInApp(filtro)

            if (anyMaintenanceCreatedInApp == true) {
                val allMaintenances: List<String>? = db!!.getAllCarMaintenances(filtro)

                allMaintenances?.indices?.forEach { i ->
                    id = allMaintenances[i]
                    DeleteAllMaintenancesUpSync(id.toInt())
                   // Toast.makeText(
                     //       context,
                       //     "Car maintenance id $id was uploaded to the server!",
                         //   Toast.LENGTH_SHORT
                    //     ).show()

                    exito = true
                    // UpdateMaintenancesCreatedInApp(id.toInt())
                }
            }
        }

    suspend fun PostCarsWhileUploading() {
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String
        var filtro = " Where CreatedInApp = 1 OR Deleted = 1"
        var anyMaintenanceCreatedInApp = db!!.getIfAnyCarmaintenanceWasRecordedInApp(filtro)

        if (anyMaintenanceCreatedInApp == true) {
            val allMaintenances: List<String>? = db!!.getAllCarMaintenances(filtro)

            allMaintenances?.indices?.forEach { i ->
                id = allMaintenances[i]
                PostAllCarMaintenancesUpSync(id.toInt())
                Toast.makeText(
                        context,
                        "Car maintenance id $id was uploaded to the server!",
                        Toast.LENGTH_SHORT
                ).show()

                exito = true
                // UpdateMaintenancesCreatedInApp(id.toInt())
            }
        }
    }

    suspend fun UpdateCarsWhileUploading() {
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String
        var filtro = " Where CreatedInApp = 0 AND UpdatedInApp = 1 AND Deleted = 0"
        var anyMaintenanceCreatedInApp = db!!.getIfAnyCarmaintenanceWasRecordedInApp(filtro)

        if (anyMaintenanceCreatedInApp == true) {
            val allMaintenances: List<String>? = db!!.getAllCarMaintenances(filtro)

            allMaintenances?.indices?.forEach { i ->
                id = allMaintenances[i]
                UpdateMaintenanceRecordsUpSync(id.toInt())
                Toast.makeText(
                        context,
                        "Car maintenance id $id was updated in the server!",
                        Toast.LENGTH_SHORT
                ).show()

                exito = true
                // UpdateMaintenancesCreatedInApp(id.toInt())
            }
        }
    }


    fun LoopThroughAllCompanies() {
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String

        var filtro: String = " Where CreatedInApp = 1"
        var anyCompanyEditedInApp = db!!.getIfAnyCompanyEditedInApp(filtro)

        if(anyCompanyEditedInApp == true){
            val companiesEditedInApp: List<String>? = db!!.getAllCompaniesEditedInApp()

            companiesEditedInApp?.indices?.forEach { i ->
                id = companiesEditedInApp[i]
                UpdateCompaniesUpSync(id.toInt())
                Toast.makeText(context, "Company id $id was synchronized to the server!", Toast.LENGTH_SHORT).show()

                exito = true
              //  UpdateMarketPriceCreatedInApp(id.toInt())
            }
        }
    }

    fun LoopThroughAllMarketPrices() {
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String

        var filtro: String = " Where CreatedInApp = 1"
        var anyCompanyEditedInApp = db!!.getIfAnyMarketPriceEditedInApp(filtro)

        if(anyCompanyEditedInApp == true){
            val companiesEditedInApp: List<String>? = db!!.getAllMarketPricesEditedInApp()

            companiesEditedInApp?.indices?.forEach { i ->
                id = companiesEditedInApp[i]
                UpdateMarketPriceUpSync(id.toInt())
                Toast.makeText(context, "Market price of company id $id was uploaded to the server!", Toast.LENGTH_SHORT).show()

                exito = true
                //  UpdateMarketPriceCreatedInApp(id.toInt())
            }
        }
    }

    fun LoopThroughAllCarModels() {
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String

        val carModelsLastEditDate: List<String>? = db!!.getAllCarModels(2)
            val carModels: List<String>? = db!!.getAllCarModels(1)

        carModels?.indices?.forEach { i ->
                id = carModels[i]

            //date in the app
            val dateString = carModelsLastEditDate?.get(i)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val strDate: Date = sdf.parse(dateString)

            //date in the main Database
            val dateStringMD = retrievedMilleageLastEditDate
            val sdfMD = SimpleDateFormat("yyyy-MM-dd")
            val strDateMD: Date = sdfMD.parse(dateStringMD)


            if ((strDate.after(strDateMD)) || (strDate.equals(strDateMD))) {
                UpdateCarModelsUpSync(id.toInt())
                Toast.makeText(context, "Current Milleage of car model $id was uploaded to the server!", Toast.LENGTH_SHORT).show()

                exito = true
                //  UpdateMarketPriceCreatedInApp(id.toInt())
            }
            }
    }

    fun LoopThroughAllMarketPriceSeguimientoRecords() = runBlocking {
        var exito = false
        db = DatabaseHelper(requireContext())
        var id: String

        var anyMarketPriceSeguiminetoRecord = db!!.getIfAnyMarketPriceSeguiminetoRecord()

        if(anyMarketPriceSeguiminetoRecord == true){
            val marketPricesSeguiminetoJson: List<String>? = db!!.getAllMarketPriceSeguimiento()

           launch{marketPricesSeguiminetoJson?.indices?.forEach { i ->
                id = marketPricesSeguiminetoJson[i]
               DeleteMarketPriceSeguimientoUpSync(id.toInt())
                exito = true
                //  UpdateMarketPriceCreatedInApp(id.toInt())
            }
           }

            launch {
                marketPricesSeguiminetoJson?.indices?.forEach { i ->
                    id = marketPricesSeguiminetoJson[i]
                    PostMarketPriceSeguimientoUpSync(id.toInt())
                    Toast.makeText(context, "Market price # $id was uploaded to the server!", Toast.LENGTH_SHORT).show()

                    exito = true
                    //  UpdateMarketPriceCreatedInApp(id.toInt())
                }
            }
        }
    }

   suspend fun PostBankBalanceUpSync(Id: Int) {

                val data: String? = db?.getTransactionstoUpSync(Id)

                // Instantiate the RequestQueue.
                val mQueue = Volley.newRequestQueue(context)
                val url = "http://$retrievedIP/api/BankBalance"
                // Request a string response from the provided URL.
                val request: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        //  btSyncUpClients.setEnabled(true);
                        //  btSyncUpPurchasesSales.setEnabled(false);
                        //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { Toast.makeText(activity, "Customer/Supplier $Id has not been uploaded to the server!", Toast.LENGTH_SHORT).show() }) {
                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray? {
                        return try {
                            if (data == null) null else data.toByteArray(charset("utf-8"))
                        } catch (uee: UnsupportedEncodingException) {
                            null
                        }
                    }
                }

                //Add the request to the RequestQ Queue
                mQueue.add(request)

    }

    suspend fun PostMarketPriceSeguimientoUpSync(Id: Int) {

        val data: String? = db?.getMarketPriceSeguimientoToUpSync(Id)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(context)
        val url = "http://$retrievedIP/api/MarketPriceSeguimiento"
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpClients.setEnabled(true);
                //  btSyncUpPurchasesSales.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(activity, "Market price id $Id has not been uploaded to the server!", Toast.LENGTH_SHORT).show() }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)

    }

    suspend fun PostSpencesUpSync(Id: Int) {

        val data: String? = db?.getSpencestoUpSync(Id)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(context)
        val url = "http://$retrievedIP/api/Spences"
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpClients.setEnabled(true);
                //  btSyncUpPurchasesSales.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(activity, "Expense id $Id was not uploaded!", Toast.LENGTH_SHORT).show() }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)

    }

    suspend fun PostAllCarMaintenancesUpSync(Id: Int) {

        val data: String? = db?.getAllCarmaintenancestoUpSync(Id)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(context)
        val url = "http://$retrievedIP/api/MaintenanceRecords"
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpClients.setEnabled(true);
                //  btSyncUpPurchasesSales.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(activity, "Maintenance record id $Id was not uploaded!", Toast.LENGTH_SHORT).show() }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)

    }

    fun UpdateCompaniesUpSync(Id: Int) {

            val data: String? = db?.getCompaniestoUpSync(Id)

            // Instantiate the RequestQueue.
            val mQueue = Volley.newRequestQueue(activity)
            val url = "http://$retrievedIP/api/Companies/$Id"
            // Request a string response from the provided URL.
            val request: StringRequest = object : StringRequest(Method.PUT, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    //  btSyncUpItems.setEnabled(true);
                    //  btSyncUpClients.setEnabled(false);
                    //  Toast.makeText(getActivity(), "There was an error while updating items, please try again!", Toast.LENGTH_LONG).show();
                    e.printStackTrace()
                }
            }, Response.ErrorListener { Toast.makeText(activity, "Market price of company id $Id was not sychronized successfuly!", Toast.LENGTH_SHORT).show() }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        if (data == null) null else data.toByteArray(charset("utf-8"))
                    } catch (uee: UnsupportedEncodingException) {
                        null
                    }
                }
            }

            //Add the request to the RequestQ Queue
            mQueue.add(request)
            //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    fun UpdateCarModelsUpSync(Id: Int) {

        val data: String? = db?.getCarModelstoUpSync(Id)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://$retrievedIP/api/CarModels/$Id"
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.PUT, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpItems.setEnabled(true);
                //  btSyncUpClients.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while updating items, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(activity, "Current Milleage of car model $Id was not sychronized successfuly!", Toast.LENGTH_SHORT).show() }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    suspend fun UpdateMaintenanceRecordsUpSync(Id: Int) {

        val data: String? = db?.getMaintenanceRecordsUpdateToUpSync(Id)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://$retrievedIP/api/MaintenanceRecords/$Id"
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.PUT, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpItems.setEnabled(true);
                //  btSyncUpClients.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while updating items, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(activity, "maintenance id $Id was not sychronized successfuly!", Toast.LENGTH_SHORT).show() }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    fun UpdateMarketPriceUpSync(Id: Int) {

        val data: String? = db?.getMarketPricestoUpSync(Id)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://$retrievedIP/api/MarketPrices/$Id"
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.PUT, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpItems.setEnabled(true);
                //  btSyncUpClients.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while updating items, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(activity, "Market price of company id $Id was not sychronized successfuly!", Toast.LENGTH_SHORT).show() }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    fun UpdateSyncAuditUpSync(Id: Int, name: String) {

        val data: String? = db?.getSyncAudittoUpSync(Id, name)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://$retrievedIP/api/SyncAudit/$Id"
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.PUT, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpItems.setEnabled(true);
                //  btSyncUpClients.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while updating items, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { Toast.makeText(activity, "Sync Audit was not sychronized successfuly!", Toast.LENGTH_SHORT).show() }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return try {
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
    }

    suspend fun DeleteSpencesUpSync(Id: Int) {

        var spenceId: String? = db?.getFromSpencesSpenceIdAndDay(Id, 0)
        var day: String? = db?.getFromSpencesSpenceIdAndDay(Id, 1)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(context)
        val url = "http://$retrievedIP/api/Spences?spenceId=" + spenceId + "&day=" + day
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.DELETE, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpClients.setEnabled(true);
                //  btSyncUpPurchasesSales.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { // Toast.makeText(activity, "Support id $Id", Toast.LENGTH_SHORT).show() //When deleting something that does not exist comes here, that is why I comment it
         }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)
    }

    suspend fun DeleteMarketPriceSeguimientoUpSync(Id: Int) {

        var day: String? = db?.getFromMarketPRiceSeguimientoDay(Id, 1)
        var companyId: String? = db?.getFromMarketPRiceSeguimientoDay(Id, 2)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(context)
        val url = "http://$retrievedIP/api/MarketPriceSeguimiento?dia=" + day + "&companyId=" + companyId
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.DELETE, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpClients.setEnabled(true);
                //  btSyncUpPurchasesSales.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { //Toast.makeText(activity, "Support id $Id", Toast.LENGTH_SHORT).show() //When deleting something that does not exist comes here, that is why I comment it
         }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)
    }
    suspend fun DeleteAllMaintenancesUpSync(id: Int) {

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(context)
        val url = "http://$retrievedIP/api/MaintenanceRecords?id="+ id.toString()
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.DELETE, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpClients.setEnabled(true);
                //  btSyncUpPurchasesSales.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { //Toast.makeText(activity, "Support Car", Toast.LENGTH_SHORT).show() //When deleting something that does not exist comes here, that is why I comment it
         }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        //Add the request to the RequestQ Queue
        mQueue.add(request)
    }

    suspend fun DeleteBankBalanceUpSync(id: Int) {

        var bankId: String? = db?.getFromBankBalanceBankIdAndDay(id, 0)
        var day: String? = db?.getFromBankBalanceBankIdAndDay(id, 1)
        var operation: String? = db?.getFromBankBalanceBankIdAndDay(id, 2)

        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(context)
        val url = "http://$retrievedIP/api/BankBalance?BankId=" + bankId + "&day=" + day + "&operation=" + operation
        // Request a string response from the provided URL.
        val request: StringRequest = object : StringRequest(Method.DELETE, url, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: JSONException) {
                //  btSyncUpClients.setEnabled(true);
                //  btSyncUpPurchasesSales.setEnabled(false);
                //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                e.printStackTrace()
            }
        }, Response.ErrorListener { //Toast.makeText(activity, "Support Bank Balance", Toast.LENGTH_SHORT).show() //When deleting something that does not exist comes here, that is why I comment it
         }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        //Add the request to the RequestQ Queue
        mQueue.add(request)

    }

    suspend fun CheckIfBankBalancesAreAlreadySync() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/BankBalance"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var bankId: Int? = 0
                    var value: Double? = 0.00
                    var recordDate: String? = "0"

                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            bankId = item.getInt("BankId")
                            value = item.getDouble("Value")
                            recordDate = item.getString("RecordDate")
                            // Toast.makeText(
                            //        layoutInflater.context,
                            //        "Verifying Bank synchronization: $percentage%",
                            //        Toast.LENGTH_SHORT
                            //  ).show()

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.updateBankBalanceWhenSyncConfirmed(bankId, recordDate, value)
                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Bank verification complete!",
                            Toast.LENGTH_SHORT
                    ).show()
                },
                { error -> // error listener
                    //Toast.makeText(
                    //      layoutInflater.context,
                    //      error.toString() + "_onResponse",
                    //       Toast.LENGTH_SHORT
                    //  ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }


    suspend fun CheckIfSpencesAreAlreadySync() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/Spences"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var spenceId: Int? = 0
                    var spence: Double? = 0.00
                    var dspences: String? = "0"

                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            spenceId = item.getInt("SpenceId")
                            spence = item.getDouble("Spence")
                            dspences = item.getString("DSpences")
                            // Toast.makeText(
                            //         layoutInflater.context,
                            //        "Verifying Expense synchronization: $percentage%",
                            //        Toast.LENGTH_SHORT
                            // ).show()

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.updateSpencesWhenSyncConfirmed(spenceId, dspences, spence)

                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()

                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Expense verification complete!",
                            Toast.LENGTH_SHORT
                    ).show()
                },
                { error -> // error listener
                    // Toast.makeText(
                    //        layoutInflater.context,
                    //       error.toString() + "_onResponse",
                    //        Toast.LENGTH_SHORT
                    //).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }


    suspend fun CheckIfCarMaintenancesAreAlreadySync() {
        // Instantiate the RequestQueue.
        val mQueue = Volley.newRequestQueue(activity)
        val url = "http://" + retrievedIP + "/api/MaintenanceRecords?createdInApp=1"

        // request json array response from the provided url
        val request = JsonArrayRequest(
                Request.Method.GET, // method
                url, // url
                null, // json request
                { response -> // response listener
                    var modelId: Int? = 0
                    var maintenanceName: String? = "hola"
                    var lastEditDate: String? = "hola"
                    var item: JSONObject? = null
                    try {

                        a = 0
                        totalArrayLength = 0
                        for (i in 0 until response.length()) {
                            totalArrayLength = i
                        }
                        a = totalArrayLength + 1

                        // loop through the array elements
                        for (i in 0 until response.length()) {
                            val percentage = (i + 1) * 100 / a
                            item = response.getJSONObject(i)
                            modelId = item.getInt("ModelId")
                            maintenanceName = item.getString("MaintenanceName")
                            lastEditDate = item.getString("LastEditDate")

                            //   Toast.makeText(
                            //          layoutInflater.context,
                            //          "Verifying car maintenance synchronization: $percentage%",
                            //          Toast.LENGTH_SHORT
                            //   ).show()

                            val dataBasehelper = DatabaseHelper(layoutInflater.context)
                            val success: Boolean = dataBasehelper.updateCarMaintenancesWhenSyncConfirmed(modelId, maintenanceName, lastEditDate)

                        }

                    } catch (e: JSONException) {

                        //  btSyncDownClients.setEnabled(false);
                        //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                        e.printStackTrace()
                    }
                    Toast.makeText(
                            layoutInflater.context,
                            "Car maintenance verification complete!",
                            Toast.LENGTH_SHORT
                    ).show()

                },
                { error -> // error listener
                    //  Toast.makeText(
                    //         layoutInflater.context,
                    //         error.toString() + "_onResponse",
                    //        Toast.LENGTH_SHORT
                    // ).show()
                }
        )

        //Add the request to the RequestQ Queue
        mQueue.add<JSONArray>(request)
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }

    fun CheckIfAlreadySync() = runBlocking { // this: CoroutineScope
        //val job1= launch {CheckIfBankBalancesAreAlreadySync()}
        //job1.join()
        // val job2= launch {CheckIfSpencesAreAlreadySync()}
        //  job2.join()
        //  val job3= launch {CheckIfCarMaintenancesAreAlreadySync()}
        //  job3.join()

        launch { CheckIfBankBalancesAreAlreadySync() }
        launch { CheckIfSpencesAreAlreadySync() }
        launch { CheckIfCarMaintenancesAreAlreadySync() }
        BajarCarModels(false) //con falso para que no se guarde sino solo recupere la fecha de la ultima edicion del kilometraje

        // Toast.makeText(requireContext(), retrievedMilleageLastEditDate.toString(), Toast.LENGTH_SHORT).show()
        // Delay of 5 sec

        //The below method works but it is deprecated
        //  Handler().postDelayed({
        //     EnableButtons(true)
        //  }, 5000)

        //Its replaced by the below
        Handler(Looper.getMainLooper()).postDelayed({
            EnableButtons(true)
        }, 5000)

        //The below does not work to interact with the UI since it uses a different trade, app crashes
        //Timer().schedule(2000) {
          //  EnableButtons(true)
        //}
    }

    private fun EnableButtons(estado: Boolean){

            btSyncUpPortfolio?.isEnabled = estado
            btSyncUpCars?.isEnabled = estado
            btSyncUpExpenses?.isEnabled = estado
            btSyncUpBanks?.isEnabled = estado
            btSyncDownCars?.isEnabled = estado
            btSyncDownExpenses?.isEnabled = estado
            btSyncDownPortfolio?.isEnabled = estado
            btSyncDownBanks?.isEnabled = estado
    }

}