package com.michalplachta.shoesorter

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.sharding.ShardRegion
import spray.json.DefaultJsonProtocol._

import scala.util.Try

/**
 * @author michal.plachta
 */
case class Junction(id: Int)

case class Container(id: Int)

case class WhereShouldIGo(junction: Junction, container: Container)

case class Go(targetConveyor: String)

object Go {
  implicit val goJson = jsonFormat1(Go.apply)
}

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
  var myJunction: Junction = _

  def receive: Receive = {
    case WhereShouldIGo(junction, container) => {
      myJunction = junction
      val from = Try {
        Cluster(context.system).selfAddress.hostPort
      }.getOrElse("single-node")

      val targetConveyor = makeDecision(container)
      log.info(s"[$from ${context.self.path}}] Container ${container.id} on junction ${junction.id} directed to ${targetConveyor}")
      sender ! Go(targetConveyor)
    }
  }

  def makeDecision(container: Container): String = {
    Thread.sleep(1)
    val seed = util.Random.nextInt(10000)
    return s"CVR_${myJunction.id}_${seed % 2 + 1}"
  }
}
