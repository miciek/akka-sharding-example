package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import com.michalplachta.shoesorter.DecidersGuardian
import com.typesafe.config.ConfigFactory

object SingleNodeApp extends App {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem(config getString "application.name")

  val decider = system.actorOf(Props(new DecidersGuardian))
  system.actorOf(Props(new RestInterface(decider, 8080)))
}
