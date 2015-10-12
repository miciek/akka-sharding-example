package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import com.michalplachta.shoesorter.DecidersGuardian
import com.typesafe.config.ConfigFactory

object SingleNodeApp extends App {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem(config getString "application.name")

  val decidersGuardian = system.actorOf(Props[DecidersGuardian])
  system.actorOf(RestInterface.props(decidersGuardian, config getInt "application.exposed-port"))
}
