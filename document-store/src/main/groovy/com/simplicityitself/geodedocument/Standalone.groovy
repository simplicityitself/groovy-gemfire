package com.simplicityitself.geodedocument

import com.simplicityitself.geodedocument.store.DocumentStore
import com.simplicityitself.geodedocument.store.GeodeConfiguration
import groovy.util.logging.Slf4j
import io.muoncore.Muon
import io.muoncore.extension.amqp.AmqpTransportExtension
import io.muoncore.extension.amqp.discovery.AmqpDiscovery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

import javax.annotation.PostConstruct

/**
 * Boots a standalone document server.
 * Expects to connect to a running geode cluster.
 */
@SpringBootApplication
@Import([
    GeodeConfiguration
])
@Slf4j
class Standalone {

  @Value('${muon.rabbitmq.url}')
  String rabbitUrl

  @Autowired
  DocumentStore store

  @PostConstruct
  void populate() {
    store.putById("awesome", "simple", [
        name:"The Awesome",
        simple:"Simples!!"
    ])
    log.info "Loading For the Awesome!"
  }

  static void main(String[] args) {
    SpringApplication.run Standalone, args

    println "DONE MY AWESOME!"
  }

  @Bean Muon muon() {

    def discovery = new AmqpDiscovery(rabbitUrl)

    def muon = new Muon(discovery)

    muon.setServiceIdentifer("geodoc")
    new AmqpTransportExtension(rabbitUrl).extend(muon)

    //TODO, richer codec support, more transports, better discovery

    muon.start()

    return muon
  }
}
