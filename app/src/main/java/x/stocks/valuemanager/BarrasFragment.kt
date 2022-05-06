package x.stocks.valuemanager

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import x.stocks.valuemanager.DatabaseHelper.DatabaseHelper
import x.stocks.valuemanager.Models.CompaniesModel
import x.stocks.valuemanager.Models.DividendosModel
import x.stocks.valuemanager.Models.InvestmentModel

class BarrasFragment : Fragment() {

    var vista: View? = null
    var queGrafico: Int = 0
    var year: String? = ""
    var allYears: Boolean = false
    var selectedCompany: String? = null
    var itemValuesOwned: ArrayList<BarEntry>? = null
    var itemValues: ArrayList<BarEntry>? = null
    var itemValues2: ArrayList<BarEntry>? = null
    var itemValues3: ArrayList<BarEntry>? = null
    var itemnames: ArrayList<String>? = null
    var companiesModel: ArrayList<CompaniesModel> = ArrayList<CompaniesModel>()
    var dividendosModel: ArrayList<DividendosModel> = ArrayList<DividendosModel>()
    var investmentModel: ArrayList<InvestmentModel> = ArrayList<InvestmentModel>()
    var barChart: BarChart? = null
    var db: DatabaseHelper? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // getActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_barras, container, false)


        val bundle = arguments
        if (bundle != null) {

            queGrafico = bundle.getInt("queGrafico")
            allYears = bundle.getBoolean("AllYears")
            year = bundle.getString("Year")
            selectedCompany = bundle.getString("SelectedCompany")

            when (queGrafico) {
                1 -> requireActivity().title = "Ranking"
                2 -> requireActivity().title = "Purchases and sales"
                4 -> requireActivity().title = "Dividends"
                5 -> requireActivity().title = "Profit"
                6 -> requireActivity().title = "Price"
            }

            barChart = vista?.findViewById(R.id.barChart)
            itemValuesOwned = ArrayList<BarEntry>()
            itemValues = ArrayList<BarEntry>()
            itemValues2 = ArrayList<BarEntry>()
            itemValues3 = ArrayList<BarEntry>()
            itemnames = ArrayList<String>()
            db = DatabaseHelper(requireContext())
            var itemName: String
            var itemValueOwned: Double
            var itemValue: Double
            var itemValue2: Double
            var itemValue3: Double
            when (queGrafico) {
                1 -> companiesModel = db!!.LoadCompanies()
                2 -> investmentModel = db!!.LoadInvestment(" ORDER BY Investment.TotalOwned DESC")
                4 -> if (allYears == false) {
                    dividendosModel = db!!.LoadDividendos(" WHERE Companies.Exclude = 'no' AND Year = " + year, 1)
                } else {
                    dividendosModel = db!!.LoadDividendos(" WHERE Companies.Exclude = 'no'", 1)
                }
                5 -> investmentModel = db!!.LoadInvestment(" ORDER BY Investment.UtilidadAccion DESC")
                6-> companiesModel = db!!.LoadCompanies()
             }
            when (queGrafico) {
                1 -> for (i in companiesModel.indices) {
                    itemName = companiesModel.get(i).Company + " (" + companiesModel.get(i).Ranking.toString() + ")"
                    itemValue = companiesModel.get(i).Ponderated
                    itemValues!!.add(BarEntry(itemValue.toFloat(), i))
                    itemnames!!.add(itemName)
                }
                2 -> for (i in investmentModel.indices) {
                    itemName = investmentModel.get(i).Company

                    itemValueOwned = investmentModel.get(i).TotalOwned.toDouble()
                    itemValuesOwned!!.add(BarEntry(itemValueOwned.toFloat(), i))

                    itemValue = investmentModel.get(i).SoloCompradas.toDouble()
                    itemValues!!.add(BarEntry(itemValue.toFloat(), i))

                    itemValue2 = investmentModel.get(i).SoloVendidas.toDouble()
                    itemValues2!!.add(BarEntry(itemValue2.toFloat(), i))

                    itemValue3 = investmentModel.get(i).TotalOwned.toDouble() - investmentModel.get(i).SoloCompradas.toDouble() + investmentModel.get(i).SoloVendidas.toDouble()
                    itemValues3!!.add(BarEntry(itemValue3.toFloat(), i))

                    itemnames!!.add(itemName)
                }
                4 -> for (i in dividendosModel.indices) {
                    itemName = dividendosModel.get(i).Company
                    itemValue = dividendosModel.get(i).AccionesPorAccion
                    itemValue2 = dividendosModel.get(i).EfectivoPorAccion
                    itemValues!!.add(BarEntry(floatArrayOf(itemValue.toFloat(), itemValue2.toFloat()), i))
                    itemnames!!.add(itemName)
                }
                5 -> for (i in investmentModel.indices) {
                    itemName = investmentModel.get(i).Company

                    itemValue = investmentModel.get(i).UtilidadBancaria.toDouble()
                    itemValues!!.add(BarEntry(itemValue.toFloat(), i))

                    itemValue2 = investmentModel.get(i).UtilidadAccion.toDouble()
                    itemValues2!!.add(BarEntry(itemValue2.toFloat(), i))

                    itemnames!!.add(itemName)
                    }
                6 -> for (i in companiesModel.indices) {
                    itemName = companiesModel.get(i).Company

                    itemValue = companiesModel.get(i).MarketPrice.toDouble()
                    itemValues!!.add(BarEntry(itemValue.toFloat(), i))

                    itemValue2 = companiesModel.get(i).RealPrice.toDouble()
                    itemValues2!!.add(BarEntry(itemValue2.toFloat(), i))

                    itemnames!!.add(itemName)
                }
                }

            val colorClassArray = intArrayOf(Color.BLUE, Color.RED)

            var bardatasetOwned = BarDataSet(itemValuesOwned, "Owned")
            when (queGrafico) {
                2-> bardatasetOwned.color = Color.GREEN
            }

            var bardatasetRanking = BarDataSet(itemValues, "Companies")
            var bardataset = BarDataSet(itemValues, "Purchased")
            var bardatasetDividendos = BarDataSet(itemValues, "")

            when (queGrafico) {
                1 -> bardatasetRanking.setColors(ColorTemplate.COLORFUL_COLORS)
                1 -> bardatasetRanking = BarDataSet(itemValues, "Companies")
                2 -> bardataset.color = Color.RED
                4 -> bardatasetDividendos.setColors(colorClassArray)
                4 -> bardatasetDividendos = BarDataSet(itemValues, "")
            }

            var bardatasetMarketPrice = BarDataSet(itemValues, "Market price")
            when (queGrafico) {
                6 -> bardatasetMarketPrice.color = Color.RED
            }
            var bardatasetRealPrice = BarDataSet(itemValues2, "Intrinsic price")
            when (queGrafico) {
                6 -> bardatasetRealPrice.color = Color.BLUE
            }

            var bardatasetBank = BarDataSet(itemValues, "Bank")
            when (queGrafico) {
                5 -> bardatasetBank.color = Color.RED
            }
            var bardatasetProfit = BarDataSet(itemValues2, "Stocks")
            when (queGrafico) {
                5 -> bardatasetProfit.color = Color.BLUE
            }
            
            val bardataset2 = BarDataSet(itemValues2, "Sold")
            when (queGrafico) {
                2 -> bardataset2.color = Color.BLUE
                5 -> bardataset2.color = Color.BLUE
            }

            val bardataset3 = BarDataSet(itemValues3, "Earned")
            when (queGrafico) {
                2 -> bardataset3.color = Color.YELLOW
            }

            val varDataSetFinal = ArrayList<BarDataSet>()
            varDataSetFinal.add(bardatasetOwned)
            varDataSetFinal.add(bardataset)
            varDataSetFinal.add(bardataset2)
            varDataSetFinal.add(bardataset3)

            val varDataSetFinalProfit = ArrayList<BarDataSet>()
            varDataSetFinalProfit.add(bardatasetBank)
            varDataSetFinalProfit.add(bardatasetProfit)

            val varDataSetFinalPrice = ArrayList<BarDataSet>()
            varDataSetFinalPrice.add(bardatasetMarketPrice)
            varDataSetFinalPrice.add(bardatasetRealPrice)

            when (queGrafico) {
                4 -> bardatasetDividendos.stackLabels = arrayOf("Stocks", "Cash")
            }

          //  bardataset.setValueTextColor(Color.BLACK)
          //  bardataset.setValueTextSize(12f)

            var bardata = BarData(itemnames, bardataset)

            when (queGrafico) {
                1 -> bardata = BarData(itemnames, bardatasetRanking)
                2 -> bardata = BarData(itemnames, varDataSetFinal as List<IBarDataSet>?)
                4 -> bardata = BarData(itemnames, bardatasetDividendos)
                5 -> bardata = BarData(itemnames, varDataSetFinalProfit as List<IBarDataSet>?)
                6 -> bardata = BarData(itemnames, varDataSetFinalPrice as List<IBarDataSet>?)
            }
           // barChart.setFitBars(true)

            barChart?.data = bardata
           // barChart.getDescription().setText("Top performing items")

            val x = barChart!!.xAxis
           // x.valueFormatter = IndexAxisValueFormatter(itemnames)
            x.position = XAxis.XAxisPosition.TOP
            x.setDrawGridLines(false)
            x.setDrawAxisLine(false)
            //x.setGranularity(1f)
            //x.setLabelCount(itemnames!!.size)
            x.labelRotationAngle = 270f

            barChart?.animateY(2000)
            barChart?.setDescription("")
            barChart?.invalidate()
        }
        // Inflate the layout for this fragment


       return vista

    }

}


