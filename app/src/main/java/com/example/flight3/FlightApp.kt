package com.example.flight3

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flight3.ui.navigation.FlightNavHost
import com.example.flight3.ui.theme.Flight3Theme

@Composable
fun FlightApp(navController: NavHostController = rememberNavController()){
    FlightNavHost(navController = navController)
}

/**
 * App bar to display title
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
){
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
            )
                },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun InputForm(
    partName: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true
){
    OutlinedTextField(
        value = partName,
        onValueChange ={onValueChange(it)},
        placeholder = { Text(stringResource(id = R.string.input_airport)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon)
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_mic_24),
                contentDescription = null
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small)),
        enabled = enabled,
        singleLine = true,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium))
    )
}

@Preview(showBackground = true)
@Composable
private fun InputFormPreview(){
    Flight3Theme {
        Surface {
            InputForm(
                partName = "",
                onValueChange = {}
            )
        }
    }
}