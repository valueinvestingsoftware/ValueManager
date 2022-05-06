package x.stocks.valuemanager

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.BankBalanceModel
import x.stocks.valuemanager.Models.ParametersModel
import x.stocks.valuemanager.Models.SelfLoansModel
import x.stocks.valuemanager.Models.SpencesModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormatSymbols
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SpencesFragment : Fragment() {

    var lvDate: TextView? = null; var textExpense: TextView? = null; var textMonthBudget: TextView? = null
    var textAvailableBudget: TextView? = null; var textMonthExpenses: TextView? = null; var textTotalMExpenses: TextView? = null
    var textYearExpenses: TextView? = null; var textMonthNameExpenses: TextView? = null
    var textCreatedInApp: TextView? = null
    var btDate: Button? = null; var btGraphOneExpense: Button? = null; var btGraphExpenses: Button? = null
    var btSaveExpense: Button? = null; var btSaveBudget: Button? = null;
    var spinnerExpenseCategories: Spinner? = null; var spinnerBankExpenses: Spinner? = null;
    var spinnerYearExpensesGraph: Spinner? = null; var spinnerTopGraph: Spinner? = null
    var switchBankExpenses: Switch? = null; var switchtopBottom: Switch? = null;
    var mYear = 0; var mMonth:Int = 0; var mDayOfMonth:Int = 0; var mDayOfWeek: Int = 0; var a:Int = 0;
    var top: String? = null; var topNumber: String? = null; var spence: String? = null; var cuenta: String? = null
    var mDayName: String = "No Day Name"; var mMonthName: String = "No Month Name";
    var retrievedDate: String? = null; var retrievedMonthNumber: String? = null; var retrievedMonthName: String? = null
    var retrievedDayOfWeekNumber: String? = null; var retrievedDayOfMonthNumber: String? = null; var retrievedDayName: String? = null
    var retrievedYear: String? = null; var yearGraph: String? = null

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "EXPENSE MANAGEMENT"

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val spRetrieve = inflater.context.applicationContext.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)

        cuenta = spRetrieve.getString("cuenta", "None")
        spence = spRetrieve.getString("spence", "None")
        top = spRetrieve.getString("top", "0")
        topNumber = spRetrieve.getString("topnumber", "10")
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
        val view: View? = inflater.inflate(R.layout.fragment_spences, container, false)

        val db = DatabaseHelper(requireContext())
        textCreatedInApp = view?.findViewById<TextView>(R.id.txtCreatedInApp)
        textExpense = view?.findViewById<TextView>(R.id.txtExpense)
        var expenseString: String? = null
        var expenseDouble: Double? = null
        textExpense?.setOnClickListener {
            if (TextUtils.isEmpty(textExpense?.text.toString())) {

            } else {
                expenseString = textExpense?.text.toString()
                expenseDouble = expenseString!!.toDouble()
                if(expenseDouble == 0.0){
                    textExpense!!.setText("")
                }
            }
        }

        switchtopBottom = view?.findViewById<Switch>(R.id.switchTopLowerSpences)
        if(top == "1"){
            switchtopBottom?.setChecked(true)
        }

        spinnerYearExpensesGraph = view?.findViewById<Spinner>(R.id.spYearExpenseGraph)
        loadYears(requireContext())

        spinnerTopGraph = view?.findViewById<Spinner>(R.id.spinnerTopGraph)
        LoadTopToShowInGraph(requireContext())

        btGraphOneExpense = view?.findViewById<Button>(R.id.btnGraphOneExpense)
        btGraphOneExpense?.setOnClickListener {

            top = "0"
            if(switchtopBottom?.isChecked == true){
                top = "1"
            }
            val editor = spRetrieve.edit()
            editor.putString("cuenta", spinnerBankExpenses?.selectedItem.toString())
            editor.putString("spence", spinnerExpenseCategories?.selectedItem.toString())
            editor.putString("top", top)
            editor.putString("topnumber", spinnerTopGraph?.selectedItem.toString())
            editor.putString("yeargraph", spinnerYearExpensesGraph?.selectedItem.toString())
            editor.commit()

            if(spinnerTopGraph?.selectedItem.toString() == "20"){
                CallTheRightGraph(5)
            }else{
                CallTheRightGraph(3)
            }
        }

        btGraphExpenses = view?.findViewById<Button>(R.id.btnGraphExpenses)
        btGraphExpenses?.setOnClickListener {

            top = "0"
            if(switchtopBottom?.isChecked == true){
                top = "1"
            }
           val editor = spRetrieve.edit()
            editor.putString("cuenta", spinnerBankExpenses?.selectedItem.toString())
            editor.putString("spence", spinnerExpenseCategories?.selectedItem.toString())
            editor.putString("top", top)
            editor.putString("topnumber", spinnerTopGraph?.selectedItem.toString())
            editor.putString("yeargraph", spinnerYearExpensesGraph?.selectedItem.toString())
            editor.commit()

            CallTheRightGraph(2)
        }

        textMonthBudget = view?.findViewById<TextView>(R.id.txtMonthlyBudget)
        textAvailableBudget = view?.findViewById<TextView>(R.id.txtAvailableBudget)
        textMonthNameExpenses = view?.findViewById<TextView>(R.id.txtMonthNameSpences)
        textYearExpenses = view?.findViewById<TextView>(R.id.txtYearSpences)

        var month: String? = retrievedDate?.substring(5, 7)
        if (month != null) {
            if(month.startsWith("0"))
                month = month.replaceFirst("0", "")
            var monthNumber: Int? = month?.toInt()
             textMonthNameExpenses?.text = DateFormatSymbols().getMonths()[(monthNumber?.minus(1)!!)]
            textYearExpenses?.text = retrievedDate?.take(4)
        }
        textMonthExpenses = view?.findViewById<TextView>(R.id.txtMonthExpenses)
       // textDateExpense = view?.findViewById<TextView>(R.id.txtDateExpense) //in lvDate ya esta esta referencia

        textTotalMExpenses = view?.findViewById<TextView>(R.id.txtTotalMExpenses)

        btSaveBudget = view?.findViewById<Button>(R.id.btnSaveBudget)
        btSaveBudget?.setOnClickListener {
            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
        var presupuesto: Double = 0.00

                        if (TextUtils.isEmpty(textMonthBudget?.text.toString())) {
                            textMonthBudget?.setError("Type your month budget!")
                        } else {

                            presupuesto = db.getMonthBudget(retrievedMonthName.toString(), retrievedYear?.toInt())

                            var availableMonthBudgetString: String? = null
                            var availableMonthBudgetDouble: Double? = null
                            availableMonthBudgetString = textMonthBudget?.text.toString()
                            availableMonthBudgetDouble =  availableMonthBudgetString.toDouble()


                            if(presupuesto == 0.00){
                                if(availableMonthBudgetDouble == 0.00){
                                    textMonthBudget?.setError("Type a value bigger than 0!")
                                }else{
                                    InsertParameters(availableMonthBudgetDouble)
                                }
                            }else{
                                UpdateParameters(availableMonthBudgetDouble)
                            }
                            getSpenceValues()
                            textMonthBudget?.setError(null)
                            textExpense?.setError(null)
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
            builder.setMessage("Do you to update the current month budget?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()
        }

        btSaveExpense = view?.findViewById<Button>(R.id.btnSaveExpense)

        btSaveExpense?.setOnClickListener {
            var tipoPago: String? = null
            var debit: Boolean? = null
            if(switchBankExpenses?.isChecked == true){
                tipoPago = "as debit of " + spinnerBankExpenses?.selectedItem.toString()
                debit = true
            }else{
                tipoPago = "in cash"
                debit = false
            }


            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                        if (TextUtils.isEmpty(textMonthBudget?.text.toString())) {
                            textMonthBudget?.setError("Type your month budget and save it!")
                        } else {

                            if (TextUtils.isEmpty(textExpense?.text.toString())) {
                                textExpense?.setError("Type your expense please!")

                            } else {

                                //I check if I have already recorded an installment this day
                                var installmentAlreadyRecorded: Boolean = db.RetrievePayedInstallment(retrievedDate!!)

                                //Recupero shared preference
                                var installment: String? = "0"
                                var doubleInstallment: Double = 0.0
                                val spRetrieve = inflater.context.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
                                installment = spRetrieve.getString("installment", "0")
                                doubleInstallment = installment!!.toDouble()

                              //  Toast.makeText(requireContext(), installment.toString(), Toast.LENGTH_LONG).show()

                                //To assign into shared preferences
                                sharedPreferences = inflater.context.getSharedPreferences(
                                        "MyUserPrefs",
                                        Context.MODE_PRIVATE
                                )

                                //Creo objeto selfLoans para insertarlo en tabla SelfLoans
                                val selfLoansModel = SelfLoansModel(
                                        retrievedDate!!,
                                        0.0,
                                        doubleInstallment,
                                        0.0,
                                        0.0
                                )

                                var uniqueID = UUID.randomUUID().toString()

                                var stringExpense: String = textExpense?.text.toString()
                                var doubleExpense: Double = stringExpense.toDouble()
                                var stringAvailableBudget: String  = textAvailableBudget?.text.toString()
                                var doubleAvailableBudget: Double = stringAvailableBudget.toDouble()
                                var spenceId: Int = 0
                                spenceId = db.getSpenceId(spinnerExpenseCategories?.selectedItem.toString())
                                var tieneExpense: Boolean = false
                                tieneExpense = db.getSpence(retrievedDate.toString(), spenceId)
                                var availableLoanBudget: Double = db.getAvailableBudget()
                                var totalAvailableBudget: Double = doubleAvailableBudget + availableLoanBudget + doubleInstallment
                               // Toast.makeText(requireContext(), availableLoanBudget.toString(), Toast.LENGTH_LONG).show()
                                if(doubleExpense > totalAvailableBudget)  {
                                    textExpense?.setError("You do not have enough budget!")
                                }else{

                                    if (switchBankExpenses?.isChecked == true) {  //If it is a bank Debit

                                        //Always delete de debit each time I insert or update un excense, so I always insert it whatever I do
                                        db.delete("BankBalance", "RecordDate = '" + retrievedDate.toString() + "T00:00:00' AND Comment LIKE '%" + spinnerExpenseCategories?.selectedItem.toString() + "%'")

                                        if (tieneExpense == false) {  //If expense is recorded for the first time
                                            InsertSpence(debit, uniqueID)
                                           InsertDebit("Debit", uniqueID)
                                            if(installmentAlreadyRecorded ==  false){
                                                db.addOneSelfLoans(selfLoansModel)
                                            }
                                        } else { //if expense is being edited as debit payment
                                            UpdateSpence(debit, uniqueID, true)
                                            InsertDebit("Debit", uniqueID)
                                        }
                                        Toast.makeText(layoutInflater.context,"Bank debit was recorded!", Toast.LENGTH_LONG).show()
                                    } else {   //if it is a cash payment
                                        if (tieneExpense == false) {  //cash payment payed for the first time
                                            InsertSpence(debit, uniqueID)
                                            if(installmentAlreadyRecorded ==  false){
                                                db.addOneSelfLoans(selfLoansModel)
                                            }
                                        } else { // payment being edited as a cash payment
                                            var priorUniqueIdentifier: String = db.RetrieveDebitUniqueIdentifier_Spences(spenceId, retrievedDate!!)
                                            if(priorUniqueIdentifier == "0"){   //I retrieve existing debitUniqueIdentifier
                                                //I do nothing and continue to UpdateSpence()
                                            }else{
                                                 if(doubleExpense == 0.0){ //if spence is 0.0 I delete bank debit if it exists
                                                     db.deleteOneBankBalanceBasedOnUniqueIdentifier(priorUniqueIdentifier)
                                                     Toast.makeText(layoutInflater.context,"If it existed, bank debit was deleted!", Toast.LENGTH_LONG).show()
                                                 }
                                            }

                                            UpdateSpence(debit, uniqueID, false)  //Whatever the above conditions I update expense
                                            // Toast.makeText(layoutInflater.context,"Update is needed as cash!", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }

                                getSpenceValues()
                            }
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
            builder.setMessage("Do you want to record this expense " + tipoPago + "?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()
        }

        switchBankExpenses = view?.findViewById<Switch>(R.id.swBankExpenses)

        spinnerExpenseCategories = view?.findViewById<Spinner>(R.id.spExpenseCategories)
        loadSpenceCategories(requireContext())

        spinnerExpenseCategories?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getSpenceValues()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        spinnerBankExpenses = view?.findViewById<Spinner>(R.id.spBankExpenses)
        loadBanks(requireContext())
        spinnerBankExpenses?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        lvDate = view?.findViewById<TextView>(R.id.txtDateExpense)
        //lvDate?.setText(retrievedDate)

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")//("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        lvDate?.setText(formatted)
        retrievedDate = formatted

/// Esto de abajo pense era importante pero en realizad no parece serlo con todooo lo dejo como referencia

        retrievedYear = current.getYear().toString()
        retrievedDayOfWeekNumber = current.dayOfWeek.value.toString()
        retrievedDayName = DateFormatSymbols().weekdays[retrievedDayOfWeekNumber!!.toInt()]
        retrievedDayOfMonthNumber = current.dayOfMonth.toString()
        var mes: String? = retrievedDate?.substring(5, 7)
        var monthNumber: Int? = null
        if (mes != null) {
            if (mes.startsWith("0"))
                mes = mes.replaceFirst("0", "")
            monthNumber = mes?.toInt()

            retrievedMonthNumber = monthNumber.toString()
        }
        var nombreMes: String = DateFormatSymbols().months[monthNumber?.toInt()!! - 1].toLowerCase()
        retrievedMonthName = nombreMes[0].toUpperCase() + nombreMes.substring(1)
///
        getSpenceValues()  //cargo los gastos registrados del dia actual, de la categoria seleccionada

        btDate = view?.findViewById<Button>(R.id.btnDateExpenses)

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
                textYearExpenses?.text = year.toString()
                textMonthNameExpenses?.text = DateFormatSymbols().getMonths()[month]

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
                textMonthBudget?.setError(null)
                textExpense?.setError(null)

                getSpenceValues()

                val editor = spRetrieve.edit()
                editor.putString("cuenta", spinnerBankExpenses?.selectedItem.toString())
                editor.putString("spence", spinnerExpenseCategories?.selectedItem.toString())
                editor.putString("top", top)
                editor.putString("topnumber", spinnerTopGraph?.selectedItem.toString())
                editor.putString("yeargraph", spinnerYearExpensesGraph?.selectedItem.toString())
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
        }

        return view
    }

    private fun getSpenceValues() {
        val db = DatabaseHelper(requireContext())

        var bankDebit: Int
        bankDebit = db.getFromSpencesBankDebit(spinnerExpenseCategories?.selectedItem.toString(), retrievedDate.toString())
        if(bankDebit == 0){
            switchBankExpenses?.isChecked = false
        }else{
            switchBankExpenses?.isChecked = true
        }

        textExpense?.text = db.getFromSpences(1, spinnerExpenseCategories?.selectedItem.toString(), retrievedDate.toString(), 0, "2000").toString()
        var month: String? = retrievedDate?.substring(5, 7)
        var monthNumber: Int? = null
        if (month != null) {
            if(month!!.startsWith("0"))
                month = month?.replaceFirst("0", "")
            monthNumber = month?.toInt()
        }
        if (month != null) {
            var expensesMes: Double = db.getFromSpences(2, spinnerExpenseCategories?.selectedItem.toString(), retrievedDate.toString(), monthNumber, textYearExpenses?.text.toString())
            val decimalExpensesMes = BigDecimal(expensesMes).setScale(2, RoundingMode.HALF_EVEN)
            textMonthExpenses?.text = decimalExpensesMes.toString()
            val dobleV: Double = db.getFromSpences(3, spinnerExpenseCategories?.selectedItem.toString(), retrievedDate.toString(), monthNumber, textYearExpenses?.text.toString())
            val decimal = BigDecimal(dobleV).setScale(2, RoundingMode.HALF_EVEN)
            textTotalMExpenses?.text = decimal.toString()
            var monthBudget: Double = db.getFromSpences(5, spinnerExpenseCategories?.selectedItem.toString(), retrievedDate.toString(), monthNumber, textYearExpenses?.text.toString())
            textMonthBudget?.text = monthBudget.toString()
            val decimalRemaining = BigDecimal(monthBudget.minus(dobleV)).setScale(2, RoundingMode.HALF_EVEN)
            textAvailableBudget?.text = decimalRemaining.toString()
            var createdInApp: String? = db.getSpencesCreatedInApp(spinnerExpenseCategories?.selectedItem.toString(), retrievedDate.toString())
            if(createdInApp == "0"){
                textCreatedInApp?.setText("Not recorded in app")
                textExpense?.isEnabled = false
                btSaveExpense?.isEnabled = false
            }else{
                textCreatedInApp?.setText("Recorded in app")
                textExpense?.isEnabled = true
                btSaveExpense?.isEnabled = true
            }

            if(createdInApp == null){
                textCreatedInApp?.setText("Not recorded yet")
                textExpense?.isEnabled = true
                btSaveExpense?.isEnabled = true
            }

            var parameterCreatedInApp: String? = db.getParametersCreatedInApp(retrievedYear!!.toInt(), retrievedMonthNumber!!.toInt())
            if(parameterCreatedInApp == "0"){
                textMonthBudget?.isEnabled = false
                btSaveBudget?.isEnabled = false
            }else{
                textMonthBudget?.isEnabled = true
                btSaveBudget?.isEnabled = true
            }

            if(parameterCreatedInApp == null){
                textMonthBudget?.isEnabled = true
                btSaveBudget?.isEnabled = true
            }
        }
    }

    private fun returnCorrectNumberDigits(entra: Int): String {
        var sale = entra.toString()
        val intermedio = entra.toString()
        if (intermedio.length < 2) {
            sale = "0$entra"
        }
        return sale
    }

    private fun loadSpenceCategories(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)

            // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: List<String> = db.getSpenceCategories(1)

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
            spinnerExpenseCategories?.adapter  = dataAdapter

            spinnerExpenseCategories?.setSelection(
                    getIndex(
                            spinnerExpenseCategories!!,
                            spence.toString()
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
            spinnerBankExpenses?.adapter  = dataAdapter

            spinnerBankExpenses?.setSelection(
                    getIndex(
                            spinnerBankExpenses!!,
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

    private fun InsertSpence(bankDebit: Boolean, debitUniqueIdentifier: String) {

        val dataBasehelper = DatabaseHelper(requireContext())
        var amountSpence: String? = null
        amountSpence = textExpense?.text.toString()
        var dobleAmountSpence: Double = amountSpence.toDouble()

        var dateSpence: String? = null
        dateSpence = lvDate?.text.toString()

        var spenceId: Int = 0
        spenceId = dataBasehelper.getSpenceId(spinnerExpenseCategories?.selectedItem.toString())

        textExpense?.setError(null)
        var spencesModel: SpencesModel? = null
        try {
            spencesModel = SpencesModel(-1, spenceId, "", dobleAmountSpence, dateSpence + "T12:00:00", retrievedDayName.toString(), retrievedDayOfWeekNumber!!.toInt().minus(1),
                    retrievedDayOfMonthNumber!!.toInt(), retrievedMonthName.toString(), retrievedMonthNumber!!.toInt(), retrievedYear!!.toInt(), true, bankDebit, debitUniqueIdentifier)
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.addOneSpences(spencesModel)
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
    }

    private fun UpdateSpence(debit: Boolean, debitUniqueIdentifier: String, updateUniqueIdentifier: Boolean) {

        val dataBasehelper = DatabaseHelper(requireContext())
        var amountSpence: String? = null
        amountSpence = textExpense?.text.toString()
        var dobleAmountSpence: Double = amountSpence.toDouble()

        var dateSpence: String? = null
        dateSpence = lvDate?.text.toString()

        var spenceId: Int = 0
        spenceId = dataBasehelper.getSpenceId(spinnerExpenseCategories?.selectedItem.toString())

        textExpense?.setError(null)
        var spencesModel: SpencesModel? = null
        try {
            spencesModel = SpencesModel(-1, spenceId, "", dobleAmountSpence, dateSpence + "T12:00:00", retrievedDayName.toString(), retrievedDayOfWeekNumber!!.toInt().minus(1),
                    retrievedDayOfMonthNumber!!.toInt(), retrievedMonthName.toString(), retrievedMonthNumber!!.toInt(), retrievedYear!!.toInt(), true, debit, debitUniqueIdentifier)
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.updateOneSpences(spencesModel, dateSpence + "T12:00:00", spenceId, debit, updateUniqueIdentifier)
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
    }

    private fun InsertDebit(operation: String, debitUniqueIdentifier: String) {

        val dataBasehelper = DatabaseHelper(requireContext())
        var amountSpence: String? = null
        amountSpence = textExpense?.text.toString()
        var dobleAmountSpence: Double = amountSpence.toDouble()

        var dateSpence: String? = null
        dateSpence = lvDate?.text.toString()

        var bankId: Int = 0
        bankId = dataBasehelper.getBankId((spinnerBankExpenses?.selectedItem.toString()))

        textExpense?.setError(null)

        var bankBalanceModel: BankBalanceModel? = null
        try {
            bankBalanceModel = BankBalanceModel(-1, bankId, "", operation, dobleAmountSpence * -1, "Expense recorded in this App: " + spinnerExpenseCategories?.selectedItem.toString(), retrievedDate.toString() + "T00:00:00",
                    retrievedDate.toString() + "T00:00:00", true, retrievedMonthName.toString(), retrievedMonthNumber!!.toInt(), retrievedYear!!.toInt(), 0.0, debitUniqueIdentifier)
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.addOneBankBalance(bankBalanceModel)
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
    }

    private fun InsertParameters(presupuesto: Double) {

        val dataBasehelper = DatabaseHelper(requireContext())
        textMonthExpenses?.setError(null)

        var parametersModel: ParametersModel? = null
        try {
            parametersModel = ParametersModel(-1, retrievedYear?.toInt(), retrievedMonthName, retrievedMonthNumber?.toInt(), (presupuesto * 5)/100, presupuesto, true)
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.addOneParameters(parametersModel)
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
    }

    private fun UpdateParameters(presupuesto: Double?) {

        val dataBasehelper = DatabaseHelper(requireContext())
        textMonthExpenses?.setError(null)

        var parametersModel: ParametersModel? = null
        try {
            parametersModel = ParametersModel(-1, retrievedYear?.toInt(), retrievedMonthName, retrievedMonthNumber?.toInt(), 0.00, presupuesto, true)
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.updateOneParameters(parametersModel, retrievedYear?.toInt(), retrievedMonthNumber?.toInt())
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
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
            spinnerYearExpensesGraph?.adapter = dataAdapter
            spinnerYearExpensesGraph?.setSelection(
                getIndex(
                    spinnerYearExpensesGraph!!,
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

    private fun LoadTopToShowInGraph(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)
           // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: MutableList<String> = ArrayList()
            lables?.add("5")
            lables?.add("6")
            lables?.add("7")
            lables?.add("8")
            lables?.add("9")
            lables?.add("10")
            lables?.add("11")
            lables?.add("12")
            lables?.add("13")
            lables?.add("14")
            lables?.add("15")
            lables?.add("20")
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
            spinnerTopGraph?.adapter = dataAdapter
            spinnerTopGraph?.setSelection(getIndex(
                    spinnerTopGraph!!,
                    topNumber.toString()))
            // Spinner click listener

        } catch (e: Exception) {
            exito = false
            exito
        }
        return exito
    }

    private fun CallTheRightGraph(queGrafico: Int) {

        var allYears: Boolean = false
        var year: String = spinnerYearExpensesGraph?.selectedItem.toString()
        var chartFragment: Fragment? = null
        when(queGrafico){
            3 -> chartFragment = RadarFragment()
            2 -> chartFragment = PieFragment()
            5 -> chartFragment = RadarFragment()
        }
        val bundle = Bundle()
        bundle.putInt("queGrafico", queGrafico)
        bundle.putBoolean("AllYears", allYears)
        bundle.putString("Year", year)
        when(queGrafico){
            3 ->  bundle.putString("SelectedCompany", spinnerExpenseCategories?.selectedItem.toString())
            5 ->  bundle.putString("SelectedCompany", "Total")
            2 -> if(top == "1"){
                  bundle.putString("SelectedCompany", " limit " + spinnerTopGraph?.selectedItem.toString())
                  }else{
                  bundle.putString("SelectedCompany", " ORDER BY Spence DESC limit " + spinnerTopGraph?.selectedItem.toString())
                  }
        }

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
}