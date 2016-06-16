package com.michalplachta.shoesorter

import akka.actor.{ActorLogging, Actor, Props}
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.{ExtractEntityId, ExtractShardId}
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import com.michalplachta.shoesorter.Messages._

object SortingDecider {
  def name = "sortingDecider"

  def props = Props[SortingDecider]

  def extractShardId: ExtractShardId = {
    case WhereShouldIGo(junction, _) =>
      (junction.id % 2).toString
  }

  def extractEntityId: ExtractEntityId = {
    case msg @ WhereShouldIGo(junction, _) =>
      (junction.id.toString, msg)
  }
}

class SortingDecider extends Actor with ActorLogging {
  def receive = {
    case WhereShouldIGo(junction, container) =>
      val decision = Decisions.whereShouldContainerGo(junction, container)
      log.info("Decision on junction {} for container {}: {}", junction.id, container.id, decision)
      sender ! Go(decision)
  }
}
