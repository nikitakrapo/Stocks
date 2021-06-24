package com.nikitakrapo.android.stocks.view.news

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.ActivityNewsArticleBinding
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.utils.TAG

class NewsArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsArticleBinding
    private lateinit var article: MarketNewsArticle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        article = intent.getSerializableExtra(ARTICLE_EXTRA) as MarketNewsArticle
        binding.article = article

        binding.shareButton.setOnClickListener {
            sendArticle(article)
        }

        setupToolbar()
    }

    private fun setupToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun sendArticle(marketNewsArticle: MarketNewsArticle){
        val sendIntent = Intent().apply{
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, marketNewsArticle.url)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_news_article_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_open_in_browser -> {
                openInBrowser(article.url ?: "")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openInBrowser(url: String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    companion object{
        private const val TAG = "NewsArticleActivity"

        const val ARTICLE_EXTRA = "ARTICLE_EXTRAS"
    }
}