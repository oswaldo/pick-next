#!/usr/bin/env -S scala-cli shebang -S 3

// This was generated with some scaffold to easy the creation of a new program.

//> using toolkit latest
//> using dep "com.lihaoyi::pprint::0.8.1"
//> using file "/Users/oswaldo.dantas/git/tools/common/core.sc"
// add using statements for the scripts you want to use

import os.*
import core.*
import core.given
import util.*
import util.chaining.scalaUtilChainingOps
import scala.annotation.tailrec
import pprint.*
// add import statements for the scripts you want to use

given Array[String] = args

case class PickNextArgs(
  someRequiredArgument: Int,
  someOptionalArgument: String,
  someOptionalPath: Path,
):
  require(true, "some characteristic needs to be tested!")

val pickNextArgs = Try {
  PickNextArgs(
    someRequiredArgument = argRequired(0, "someRequiredArgument is required!"),
    someOptionalArgument = arg(1, "someDefaultValue"),
    someOptionalPath = argCallerOrCurrentFolder(2),
    // we could have as many arguments as we want, including from environment variables using argOrEnv or argOrEnvRequired
  )
} match
  case Success(args) => args
  case Failure(e) =>
    throw new Exception(
      """Invalid arguments.
        |  Usage:   pickNext <someRequiredArgument> [[<someOptionalArgument>] <someOptionalPath>]
        |  Notes: Some notes about arguments or syntax if relevant
        |  Example: pickNext 123 "Hello World!"
        |    Some explanation of what happens after calling the program with that set of arguments
        |    e.g.: Executes pickNext processing someRequiredArgument as 123, someOptionalArgument as "Hello World!", and assumes for someOptionalPath the caller script folder or the current folder if the script was triggered directly""".stripMargin,
      e,
    )
import pickNextArgs.*
//do some fancy stuff
pprint.pprintln(pickNextArgs)
