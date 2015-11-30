package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
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

  val decidersGuardian = ClusterSharding(system).shardRegion(SortingDecider.shardName)
  system.actorOf(Props(new RestInterface(decidersGuardian, config getInt "application.exposed-port")))
}
