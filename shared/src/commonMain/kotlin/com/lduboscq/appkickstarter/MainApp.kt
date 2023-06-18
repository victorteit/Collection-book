package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.lduboscq.appkickstarter.list.ListScreenContent
import com.lduboscq.appkickstarter.list.PersonsListScreen
import com.lduboscq.appkickstarter.model.BookScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
public fun MainApp() {
        Navigator(BookScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }

