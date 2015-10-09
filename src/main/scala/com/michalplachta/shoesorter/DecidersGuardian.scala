package com.michalplachta.shoesorter

import akka.actor.{Props, ActorRef, Actor}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object DecidersGuardian {
  def props = Props[DecidersGuardian]
}

class DecidersGuardian extends Actor {
  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case msg @ WhereShouldIGo(junction, _) =>
      val sortingDecider = getOrCreateChild("J" + junction.id,
                                            SortingDecider.props)
      val futureAnswer = (sortingDecider ? msg).mapTo[Go]
      futureAnswer.pipeTo(sender())
  }

  def getChild(name: String): Option[ActorRef] = context.child(name)

  def getOrCreateChild(name: String, props: Props): ActorRef = {
    getChild(name) getOrElse context.actorOf(props, name)
  }
}
