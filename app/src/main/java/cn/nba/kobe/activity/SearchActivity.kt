package cn.nba.kobe.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.kobe.R
import com.mapbox.geojson.Point
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.mapbox.search.ui.view.SearchBottomSheetView
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    val searchEngine by lazy {
        MapboxSearchSdk.getSearchEngine()
    }
    private val searchCallback = object : SearchSelectionCallback, SearchMultipleSelectionCallback {
        override fun onCategoryResult(
            suggestion: SearchSuggestion,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
        }

        override fun onError(e: Exception) {
//            hideToastBox()
        }

        override fun onResult(
            suggestions: List<SearchSuggestion>,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
//            hideToastBox()
            results.firstOrNull()?.coordinate?.let {
//                jumpByPoint(it)
                route2Result(it)
            } ?: kotlin.run {

            }
        }


        override fun onResult(suggestion: SearchSuggestion, result: SearchResult, responseInfo: ResponseInfo) {
        }

        override fun onSuggestions(suggestions: List<SearchSuggestion>, responseInfo: ResponseInfo) {
            suggestions.firstOrNull()?.let {
                searchEngine.select(suggestions, this)
            } ?: kotlin.run {
//                hideToastBox()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        search.apply {
            initializeSearch(savedInstanceState, SearchBottomSheetView.Configuration())
            addOnCategoryClickListener {
                searchEngine.search(
                    it.geocodingCanonicalName,
                    SearchOptions(),
                    searchCallback
                )
            }
            addOnFavoriteClickListener {
                route2Result(it.coordinate)
            }
            addOnHistoryClickListener {
                it.coordinate?.let {
                    route2Result(it)
                } ?: kotlin.run {
                    searchEngine.search(
                        it.name,
                        SearchOptions(),
                        searchCallback
                    )
                }
            }
            addOnSearchResultClickListener { searchResult, responseInfo ->
                searchResult.coordinate?.let {
                    route2Result(it)
                } ?: kotlin.run {
                    searchEngine.search(
                        searchResult.name,
                        SearchOptions(),
                        searchCallback
                    )
                }
            }
        }
    }

    private fun route2Result(it: Point) {
        startActivity(Intent(this, ResultActivity::class.java).apply {
            putExtra("lat", it.latitude())
            putExtra("lng", it.longitude())
        })
    }
}