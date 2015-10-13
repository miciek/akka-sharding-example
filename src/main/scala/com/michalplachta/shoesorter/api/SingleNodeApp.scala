package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import com.michalplachta.shoesorter.SortingDecider
import com.typesafe.config.ConfigFactory

object SingleNodeApp extends App {
  implicit val system = ActorSystem("sorter")

  val decider = system.actorOf(Props[SortingDecider])
  system.actorOf(Props(new RestInterface(decider, 8080)))
}
