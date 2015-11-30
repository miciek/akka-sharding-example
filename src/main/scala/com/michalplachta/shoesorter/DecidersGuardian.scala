package com.michalplachta.shoesorter

import akka.actor.{Props, ActorRef, Actor}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.michalplachta.shoesorter.Messages._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class DecidersGuardian extends Actor {
  def receive = {
    case m: WhereShouldIGo =>
      val name = s"J${m.junction.id}"
      val actor = context.child(name) getOrElse context.actorOf(Props(new SortingDecider))
      actor forward m
  }
}
