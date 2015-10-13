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

