# Vision

The project will initially focus on building Compose-first Android only app, 
with long-term goal of supporting Kotlin Multiplatform with minimal code changes. 
Thus, any library which has KMP support will be preferred over android specific alternatives.

# Architecture

The project will be divided into 3 layers
* **data** - Responsible for handling the network, database & other sources of data storage, including an in-memory cache
* **domain** - Responsible for all the business logic around data retrieval, caching, transformation, etc.
* **presentation** - Responsible for User Interface of the app


# Libraries used

### [circuit](https://github.com/slackhq/circuit)

Coming from the traditional android background, Jetpack ViewModel is the most intuitive
library for maintaining the presentation logic & state management. It can be easily scoped
at Activity / Fragment / Jetpack NavGraph level using hilt. [Recently](https://developer.android.com/jetpack/androidx/releases/lifecycle?s=09#2.8.0-alpha03),
it started supporting Kotlin Multiplatform as well.
Despite all of this, it lacks a good architecture specifically designed for Jetpack Compose.
Circuit on the other hand, is compose-first, multiplatform library with really nice abstraction
for State, UDF, Navigation, surviving configuration changes and DI. At current stage, a KMP
support from a 3rd party library is more promising than androidx equivalents as we never know
when the priorities would turn the table.

### [kotlin-result](https://github.com/michaelbull/kotlin-result)

Project required a simple wrapper class to encapsulate API errors. While this might sound like
premature optimization, constructing `Exception` object is expensive operation due its construction
of stacktrace. Furthermore, it's recommended not to use Exceptions for the flow control ([ref](https://web.archive.org/web/20140430044213/http://c2.com/cgi-bin/wiki?DontUseExceptionsForFlowControl))
We can't control external libraries from creating exceptions, but it can be avoided
in the custom error handling. kotlin-result library is primarily considered for 2 reasons:
* It's decoupling from native `Exception/Thorwable` classes
* Reduced runtime [overhead](https://github.com/michaelbull/kotlin-result/wiki/Overhead) with 0 allocations on happy path


### [kotlin-inject](https://github.com/evant/kotlin-inject)

kotlin-inject, is kotlin-first, multiplatform, compile-time DI framework with very similar APIs
to dagger. Since this project mainly focuses on performance & multiplatform capabilities, kotlin-inject
suits very well here 

### [bytemask](https://github.com/PatilShreyas/bytemask)

Since this a sample app with dummy data, security is not our primary concern. But if it comes 
for free, with minimal efforts, it's always welcome. Bytemask masks secret strings for the app
in the source code making it difficult to extract from reverse engineering.

### [lyricist](https://github.com/adrielcafe/lyricist)

Multiplatform way of sharing the local strings with compose friendly API. It has type-safe
API for parameterized strings and with its code first approach, we can even load these strings
dynamically from the API.

### [coil](https://github.com/coil-kt/coil)

Coil has become de-facto image loading library, built using kotlin coroutines and even supports
kotlin multiplatform.

### [version-catalog-update-plugin](https://github.com/littlerobots/version-catalog-update-plugin)

Best way to maintain version catalog is to keep it sorted, clean & up-to-date. This library
does all of this for you

### [dependency-guard](https://github.com/dropbox/dependency-guard)

A Gradle plugin that guards against unintentional dependency changes. It surfaces dependency
changes due to transitive dependencies, allows maintaining deny-list of libraries that should
never be shipped to production and generates a baseline file that can be version controlled