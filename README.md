# Users-List

Firstly, I didn't manage to finish implementing unit tests. I didn't have enough time.

I did't use dagger for the same reason. I just injected all dependencies inside MainActivity.
I am aware that this is not the best practise but I wanted to finish all features before time passed.

I had a problem with github urls and I cannot test the app with it because very often I had 403 error (too many requests).
In order to go around that I prepared a fake repository and the app is working with it correctly.
It should also work with remote data but as I wrote I didn't have many opportunities to test with server data.
To use fake data FakeUserRepository needs to be injected instead of RemoteUserRepository in ListActivity.

I implemented two layers (api and database) and use it in the UI layer. I used RxJava to merge local and remote data.
Fetched data is then mapped and propagated to list adapter.

I didn't manage orientation change in any way as I think it's not the crucial part of this task
but if necessary I will probably do it with Android ViewModel to persist data on orientation change somehow.
I didn't handle errors in the UI properly (dialog, toast) because of time restriction as well.

I load repositories lazily when view is being bound to the list but with only 30 users I could do it at once
when loading all users. At the end it caused architectures problems for me (with local data) and I needed
to implement some RxJava operators magic. I would probably changed that but I decided to leave it as it is because of time.

# Additional note
This solution doesn't work well with remote data. I wanted to fetch repositories in view holder bind method
which is not a good idea. I fixed it in separate 'develop' branch but I did it after available time. It works much better there.