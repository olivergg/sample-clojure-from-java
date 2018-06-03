# sample-clojure-from-java
Sample project to demonstrate how to leverage Clojure power from a standard Java Spring application

## Use case : SQL query
One use case is to store complex SQL queries in EDN files (a query is just a String wrapped in a Clojure function) 
instead of using cumbersome `String.append` or storing them in text files.

## Example
This project contains an example of how to call Clojure function stored in .edn files (which are placed in a arbitrary package).
Those functions are just pure function that returns a String. The `(sql ...)` function is just a wrapper around the `(str ...)` core function.
