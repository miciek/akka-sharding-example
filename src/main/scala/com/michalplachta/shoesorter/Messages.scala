package com.michalplachta.shoesorter

import com.michalplachta.shoesorter.Domain.{Container, Junction}
import spray.json.DefaultJsonProtocol._

object Messages {
  case class Go(targetConveyor: String)

  object Go {
    implicit val goJson = jsonFormat1(Go.apply)
  }
}
