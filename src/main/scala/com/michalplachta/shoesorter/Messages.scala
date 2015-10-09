package com.michalplachta.shoesorter

import spray.json.DefaultJsonProtocol._

object Messages {
  case class Junction(id: Int)

  case class Container(id: Int)

  case class WhereShouldIGo(junction: Junction, container: Container)

  case class Go(targetConveyor: String)

  object Go {
    implicit val goJson = jsonFormat1(Go.apply)
  }
}
