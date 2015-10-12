package com.michalplachta.shoesorter
import com.michalplachta.shoesorter.Domain.{Container, Junction}

object Decisions {
  def whereShouldContainerGo(junction: Junction, container: Container): String = {
    Thread.sleep(5) // just to simulate resource hunger
    val seed = util.Random.nextInt(10000)
    s"CVR_${junction.id}_$seed"
  }
}
