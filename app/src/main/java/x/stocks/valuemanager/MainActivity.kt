package x.stocks.valuemanager

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var drawer: DrawerLayout? = null
    var buySellWasDownloaded: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.openDrawer, R.string.closeDrawer
        )
        drawer?.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            //without clicking just when it starts opens this fragment
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                SpencesFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.Expenses)
        }

        drawer?.openDrawer(GravityCompat.START)
        drawer?.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
           // super.onBackPressed()
            return;
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bank -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                BankFragment()
            ).addToBackStack(null).commit()
            R.id.car -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                CarFragment()
            ).addToBackStack(null).commit()
            R.id.Expenses -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                SpencesFragment()
            ).addToBackStack(null).commit()
            R.id.Stocks -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                StocksFragment()
            ).addToBackStack(null).commit()
            R.id.Sync -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                SyncFragment()
            ).addToBackStack(null).commit()
            R.id.Exit -> finishAffinity()
        }

        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }
}