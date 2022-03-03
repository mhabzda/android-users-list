# Android Users List

This is an application that displays a list of Github users and their repositories.
The data is cached locally so after the first retrieve app can work in an offline mode.

Github has a really low [rate limit](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#requests-from-user-accounts) (up to 60 requests per hour) 
when you are not authenticated. 
The app manages to fetch the whole list with this restriction, but only once. After that, there is an error about the rate limit excess.

If there is a need to work with the app without such a low limit the `github_token.txt` file can be placed in the root directory.
Inside, a [Github token](https://github.com/settings/tokens) needs to be provided. Thanks to that there will be a possibility to accomplish many more requests.

### Technology stack
- AndroidX
- Dagger 2
- RxJava
- Retrofit
- Moshi
- OkHttp3
- AndroidX Room
- Glide
- JUnit 5
