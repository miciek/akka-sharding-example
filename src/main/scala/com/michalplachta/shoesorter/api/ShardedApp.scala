package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import akka.contrib.pattern.ClusterSharding
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
      entryProps = Some(SortingDecider.props),
      idExtractor = SortingDecider.idExtractor,
      shardResolver = SortingDecider.shardResolver)

    if(port == 2551) {
      val decider = ClusterSharding(system).shardRegion(SortingDecider.shardName)
      system.actorOf(
        RestInterface.props(
          defaultConfig getInt "application.exposed-port",
          decider),
        name = "restInterfaceService")
    }
  }
}
