package com.example.onlineshop.screen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.onlineshop.R
import com.example.onlineshop.preferences.LocaleManager
import com.example.onlineshop.screen.cart.CartFragment
import com.example.onlineshop.screen.favourites.FavouriteFragment
import com.example.onlineshop.screen.home.HomeFragment
import com.example.onlineshop.screen.profile.ProfileFragment
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity() {

    //  Fragment variables
    private val homeFragment = HomeFragment.newInstance()
    private val favouriteFragment = FavouriteFragment.newInstance()
    private val cartFragment = CartFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()

    //  Active fragment which is on the screen  
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      If user has not internet, then shows NO CONNECTION view  
        if (!isNetworkAvailable(this)) {
            fragment_container.visibility = View.GONE
            no_connection.visibility = View.VISIBLE
        }
//      When presses this button, if network is available then activity starts again
        restart_activity.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }
//      Adding fragments to supportFragmentManager to make them ready to be shown
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, homeFragment, homeFragment.tag).hide(homeFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, favouriteFragment, favouriteFragment.tag)
            .hide(favouriteFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, cartFragment, cartFragment.tag).hide(cartFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profileFragment, profileFragment.tag)
            .hide(profileFragment).commit()
//      When user enters the app for the first time, then Home Fragment will be shown because it is an active fragment by default
        supportFragmentManager.beginTransaction().show(activeFragment).commit()
//      Adding listener to bottom navigation view so that it can change the fragments when its items are pressed  
        bottom_navigation_view.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.actionHome -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(homeFragment).commit()
                    activeFragment = homeFragment
                }
                R.id.actionFavourite -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(favouriteFragment).commit()
                    activeFragment = favouriteFragment
                }
                R.id.actionCart -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(cartFragment).commit()
                    activeFragment = cartFragment
                }
                R.id.actionProfile -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(profileFragment).commit()
                    activeFragment = profileFragment
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

//      There is a menu on the top left corner, when it is pressed it will start navigation drawer screen  
        ic_menu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
//      This is header layout for navigation drawer fragment, and when its items are pressed then it will execute code below
        val view: View = nav_view.getHeaderView(0)
        view.uz_cardView.setOnClickListener {
            Hawk.put("language_pref", "uz")
            this.finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        view.ru_cardView.setOnClickListener {
            Hawk.put("language_pref", "ru")
            this.finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        view.eng_cardView.setOnClickListener {
            Hawk.put("language_pref", "en")
            this.finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        ic_loupe.setOnClickListener {
            Toast.makeText(this, R.string.function_not_implemented, Toast.LENGTH_LONG).show()
        }


    }

    //  This is for Locale Manager for language preferences
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleManager.setLocale(newBase))
    }

    //  Code that checks if the network is available or not
    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}