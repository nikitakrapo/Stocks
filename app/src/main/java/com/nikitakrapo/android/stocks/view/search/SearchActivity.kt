package com.nikitakrapo.android.stocks.view.search

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.ActivitySearchBinding
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.utils.ErrorTextUtils
import com.nikitakrapo.android.stocks.viewmodel.SearchViewModel
import com.nikitakrapo.android.stocks.viewmodel.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {

    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            StockRepository.getInstance(applicationContext)
        )
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipeRefreshLayout.isEnabled = false
        binding.searchViewModel = searchViewModel

        recyclerView = binding.recyclerView

        val adapter = SearchResultAdapter()
        recyclerView.adapter = adapter

        observeSymbolLookup()

        observeRefreshing()

        setupToolbar()
    }

    private fun observeRefreshing() {
        searchViewModel.isRefreshing.observe(this, Observer {
            binding.swipeRefreshLayout.isRefreshing = it
        })
    }

    private fun observeSymbolLookup() {
        searchViewModel.result.observe(this, Observer {
            Log.d(TAG, it.toString())
            when (it) {
                is NetworkResult.Error -> showShortSnackbar(
                    ErrorTextUtils.getErrorSnackBarText(
                        it, this
                    )
                )
                is NetworkResult.Success -> {
                    (recyclerView.adapter as SearchResultAdapter).submitList(it.data.result)
                }
            }
            Log.d(TAG, (recyclerView.adapter as SearchResultAdapter).currentList.toString())
        })
    }

    private fun showShortSnackbar(text: String) {
        val contextView = binding.recyclerView
        Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_activity, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.apply {
            setIconifiedByDefault(false)
            queryHint = getString(R.string.hint_stock_search)
            maxWidth = Int.MAX_VALUE
        }
        setOnQueryTextListener(searchView)
        return true
    }

    private fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    searchViewModel.searchNews(
                        newText,
                        resources.getInteger(R.integer.search_delay_ms)
                    )
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchViewModel.searchNews(
                        query,
                        0
                    )
                    return true
                }
            }
        )
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    companion object {
        private const val TAG = "SearchActivity"
    }
}