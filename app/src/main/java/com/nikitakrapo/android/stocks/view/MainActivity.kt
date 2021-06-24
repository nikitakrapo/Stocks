package com.nikitakrapo.android.stocks.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.ActivityMainBinding
import com.nikitakrapo.android.stocks.databinding.DialogAddPortfolioBinding
import com.nikitakrapo.android.stocks.model.StockPortfolio
import com.nikitakrapo.android.stocks.repository.PortfolioRepository
import com.nikitakrapo.android.stocks.utils.ConnectionLiveData
import com.nikitakrapo.android.stocks.view.search.SearchActivity
import com.nikitakrapo.android.stocks.viewmodel.PortfolioViewModel
import com.nikitakrapo.android.stocks.viewmodel.PortfolioViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val portfolioViewModel: PortfolioViewModel by viewModels {
        PortfolioViewModelFactory(
            PortfolioRepository.getInstance(applicationContext)
        )
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var toolbar: Toolbar

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationBar()

        observeConnection()
    }

    private fun observeConnection() {
        val connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this, Observer { hasConnection ->
            setNoInternetBannerVisibility(!hasConnection)
        })
    }

    @Suppress("DEPRECATION")
    private fun setNoInternetBannerVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.noInternetBanner.apply {
                setText(R.string.no_internet_connection)
                setBackgroundColor(resources.getColor(R.color.light_gray))
                visibility = View.VISIBLE
            }
        } else {
            binding.noInternetBanner.apply {
                setText(R.string.connection_restored)
                setBackgroundColor(resources.getColor(R.color.light_green))
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    visibility = View.GONE
                }
            }
        }
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNav
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.market, R.id.news, R.id.portfolio)
        )

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setOnDestinationChangedListener(){
        navController.addOnDestinationChangedListener { navController, navDestination, bundle ->
            Log.d(TAG, "navDestination: $navDestination")
            toolbar.menu.findItem(R.id.action_add_portfolio).isVisible =
                (navDestination.id == R.id.portfolioContainerFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main_activity, menu)
        setOnDestinationChangedListener()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                launchSearchActivity()
                true
            }
            R.id.action_add_portfolio -> {
                showAddPortfolioDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchSearchActivity() {
        val intent = Intent(applicationContext, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun showAddPortfolioDialog(){
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }

        val binding: DialogAddPortfolioBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.dialog_add_portfolio, null, false
        )

        builder.apply {
            setTitle(R.string.add_portfolio)
            setView(binding.root)
            setPositiveButton(R.string.add, DialogInterface.OnClickListener { dialog, id ->
                val portfolioName = binding.portfolioNameEditText.text.toString()
                portfolioViewModel.addPortfolio(
                    StockPortfolio(
                        portfolioName, emptyList()
                    )
                )
            })
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
