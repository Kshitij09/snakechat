# Vision

The project will initially focus on building Compose-first Android only app, with long-term goal of supporting Kotlin Multiplatform with minimal code changes. Thus, any library which has KMP support will be preferred over android specific alternatives.

# Architecture

The project will be divided into 3 layers
* **data** - Responsible for handling the network, database & other sources of data storage, including an in-memory cache
* **domain** - Responsible for all the business logic around data retrieval, caching, transformation, etc.
* **presentation** - Responsible for User Interface of the app


# Libraries used

* `circuit`

Coming from the traditional android background, `androidx.viewmodel` is the most intuitive library for maintaining the presentation logic & state management. `ViewModel` is easier to scope (retain) around `Activity` and `Fragment` lifecycles as well as the `androidx.navigation` graph

Despite all of this, when we start working on a pure Compose app, it becomes very hard to achieve similar scoping of viewmodels due to its very nature of ephemeral recompositions. By the time `androidx.navigation` reaches its stability with type-safe arguments, many compose-first navigation libraries were born. `circuit` is one of such libraries which has very promising and intuitive APIs for compose. Thus, the project will be using it for building the presentation layer of the app      

* [kotlin-result](https://github.com/michaelbull/kotlin-result)

Project required a simple wrapper class to encapsulate API errors. While this might sound like
premature optimization, constructing `Exception` object is expensive operation due its construction
of stacktrace. We can't control external libraries from creating exceptions, but it can be avoided
in the custom error handling. kotlin-result library is primarily considered for 2 reasons:
* It's decoupling from native `Exception/Thorwable` classes
* Reduced runtime [overhead](https://github.com/michaelbull/kotlin-result/wiki/Overhead) with 0 allocations
on happy path
