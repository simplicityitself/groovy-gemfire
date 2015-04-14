package com.simplicityitself.geodedocument.store

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.CacheFactoryBean

@ComponentScan
@Configuration
class GemfireConfiguration {

  @Bean
  CacheFactoryBean cacheFactoryBean() {
    //TODO, connect to remote locators if set in config ...
    def factory = new CacheFactoryBean();
    Properties prop = new Properties();
    prop["start-locator"] = "localhost[34567]"
    factory.setProperties(prop)
    factory.pdxReadSerialized=true
    return factory
  }

//  @Bean
//  LocalRegionFactoryBean<String, Map> localRegionFactory(final GemFireCache cache) {
//    return new LocalRegionFactoryBean<String, Map>() {
//
//      {
//        setCache(cache);
//        setName("wordCloud");
//        setClose(true);
//      }
//    };
//  }
}
