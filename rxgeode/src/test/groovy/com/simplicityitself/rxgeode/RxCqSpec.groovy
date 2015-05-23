package com.simplicityitself.rxgeode

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.CacheFactory
import com.gemstone.gemfire.cache.Region
import com.gemstone.gemfire.cache.client.ClientCache
import com.gemstone.gemfire.cache.client.ClientCacheFactory
import com.gemstone.gemfire.cache.client.ClientRegionShortcut
import com.gemstone.gemfire.cache.query.CqEvent
import reactor.rx.Stream
import reactor.rx.Streams
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.security.SecureRandom

class RxCqSpec extends Specification {


  @AutoCleanup("close")
  ClientCache cache = geodeClientCache()

  def "can open a GeodeRxCq on a client region and receive streaming updates from server cache"() {

    given:"A place to store data"
    def data = []
    Region region = cache.getRegion("TradeOrder")

    region.put("123", [
        "name":"somename",
        "price": 120.0d
    ])

    and: "A running reactive stream based continuous query"

    SecureRandom rand = new SecureRandom()

    Stream<CqEvent> str = Streams.wrap(new GeodeRxCq(
        cache,
        "geodeRxCq+${rand.nextLong()}",
        """SELECT * FROM /TradeOrder t WHERE t.get('price') > 90"""
    ))

    str.observeError(Exception) { Object o, Exception e ->
      println "Seen error!"
      e.printStackTrace()
    }.map {
      it.newValue.extraField = "convertedByReactor"
      it.newValue
    }.consume {
      data << it
    }

    when: "Data is put in the region"

    println "Loading data now"
    region.put("124", [
        "name":"mynamehello",
        "price": 80
    ])
    region.put("125", [
        "name":"somename",
        "price": 145
    ])
    region.put("126", [
        "name":"hellomyname",
        "price": 99
    ])

    then: "Data passed through the Reactor FRP stream"
    new PollingConditions(timeout:1).eventually {
      data.size() == 2
      data[1].extraField == "convertedByReactor"
    }
  }

  static def geodeClientCache() {
    def cf = new ClientCacheFactory()
        .setPoolSubscriptionEnabled(true)
        .addPoolLocator("localhost", 41111)
        .set("log-level", "warning")
        .create();

    cf.createClientRegionFactory(ClientRegionShortcut.PROXY).create("TradeOrder")

    cf
  }

//
//  static def geode() {
//    def cf = new CacheFactory()
//        .set("start-locator", "localhost[55221]")
//        .set("mcast-port", "0")
//        .set("log-level", "error")
//        .create();
//
//    cf.createRegionFactory()
//        .create("TradeOrder")
//
//    cf
//  }

}
