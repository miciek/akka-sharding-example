package com.michalplachta.shoesorter

import akka.actor.{Props, ActorRef, Actor}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.michalplachta.shoesorter.Messages._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class DecidersGuardian extends Actor {
  def receive = {
    case msg: WhereShouldIGo =>
      val name = s"J${msg.junction.id}"
      val worker = context.child(name) getOrElse createChild(name)
      worker forward msg
  }

  private def createChild(name: String) =
    context.actorOf(Props(new SortingDecider), name)
}
