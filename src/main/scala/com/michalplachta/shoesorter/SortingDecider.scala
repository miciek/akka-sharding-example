package com.michalplachta.shoesorter

import akka.actor.{Actor, Props}
import akka.cluster.sharding.ShardRegion
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}

object SortingDecider {
  def props = Props[SortingDecider]

  def shardName = "sortingDecider"

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case m: WhereShouldIGo =>
      (m.junction.id.toString, m)
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case WhereShouldIGo(junction, _) =>
      (junction.id % 2).toString
  }
}

class SortingDecider extends Actor {
  def receive = {
    case WhereShouldIGo(junction, container) =>
      val decision = Decisions.whereShouldContainerGo(junction, container)
      sender ! Go(decision)
  }
}


