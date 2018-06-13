package com.example.rafles.att_group

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.rafles.att_group.ApiMaps.ListOnline
import com.example.rafles.att_group.ApiMaps.LoginFirebaseActivity
import com.example.rafles.att_group.ApiMaps.MapsActivity

import com.example.rafles.att_group.MyFragment.OneFragment
import com.example.rafles.att_group.RetrofitCrud.RetrofitActivity
import com.example.rafles.att_group.barcode.BarcodeActivity
import com.example.rafles.att_group.barcode.BarcodeInputActivty
import com.example.rafles.att_group.camera.CameraAction
import com.example.rafles.att_group.crud_firebase.CrudFirebaseActivity
import com.example.rafles.att_group.crud_mysql.CrudMysqlActivity
import com.example.rafles.att_group.login.MyloginActivity
import com.example.rafles.att_group.treject.TrejectActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.example.rafles.att_group.login.LoginActivity
import com.example.rafles.att_group.login.SharedPrefManager
import com.example.rafles.att_group.recyclerview_data.RecyclerMainActivity
import com.firebase.ui.auth.AuthUI


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var sharedPrefManager: SharedPrefManager? = null
    private val LOGIN_PERMISSION = 1000


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

        //Call Fragment
        val fragManager = getSupportFragmentManager()
        fragManager.beginTransaction()
                .add(R.id.frame_content, OneFragment())
                .addToBackStack("fragment")
                .commit()
//        supportActionBar!!.setTitle("Fragment One")

        // get last open Activity when open apps


    }

    fun showDialoExit() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
        dialogBuilder.setView(dialogView)
//        val editText = dialogView.findViewById<View>(R.id.editTextName) as EditText
        dialogBuilder.setTitle("Confirm !")
        dialogBuilder.setMessage("Are You Sure Exit ?")
        dialogBuilder.setPositiveButton("OKE", DialogInterface.OnClickListener { _, _ ->
            //do something with edt.getText().toString();
            // Handler code here.
            Toast.makeText(this@MainActivity, "Back presssed and save !", Toast.LENGTH_LONG).show()
            finishAffinity()
        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
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
            R.id.nav_maps -> {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAllowNewEmailAccounts(true).build(), LOGIN_PERMISSION)
//                startActivity(Intent(this@MainActivity, LoginFirebaseActivity::class.java))
            }
            R.id.nav_retrofit -> {
                startActivity(Intent(this@MainActivity, RetrofitActivity::class.java))
            }
            R.id.nav_setting -> {
                Toast.makeText(this@MainActivity, "Menu Settings masih dalam Pengembangan !", Toast.LENGTH_LONG).show()
            }
            R.id.nav_firebase -> {
                startActivity(Intent(this@MainActivity, CrudFirebaseActivity::class.java))
            }
            R.id.nav_recycler -> {
                startActivity(Intent(this@MainActivity, RecyclerMainActivity::class.java))
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
//        showDialoExit();
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == LOGIN_PERMISSION) {
            startNewActivity(resultCode, data)
        }
    }

    private fun startNewActivity(resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            val intent = Intent(this@MainActivity, ListOnline::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@MainActivity, "Login falset", Toast.LENGTH_SHORT).show()
        }
    }

}
