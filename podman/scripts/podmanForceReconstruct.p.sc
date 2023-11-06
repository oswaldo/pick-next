#!/usr/bin/env -S scala-cli shebang -S 3

// Utility script to force the reconstruct of a podman machine

//> using toolkit latest
//> using dep "com.lihaoyi::pprint::0.8.1"
//> using file "../../common/core.sc"
//> using file "../podman.sc"

import os.*
import core.*
import core.given
import util.*
import util.chaining.scalaUtilChainingOps
import scala.annotation.tailrec
import pprint.*
import podman.*

given Array[String] = args

case class PodmanForceReconstructArgs(
  machineName: Option[String],
):
  require(true, "some characteristic needs to be tested!")

val podmanForceReconstructArgs = Try {
  PodmanForceReconstructArgs(
    machineName = arg(0, Option.empty[String]),
  )
} match
  case Success(args) => args
  case Failure(e) =>
    throw new Exception(
      """Invalid arguments.
        |  Usage:   podmanForceReconstruct [<machineName>]
        |  Example: podmanForceReconstruct my-machine
        |    This will force the reconstruct of a machine called my-machine
        |    meaning, if it is stopped, it will start, if it is started,
        |    it will stop and start and if some other machine is started,
        |    that will be stopped before my-machine is started as with
        |    podman there can be only one vm started at a time.
        |    Cherry on top? If the machine doesn't exists, it will be
        |    created and started.
        |  Example: podmanForceReconstruct
        |    Forces the reconstruct of the default machine
        |  """.stripMargin,
      e,
    )
import podmanForceReconstructArgs.*
podman.machine.forceReconstruct(machineName)
