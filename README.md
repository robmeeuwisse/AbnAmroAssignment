# ABN AMRO GitHub Repositories

This project is my submission for the Android assessment test by ABN AMRO. 
The requirements can be found in assignment.pdf.

Rob Meeuwisse, 4 March 2025 

## Description

GitHub repository information is fetched from their API and cached in a Room database. The list 
screen displays the repos and requests more items when it reaches the bottom of the currently 
displayed items. 

CLick the refresh action in the top-right to clear the cache and reload items from the API again. 

## GitHub Personal Access Token

The app works without it, but you can add your GitHub Personal Access Token so the app might
be able to show private repositories too. Provide the following Gradle property to do so: 

```
com.alobarproductions.abnamrorepos.github.token=<Your-GitHub-PAT>
```

## Used dependencies
- Jetpack Compose
- Room DB
- Coil
- Kotlin Serialization 
- Retrofit/OkHttp
- Mockk
