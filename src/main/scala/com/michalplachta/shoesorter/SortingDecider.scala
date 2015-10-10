package com.michalplachta.shoesorter

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.sharding.ShardRegion
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}

import scala.util.Try

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

class SortingDecider extends Actor with ActorLogging {
  def receive: Receive = {
    case WhereShouldIGo(junction, container) => {
      val from = Try {
        Cluster(context.system).selfAddress.hostPort
      }.getOrElse("single-node")

      val targetConveyor = Decisions.whereShouldContainerGo(junction)(container)
      log.info(s"[$from ${context.self.path}}] Container ${container.id} on junction ${junction.id} directed to $targetConveyor")
      sender ! Go(targetConveyor)
    }
  }
}
