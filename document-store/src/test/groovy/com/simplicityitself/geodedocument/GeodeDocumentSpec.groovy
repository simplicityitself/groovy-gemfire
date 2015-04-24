package com.simplicityitself.geodedocument

import groovy.json.JsonSlurper
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.security.SecureRandom

/**
 * Highest level spec.
 *
 * Stand up an geodedoc instance and
 * backing geode instance
 *
 * Interact via HTTP for great goodness.
 */
@SpringApplicationConfiguration(
    classes = [
        Standalone
    ])
@WebIntegrationTest
class GeodeDocumentSpec extends Specification {

  def "can store JSON in arbitrary regions and return by ID"() {

    def region = "myregion${new SecureRandom().nextInt()}"

    given:


    when:
    def json = new JsonSlurper().parseText(new URL("http://localhost:8080/${region}/awesome").text)

    then:
    json.id == "awesome"
  }

  def "can use OQL to find items"() {
    given:
    //TODO, some data injected into a region

    //TODO, use the muon API (well, need to add one first ..)

    when:
    def json = new JsonSlurper().parseText(new URL("http://localhost:8080/Products?name=simple").text)

    then:
    json.size() == 2
    with(json[0]) {
      name=='simple'
    }
  }

  def "can use groovy aggregation API to find and generate projections"() {
    //have a standalone aggregation system. possibly based on reactor/ rxjava?
  }

  def "can use groovy map reduce via functions"() {
    //doc store should inject a function into the distributed system that allows groovy code
    //passed as text to be evaluated.

    //is this fast enough?

    //then, that can be used to perform query via OQL, then groovy map/ reduce
    //can we distribute the reduce phase too?
  }

  def "receive event push via the muon reactive stream API"() {




  }

}
