package com.simplicityitself.geodedocument.ui

import com.simplicityitself.geodedocument.store.DocumentStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class DocumentController {

  @Autowired
  DocumentStore documentStore

  @RequestMapping(value = "/{region}", method=RequestMethod.GET)
  public @ResponseBody Collection query(
      @PathVariable String region,
      @RequestParam Map params) {
    return documentStore.query(region, params)
  }

  @RequestMapping(value = " /{region}/{docId}", method=RequestMethod.GET)
  public @ResponseBody Map byId(
      @PathVariable String docId,
      @PathVariable String region) {
    return documentStore.findById(region, docId)
  }

}
