package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import com.michalplachta.shoesorter.DecidersGuardian
import com.typesafe.config.ConfigFactory

object SingleNodeApp extends App {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem(config getString "application.name")

  val decider = system.actorOf(Props[DecidersGuardian])
  system.actorOf(Props(classOf[RestInterface], decider, 8080))
}
