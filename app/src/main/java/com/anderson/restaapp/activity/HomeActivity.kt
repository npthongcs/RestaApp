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
import com.anderson.restaapp.listener.OnBackPressedListener
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    companion object {
        var homeViewModel = HomeViewModel()
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

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
                R.id.profileFragment
            ), binding.drawerLayout
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.botNav.setupWithNavController(navController)
        binding.navView.setupWithNavController(navController)
    }

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

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]
        navController = navHostFragment.findNavController()
        if (currentFragment is OnBackPressedListener) (currentFragment as OnBackPressedListener).onBackPressed()
        else if (!navController.popBackStack()) finish()
    }
}