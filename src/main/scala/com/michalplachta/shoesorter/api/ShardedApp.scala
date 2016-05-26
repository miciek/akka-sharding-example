package com.michalplachta.shoesorter.api

import akka.actor.{Props, ActorSystem}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.michalplachta.shoesorter.SortingDecider
import com.typesafe.config.ConfigFactory

object ShardedApp extends App {
  val config = ConfigFactory.load("sharded")
  implicit val system = ActorSystem(config getString "application.name", config)

  ClusterSharding(system).start(
    typeName = SortingDecider.name,
    entityProps = SortingDecider.props,
    settings = ClusterShardingSettings(system),
    extractShardId = SortingDecider.extractShardId,
    extractEntityId = SortingDecider.extractEntityId
  )

  val decider = ClusterSharding(system).shardRegion(SortingDecider.name)
  system.actorOf(Props(new RestInterface(decider, config getInt "application.exposed-port")))
}
