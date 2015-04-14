package com.simplicityitself.geodedocument

import com.simplicityitself.geodedocument.store.GemfireConfiguration
import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

/**
 * Boots a standalone document server.
 * Expects to connect to a running gemfire cluster.
 */
@SpringBootApplication
@Import([
    GemfireConfiguration
])
@Slf4j
class Standalone {
  static void main(String[] args) {
    SpringApplication.run Standalone, args

  }
}
