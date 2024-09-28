# Vision

The project is currently a simple REST API server, and it aims to preserve 
its simplicity - from development to deployment. However, if we ever need to
choose between cost-effectiveness and developer productivity, we would lean
towards cost. One of the aggressive goals of this project is to fit the entire
backend on AWS EC2 t2.nano.

In the future, we might expose these backend APIs to public, allowing them to build
their own frontends for the same. It can be more complex and dynamic version of
[jsonplaceholder](https://jsonplaceholder.typicode.com/)

# Code Architecture

Project follows domain based package structure based on this [talk](https://www.youtube.com/watch?v=MzTcsI6tn-0).
* Root packages aim to dictate high level functionalities of the app.
* **domain** package contain models used for writing business logic. 
* DB access APIs are abstracted by the `*Dao` interface
* **sqlite** package implements `*Dao` interfaces for SQLite database
* Business logic APIs are wrapped in a struct `*Service` which accepts respective
`*Dao` as constructor argument
* **transport** package is responsible for REST APIs and their DTO models
  * Project uses generic `APIError` struct to model the error responses
  * Project does not use any external library for modelling the REST APIs.
  It has its own `Handler` type with similar signature to `http.HandlerFunc`, except
  the fact that it returns `error`. While bridging the custom handler to http counterpart,
  we check if error is `APIError` and return them as HTTP error response. Otherwise,
  the errors are printed to `stdout` while returning generic "Internal Server Error" response.
  * Project also introduces a decorator type `Middleware` which is used to wrap some additional
  functionalities around existing `Handler`s

# External Libraries

* [mattn/go-sqlite3](https://github.com/mattn/go-sqlite3)

    sqlite3 driver for go using database/sql

* [go/x/crypto](https://pkg.go.dev/golang.org/x/crypto/acme/autocert)

    Project uses autocert package to provide automatic access to certificates from Let's Encrypt

* [go/x/time](https://pkg.go.dev/golang.org/x/time/rate)

    Project uses rate package for rate limiter middleware