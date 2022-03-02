# Android Users List

This is a simple application that displays a list of Github users and their repositories.
Data is cached locally so after the first retrieve app can work in offline mode.

Github has a really low [rate limit](https://developer.github.com/v3/#rate-limiting) (up to 60 requests per hour) when you are not authenticated.
The app manages to fetch the whole list with this restriction but only once.
After that, there is info about rate limit exceed until it's reset.
If you want to work with the app without such a low limit you can place `github_token.txt` file in the root directory.
Inside you just need to provide [Github token](https://github.com/settings/tokens)
and you will be authorized with the possibility to accomplish many more requests.

### Technology stack
- Dagger 2
- RxJava
- Retrofit
- Moshi
- OkHttp3
- Room
- Glide
- JUnit 5
