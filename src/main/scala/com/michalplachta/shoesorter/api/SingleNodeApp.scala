package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import com.michalplachta.shoesorter.DecidersGuardian
import com.typesafe.config.ConfigFactory

object SingleNodeApp extends App {
  val config = ConfigFactory.load()

  implicit val system = ActorSystem(config getString "application.name")
  sys.addShutdownHook(system.terminate())

  val decidersGuardian = system.actorOf(DecidersGuardian.props)
  RestInterface.bind(decidersGuardian, config getInt "application.exposed-port")
}
