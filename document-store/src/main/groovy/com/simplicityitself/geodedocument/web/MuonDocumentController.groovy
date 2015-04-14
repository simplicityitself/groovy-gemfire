package com.simplicityitself.geodedocument.web

import com.simplicityitself.geodedocument.store.DocumentStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class MuonDocumentController {

  @Autowired
  DocumentStore documentStore

  @RequestMapping(value = " /", method=RequestMethod.GET)
  public @ResponseBody Map query(
      @RequestParam(required = true) String q) {
    return [
        "hello":"World",
        query:q
    ]
  }

  @RequestMapping(value = " /{region}/{docId}", method=RequestMethod.GET)
  public @ResponseBody Map byId(
      @PathVariable String docId,
      @PathVariable String region) {
    return documentStore.findById(region, docId)
  }

}
