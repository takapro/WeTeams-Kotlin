package com.example.weteams.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.weteams.R
import com.example.weteams.screen.main.MainViewModel
import com.google.firebase.auth.FirebaseUser

@Composable
fun DrawerContent(scaffoldState: ScaffoldState, viewModel: MainViewModel) {
    val user = viewModel.user.value
    if (user == null) {
        return
    }

    val currentScreen by viewModel.currentScreen.observeAsState(Screen.PROJECTS)
    val currentProject by viewModel.currentProject.observeAsState()

    ScrollableColumn {
        DrawerHeader(user)

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            getScreenGroups(currentProject).forEachIndexed { index, screenGroup ->
                DrawerGroup(isTop = index == 0, text = screenGroup.title)
                for (screen in screenGroup.screens) {
                    DrawerItem(
                        screen = screen,
                        enabled = screenGroup.enabled,
                        selected = screen == currentScreen
                    ) {
                        viewModel.currentScreen.value = screen
                        viewModel.currentProject.value =
                            if (screen != Screen.SETTINGS) "My Great Project" else null
                        scaffoldState.drawerState.close()
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerHeader(user: FirebaseUser) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .padding(16.dp)
    ) {
        Image(
            imageVector = vectorResource(R.drawable.ic_launcher_foreground)
        )

        Text(
            text = user.displayName ?: "unknown",
            color = MaterialTheme.colors.background,
            fontSize = TextUnit.Sp(20)
        )

        Text(
            text = user.email ?: "unknown",
            color = MaterialTheme.colors.background
        )
    }
}

@Composable
fun DrawerGroup(isTop: Boolean = false, text: String? = null) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!isTop) {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray, thickness = 0.5.dp)
        }

        if (text != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DrawerItem(screen: Screen, enabled: Boolean, selected: Boolean, onSelect: () -> Unit) {
    val color =
        if (selected) {
            MaterialTheme.colors.primary
        } else if (enabled) {
            contentColorFor(MaterialTheme.colors.background)
        } else {
            Color.Gray
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .let {
                if (enabled) {
                    it.clickable { onSelect() }
                } else {
                    it
                }
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = vectorResource(screen.imageRes),
            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
            colorFilter = ColorFilter.tint(color)
        )

        Text(
            text = screen.text,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}