package com.michalplachta.shoesorter.api

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.michalplachta.shoesorter.Decisions
import com.michalplachta.shoesorter.Domain.{Junction, Container}
import com.michalplachta.shoesorter.Messages._
import spray.can.Http
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class RestInterface(decider: ActorRef, exposedPort: Int) extends Actor with HttpServiceBase with ActorLogging {
  val route: Route = {
    path("junctions" / IntNumber / "decisionForContainer" / IntNumber) { (junctionId, containerId) =>
      get {
        complete {
          log.info(s"Request for junction $junctionId and container $containerId")
          val junction = Junction(junctionId)
          val container = Container(containerId)
          decider.ask(WhereShouldIGo(junction, container))(5 seconds).mapTo[Go]
        }
      }
    }
  }

  def receive = runRoute(route)

  implicit val system = context.system
  IO(Http) ! Http.Bind(self, interface = "0.0.0.0", port = exposedPort)
}
