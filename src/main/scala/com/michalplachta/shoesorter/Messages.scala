package com.michalplachta.shoesorter

import com.michalplachta.shoesorter.Domain.{Container, Junction}
import spray.json.DefaultJsonProtocol._

object Messages {
  case class WhereShouldIGo(junction: Junction, container: Container) // TODO: maybe it should be named differently (it is 'sent' from the RestInterface...)?

  case class Go(targetConveyor: String)

  object Go {
    implicit val goJson = jsonFormat1(Go.apply)
  }
}
