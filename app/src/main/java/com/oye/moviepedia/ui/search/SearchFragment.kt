package com.oye.moviepedia.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
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
import com.oye.moviepedia.R

class SearchFragment : Fragment() {


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

    @Composable
    fun Body() {
        Column {
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                Title()
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            ) {
                SearchTextField()
            }
        }

    }

    @OptIn(ExperimentalTextApi::class)
    @Composable
    fun Title() {
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
    fun SearchTextField() {
        var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = textFieldValue,
            onValueChange = { newValue -> textFieldValue = newValue },
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

}