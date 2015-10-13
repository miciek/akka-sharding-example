package com.michalplachta.shoesorter

import akka.actor.{Props, ActorRef, Actor}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.michalplachta.shoesorter.Messages._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

