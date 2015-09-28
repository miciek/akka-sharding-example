package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import com.michalplachta.shoesorter.DecidersGuardian
import com.typesafe.config.ConfigFactory

/**
 * @author michal.plachta
 */
object SingleNodeApp extends App {
  val config = ConfigFactory.load()

  val system = ActorSystem(config getString "application.name")
  sys.addShutdownHook(system.shutdown())

  val decidersGuardian = system.actorOf(DecidersGuardian.props)
  new RestInterface(config getInt "application.exposed-port", decidersGuardian)
}
