package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.michalplachta.shoesorter.SortingDecider
import com.typesafe.config.ConfigFactory

/**
 * @author michal.plachta
 */
object ShardedApp extends App {
  val defaultConfig = ConfigFactory.load("sharded")

  Seq(2551, 2552) foreach { port =>
    // Override the configuration of the port
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
      withFallback(defaultConfig)

    // Create an Akka system
    val system = ActorSystem(config getString "clustering.cluster.name", config)

    ClusterSharding(system).start(
      typeName = SortingDecider.shardName,
      entityProps = SortingDecider.props,
      settings = ClusterShardingSettings(system),
      extractEntityId = SortingDecider.extractEntityId,
      extractShardId = SortingDecider.extractShardId)

    if (port == 2551) {
      val decider = ClusterSharding(system).shardRegion(SortingDecider.shardName)
      new RestInterface(defaultConfig getInt "application.exposed-port", decider)
    }
  }
}
