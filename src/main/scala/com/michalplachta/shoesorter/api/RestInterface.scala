package com.michalplachta.shoesorter.api

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.michalplachta.shoesorter.{Container, Go, Junction, WhereShouldIGo}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object RestInterface {
  def bind(decider: ActorRef, exposedPort: Int)(implicit system: ActorSystem) {
    implicit val timeout = Timeout(5 seconds)
    implicit val materializer = ActorMaterializer()

    val route =
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

    Http().bindAndHandle(route, "0.0.0.0", exposedPort)
  }
}
