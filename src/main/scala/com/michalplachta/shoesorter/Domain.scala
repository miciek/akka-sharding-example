package com.michalplachta.shoesorter

object Domain {
  case class Junction(id: Int) // TODO: decide whether it should be called Split or Divert

  case class Container(id: Int)
}
