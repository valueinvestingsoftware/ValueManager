package x.stocks.valuemanager

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.*

class RadarFragment : Fragment() {

    lateinit var radarDataSet: RadarDataSet
    lateinit var radarData: RadarData

    var vista: View? = null
    var queGrafico: Int = 0
    var year: String? = ""
    var allYears: Boolean = false
    var selectedItemName: String = ""

    var itemValues: ArrayList<Entry>? = null
    var itemnames: ArrayList<String>? = null
    var dividendosModel: ArrayList<DividendosModel> = ArrayList<DividendosModel>()
    var spencesModel: ArrayList<SpencesModel> = ArrayList<SpencesModel>()
    var bankSummariesModel: ArrayList<BankSummariesModel> = ArrayList<BankSummariesModel>()

    var radarChart: RadarChart? = null
    var db: DatabaseHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_radar, container, false)

        val bundle = arguments
        if (bundle != null) {

            queGrafico = bundle.getInt("queGrafico")
            allYears = bundle.getBoolean("AllYears")
            year = bundle.getString("Year")
            selectedItemName = bundle.getString("SelectedCompany").toString()

            if(allYears == true){
                requireActivity().title = selectedItemName // "All accounts with transactions"
            }else{
                requireActivity().title = selectedItemName
            }


            radarChart = vista?.findViewById(R.id.radarChart)

            itemValues = ArrayList<Entry>()
            itemnames = ArrayList<String>()
            db = DatabaseHelper(requireContext())

            var itemValue: Double = 0.0
            var itemName: String

            if(queGrafico <= 2){
                var companyId: Int = db!!.RetrieveCompanyId(selectedItemName)
                dividendosModel = db!!.LoadDividendos(" WHERE Dividendos.CompanyId = " + companyId.toString(), 2)

                for (i in dividendosModel.indices) {
                    itemName= dividendosModel.get(i).Year.toString()
                    when(queGrafico){
                        1 -> itemValue = dividendosModel.get(i).EfectivoPorAccion
                        2 ->  itemValue = dividendosModel.get(i).AccionesPorAccion
                    }

                    itemValues!!.add(Entry(itemValue.toFloat(), i))
                    itemnames!!.add(itemName)
                }
            }

            if(queGrafico == 3){
                var spenceId: Int = db!!.RetrieveSpenceId(selectedItemName)
                spencesModel = db!!.LoadSpences(" WHERE Spences.SpenceId = " + spenceId.toString() + " AND Spences.YSpence = " + year.toString())

                for (i in spencesModel.indices) {
                    itemName= spencesModel.get(i).MSpences.toString()
                    itemValue = spencesModel.get(i).Spence
                    itemValues!!.add(Entry(itemValue.toFloat(), i))
                    itemnames!!.add(itemName)
                }
            }

            if(queGrafico == 5){
                spencesModel = db!!.LoadSpences(" WHERE Spences.YSpence = " + year.toString())

                for (i in spencesModel.indices) {
                    itemName= spencesModel.get(i).MSpences.toString()
                    itemValue = spencesModel.get(i).Spence
                    itemValues!!.add(Entry(itemValue.toFloat(), i))
                    itemnames!!.add(itemName)
                }
            }

            if(queGrafico == 4){
                var bankId: Int = db!!.RetrieveBankId(selectedItemName)
                if(allYears == true){
                    bankSummariesModel = db!!.LoadBankBalancesPerMonth(" WHERE YSpences = " +  year.toString())
                }else{
                    bankSummariesModel = db!!.LoadBankBalancesPerMonth(" WHERE BankId = " + bankId.toString() + " AND YSpences = " +  year.toString())
                }


                for (i in bankSummariesModel.indices) {
                    itemName= bankSummariesModel.get(i).MonthName.toString()
                    itemValue = bankSummariesModel.get(i).Balance!!
                    itemValues!!.add(Entry(itemValue.toFloat(), i))
                    itemnames!!.add(itemName)
                }
            }
            when(queGrafico){
                1 -> radarDataSet = RadarDataSet(itemValues, "Cash dividends")
                2 -> radarDataSet = RadarDataSet(itemValues, "Stock dividends")
                3 -> radarDataSet = RadarDataSet(itemValues, "Spences")
                4 -> radarDataSet = RadarDataSet(itemValues, "bank balance")
                5 -> radarDataSet = RadarDataSet(itemValues, "Total spences")
            }

            radarData = RadarData(itemnames, radarDataSet)
            radarData.setDrawValues(true) //this are the values of my data

            val y = radarChart!!.yAxis
            y.setDrawLabels(false) //esto remuevo los scale labels del eje de las y

            val x = radarChart!!.xAxis
           x.isEnabled = true

            radarChart?.data = radarData

            x.position = XAxis.XAxisPosition.BOTTOM
            x.setDrawGridLines(false)
            x.setDrawAxisLine(false)
            //x.setGranularity(1f)
            //x.setLabelCount(itemnames!!.size)
            x.labelRotationAngle = 270f

            radarDataSet.color = Color.BLUE
            radarDataSet.valueTextColor = Color.BLUE
            radarDataSet.valueTextSize = 12f
            radarDataSet.setDrawFilled(true)
            radarChart?.animateXY(300, 300)
            radarChart?.setDescription("")
            radarChart?.invalidate()
        }

        return vista
    }

}