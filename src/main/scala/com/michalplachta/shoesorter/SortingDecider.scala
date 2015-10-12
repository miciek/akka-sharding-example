package com.michalplachta.shoesorter

import akka.actor.{Actor, Props}
import akka.cluster.sharding.ShardRegion
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}

// TODO: Rename to JunctionDecider
object SortingDecider {
  val props = Props[SortingDecider]

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case m: WhereShouldIGo => (m.junction.id.toString, m)
  }

  val extractShardId: ShardRegion.ExtractShardId = {
    case WhereShouldIGo(junction, _) => (junction.id % 2).toString
  }

  val shardName = "sortingDecider"
}

class SortingDecider extends Actor {
  def receive: Receive = {
    case WhereShouldIGo(junction, container) => {
      val targetConveyor = Decisions.whereShouldContainerGo(junction, container)
      sender ! Go(targetConveyor)
    }
  }
}
