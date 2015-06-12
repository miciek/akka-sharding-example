package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import com.michalplachta.shoesorter.{DecidersGuardian, SortingDecider}
import com.typesafe.config.ConfigFactory

/**
 * @author michal.plachta
 */
object SingleNodeApp extends App {
  val config = ConfigFactory.load()

  val system = ActorSystem(config getString "application.name")
  sys.addShutdownHook(system.shutdown())

  val decidersGuardian = system.actorOf(DecidersGuardian.props)
  system.actorOf(
    RestInterface.props(
      config getInt "application.exposed-port",
      decidersGuardian),
    name = "restInterfaceService")
}
