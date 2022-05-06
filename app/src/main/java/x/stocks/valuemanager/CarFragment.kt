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
import androidx.fragment.app.Fragment
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.CarModelsModel
import x.stocks.valuemanager.Models.MaintenanceRecordsModel
import java.text.SimpleDateFormat
import java.util.*


class CarFragment : Fragment() {

    var fulfilled: Int = 0
    var pendingFulfilled: String? = null
    var lvDate: TextView? = null
    var btDate: Button? = null; var btSaveMaintenance: Button? = null; var btNewMaintenance: Button? = null
    var btDeleteMaintenance: Button? = null; var btUpdateCurrentKms: Button? = null;
    var btCheckComingMaintenances: Button? = null; var btLoadPendingFulfilled: Button? = null
    var textNewMaintenance: TextView? = null; var textCurrentKms: TextView? = null; var textNextKms: TextView? = null
    var textCommentsCar: TextView? = null; var textRecordDateCar: TextView? = null
       var textStartKms: TextView? = null; var textEndKms: TextView? = null; var textPendingFulfilled: TextView? = null
    var spinnerCar: Spinner? = null; var spinnerMaintenance: Spinner? = null; lateinit var spinnerComingMaintenances: Spinner
   var switchpendingFulfilled: Switch? = null
    var mYear = 0; var mMonth:Int = 0; var mDayOfMonth:Int = 0; var a:Int = 0
    var retrievedDate:kotlin.String? = null
  //  var retrievedCurrentMilleage: String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "CAR MAINTENANCE"

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val spRetrieve = inflater.context.applicationContext.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        retrievedDate = spRetrieve.getString("dateCar", "2021-01-01")
      // retrievedCurrentMilleage = spRetrieve.getString("milleageCar", "0")
        // Toast.makeText(inflater.context, retrievedDate, Toast.LENGTH_SHORT).show()

        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_car, container, false)

        val db = DatabaseHelper(requireContext())


        spinnerCar = view?.findViewById<Spinner>(R.id.spCar)
        loadCars(requireContext())
        spinnerCar?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(switchpendingFulfilled?.isChecked == false){
                    fulfilled = 0
                }else{
                    fulfilled = 1
                }
                loadMaintenances(requireContext(), spinnerMaintenance, 1, spinnerCar?.selectedItem.toString(), fulfilled, 100)
                var currentMileage: Int = db.getMileageANDModelId(spinnerCar?.selectedItem.toString(), 1)
                textCurrentKms?.setText(currentMileage.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spinnerMaintenance = view?.findViewById<Spinner>(R.id.spMaintenance)
        spinnerMaintenance?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(switchpendingFulfilled?.isChecked == false){
                    fulfilled = 0
                }else{
                    fulfilled = 1
                }

              textCommentsCar?.text = db.getFromMaintenances(1, spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString(), fulfilled)
               textCommentsCar?.requestFocus()

                textStartKms?.text = db.getFromMaintenances(2, spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString(), fulfilled)
              textEndKms?.text = db.getFromMaintenances(3, spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString(), fulfilled)
              textNextKms?.text = db.getFromMaintenances(4, spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString(), fulfilled)
                textRecordDateCar?.text = db.getFromMaintenances(5, spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString(), fulfilled)
                var pendingfulfilled: String? = db.getFromMaintenances(7, spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString(), fulfilled)
              switchpendingFulfilled?.isEnabled = true
                if(pendingfulfilled == "1"){
                    switchpendingFulfilled?.isChecked = true
                    pendingfulfilled = "Fulfilled"
                    btNewMaintenance?.text = "Clon"
              }else{
                    switchpendingFulfilled?.isChecked = false
                    pendingfulfilled = "Pending"
                    btNewMaintenance?.text = "New"
              }
              textPendingFulfilled?.setText(pendingfulfilled)
              textNewMaintenance?.text = spinnerMaintenance?.selectedItem.toString()
              btDeleteMaintenance?.isEnabled = true
                var fechaMantenimiento: String? = db.getFromMaintenances(5, spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString(), fulfilled)
                lvDate?.text = fechaMantenimiento?.dropLast(9)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        if (view != null) {
            spinnerComingMaintenances = view.findViewById<Spinner>(R.id.spComingMaintenances)
        }
        spinnerComingMaintenances?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                spinnerMaintenance?.setSelection(getIndex(spinnerMaintenance!!, spinnerComingMaintenances.selectedItem.toString()))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        textPendingFulfilled = view?.findViewById<TextView>(R.id.txtPendingFulfilled)
        textRecordDateCar = view?.findViewById<TextView>(R.id.txtDateCarRecording)

        switchpendingFulfilled = view?.findViewById<Switch>(R.id.swPendingFulfilled)

        btSaveMaintenance = view?.findViewById<Button>(R.id.btnSaveMaintenance)
        btSaveMaintenance?.setOnClickListener{
            var queHago: String? = null
            if(btDeleteMaintenance?.isEnabled == true) {
                queHago = "existing"
            }else{
                queHago = "new"
            }

            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                        var todosLosCamposNecesariosConValores: Boolean = true

                        if (spinnerMaintenance?.selectedItem.toString() == textNewMaintenance?.text.toString()) {
                            //I do nothing
                        } else {
                            if (db.getIfAnyCarmaintenanceWasRecordedInApp(" WHERE MaintenanceName = '" + textNewMaintenance?.text + "'") == true) {
                                textNewMaintenance?.setError("This Maintenance name already exists!")
                                todosLosCamposNecesariosConValores = false
                            } else {
                                textNewMaintenance?.setError(null)
                            }
                        }

                        if (TextUtils.isEmpty(textNewMaintenance?.text.toString())) {
                            textNewMaintenance?.setError("Type the new maintenance name!")
                            todosLosCamposNecesariosConValores = false
                        } else {
                            textNewMaintenance?.setError(null)
                        }

                        if (TextUtils.isEmpty(textStartKms?.text.toString())) {
                            textStartKms?.setError("Type the start mileage of this maintenance!")
                            todosLosCamposNecesariosConValores = false
                        } else {
                            textStartKms?.setError(null)
                        }


                        if (TextUtils.isEmpty(textNextKms?.text.toString())) {
                            textNextKms?.setError("Type the mileage of next maintenance!")
                            todosLosCamposNecesariosConValores = false
                        } else {
                            textNextKms?.setError(null)
                        }

                        if (TextUtils.isEmpty(textCommentsCar?.text.toString())) {
                            textCommentsCar?.setError("Type a comment!")
                            todosLosCamposNecesariosConValores = false
                        } else {
                            textCommentsCar?.setError(null)
                        }

                        if (todosLosCamposNecesariosConValores == true) {
                            // Toast.makeText(requireContext(), "All fine to save", Toast.LENGTH_SHORT).show()

                            if (switchpendingFulfilled?.isChecked == false) {
                                fulfilled = 0
                            } else {
                                fulfilled = 1
                            }

                            var fulfilledtoSave: Int? = null
                            if (textPendingFulfilled?.text == "Pending") {
                                fulfilledtoSave = 0
                            } else {
                                fulfilledtoSave = 1
                            }

                            var maintenanceName: String = textNewMaintenance?.text.toString()
                            if (btDeleteMaintenance?.isEnabled == true) {
                                InsertUpdateMaintenance(2, fulfilledtoSave, maintenanceName) //Update
                                loadMaintenances(requireContext(), spinnerMaintenance, 1, spinnerCar?.selectedItem.toString(), fulfilled, 100)
                                spinnerMaintenance?.setSelection(getIndex(spinnerMaintenance!!, maintenanceName))
                            } else {
                                InsertUpdateMaintenance(1, fulfilledtoSave, maintenanceName)  //insert
                                loadMaintenances(requireContext(), spinnerMaintenance, 1, spinnerCar?.selectedItem.toString(), fulfilled, 100)
                                spinnerMaintenance?.setSelection(getIndex(spinnerMaintenance!!, maintenanceName))
                            }
                        } else {
                            // Toast.makeText(requireContext(), "Not fine to save", Toast.LENGTH_SHORT).show()
                        }

                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

            var fulfilled: String? = null
            if(switchpendingFulfilled?.isChecked == true){
                fulfilled = " as fulfilled"
            }else{
                fulfilled = ""
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Do you want to save this " + queHago + " maintenance" + fulfilled + "?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()
        }

        btNewMaintenance = view?.findViewById<Button>(R.id.btnNewMaintenance)

        btNewMaintenance?.setOnClickListener{

            var mensaje: String? = null

            if(btNewMaintenance!!.text == "Clon"){
                mensaje = " clon this maintenance?"
            }else{
                mensaje = " record a new maintenance?"
            }

            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                        if (btNewMaintenance?.text == "Clon") {
                            if (TextUtils.isEmpty(textNewMaintenance?.text.toString())) {
                                textNewMaintenance?.setError("Type the new maintenance name!")
                            } else {
                                if (db.getIfAnyCarmaintenanceWasRecordedInApp(" WHERE MaintenanceName = '" + textNewMaintenance?.text + "_" + textCurrentKms?.text.toString() + "'") == true) {
                                    textNewMaintenance?.setError("This Maintenance name already exists!")
                                } else {
                                    textNewMaintenance?.setError(null)
                                    textNextKms?.setError(null)
                                    textCommentsCar?.setError(null)
                                    switchpendingFulfilled?.isChecked = false
                                    textNewMaintenance?.text = spinnerMaintenance?.selectedItem.toString() + "_" + textCurrentKms?.text.toString()
                                    textStartKms?.text = textCurrentKms?.text.toString()
                                    var newmaintenancetoClon: String = textNewMaintenance?.text.toString()
                                    InsertUpdateMaintenance(1, 1, newmaintenancetoClon)  //insert
                                    loadMaintenances(requireContext(), spinnerMaintenance, 1, spinnerCar?.selectedItem.toString(), 0, 100)
                                    spinnerMaintenance?.setSelection(getIndex(spinnerMaintenance!!, newmaintenancetoClon))
                                    switchpendingFulfilled?.isChecked = false
                                    textPendingFulfilled?.setText("Pending")
                                }
                            }
                        } else {
                            if (btDeleteMaintenance?.isEnabled == true) {
                                textNewMaintenance?.setError(null)
                                textNextKms?.setError(null)
                                textCommentsCar?.setError(null)
                                btNewMaintenance!!.text = "Undo"
                                textNewMaintenance?.setText("")
                                textStartKms?.setText(textCurrentKms?.text.toString())
                                textNextKms?.setText("")
                                textCommentsCar?.setText("")
                                btDeleteMaintenance?.isEnabled = false
                                switchpendingFulfilled?.isChecked = false
                                switchpendingFulfilled?.isEnabled = false
                            } else {
                                textNewMaintenance?.setError(null)
                                textNextKms?.setError(null)
                                textCommentsCar?.setError(null)
                                btNewMaintenance!!.text = "New"
                                var newmaintenancetoClon: String = textNewMaintenance?.text.toString()
                                loadMaintenances(requireContext(), spinnerMaintenance, 1, spinnerCar?.selectedItem.toString(), 0, 100)
                                spinnerMaintenance?.setSelection(getIndex(spinnerMaintenance!!, newmaintenancetoClon))
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
            builder.setMessage("Do you want to" + mensaje).setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()
        }
        btDeleteMaintenance = view?.findViewById<Button>(R.id.btnDeleteMaintenance)
        btDeleteMaintenance?.setOnClickListener{

            if(switchpendingFulfilled?.isChecked == false){
                fulfilled = 0
            }else{
                fulfilled = 1
            }

            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        var maintenanceId: Int? = null
                        maintenanceId = db.getMaintenanceId(spinnerCar?.selectedItem.toString(), spinnerMaintenance?.selectedItem.toString())
                        db.updateDeleteMaintenance(maintenanceId)
                        loadMaintenances(requireContext(), spinnerMaintenance, 1, spinnerCar?.selectedItem.toString(), fulfilled, 100)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Do you want to delete the current maintenance?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                    .setNegativeButton("No", dialogOnClickListener).show()
        }

        btUpdateCurrentKms = view?.findViewById<Button>(R.id.btnUpdateCurrentKms)
        btUpdateCurrentKms?.setOnClickListener {
            if(TextUtils.isEmpty(textCurrentKms?.text.toString())) {
                textCurrentKms?.setError("Type the current mileage!")
            }else{

                val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {

                            UpdateCarBrandCurrentKms()

                            Toast.makeText(requireContext(), "Current mileage was updated!", Toast.LENGTH_SHORT).show()

                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Confirmation")
                builder.setIcon(android.R.drawable.stat_sys_download)
                builder.setCancelable(false)
                builder.setMessage("Do you want to update the current mileage?").setPositiveButton(
                        "Yes",
                        dialogOnClickListener
                )
                        .setNegativeButton("No", dialogOnClickListener).show()
            }

        }

        btCheckComingMaintenances = view?.findViewById<Button>(R.id.btnCheckComingMaintenances)
        btCheckComingMaintenances?.setOnClickListener {
            if (TextUtils.isEmpty(textCurrentKms?.text.toString())) {
                textCurrentKms?.setError("Type your current mileage!")
            }else{
                loadMaintenances(requireContext(), spinnerComingMaintenances, 2, spinnerCar?.selectedItem.toString(), 0, textCurrentKms?.text.toString().toInt() + 1000)

            }
        }

        btLoadPendingFulfilled = view?.findViewById<Button>(R.id.btnLoadPendingFulfilled)
        btLoadPendingFulfilled?.setOnClickListener{
            var pendingOrFulfilled: String? = null
            if(switchpendingFulfilled?.isChecked == true) {
                pendingOrFulfilled = "fulfilled"
            }else {
                pendingOrFulfilled = "pending"
            }

            val dialogOnClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (switchpendingFulfilled?.isChecked == false) {
                            fulfilled = 0
                            btNewMaintenance?.text = "New"
                        } else {
                            fulfilled = 1
                            btNewMaintenance?.text = "Clon"
                        }
                        loadMaintenances(requireContext(), spinnerMaintenance, 1, spinnerCar?.selectedItem.toString(), fulfilled, 100)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirmation")
            builder.setIcon(android.R.drawable.stat_sys_download)
            builder.setCancelable(false)
            builder.setMessage("Do you want to load all " + pendingOrFulfilled + " maintenances?").setPositiveButton(
                    "Yes",
                    dialogOnClickListener
            )
                .setNegativeButton("No", dialogOnClickListener).show()
        }


        textNewMaintenance = view?.findViewById<TextView>(R.id.txtNewMaintenance)

        textCurrentKms = view?.findViewById<TextView>(R.id.txtCurrentKms)
      //  textCurrentKms?.setText(retrievedCurrentMilleage)

        textNextKms = view?.findViewById<TextView>(R.id.txtNextKms)
        textCommentsCar = view?.findViewById<TextView>(R.id.txtCommentMaintenance)

        textStartKms = view?.findViewById<TextView>(R.id.txtStartKms)
        textEndKms = view?.findViewById<TextView>(R.id.txtEndKms)

        lvDate = view?.findViewById<TextView>(R.id.txtDateCarRecording)
        lvDate?.setText(retrievedDate)

        btDate = view?.findViewById<Button>(R.id.btnDateCar)

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

                val editor = spRetrieve.edit()
                editor.putString("dateCar", retrievedDate)
                editor.commit()
            }, mYear, mMonth, mDayOfMonth)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
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

    private fun loadCars(context: Context): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)

            // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: List<String> = db.getAllCars(1)

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
            spinnerCar?.adapter  = dataAdapter

            // Spinner click listener
            exito = true

        } catch (e: Exception) {
            exito = false
            exito
        }
        return exito
    }

    private fun loadMaintenances(context: Context, spinner: Spinner?, que: Int, brand: String, fulfilled: Int, currentKm: Int): Boolean? {
        var exito = false
        try {
            // database handler

            // database handler
            val db = DatabaseHelper(context)

            // Spinner Drop down elements

            // Spinner Drop down elements
            val lables: List<String> = db.getAllMaintenances(que, brand, fulfilled, currentKm)

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
            spinner?.adapter  = dataAdapter

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

    private fun UpdateCarBrandCurrentKms() {

        val dataBasehelper = DatabaseHelper(requireContext())
        var currentMileage: String? = null
        currentMileage = textCurrentKms?.text.toString()
       var currentMileageInt = currentMileage.toInt()
        textCurrentKms?.setError(null)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        var carModelsModel: CarModelsModel? = null

        try {
            carModelsModel = CarModelsModel(-1, spinnerCar?.selectedItem.toString(), currentMileageInt, currentDate.toString())
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        val success: Boolean = dataBasehelper.updateCarModel(carModelsModel, spinnerCar?.selectedItem.toString())
        Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
    }

    private fun InsertUpdateMaintenance(que: Int, fulfilled: Int, newMaintenanceName: String) {

        val dataBasehelper = DatabaseHelper(requireContext())

        var modelId: Int = dataBasehelper.getMileageANDModelId(spinnerCar?.selectedItem.toString(), 2)

        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = sdf.format(Date())
        var dateString: String = currentDate.toString() + "T00:00:00"
        var dateTxt: String = textRecordDateCar?.text.toString() + "T00:00:00"

        var startKms: String = textStartKms?.text.toString()
        var startKmsInt: Int = startKms.toInt()

        var nextKms: String = textNextKms?.text.toString()
        var nextKmsInt: Int = nextKms.toInt()

        var endKms: Int? = null
        endKms = startKmsInt + nextKmsInt

        var comment: String = textCommentsCar?.text.toString()

        var pendingFulfilled: Boolean = false
        if(switchpendingFulfilled?.isChecked == false){
            pendingFulfilled = false
        }else{
            pendingFulfilled = true
        }

        val db = DatabaseHelper(requireContext())
        var maxIdMaintenance: Int? = 0
        maxIdMaintenance = db.getMaxMaintenanceId("", "Max")
        maxIdMaintenance = maxIdMaintenance?.plus(1)

        var maintenanceRecordsModel: MaintenanceRecordsModel? = null

        try {
            maintenanceRecordsModel = MaintenanceRecordsModel(maxIdMaintenance!!, newMaintenanceName, comment, modelId, startKmsInt, endKms, nextKmsInt, dateTxt, dateString, pendingFulfilled, true, true, false)
            //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error recording expense", Toast.LENGTH_LONG).show()
        }

        if(que == 1) { //insert
            val success: Boolean = dataBasehelper.addOneMaintenanceRecords(maintenanceRecordsModel)
            Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
        }else{  //update
            val success: Boolean = dataBasehelper.updateOneMaintenanceRecords(maintenanceRecordsModel, spinnerMaintenance?.selectedItem.toString(), fulfilled)
            Toast.makeText(activity, "Success = $success", Toast.LENGTH_SHORT).show()
        }
    }
}