package com.example.rafles.att_group

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.rafles.att_group.RetrofitCrud.RetrofitActivity
import com.example.rafles.att_group.barcode.BarcodeActivity
import com.example.rafles.att_group.barcode.BarcodeInputActivty
import com.example.rafles.att_group.camera.CameraAction
import com.example.rafles.att_group.crud_mysql.CrudMysqlActivity
import com.example.rafles.att_group.login.MyloginActivity
import com.example.rafles.att_group.treject.TrejectActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.example.rafles.att_group.login.LoginActivity
import com.example.rafles.att_group.login.SharedPrefManager



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var sharedPrefManager: SharedPrefManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPrefManager = SharedPrefManager(this)
        val userlogin = sharedPrefManager!!.getSPNama()

        //This for title user in nav header
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.nav_title_user) as TextView
        navUsername.text = userlogin

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
//                helloText.text = "Menu CAMERA is clicked!" +biodata
                startActivity(Intent(this@MainActivity, CameraAction::class.java))
            }
            R.id.nav_mysql -> {
                startActivity(Intent(this@MainActivity, CrudMysqlActivity::class.java))
            }
            R.id.nav_barcode -> {
                startActivity(Intent(this@MainActivity, BarcodeInputActivty::class.java))
            }
            R.id.nav_treject -> {
                startActivity(Intent(this@MainActivity, TrejectActivity::class.java))
            }
            R.id.nav_retrofit -> {
                startActivity(Intent(this@MainActivity, RetrofitActivity::class.java))
            }
            R.id.nav_setting -> {
                Toast.makeText(this@MainActivity, "Menu Settings masih dalam Pengembangan !", Toast.LENGTH_LONG).show()
            }
            R.id.nav_about ->{
                Toast.makeText(this@MainActivity, "This application is use for barcoding reject house !", Toast.LENGTH_LONG).show()
            }
            R.id.nav_logout ->  {
//                startActivity(Intent(this@MainActivity, MyloginActivity::class.java))
                sharedPrefManager!!.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false)
                startActivity(Intent(this@MainActivity, MyloginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
