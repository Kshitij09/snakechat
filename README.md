![snakechat](art/banner.jpg)

# SnakeChat

SnakeChat is a sample social media application built using Kotlin and
Jetpack Compose for the front-end, and GoLang with SQLite for the back-end.
The UI draws inspiration from [ShareChat](https://play.google.com/store/apps/details?id=in.mohalla.sharechat&hl=en_IN), 
a popular social media app in India known for its complex user interface. 
This makes SnakeChat an ideal candidate for exploring performance optimizations.

<a href="https://snakechat.b-cdn.net/snakechat-v1-preview.mp4"><img src="art/demo-thumb.png" width=100/></a>

# Download
<a href="https://github.com/Kshitij09/snakechat/releases/download/release-0.1/snakechat-0.1.apk"><img src="art/android.svg" width="64"></a>

# Goals

Trying out latest in the mobile development world while monitoring performance, app size & build time,
while maintaining backend with the cheapest means possible, be it writing a functionality in Rust/Zig/C.

# Non Goals

* Listing the app on play/app store and chase downloads/ratings
* Gain active users
* Monetize the app

# Roadmap

* Get the v1 out with Jetpack Compose (✅ done)
* Migrate the data & domain layer to [KMP](https://kotlinlang.org/docs/multiplatform.html)
* Build app using views (XML)
* Short video content
* Realtime Chat
* Over-The-Air (OTA) UI updates using [redwood-treehouse](https://github.com/cashapp/redwood)
* Integrate [Google AdMob test ads](https://developers.google.com/admob/android/test-ads)

# Docs

* [Android Project Metrics](https://docs.google.com/spreadsheets/d/1WM6hUFsl_s7O8ZLuwxDjRRbZGOHS3igBpINrXXr_Bb0/edit?usp=sharing)
* [Frontend](frontend/README.md)
* [Backend](backend/README.md)