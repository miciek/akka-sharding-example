package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object SingleNodeApp extends App {
  implicit val system = ActorSystem("sorter")
  system.actorOf(Props(new RestInterface(8080)))
}
