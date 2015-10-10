package com.michalplachta.shoesorter.api

import akka.actor.{ActorRefFactory, Actor, Props, ActorRef}
import com.michalplachta.shoesorter.Decisions
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}
import spray.routing.{Route, RejectionHandler, HttpService, ExceptionHandler}
import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}
import akka.pattern.ask
import akka.io.IO
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.can.Http
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object SprayRestInterface {
  def props(port: Int, decider: ActorRef) = Props(new SprayRestInterface(port, decider))
}

class SprayRestInterface(exposedPort: Int, decider: ActorRef) extends Actor with HttpService {
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
      path("junctions" / IntNumber / "decisionForContainer" / IntNumber) { (junctionId, containerId) =>
        get {
          val decision = Future {
            Go(Decisions.whereShouldContainerGo(Junction(junctionId))(Container(containerId)))
          }
          complete(decision)
        }
      }
    }
  }

  def receive = runRoute(routes)

  IO(Http) ! Http.Bind(self, interface = "0.0.0.0", port = exposedPort)
}
