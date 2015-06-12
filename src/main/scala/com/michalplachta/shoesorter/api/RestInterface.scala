package com.michalplachta.shoesorter.api

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}
import akka.pattern.ask
import akka.io.IO
import akka.util.Timeout
import com.michalplachta.shoesorter.{Junction, Container, WhereShouldIGo, Go}
import com.typesafe.config.ConfigFactory
import spray.can.Http
import spray.http.StatusCodes
import spray.routing._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author michal.plachta
 */
object RestInterface {
  def props(port: Int, decider: ActorRef) = Props(new RestInterface(port, decider))
}

class RestInterface(exposedPort: Int, decider: ActorRef) extends Actor with HttpService {
  val config = ConfigFactory.load()
  implicit val system = context.system
  implicit def actorRefFactory: ActorRefFactory = context

  implicit val timeout = Timeout(5 seconds)
  implicit val exceptionHandler = ExceptionHandler {
    case _ =>
      complete(StatusCodes.BadRequest, "Bad Request")
  }

  implicit val rejectionHandler = RejectionHandler {
    case x :: _ =>
      complete(StatusCodes.BadRequest, x.toString)
  }

  val routes: Route = handleExceptions(exceptionHandler) {
    handleRejections(rejectionHandler) {
      get {
        path("decisions" / IntNumber / IntNumber) { (junctionId, containerId) =>
          complete {
            decider
              .ask(WhereShouldIGo(
                    Junction(junctionId),
                    Container(containerId)))
              .mapTo[Go]
          }
        }
      }
    }
  }

  def receive = runRoute(routes)

  IO(Http) ! Http.Bind(self, interface = "0.0.0.0", port = exposedPort)
}
