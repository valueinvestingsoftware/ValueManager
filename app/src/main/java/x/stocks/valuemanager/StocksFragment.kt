package x.stocks.valuemanager

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.CompaniesModel
import x.stocks.valuemanager.Models.MarketPriceSeguimientoModel
import x.stocks.valuemanager.Models.MarketPricesModel
import java.text.SimpleDateFormat
import java.util.*


class StocksFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    var imageViewLogoCompany: ImageView? = null
    var spinnerCompanies: Spinner? = null; var spinnerYearDividendos: Spinner? = null
    var switchAllYearsCompanies: Switch? = null; var switchExclude: Switch? = null; var switchDividendsForm: Switch? = null
    var btRanking: Button? = null; var btPurchasesSalesStocks: Button? = null; var btMarketPrices: Button? = null
    var btDividends: Button? = null; var btSimulatedProfit: Button? = null; var btPrices: Button? = null;
    var btUpdateMarketPrice: Button? = null; var btCompanyDividends: Button? = null
    var textIntrinsicPrice: TextView? = null; var textMarketPrice: TextView? = null; var textRanking: TextView? = null
    var textUpdateMarketPrice: TextView? = null
    var exclude: String? = null; var imageIdOfDisplayedItem: String? = null;
    var retrievedCompany: String? = null; var retrievedDividendsForm: String? = null
    var retrievedYear: String? = null; var retrievedAllYears: String? = null;
    var labelItemIds: List<String> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "PORTFOLIO MANAGEMENT"
        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_stocks, container, false)
        val db = DatabaseHelper(requireContext())

        imageViewLogoCompany = view?.findViewById<ImageView>(R.id.ivLogoCompany)
        labelItemIds = db.getAllCompanyIds()
        
        textMarketPrice = view?.findViewById<TextView>(R.id.txtMarketPrice)
        textRanking = view?.findViewById<TextView>(R.id.txtRanking)
        textIntrinsicPrice = view?.findViewById<TextView>(R.id.txtIntrinsicPrice)
        textUpdateMarketPrice = view?.findViewById<TextView>(R.id.txtUpdateMarketPrice)

        spinnerCompanies = view?.findViewById<Spinner>(R.id.spCompanies)

        switchAllYearsCompanies = view?.findViewById<Switch>(R.id.swAllYearsCompanies)
        switchDividendsForm = view?.findViewById<Switch>(R.id.swDividendsForm)
        switchExclude = view?.findViewById<Switch>(R.id.swExclude)
        switchExclude?.setOnCheckedChangeListener { buttonView, isChecked ->
            var chequed: String = ""
            if (isChecked) {
                chequed = "si"
            } else {
                chequed = "no"
            }

            val dataBasehelper = DatabaseHelper(requireContext())
            var companyId: Int = 0
            companyId =
                    dataBasehelper.getCompanyId(spinnerCompanies?.selectedItem.toString())

            var marketPrice: String? = null
            marketPrice = textMarketPrice?.text.toString()
            var dobleAmountmarketPrice: Double = marketPrice.toDouble()

            UpdateMarketPriceInCompanies(companyId, chequed, dobleAmountmarketPrice, false)
        }

        loadCompanies(requireContext())

        spinnerYearDividendos = view?.findViewById<Spinner>(R.id.spYearDividendos)
        loadYears(requireContext())
        spinnerYearDividendos?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

        sharedPreferences = inflater.context.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        retrievedCompany = sharedPreferences.getString("company", "")
        retrievedDividendsForm = sharedPreferences.getString("dividendsform", "1")
        retrievedYear = sharedPreferences.getString("year", "2021")
        retrievedAllYears = sharedPreferences.getString("allYears", "1")

        spinnerCompanies?.setSelection(getIndex(spinnerCompanies!!, retrievedCompany.toString()))
       if(retrievedDividendsForm == "1"){
         switchDividendsForm?.isChecked = false
       }else{
           switchDividendsForm?.isChecked = true
       }

        if(retrievedAllYears == "1"){
            switchAllYearsCompanies?.isChecked = true
        }else{
            switchAllYearsCompanies?.isChecked = false
        }

        spinnerYearDividendos?.setSelection(getIndex(spinnerYearDividendos!!, retrievedYear.toString()))

        spinnerCompanies?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {

                textIntrinsicPrice?.text = db.getFromCompanies(
                        2,
                        spinnerCompanies?.selectedItem.toString()
                ).toString()
                textRanking?.text = db.getFromCompanies(
                        1,
                        spinnerCompanies?.selectedItem.toString()
                ).toInt().toString()
                textMarketPrice?.text = db.getFromCompanies(
                        3,
                        spinnerCompanies?.selectedItem.toString()
                ).toString()
                textUpdateMarketPrice?.text = db.getFromCompanies(
                        3,
                        spinnerCompanies?.selectedItem.toString()
                ).toString()
                exclude = db.getFromCompaniesExclude(spinnerCompanies?.selectedItem.toString())


                // On selecting a spinner item
                val label = parent!!.getItemAtPosition(position).toString()
                //Show image in imageview

                // Showing selected spinner item
                //  Toast.makeText(parent.getContext(), String.valueOf(labelItemIds.get(position)), Toast.LENGTH_LONG).show();

                if(labelItemIds.isEmpty() == false){
                    //Show image in imageview
                    imageIdOfDisplayedItem = labelItemIds?.get(position).toString()
                    val bmp: Bitmap? = db.getImage(imageIdOfDisplayedItem?.toInt())
                    imageViewLogoCompany?.setImageBitmap(bmp)
                }

                if(exclude == "no"){
                    switchExclude?.isChecked = false
                }else{
                    switchExclude?.isChecked = true
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


       btCompanyDividends = view?.findViewById<Button>(R.id.btnCompanyDividends)
        btCompanyDividends?.setOnClickListener {
            if(switchDividendsForm?.isChecked == false){
                CallTheRightGraph(1, 3)
            }else{
                CallTheRightGraph(2, 3)
            }
        }

        btRanking = view?.findViewById<Button>(R.id.btnRanking)
        btRanking?.setOnClickListener{
            CallTheRightGraph(1, 1)
        }

        btMarketPrices = view?.findViewById<Button>(R.id.btnMarketPrices)
        btMarketPrices?.setOnClickListener{
            CallTheRightGraph(1, 2)
        }

        btPurchasesSalesStocks = view?.findViewById<Button>(R.id.btnPurchasesSoldStocks)
        btPurchasesSalesStocks?.setOnClickListener {
            CallTheRightGraph(2, 1)
        }

        btDividends = view?.findViewById<Button>(R.id.btnDividends)
        btDividends?.setOnClickListener {
            CallTheRightGraph(4, 1)
        }

        btSimulatedProfit = view?.findViewById<Button>(R.id.btnSimulatedProfit)
        btSimulatedProfit?.setOnClickListener {
            CallTheRightGraph(5, 1)
        }
        btPrices = view?.findViewById<Button>(R.id.btnPrices)
        btPrices?.setOnClickListener {
            CallTheRightGraph(6, 1)
        }

        btUpdateMarketPrice = view?.findViewById<Button>(R.id.btnUpdateMarketPrice)
        btUpdateMarketPrice?.setOnClickListener{
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (TextUtils.isEmpty(textMarketPrice?.text.toString())) {
                            textMarketPrice?.setError("Type the market price!")
                        } else {
                            textMarketPrice?.setError(null)
                            val dataBasehelper = DatabaseHelper(requireContext())
                            var companyId: Int = 0
                            companyId =
                                    dataBasehelper.getCompanyId(spinnerCompanies?.selectedItem.toString())

                            UpdateMarketPrice(companyId)
                            var chequed: String = ""
                            if (switchExclude?.isChecked == true) {
                                chequed = "si"
                            } else {
                                chequed = "no"
                            }

                            var marketPrice: String? = null
                            marketPrice = textUpdateMarketPrice?.text.toString()
                            var dobleAmountmarketPrice: Double = marketPrice!!.toDouble()

                            UpdateMarketPriceInCompanies(companyId, chequed, dobleAmountmarketPrice, true)
                            textMarketPrice?.setText(textUpdateMarketPrice?.text.toString())
                        }

                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Do you to update today's market price of the current stock?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()
         }

        return view
    }

    private fun loadCompanies(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)

            // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: List<String> = db.getCompanies(1)

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
            spinnerCompanies?.adapter  = dataAdapter

            // Spinner click listener
            exito = true

        } catch (e: Exception) {
            exito = false
            exito
        }
        return exito
    }

    private fun loadYears(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)

            // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: List<String> = db.getYears(1)
            val lastYear: Int = db.getLastYear()
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
            spinnerYearDividendos?.adapter  = dataAdapter

            spinnerYearDividendos?.setSelection(lables.indexOf(lastYear.toString()));
            // Spinner click listener
            exito = true

        } catch (e: Exception) {
            exito = false
            exito
        }
        return exito
    }

    private fun UpdateMarketPrice(companyId: Int) {

        val dataBasehelper = DatabaseHelper(requireContext())
        var marketPrice: String? = null
        marketPrice = textUpdateMarketPrice?.text.toString()
        var dobleAmountMarketPrice: Double = marketPrice.toDouble()

        textUpdateMarketPrice?.setError(null)
        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var currentDate = sdf.format(Date())
        var dateString: String = currentDate.toString()

        var marketPricesModel: MarketPricesModel? = null
        var marketPriceSeguimientoModel: MarketPriceSeguimientoModel? = null

        try {
            marketPricesModel = MarketPricesModel(
                    companyId,
                    dobleAmountMarketPrice,
                    dateString,
                    true
            )
            marketPriceSeguimientoModel = MarketPriceSeguimientoModel(
                    companyId,
                    dateString,
                    dobleAmountMarketPrice,
                    dateString,
                    true
            )
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.updateOneMarketPrices(marketPricesModel, companyId)
        dataBasehelper.delete("MarketPriceSeguimiento", "Day = '" + dateString + "' AND CompanyId = " + companyId)
        dataBasehelper.addOneMarketPriceSeguimiento(marketPriceSeguimientoModel)

        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
    }

    private fun UpdateMarketPriceInCompanies(companyId: Int, chequed: String, dobleAmountmarketPrice: Double, showSuccess: Boolean) {

        val dataBasehelper = DatabaseHelper(requireContext())

                var companiesModel: CompaniesModel? = null
        try {
            companiesModel = CompaniesModel(
                    companyId,
                    "Dummy",
                    0.00,
                    0.00,
                    0,
                    dobleAmountmarketPrice,
                    chequed,
                    null,
                    true
            )
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.updateOneCompanies(companiesModel, companyId, showSuccess)
        if(showSuccess == true){
            Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
        }
    }
    private fun CallTheRightGraph(queGrafico: Int, que: Int) {

        SaveSharedPreferences()

                var allYears: Boolean = false
        var year: String = spinnerYearDividendos?.selectedItem.toString()
        if(switchAllYearsCompanies?.isChecked == true){
            allYears = true
        }
        var chartFragment: Fragment? = null
        when(que){
            1 -> chartFragment = BarrasFragment()
            2 -> chartFragment = LineasFragment()
            3 -> chartFragment = RadarFragment()
        }
                    val bundle = Bundle()
                    bundle.putInt("queGrafico", queGrafico)
                    bundle.putBoolean("AllYears", allYears)
                    bundle.putString("Year", year)
                    bundle.putString("SelectedCompany", spinnerCompanies?.selectedItem.toString())
                    chartFragment?.setArguments(bundle)

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, chartFragment!!)
        transaction?.commit()

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

    private fun SaveSharedPreferences(){
        retrievedCompany = spinnerCompanies?.selectedItem.toString()
        if(switchDividendsForm?.isChecked == false){
            retrievedDividendsForm = "1"
        }else{
            retrievedDividendsForm = "2"
        }

        if(switchAllYearsCompanies?.isChecked ==true){
           retrievedAllYears = "1"
        }else {
            retrievedAllYears = "0"
        }
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("company", retrievedCompany)
        editor.putString("dividendsform", retrievedDividendsForm.toString())
        editor.putString("year", spinnerYearDividendos?.selectedItem.toString())
        editor.putString("allYears", retrievedAllYears)
        editor.commit()
    }
}


