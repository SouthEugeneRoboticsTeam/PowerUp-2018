# PowerUp

[![Travis][travis-img]][travis-url]
[![Coveralls][coveralls-img]][coveralls-url]

SERT's robot code for the 2018 PowerUp game.

## Building

Unfortunately, GradleRIO made a bad decision to use dynamic versioning which is a feature Gradle has
that will find the latest version of a dependency for you. This sounds good, but is very bad for
several reasons: lack of build reproducibility, stuff could change under you without notice, Gradle
makes a network call to check if you have the latest version of a dependency on every build, etc.
The last example is the main issue because it prevents building robot code unless you have an
internet connection (which the robot doesn't). To get around this issue, you have to push code to
the robot with the `--offline` flag.

### TL;DR

1. To get everything downloaded and setup, run `./gradlew build` once **while online**
2. When pushing code to the robot, use `./gradlew deploy --offline`

[travis-img]: https://img.shields.io/travis/SouthEugeneRoboticsTeam/PowerUp-2018.svg?style=flat-square
[travis-url]: https://travis-ci.org/SouthEugeneRoboticsTeam/PowerUp-2018
[coveralls-img]: https://img.shields.io/coveralls/SouthEugeneRoboticsTeam/PowerUp-2018.svg?style=flat-square
[coveralls-url]: https://coveralls.io/github/SouthEugeneRoboticsTeam/PowerUp-2018
