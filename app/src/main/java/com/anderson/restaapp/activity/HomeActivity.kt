package com.anderson.restaapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.anderson.restaapp.R
import com.anderson.restaapp.databinding.ActivityHomeBinding
import com.anderson.restaapp.databinding.NavViewHeaderBinding
import com.anderson.restaapp.fragment.SelectDessertFragment
import com.anderson.restaapp.fragment.SelectDinkFragment
import com.anderson.restaapp.fragment.SelectFoodFragment
import com.anderson.restaapp.fragment.SelectTimeFragment
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private val timeFragment = SelectTimeFragment()
    private val foodFragment = SelectFoodFragment()
    private val drinkFragment = SelectDinkFragment()
    private val dessertFragment = SelectDessertFragment()
    private val fm = supportFragmentManager
    private var active: Fragment = timeFragment

    companion object {
        var homeViewModel = HomeViewModel()
    }
    fun getHomeViewModel(): HomeViewModel{
        return homeViewModel
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //setupBottomNavigation()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.selectFoodFragment,
                R.id.selectTimeFragment,
                R.id.selectDinkFragment,
                R.id.selectDessertFragment,
                R.id.viewReviewFragment,
                R.id.locationFragment,
                R.id.myBookingsFragment,
                R.id.profileFragment,
                R.id.detailBookingFragment
            ), binding.drawerLayout
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.botNav.setupWithNavController(navController)
        binding.navView.setupWithNavController(navController)
    }

//    private fun setupBottomNavigation() {
//
//        binding.botNav.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.selectTimeFragment -> {
//                    fm.beginTransaction().hide(active).show(timeFragment).commit()
//                    active = timeFragment
//                    return@setOnItemSelectedListener true
//                }
//                R.id.selectFoodFragment -> {
//                    fm.beginTransaction().hide(active).show(foodFragment).commit()
//                    active = foodFragment
//                    return@setOnItemSelectedListener true
//                }
//                R.id.selectDinkFragment -> {
//                    fm.beginTransaction().hide(active).show(drinkFragment).commit()
//                    active = drinkFragment
//                    return@setOnItemSelectedListener true
//                }
//                R.id.selectDessertFragment -> {
//                    fm.beginTransaction().hide(active).show(dessertFragment).commit()
//                    active = dessertFragment
//                    return@setOnItemSelectedListener true
//                }
//            }
//            return@setOnItemSelectedListener false
//        }
//
//        fm.beginTransaction().add(R.id.nav_host_fragment,dessertFragment,"4").hide(dessertFragment).commit()
//        fm.beginTransaction().add(R.id.nav_host_fragment,drinkFragment,"3").hide(drinkFragment).commit()
//        fm.beginTransaction().add(R.id.nav_host_fragment,foodFragment,"2").hide(foodFragment).commit()
//        fm.beginTransaction().add(R.id.nav_host_fragment,timeFragment,"1").commit()
//    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}