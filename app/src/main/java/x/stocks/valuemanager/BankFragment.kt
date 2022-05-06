package x.stocks.valuemanager

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.BankBalanceModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormatSymbols
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class BankFragment : Fragment() {

    var lvDate: TextView? = null; var textTotalAllbanks: TextView? = null; var textTotalSelectedBank: TextView? = null
    var textAmountBank: TextView? = null; var textCommentBank: TextView? = null
    var btSaveBankTransaction: Button? = null; var btGraphOneAccount: Button? = null; var btGraphAccounts: Button? = null;
    var btDate: Button? = null; var btDeleteBankTransaction: Button? = null; var btNewBankTransaction: Button? = null

    var spinnerBanks: Spinner? = null; lateinit var spinnerSavedRecords: Spinner; var spinnerYearsBank: Spinner? = null
    var swichDebitCredit: Switch? = null; var switchAllAccounts: Switch? = null
    var retrievedDate: String? = null; var retrievedMonthNumber: String? = null; var retrievedMonthName: String? = null
    var retrievedDayOfWeekNumber: String? = null; var retrievedDayOfMonthNumber: String? = null; var retrievedDayName: String? = null
    var retrievedYear: String? = null; var yearGraph: String? = null; var cuenta: String? = null

    var mYear = 0; var mMonth:Int = 0; var mDayOfMonth:Int = 0; var a:Int = 0

    override fun onCreateView(

            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "BANK ACCOUNTS"

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val spRetrieve = inflater.context.applicationContext.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        cuenta = spRetrieve.getString("cuenta", "No account")
        yearGraph = spRetrieve.getString("yeargraph", "2021")
        retrievedDate = spRetrieve.getString("dateSpences", "2021-01-01")
        retrievedMonthNumber = spRetrieve.getString("monthNumber", "0")
        var monthName: String = spRetrieve.getString("monthName", "None")?.toLowerCase()!!
        retrievedMonthName = monthName[0].toUpperCase() + monthName?.substring(1)
        retrievedDayOfWeekNumber = spRetrieve.getString("dayOfWeekNumber", "0")
        retrievedDayOfMonthNumber = spRetrieve.getString("dayOfMonthNumber", "0")
        retrievedDayName = spRetrieve.getString("dayName", "None")
        retrievedYear = spRetrieve.getString("year", "1999")
        // Toast.makeText(inflater.context, retrievedDate, Toast.LENGTH_SHORT).show()

        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_bank, container, false)


        val db = DatabaseHelper(requireContext())
        textTotalAllbanks = view?.findViewById<TextView>(R.id.txtTotalAllBanks)
        AllBanksTotal()

        textTotalSelectedBank = view?.findViewById<TextView>(R.id.txtTotalSelectedBank)

        textCommentBank = view?.findViewById<TextView>(R.id.txtCommentBanks)

        switchAllAccounts =  view?.findViewById<Switch>(R.id.swAllAccounts)

        spinnerYearsBank = view?.findViewById<Spinner>(R.id.spYearBanks)
        loadYears(requireContext())

        btGraphOneAccount = view?.findViewById<Button>(R.id.btnGraphOneAccount)
        btGraphOneAccount?.setOnClickListener {

            val editor = spRetrieve.edit()
            editor.putString("cuenta", spinnerBanks?.selectedItem.toString())
            editor.putString("yeargraph", spinnerYearsBank?.selectedItem.toString())
            editor.commit()

            CallTheRightGraph(4)
        }

        btGraphAccounts = view?.findViewById<Button>(R.id.btnGraphAccounts)
        btGraphAccounts?.setOnClickListener {

            val editor = spRetrieve.edit()
            editor.putString("cuenta", spinnerBanks?.selectedItem.toString())
            editor.putString("yeargraph", spinnerYearsBank?.selectedItem.toString())
            editor.commit()

            CallTheRightGraph(3)
        }

      //  textDateBank = view?.findViewById<TextView>(R.id.txtDateBank)  //ya hay lvDate, esto esta duplicado
        textAmountBank = view?.findViewById<TextView>(R.id.txtAmountBank)
        textAmountBank?.isEnabled = false
        textAmountBank?.doAfterTextChanged {
            val cantidadString: String = textAmountBank?.text.toString()
            val cantidadSpinner: String = spinnerSavedRecords.selectedItem.toString()
            if(cantidadString != cantidadSpinner){
                btSaveBankTransaction?.isEnabled = true
            }
        }
        btNewBankTransaction = view?.findViewById<Button>(R.id.btnNewBankTransaction)
        btNewBankTransaction?.text == "New"
        btNewBankTransaction?.setOnClickListener {

            if(btNewBankTransaction?.text == "New") {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                            textCommentBank?.setText(null)
                            textAmountBank?.setError(null)
                        textAmountBank?.isEnabled = true
                        btDate?.isEnabled = false
                            swichDebitCredit?.isEnabled = true
                            swichDebitCredit?.isChecked = false
                            btDeleteBankTransaction?.isEnabled = false
                            btSaveBankTransaction?.text = "SAVE NEW"
                            btSaveBankTransaction?.isEnabled = true
                            textAmountBank?.setText("")
                            btNewBankTransaction?.text = "Undo"
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Do you want to record a new bank transaction?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()

            }else{
                textCommentBank?.setText(null)
                textAmountBank?.setError(null)
                loadSavedRecords(requireContext())
                CargarTextViews()
                btNewBankTransaction?.text = "New"
                btSaveBankTransaction?.text = "UPDATE"
                textAmountBank?.isEnabled = false
                btDate?.isEnabled = true
            }
        }

        btSaveBankTransaction = view?.findViewById<Button>(R.id.btnSaveBankTransaction)
        btSaveBankTransaction?.isEnabled = false
        btSaveBankTransaction?.setOnClickListener{

            val db = DatabaseHelper(requireContext())
            var operation: String? = null
            var signo: Int = 1

            if(TextUtils.isEmpty(textAmountBank?.text.toString())) {
                textAmountBank?.setError("Type a transaction ammount!")
            }else{
                textAmountBank?.setError(null)

                var cantidad: String = textAmountBank?.text.toString()
                var cantidadDoble: Double = cantidad.toDouble()
                var updateorSave: String?  = null

                if(swichDebitCredit?.isChecked == true){
                    operation = "Deposit"
                    if(cantidadDoble > 0) {
                        signo = 1
                    }
                }else{
                    operation = "Withdrawal"
                    if(cantidadDoble >= 0) {
                        signo = -1
                    }else{
                        signo = 1
                    }

                }
                var tieneRegistroBancario: Boolean = false
                if (btDeleteBankTransaction?.isEnabled == true){
                    tieneRegistroBancario = true
                    updateorSave = "update"
                }else{
                    tieneRegistroBancario = false
                    updateorSave = "save"
                }

                val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {

                            if(tieneRegistroBancario== true){

                                var bankId: Int = 0             //With the below I do not let user to edit debit if it has been recorded from the Spences Module
                                bankId = db.getBankId((spinnerBanks?.selectedItem.toString()))

                                var amountSpence: String? = null
                                amountSpence = spinnerSavedRecords?.selectedItem.toString()  //textAmountBank?.text.toString()
                                var dobleAmountSpence: Double = amountSpence.toDouble()

                                var uniqueIdentifier: String = db.RetrieveDebitUniqueIdentifier_Banks(bankId, retrievedDate.toString() + "T00:00:00", dobleAmountSpence)

                                if(uniqueIdentifier == "0"){
                                    updateDebitCredit(retrievedDate.toString() + "T00:00:00", spinnerBanks?.selectedItem.toString(), spinnerSavedRecords.selectedItem.toString(), signo)
                                }else{
                                    Toast.makeText(
                                            layoutInflater.context,
                                            "This debit was recorded from the Expenses module!", Toast.LENGTH_LONG
                                    ).show()
                                }
                            }else{
                                InsertDebitCredit(operation!!, signo)
                            }
                            btNewBankTransaction?.text = "New"
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Confirmation")
                builder.setIcon(android.R.drawable.stat_sys_download)
                builder.setCancelable(false)
                builder.setMessage("Do you want to " + updateorSave + " this " + operation + "?").setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                        .setNegativeButton("No", dialogOnClickListener).show()
            }
        }

        btDeleteBankTransaction = view?.findViewById<Button>(R.id.btnDeleteBankTransaction)
        btDeleteBankTransaction?.setOnClickListener {
            var operation: String? = null
            if(swichDebitCredit?.isChecked == true){
                operation = "Deposit"
            }else{
                operation = "Withdrawal"
            }
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        var bankId: Int = 0             //With the below I do not let user to delete debit if it has been recorded from the Spences Module
                        bankId = db.getBankId((spinnerBanks?.selectedItem.toString()))

                        var amountSpence: String? = null
                        amountSpence = spinnerSavedRecords?.selectedItem.toString()  //textAmountBank?.text.toString()
                        var dobleAmountSpence: Double = amountSpence!!.toDouble()

                        var uniqueIdentifier: String = db.RetrieveDebitUniqueIdentifier_Banks(bankId, retrievedDate.toString() + "T00:00:00", dobleAmountSpence)

                        if(uniqueIdentifier == "0"){
                            deleteDebitCredit()
                        }else{
                            Toast.makeText(
                                    layoutInflater.context,
                                    "This debit was recorded from the Expenses module!", Toast.LENGTH_LONG
                            ).show()
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
            builder.setMessage("Do you want to delete this " + operation + "?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()
        }
        if (view != null) {
            spinnerSavedRecords = view.findViewById<Spinner>(R.id.spSavedRecords)
        }
        swichDebitCredit = view?.findViewById<Switch>(R.id.swDebitCredit)

        spinnerBanks = view?.findViewById<Spinner>(R.id.spBanks)
        loadBanks(requireContext())
        spinnerBanks?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                try {
                    textCommentBank?.setText(null)
                    textAmountBank?.setError(null)
                    loadSavedRecords(requireContext())
                    CargarTextViews()
                }catch(e: Exception){
                    println("Exception is handled.")
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        spinnerSavedRecords?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var valorBanco: String? = null
                valorBanco = spinnerSavedRecords?.selectedItem.toString()
                if(valorBanco == "NoBankExpense"){
                    btDeleteBankTransaction?.isEnabled = false
                    btSaveBankTransaction?.isEnabled = false
                    btSaveBankTransaction?.text = "SAVE NEW"
                }else{
                    btDeleteBankTransaction?.isEnabled = true
                    btSaveBankTransaction?.text = "UPDATE"
                    swichDebitCredit?.isEnabled = false
                    btSaveBankTransaction?.isEnabled = false
                    textAmountBank?.text = spinnerSavedRecords?.selectedItem.toString()
                    val valorBank: Double = valorBanco.toDouble()
                    if(valorBank < 0){
                        swichDebitCredit?.isChecked = false
                    }else{
                        swichDebitCredit?.isChecked = true
                    }

                    val commentBank= db.getAllSavedRecords(2, spinnerBanks?.selectedItem.toString(), retrievedDate, spinnerSavedRecords?.selectedItem.toString())
                    textCommentBank?.text = commentBank.toString()

                    //This is to control that the value textbox is enabled to be edited
                    val noBankExpense: String = parent?.getItemAtPosition(position).toString()
                    if(noBankExpense == "NoBankExpense"){
                        textAmountBank?.isEnabled = false
                    }else{
                        textAmountBank?.isEnabled = true
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        lvDate = view?.findViewById<TextView>(R.id.txtDateBank)
       // lvDate?.setText(retrievedDate) //me parece mas adecuado que recupere la fecha de hoy
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")//("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        lvDate?.setText(formatted)
        retrievedDate = formatted

        btDate = view?.findViewById<Button>(R.id.btnDateBank)


        btDate?.setOnClickListener{
            val cal = Calendar.getInstance()
            mDayOfMonth = cal[Calendar.DATE]
            mMonth = cal[Calendar.MONTH]
            mYear = cal[Calendar.YEAR]
            val datePickerDialog = DatePickerDialog(inflater.context, android.R.style.Theme_DeviceDefault_Dialog, { view, year, month, dayOfMonth ->
                val fixedMonth: String = returnCorrectNumberDigits(month + 1)
                val fixedDayOfMonth: String = returnCorrectNumberDigits(dayOfMonth)
                lvDate?.setText("$year-$fixedMonth-$fixedDayOfMonth")
                retrievedDate = lvDate?.getText().toString()
                textCommentBank?.setText("")
                
                //Lo de abajo es para que recupere el numerodediadelasemana, etc de fechas pasadas a la actual
                val date = java.sql.Date.valueOf(retrievedDate);
                cal.time = date
                retrievedDayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK).toString()
                retrievedMonthNumber = fixedMonth
                retrievedYear = year.toString()
                retrievedDayOfMonthNumber = fixedDayOfMonth
                var monthName: String = DateFormatSymbols().months[fixedMonth.toInt() - 1].toLowerCase()
                retrievedMonthName = monthName[0].toUpperCase() + monthName.substring(1)
                retrievedDayName = DateFormatSymbols().weekdays[retrievedDayOfWeekNumber!!.toInt()]

                loadSavedRecords(requireContext())
                CargarTextViews()

                val editor = spRetrieve.edit()
                editor.putString("cuenta", spinnerBanks?.selectedItem.toString())
                editor.putString("yeargraph", spinnerYearsBank?.selectedItem.toString())
                editor.putString("dateSpences", retrievedDate)
                editor.putString("monthNumber", retrievedMonthNumber)
                editor.putString("monthName", retrievedMonthName)
                editor.putString("dayOfWeekNumber", retrievedDayOfWeekNumber)
                editor.putString("dayOfMonthNumber", retrievedDayOfMonthNumber)
                editor.putString("dayName", retrievedDayName)
                editor.putString("year", retrievedYear)
                editor.commit()

            }, mYear, mMonth, mDayOfMonth)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()

            val bankDate: String? = retrievedDate
            val bankName: String? = spinnerBanks?.selectedItem.toString()
            loadSavedRecords(requireContext())

        }

        return view
    }

    private fun returnCorrectNumberDigits(entra: Int): String {
        var sale = entra.toString()
        val intermedio = entra.toString()
        if (intermedio.length < 2) {
            sale = "0$entra"
        }
        return sale
    }

    private fun loadBanks(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)

            // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: List<String> = db.getAllBanks(1)

            // Creating adapter for spinner

            // Creating adapter for spinner
            val dataAdapter = ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, lables)

            // Drop down layout style - list view with radio button

            // Drop down layout style - list view with radio button
           // dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // attaching data adapter to spinner

            // attaching data adapter to spinner
            spinnerBanks?.adapter  = dataAdapter

            spinnerBanks?.setSelection(
                    getIndex(
                            spinnerBanks!!,
                            cuenta.toString()
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

    private fun loadSavedRecords(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)

            val bankDate: String? = retrievedDate
            val bankName: String? = spinnerBanks?.selectedItem.toString()
            // Spinner Drop down elements
            val lables: List<String> = db.getAllSavedRecords(1, bankName, bankDate)

            // Creating adapter for spinner

            // Creating adapter for spinner
            val dataAdapter = ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, lables)

            // Drop down layout style - list view with radio button

            // Drop down layout style - list view with radio button
            // dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // attaching data adapter to spinner

            // attaching data adapter to spinner
            spinnerSavedRecords?.adapter  = dataAdapter

            // Spinner click listener
            exito = true

        } catch (e: Exception) {
            exito = false
            exito
        }

        return exito
    }

    private fun CargarTextViews(){

       SelectedBankTotal()

        var valorBanco: String? = null
        valorBanco = spinnerSavedRecords?.selectedItem.toString()
        if(valorBanco == "NoBankExpense"){
               textAmountBank?.isEnabled = false
        }else{
            textAmountBank?.text = spinnerSavedRecords?.selectedItem.toString()
            var amountBankString : String = textAmountBank?.text.toString()
            var amountBankDouble : Double = amountBankString.toDouble()
            if(amountBankDouble < 0){
                swichDebitCredit?.isChecked = true
            }else{
                swichDebitCredit?.isChecked = false
            }
        }

    }

    private fun InsertDebitCredit(operation: String, signo: Int) {

        val dataBasehelper = DatabaseHelper(requireContext())
        var amountSpence: String? = null
        amountSpence = textAmountBank?.text.toString()
        var dobleAmountSpence: Double = amountSpence.toDouble()

        var dateSpence: String? = null
        dateSpence = lvDate?.text.toString()

        var bankId: Int = 0
        bankId = dataBasehelper.getBankId((spinnerBanks?.selectedItem.toString()))

        textAmountBank?.setError(null)

        var bankBalanceModel: BankBalanceModel? = null
        try {
            bankBalanceModel = BankBalanceModel(-1, bankId, "dummy", operation,  dobleAmountSpence * signo, textCommentBank?.text.toString(), retrievedDate.toString() + "T00:00:00",
                    retrievedDate.toString() + "T00:00:00", true, retrievedMonthName.toString(), retrievedMonthNumber!!.toInt(), retrievedYear!!.toInt(), 0.0, "0")
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.addOneBankBalance(bankBalanceModel)
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()

        if(success == true){ //I load all saved records
            loadSavedRecords(requireContext())
            SelectedBankTotal()
            AllBanksTotal()
            btNewBankTransaction?.text == "New"
        }
    }

    private fun updateDebitCredit(date: String, bankName: String, amount: String, signo: Int) {

        val dataBasehelper = DatabaseHelper(requireContext())
        var amountSpence: String? = null
        amountSpence = textAmountBank?.text.toString()
        var dobleAmountSpence: Double = amountSpence.toDouble()

        var dateSpence: String? = null
        dateSpence = lvDate?.text.toString()

        var bankId: Int = 0
        bankId = dataBasehelper.getBankId((spinnerBanks?.selectedItem.toString()))

        textAmountBank?.setError(null)
        var comentario: String = textCommentBank?.text.toString().replace("[", "")
        comentario = comentario.replace("]", "")
        var bankBalanceModel: BankBalanceModel? = null
        try {
            bankBalanceModel = BankBalanceModel(-1, bankId, "dummy","Dummy",  dobleAmountSpence * signo, comentario, retrievedDate.toString() + "T00:00:00",
                    retrievedDate.toString() + "T00:00:00", true, retrievedMonthName.toString(), retrievedMonthNumber!!.toInt(), retrievedYear!!.toInt(), 0.0, "0")
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.updateOneBankBalance(bankBalanceModel, retrievedDate.toString() + "T00:00:00", bankId, spinnerSavedRecords.selectedItem.toString())
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()

        if(success == true){ //I load all saved records
            loadSavedRecords(requireContext())
            spinnerSavedRecords.setSelection(getIndex(spinnerSavedRecords, textAmountBank?.text.toString()))
            SelectedBankTotal()
            AllBanksTotal()
            btNewBankTransaction?.text == "New"
        }

    }

    private fun deleteDebitCredit() {

        val dataBasehelper = DatabaseHelper(requireContext())
        var bankId: Int = 0
        bankId = dataBasehelper.getBankId((spinnerBanks?.selectedItem.toString()))

        textAmountBank?.setError(null)

        val success: Boolean = dataBasehelper.deleteOneBankBalance(retrievedDate.toString() + "T00:00:00", bankId, spinnerSavedRecords.selectedItem.toString())
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
        textCommentBank?.setText("")
        textAmountBank?.setText("")

        if(success == true){ //I load all saved records
            loadSavedRecords(requireContext())
            spinnerSavedRecords.setSelection(getIndex(spinnerSavedRecords, textAmountBank?.text.toString()))
        }

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
            spinnerYearsBank?.adapter = dataAdapter
            spinnerYearsBank?.setSelection(
                getIndex(
                    spinnerYearsBank!!,
                    yearGraph.toString()
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

    private fun CallTheRightGraph(queGrafico: Int) {

        var allYears: Boolean = false
        if(switchAllAccounts?.isChecked == true){
            allYears = true
        }

        var year: String = spinnerYearsBank?.selectedItem.toString()

        var chartFragment: Fragment? = null
        when(queGrafico){
            4 -> chartFragment = RadarFragment()
            3 -> chartFragment = PieFragment()
        }
        val bundle = Bundle()
        bundle.putInt("queGrafico", queGrafico)
        bundle.putBoolean("AllYears", allYears)
        bundle.putString("Year", year)
        bundle.putString("SelectedCompany", spinnerBanks?.selectedItem.toString())
        chartFragment?.setArguments(bundle)

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, chartFragment!!)
        transaction?.commit()
    }

    private fun AllBanksTotal(){
        val db = DatabaseHelper(requireContext())
        val saldoAllBanks: Double =  db.getTotalAllBanks()
        val saldoInicialAllBanks: Double = db.getTotalInitialBalanceAllBanks()
        val totAll = BigDecimal(saldoInicialAllBanks + saldoAllBanks).setScale(2, RoundingMode.HALF_EVEN)
        textTotalAllbanks?.text = totAll.toString()
    }

    private fun SelectedBankTotal(){
        val db = DatabaseHelper(requireContext())
        val saldoOneBank: Double =  db.getTotalOneBank(spinnerBanks?.selectedItem.toString())
        val saldoInicialOneBank: Double = db.getTotalInitialBalanceOneBank(spinnerBanks?.selectedItem.toString())
        val totOne = BigDecimal(saldoOneBank + saldoInicialOneBank).setScale(2, RoundingMode.HALF_EVEN)
        textAmountBank?.setText("")

        textTotalSelectedBank?.text = totOne.toString()
    }

}