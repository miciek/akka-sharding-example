package com.michalplachta.shoesorter
import com.michalplachta.shoesorter.Domain.{Container, Junction}

object Decisions {
  // TODO: make it long for junction, and very quick for container
  def whereShouldContainerGo(junction: Junction)(container: Container): String = {
    Thread.sleep(10)
    val seed = util.Random.nextInt(10000)
    s"CVR_${junction.id}_$seed"
  }
}
