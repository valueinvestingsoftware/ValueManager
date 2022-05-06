package x.stocks.valuemanager

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.CompaniesModel
import x.stocks.valuemanager.Models.MarketPriceSeguimientoModel

class LineasFragment : Fragment() {

    lateinit var lineDataSet: LineDataSet
    lateinit var lineData: LineData

    var vista: View? = null
    var queGrafico: Int = 0
    var year: String? = ""
    var allYears: Boolean = false
    var selectedCompany: String = ""

    var itemValues: ArrayList<Entry>? = null
    var itemnames: ArrayList<String>? = null
    var marketPriceSeguimientoModel: ArrayList<MarketPriceSeguimientoModel> = ArrayList<MarketPriceSeguimientoModel>()

    var lineChart: LineChart? = null
    var db: DatabaseHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_lineas, container, false)

        val bundle = arguments
        if (bundle != null) {

            queGrafico = bundle.getInt("queGrafico")
            allYears = bundle.getBoolean("AllYears")
            year = bundle.getString("Year")
            selectedCompany = bundle.getString("SelectedCompany").toString()

            requireActivity().title = selectedCompany

            lineChart = vista?.findViewById(R.id.lineChart)
            lineChart?.isDragEnabled = true
            lineChart?.setScaleEnabled(false)

            itemValues = ArrayList<Entry>()
            itemnames = ArrayList<String>()
            db = DatabaseHelper(requireContext())

            var itemValue: Double
            var itemName: String

            var companyId: Int = db!!.RetrieveCompanyId(selectedCompany)
            marketPriceSeguimientoModel = db!!.LoadMarketPriceSeguimiento(companyId)

            for (i in marketPriceSeguimientoModel.indices) {
                itemName= marketPriceSeguimientoModel.get(i).Day.toString()
                itemValue = marketPriceSeguimientoModel.get(i).MarketPrice
                itemValues!!.add(Entry(itemValue.toFloat(), i))
                itemnames!!.add(itemName)
            }

            lineDataSet = LineDataSet(itemValues, "Market prices")
            lineData = LineData(itemnames, lineDataSet)
            lineChart?.data = lineData

            val x = lineChart!!.xAxis
            // x.valueFormatter = IndexAxisValueFormatter(itemnames)
            x.position = XAxis.XAxisPosition.TOP
            x.setDrawGridLines(false)
            x.setDrawAxisLine(false)
            //x.setGranularity(1f)
            //x.setLabelCount(itemnames!!.size)
            x.labelRotationAngle = 270f

          lineDataSet.color = Color.BLUE
          lineDataSet.valueTextColor = Color.BLUE
            lineDataSet.valueTextSize = 12f
           lineDataSet.setDrawFilled(true)
            lineChart?.animateXY(300, 300)
            lineChart?.setDescription("")
            lineChart?.invalidate()
        }

        return vista
    }

}