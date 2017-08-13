# truly-api

An API for Truly's take-home.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

In addition, you will also need Java installed.

## Building

To build the project, run `lein uberjar`.

## Inputs

- Port (e.g. 3000, 3333)
  - Usage: `java -jar target/uberjar/truly-api-0.1.0-standalone.jar -p 3333`
- CSV file (e.g. `seed.csv`)
  - Usage: `java -jar target/uberjar/truly-api-0.1.0-standalone.jar -f seed.csv`

## Usage

`lein uberjar` will output a jar that you can run on your machine and use locally. The port and the CSV seed data can be specified when running the jar, e.g.:

    `lein uberjar
    java -jar target/truly-api-0.1.0-SNAPSHOT-standalone.jar -p 3333 -f caller-data.csv`
