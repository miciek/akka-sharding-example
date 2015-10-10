package com.michalplachta.shoesorter.api

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import com.michalplachta.shoesorter.Decisions
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object RestInterface {
  def bind(decider: ActorRef, exposedPort: Int)(implicit system: ActorSystem): Future[ServerBinding] = {
    implicit val timeout = Timeout(5 seconds)

    val route =
      path("junctions" / IntNumber / "decisionForContainer" / IntNumber) { (junctionId, containerId) =>
        get {
          val decision = Future {
            Go(Decisions.whereShouldContainerGo(Junction(junctionId))(Container(containerId)))
          }
          complete(decision)
        }
      }

    implicit val materializer = ActorMaterializer()
    Http().bindAndHandle(route, "0.0.0.0", exposedPort)
  }
}
