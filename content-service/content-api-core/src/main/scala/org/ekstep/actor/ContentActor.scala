package org.ekstep.actor

import org.ekstep.actor.core.BaseAPIActor
import org.ekstep.common.dto.Response
import org.ekstep.commons.{APIIds, Request}
import org.ekstep.managers.{ContentDialCodeManagerImpl, ContentManager}
import org.ekstep.mgr.impl.ContentManagerImpl

object ContentActor extends BaseAPIActor {

  override def onReceive(request: Request) = {
    request.apiId match {
      case APIIds.CREATE_CONTENT => sender() ! createContent(request)
      case APIIds.READ_CONTENT => sender() ! readContent(request)
      case APIIds.UPDATE_CONTENT => sender() ! updateContent(request)
      case APIIds.REVIEW_CONTENT => sender() ! reviewContent(request)
      case APIIds.UPLOAD_CONTENT => sender() ! uploadContent(request)
      case APIIds.PUBLIC_PUBLISH_CONTENT => sender() ! publishContent(request, "public")
      case APIIds.UNLISTED_PUBLISH_CONTENT => sender() ! publishContent(request, "unlisted")
      case APIIds.DIALCODE_LINK => sender() ! linkDialCode(request)
      case APIIds.DIALCODE_COLLECTION_LINK => sender() ! collectionLinkDialCode(request)
      case APIIds.DIALCODE_RESERVE => sender() ! reserveDialCode(request)
      case APIIds.DIALCODE_RELEASE => sender() ! releaseDialCode(request)
      case _ => invalidAPIResponseSerialized(request.apiId)
    }
  }

  private def createContent(request: Request) : Response = {
    try {
        val result = ContentManager.create(request)
        OK(request.apiId, result)
    } catch {
      case e: Exception =>
        getErrorResponse(e, APIIds.CREATE_CONTENT)
    }
  }


  private def readContent(request: Request) : Response  = {
    try{
      val contentMgr = new ContentManagerImpl()
      val result = contentMgr.read(request)
      //val response =
      OK(request.apiId, result)
    } catch {
      case e: Exception =>
        getErrorResponse(e, APIIds.READ_CONTENT)
    }
  }

  private def updateContent(request: Request) : Response  = {
    try{
      val contentMgr = new ContentManagerImpl()
      val result = contentMgr.update(request)
      OK(request.apiId, result)
    } catch {
      case e: Exception => getErrorResponse(e, APIIds.UPDATE_CONTENT)
    }
  }

  private def reviewContent(request: Request) : Response  = {
    try{
      val result = ContentManager.review(request)
      OK(request.apiId, result)
    } catch {
      case e: Exception => getErrorResponse(e, APIIds.REVIEW_CONTENT)
    }

  }

  /**
    * Link DIAL Code to Neo4j Object
    *
    * @param request
    * @return
    */
  private def linkDialCode(request: Request) = {
    try {
      val result = ContentDialCodeManagerImpl.linkDialCode(request)
      OK(request.apiId, result)
    } catch {
      case e: Exception => getErrorResponse(e, APIIds.DIALCODE_LINK)
    }
  }

  /**
    * Link DIAL Code to Collection Objects
    *
    * @param request
    * @return
    */
  private def collectionLinkDialCode(request: Request) = {
    try {
      val result = ContentDialCodeManagerImpl.collectionLinkDialCode(request)
      OK(request.apiId, result)
    } catch {
      case e: Exception => getErrorResponse(e, APIIds.DIALCODE_COLLECTION_LINK)
    }
  }

  /**
    * Reserve DIAL Codes for Textbook
    *
    * @param request
    * @return
    */
  private def reserveDialCode(request: Request) = {
    try {
      val result = ContentDialCodeManagerImpl.reserveDialCode(request)
      OK(request.apiId, result)
    } catch {
      case e: Exception => getErrorResponse(e, APIIds.DIALCODE_RESERVE)
    }
  }

  /**
    * Release DIAL Codes for Textbook
    *
    * @param request
    * @return
    */
  private def releaseDialCode(request: Request) = {
    try {
      val result = ContentDialCodeManagerImpl.releaseDialCode(request)
      OK(request.apiId, result)
    } catch {
      case e: Exception => getErrorResponse(e, APIIds.DIALCODE_RELEASE)
    }
  }

  private def publishContent(request: Request, publishType: String) : Response  = {
    val apiId = request.apiId
    try {
      val result = ContentManager.publishByType(request, publishType)
      OK(request.apiId, result)
    } catch {
      case e: Exception => getErrorResponse(e, apiId)
    }
  }

  private def uploadContent(request: Request) : Response  = {
    val contentMgr = new ContentManagerImpl()
    val fileUrl = request.params.getOrElse("fileUrl","")
    val result = contentMgr.uploadUrl(request)
    OK(request.apiId, result)
  }


}
