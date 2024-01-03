# Android Users List

This sample application displays a list of Github users and their repositories.
The data is cached locally so the app can work in an offline mode after the first retrieve.

To fetch users repositories we need to make a separate request for every one of them. 
Unfortunately, GitHub has a really low [rate limit](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#requests-from-user-accounts) 
(up to 60 requests per hour) when you are not authenticated.
The app fetches the whole list with this restriction, but only once. After that, there is an error about the rate limit excess.

If there is a need to work with the app without such a low limit the `github_token.txt` file can be placed in the root directory.
Inside, a [Github token](https://github.com/settings/tokens) needs to be provided.

### Demo

<div align="center">
  <video src="https://github.com/mhabzda/android-users-list/assets/26122834/31f32a3d-cbc5-48da-a91c-83b4a280a23a" width="400" />
</div>

### Architecture

Clean Architecture has been used in the project. There are multiple Gradle modules containing
specific layers in the Architecture:

- App (Android module)
- Domain (Java module)
- Data (Android module)

The UI has been created using the MVI pattern.

### Technology stack

- Android Jetpack Compose (Material3)
- Hilt
- Kotlin Coroutines
- Retrofit
- Kotlinx serialization
- OkHttp3
- AndroidX Room
- Coil
- JUnit 5
- Mockito Kotlin
- KtLint

### Gradle Versions Plugin

In order to automate dependencies version updates, the Gradle Versions plugin alongside the Version catalog
update plugin has been introduced.
There are two tasks that can help with automatic dependency updates:

- `./gradlew dependencyUpdate` - shows a report of possible updates. It takes only stable (not RC)
  versions into account.
- `./gradlew versionCatalogUpdate` - automatically updates the `libs.versions.toml` file with the
  latest stable versions.
