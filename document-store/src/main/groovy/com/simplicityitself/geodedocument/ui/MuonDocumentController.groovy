package com.simplicityitself.geodedocument.ui

import com.simplicityitself.geodedocument.store.DocumentStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Component
//@Profile("muon")
class MuonDocumentController {

  @Autowired
  DocumentStore documentStore

  public Map query(
      String region,
      String q) {
    return documentStore.query(region, q)
  }

  public Map byId(
      String docId,
      String region) {
    return documentStore.findById(region, docId)
  }

}
