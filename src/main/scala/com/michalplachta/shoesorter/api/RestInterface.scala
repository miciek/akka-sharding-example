package com.michalplachta.shoesorter.api

import akka.actor.{Actor, ActorRef, Props}
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
  def props(decider: ActorRef, port: Int) = Props(new RestInterface(decider, port))
}

class RestInterface(decider: ActorRef, exposedPort: Int) extends Actor with HttpServiceBase {
  implicit val system = context.system
  implicit val timeout = Timeout(5 seconds)

  val route: Route =
    path("junctions" / IntNumber / "decisionForContainer" / IntNumber) { (junctionId, containerId) =>
      get {
        complete {
          decider.ask(
            WhereShouldIGo(Junction(junctionId), Container(containerId))
          ).mapTo[Go]
        }
      }
    }

  def receive = runRoute(route)

  IO(Http) ! Http.Bind(self, interface = "0.0.0.0", port = exposedPort)
}
