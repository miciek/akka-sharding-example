package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.michalplachta.shoesorter.SortingDecider
import com.typesafe.config.ConfigFactory

object ShardedApp extends App {
  val config = ConfigFactory.load("sharded")
  implicit val system = ActorSystem(config getString "clustering.cluster.name", config)

  ClusterSharding(system).start(
    typeName = SortingDecider.shardName,
    entityProps = SortingDecider.props,
    settings = ClusterShardingSettings(system),
    extractEntityId = SortingDecider.extractEntityId,
    extractShardId = SortingDecider.extractShardId
  )

  val decider = ClusterSharding(system).shardRegion(SortingDecider.shardName)
  system.actorOf(RestInterface.props(decider, config getInt "application.exposed-port"))
}
