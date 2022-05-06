package x.stocks.valuemanager

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.*

class PieFragment : Fragment() {

    var vista: View? = null
    var queGrafico: Int = 0
    var year: String? = ""
    var allYears: Boolean = false
    var selectedCompany: String? = null
    var itemValues: ArrayList<Entry>? = null
    var itemnames: ArrayList<String>? = null
    var bankBalanceModel: ArrayList<BankBalanceModel> = ArrayList<BankBalanceModel>()
    var spencesModel: ArrayList<SpencesModel> = ArrayList<SpencesModel>()
    var pieChart: PieChart? = null
    var db: DatabaseHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_pie, container, false)

        val bundle = arguments
        if (bundle != null) {

            queGrafico = bundle.getInt("queGrafico")
            allYears = bundle.getBoolean("AllYears")
            year = bundle.getString("Year")
            selectedCompany = bundle.getString("SelectedCompany")

           when (queGrafico) {
                2 -> requireActivity().title = "Personal expenses " + year
                3 -> requireActivity().title = "Bank account balance " + year
           }

            pieChart = vista?.findViewById(R.id.barPie)
            itemValues = ArrayList<Entry>()
            itemnames = ArrayList<String>()
            db = DatabaseHelper(requireContext())
            var itemName: String?
            var itemValue: Double?
            when (queGrafico) {
                2 -> spencesModel = db!!.LoadSpences(selectedCompany, " WHERE Spences.YSpence = " + year)
                3 -> bankBalanceModel = db!!.LoadBankBalanceAllAccounts(" WHERE BankBalance.YSpences <= " + year)
            }
            when (queGrafico) {
                2 -> for (i in spencesModel.indices) {
                    itemName = spencesModel.get(i).SpenceName
                    itemValue = spencesModel.get(i).Spence
                    itemValues!!.add(Entry(itemValue.toFloat(), i))
                    itemnames!!.add(itemName)
                }
                3 -> for (i in bankBalanceModel.indices) {
                    itemName = bankBalanceModel.get(i).Bank

                    itemValue = bankBalanceModel.get(i).Value?.toDouble()
                    itemValues?.add(Entry(itemValue?.toFloat()!!, i))

                    itemnames?.add(itemName)
                }
            }

            val colorClassArray = intArrayOf(Color.BLUE, Color.RED)
            var piedataset = PieDataSet(itemValues, "")

            piedataset.setColors(ColorTemplate.JOYFUL_COLORS)

            var piedata = PieData(itemnames, piedataset)

            when (queGrafico) {
                2 -> piedata = PieData(itemnames, piedataset)
                3 -> piedata = PieData(itemnames, piedataset)
            }
            // barChart.setFitBars(true)

            piedata.setValueTextSize(12f)
            pieChart?.data = piedata

            pieChart?.getLegend()?.setEnabled(false)

            // barChart.getDescription().setText("Top performing items")
            pieChart?.animateY(2000)
            pieChart?.setDescription("")
            pieChart?.invalidate()
        }

        return vista
    }
}