package com.michalplachta.shoesorter

import akka.actor.{Actor, Props}
import akka.cluster.sharding.ShardRegion
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import com.michalplachta.shoesorter.Messages._


