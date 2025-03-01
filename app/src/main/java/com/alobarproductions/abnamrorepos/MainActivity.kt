package com.alobarproductions.abnamrorepos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alobarproductions.abnamrorepos.main.UserAgent
import com.alobarproductions.abnamrorepos.data.network.GitHubRepoResponse
import com.alobarproductions.abnamrorepos.data.network.GitHubServiceFactory
import com.alobarproductions.abnamrorepos.ui.theme.AbnAmroReposTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbnAmroReposTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var repos by remember { mutableStateOf<List<GitHubRepoResponse>>(emptyList()) }

                    LaunchedEffect(Unit) {
                        repos = fetchAbnAmroRepos()
                    }

                    LazyColumn(
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        items(repos.size) { index ->
                            val repo = repos[index]
                            Greeting(name = repo.name)
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchAbnAmroRepos(): List<GitHubRepoResponse> {
        val service = GitHubServiceFactory(
            baseUrl = BuildConfig.GITHUB_BASE_URL,
            userAgent = UserAgent.value,
            logHttpTraffic = BuildConfig.DEBUG,
        ).create()
        return service.listUserRepos(username = "abnamrocoesd")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AbnAmroReposTheme {
        Greeting("Android")
    }
}