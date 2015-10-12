package com.michalplachta.shoesorter

import akka.actor.{Props, ActorRef, Actor}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class DecidersGuardian extends Actor {
  def receive = {
    case m: WhereShouldIGo =>
      val deciderName = s"J${m.junction.id}"
      val decider = context.child(deciderName) getOrElse context.actorOf(SortingDecider.props)
      decider forward m
  }
}

