package com.michalplachta.shoesorter

object Domain {
  case class Junction(id: Int) // TODO: decide whether it should be called Split

  case class Container(id: Int)

  // TODO: add Conveyor?
}
