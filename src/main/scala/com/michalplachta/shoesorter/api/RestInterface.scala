package com.michalplachta.shoesorter.api

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}
import spray.can.Http
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object RestInterface {
  def props(decider: ActorRef, exposedPort: Int) = Props(new RestInterface(decider, exposedPort))
}

class RestInterface(decider: ActorRef, exposedPort: Int) extends Actor with ActorLogging with HttpServiceBase {
  val route: Route = {
    path("junctions" / IntNumber / "decisionForContainer" / IntNumber) { (junctionId, containerId) =>
      get {
        complete {
          log.info(s"Got request on junction $junctionId for container $containerId")

          implicit val timeout = Timeout(5 seconds)
          val junction = Junction(junctionId)
          val container = Container(containerId)
          val futureDecision = decider.ask(WhereShouldIGo(junction, container))
          futureDecision.mapTo[Go]
        }
      }
    }
  }

  def receive = runRoute(route)

  implicit val system = context.system
  IO(Http) ! Http.Bind(self, interface = "0.0.0.0", port = exposedPort)
}
