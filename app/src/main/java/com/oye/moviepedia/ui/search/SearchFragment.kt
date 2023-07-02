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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.getColor
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
    private var isLoaded by mutableStateOf(true)

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
                   isLoaded = false
                }

                is SearchSuccess -> {
                    searchResults.clear()
                    searchResults.addAll(it.results)
                    isLoaded = true
                }

                is SearchDataError -> {
                    Log.e("Data Error", it.ex.toString())
                    isLoaded = true
                }

                is SearchError -> {
                    it.ex.printStackTrace()
                    Log.e("Error", it.ex.toString())
                    isLoaded = true
                }

                else -> {
                    Log.d("SearchFragment", "Else")
                    isLoaded = true
                }
            }
        })
    }

    @Composable
    private fun Loading(){
        AndroidView(
            factory = { context ->
                LayoutInflater.from(context).inflate(R.layout.loader, null)
            },
        )
    }

    @Composable
    private fun Body() {
        Column {
            Box(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.search_padding).value.dp)
            ) {
                Title()
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.search_padding).value.dp)
            ) {
                SearchTextField()
            }
            if(isLoaded.not()){
                Box(
                    contentAlignment = Alignment.Center, // Centrer le composant
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Loading()
                }
            }
            else if(searchResults.isEmpty())  {
                Box(
                    contentAlignment = Alignment.Center, // Centrer le composant
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(id = R.string.search_no_result),
                        style = TextStyle(
                            fontSize = dimensionResource(id = R.dimen.body_regular).value.sp,
                            textAlign = TextAlign.Center,
                            color = colorResource(id = R.color.accent)
                        ),
                    )
                }
            } else {
                Box(
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.search_padding).value.dp,
                        end = dimensionResource(id = R.dimen.search_padding).value.dp)
                ) {
                    LazyColumn {
                        items(searchResults) { result ->
                            ResultItem(result)
                        }
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
                fontSize = dimensionResource(id = R.dimen.h1).value.sp,
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    colors = listOf(colorResource(id = R.color.accent), colorResource(id = R.color.blue)),
                    start = Offset(0f, 0f),
                    end = Offset(343f, 0f),
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
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.search_round_corner).value.dp
            ),
            label = {
                Text(stringResource(id = R.string.search_hint))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.search_field_height).value.dp)
                .padding(horizontal = dimensionResource(id = R.dimen.search_padding).value.dp),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search_icon_content_description),
                    tint = colorResource(id = R.color.search_field_text),
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = colorResource(id = R.color.search_field_container),
                textColor = colorResource(id = R.color.search_field_text),
                placeholderColor = colorResource(id = R.color.search_field_text),
                focusedIndicatorColor = colorResource(id = R.color.transparent),
                focusedLabelColor =  colorResource(id = R.color.search_field_label_focused),
                unfocusedLabelColor = colorResource(id = R.color.search_field_label_unfocused),
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
        Row(modifier = Modifier.padding(
            vertical = dimensionResource(id = R.dimen.search_medium_padding).value.dp
        )) {
            if(!movieResult.posterPath.isNullOrBlank()) {
                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.search_movie_content_description),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.search_movie_poster_width).value.dp)
                        .height(dimensionResource(id = R.dimen.search_movie_poster_height).value.dp)
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_round_corner).value.dp))
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.search_movie_poster_width).value.dp)
                        .height(dimensionResource(id = R.dimen.search_movie_poster_height).value.dp)
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_round_corner).value.dp))
                        .background(colorResource(id = R.color.search_background_card))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_movie),
                        contentDescription = stringResource(id = R.string.search_movie_content_description),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.search_image_size).value.dp)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(
                    all = dimensionResource(id = R.dimen.search_small_padding).value.dp
                ),
                text = movieResult.title,
                style = TextStyle(
                    fontSize = dimensionResource(id = R.dimen.search_text_size).value.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
            )
        }
    }

    @Composable
    private fun TvItem(tvResult: TvSearchResult) {
        val painter: Painter = rememberImagePainter(tvResult.posterPath)
        Row(modifier = Modifier.padding(
            vertical = dimensionResource(id = R.dimen.search_medium_padding).value.dp
        )) {
            if(!tvResult.posterPath.isNullOrBlank()) {
                Image(
                    painter = painter,
                    contentDescription = getString(R.string.search_tv_content_description),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.search_movie_poster_width).value.dp)
                        .height(dimensionResource(id = R.dimen.search_movie_poster_height).value.dp)
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_round_corner).value.dp))
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.search_movie_poster_width).value.dp)
                        .height(dimensionResource(id = R.dimen.search_movie_poster_height).value.dp)
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_round_corner).value.dp))
                        .background(colorResource(id = R.color.search_background_card))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_tv),
                        contentDescription = getString(R.string.search_tv_content_description),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.search_image_size).value.dp)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(
                    all = dimensionResource(id = R.dimen.search_small_padding).value.dp
                ),
                text = tvResult.name,
                style = TextStyle(
                    fontSize = dimensionResource(id = R.dimen.search_text_size).value.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
            )
        }
    }

    @Composable
    private fun PersonItem(personResult: PersonSearchResult) {
        val painter: Painter = rememberImagePainter(personResult.profilePath)
        Row(modifier = Modifier.padding(
            vertical = dimensionResource(id = R.dimen.search_medium_padding).value.dp
        )) {
            if(personResult.profilePath != null) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.search_person_size).value.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .aspectRatio(1f)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = getString(R.string.search_person_content_description),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.search_person_size).value.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.search_background_card))
                        .aspectRatio(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_director_chair),
                        contentDescription = getString(R.string.search_person_content_description),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.search_image_size).value.dp)
                            .clip(CircleShape)
                    )
                }
            }
            Box(modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.search_small_padding).value.dp
            )) {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = personResult.name,
                        style = TextStyle(
                            fontSize = dimensionResource(id = R.dimen.search_text_size).value.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.white)
                        )
                    )
                    Text(
                        text = personResult.mainJob,
                        style = TextStyle(
                            fontSize = dimensionResource(id = R.dimen.search_text_size).value.sp,
                            color = colorResource(id = R.color.gray)
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun UnknownItem() {
        Column(modifier = Modifier.padding(
            vertical = dimensionResource(id = R.dimen.search_medium_padding).value.dp
        )) {
            Text(
                text = getString(R.string.search_unknown_result),
                style = TextStyle(
                    fontSize = dimensionResource(id = R.dimen.search_text_size).value.sp
                )
            )
        }
    }
}