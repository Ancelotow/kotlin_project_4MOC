package com.oye.moviepedia.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.compose.rememberImagePainter
import com.oye.moviepedia.R
import com.oye.moviepedia.domain.entities.MovieSearchResult
import com.oye.moviepedia.domain.entities.PersonSearchResult
import com.oye.moviepedia.domain.entities.SearchResult
import com.oye.moviepedia.domain.entities.TvSearchResult
import com.oye.moviepedia.domain.uses_cases.SearchDataError
import com.oye.moviepedia.domain.uses_cases.SearchError
import com.oye.moviepedia.domain.uses_cases.SearchLoading
import com.oye.moviepedia.domain.uses_cases.SearchSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val searchResults = mutableStateListOf<SearchResult>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Body()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.searchState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SearchLoading -> {
                    Log.d("SearchFragment", "Loading")
                }

                is SearchSuccess -> {
                    searchResults.clear()
                    searchResults.addAll(it.results)
                }

                is SearchDataError -> {
                    Log.e("Data Error", it.ex.toString())
                }

                is SearchError -> {
                    it.ex.printStackTrace()
                    Log.e("Error", it.ex.toString())
                }

                else -> {
                    Log.d("SearchFragment", "Else")
                }
            }
        })
    }

    @Composable
    private fun Body() {
        Column {
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                Title()
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                SearchTextField()
            }
            Box(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                LazyColumn {
                    items(searchResults) { result ->
                        ResultItem(result)
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalTextApi::class)
    @Composable
    private fun Title() {
        Text(
            text = stringResource(id = R.string.app_name),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF9CCCA5), Color(0xFF51B1DF)),
                    start = Offset(0f, 0f),
                    end = Offset(343f, 0f), // TODO: Auto width
                    tileMode = TileMode.Clamp
                )
            ),
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SearchTextField() {
        var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                searchViewModel.getSearchResult(newValue.text)
            },
            shape = RoundedCornerShape(5.dp),
            label = {
                Text(stringResource(id = R.string.search_hint))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 10.dp),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search icon",
                    tint = Color(0xFFD4D4D4),
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0x66989898),
                textColor = Color(0xFFD4D4D4),
                placeholderColor = Color(0xFFD4D4D4),
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color(0xFF51B1DF),
                unfocusedLabelColor = Color(0xFFD4D4D4),
            )
        )
    }

    @Composable
    private fun ResultItem(result: SearchResult) {
        when (result) {
            is MovieSearchResult -> MovieItem(result)
            is TvSearchResult -> TvItem(result)
            is PersonSearchResult -> PersonItem(result)
            else -> UnknownItem()
        }
    }

    @Composable
    private fun MovieItem(movieResult: MovieSearchResult) {
        val painter: Painter = rememberImagePainter(movieResult.posterPath)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Image(
                painter = painter,
                contentDescription = "movie poster",
                modifier = Modifier
                    .width(68.dp)
                    .height(99.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            Text(
                modifier = Modifier.padding(all = 5.dp),
                text = movieResult.title,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }

    @Composable
    private fun TvItem(tvResult: TvSearchResult) {
        val painter: Painter = rememberImagePainter(tvResult.posterPath)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Image(
                painter = painter,
                contentDescription = "tv poster",
                modifier = Modifier
                    .width(68.dp)
                    .height(99.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            Text(
                modifier = Modifier.padding(all = 5.dp),
                text = tvResult.name,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }

    @Composable
    private fun PersonItem(personResult: PersonSearchResult) {
        val painter: Painter = rememberImagePainter(personResult.profilePath)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.padding(all = 5.dp),
                    text = personResult.name,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    modifier = Modifier.padding(all = 5.dp),
                    text = personResult.mainJob,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color(0xFF989898)
                    )
                )
            }
        }
    }

    @Composable
    private fun UnknownItem() {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "Unknown",
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}