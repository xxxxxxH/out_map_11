package cn.nba.kobe.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.nba.james.funcCreateLoading
import cn.nba.kobe.R
import cn.nba.kobe.adapter.Item2
import com.mapbox.geojson.Point
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_near.*

class NearActivity : AppCompatActivity() {
    val searchEngine by lazy {
        MapboxSearchSdk.getSearchEngine()
    }
    private val loadingView by lazy {
        funcCreateLoading()
    }
    private val searchCallback = object : SearchSelectionCallback, SearchMultipleSelectionCallback {
        override fun onCategoryResult(
            suggestion: SearchSuggestion,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
        }

        override fun onError(e: Exception) {
            loadingView.hide()
        }

        override fun onResult(
            suggestions: List<SearchSuggestion>,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
            loadingView.hide()
            results.firstOrNull()?.coordinate?.let {
                route2Result(it)
            } ?: kotlin.run {
                Toast.makeText(this@NearActivity,"No suggestions found", Toast.LENGTH_SHORT).show()
            }
        }


        override fun onResult(suggestion: SearchSuggestion, result: SearchResult, responseInfo: ResponseInfo) {
        }

        override fun onSuggestions(suggestions: List<SearchSuggestion>, responseInfo: ResponseInfo) {
            suggestions.firstOrNull()?.let {
                searchEngine.select(suggestions, this)
            } ?: kotlin.run {
                loadingView.hide()
                Toast.makeText(this@NearActivity,"No suggestions found", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near)
        val data = ArrayList<String>()
        data.add("airport")
        data.add("atm")
        data.add("bakery")
        data.add("bank")
        data.add("bus")
        data.add("cafe")
        data.add("church")
        data.add("cloth")
        data.add("dentist")
        data.add("doctor")
        data.add("fire station")
        data.add("gas station")
        data.add("hospital")
        data.add("hotel")
        data.add("jewelry")
        data.add("mall")
        data.add("mosque")
        data.add("park")
        data.add("pharmacy")
        data.add("police")
        data.add("post office")
        data.add("salon")
        data.add("shoe")
        data.add("stadium")
        data.add("university")
        data.add("zoo")

        val itemAdapter = ItemAdapter<Item2>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        val items = ArrayList<Item2>()
        data.forEach {
            val i = Item2()
            i.title = it
            items.add(i)
        }
        itemAdapter.add(items)
        recycler.adapter = fastAdapter
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            3,
            StaggeredGridLayoutManager.VERTICAL
        )
        recycler.layoutManager = staggeredGridLayoutManager
        fastAdapter.onClickListener = { v, a, i, p ->
            loadingView.show()
            searchEngine.search(
                data[p],
                SearchOptions(),
                searchCallback
            )
            false
        }
    }

    private fun route2Result(it: Point) {
        startActivity(Intent(this, ResultActivity::class.java).apply {
            putExtra("lat", it.latitude())
            putExtra("lng", it.longitude())
        })
    }
}