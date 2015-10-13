
package core.tfnd

import au.gov.sbr.comn.sbdm_02.MessageEventItemType
import au.gov.sbr.core.requester.RequestInterface
import au.gov.sbr.core.requester.ResponseInterface
import au.gov.sbr.core.requester.exceptions.SBRCoreException
import au.gov.sbr.core.requester.exceptions.SBRException
import au.gov.sbr.core.requester.exceptions.SBRTimeoutException
import au.gov.sbr.core.requester.model.SBRCoreServicesRequestFactory
import au.gov.sbr.core.requester.model.SecurityTokenDetails
import au.gov.sbr.core.services.ServiceType
import au.gov.abr.securitytokenmanager.exceptions.STMException
import au.gov.abr.securitytokenmanager.SecurityToken
import au.gov.sbr.xml.XMLUtils

import java.io.File
import java.io.FileInputStream
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.security.PrivateKey
import java.util.Date

import scala.collection.JavaConverters._

import org.w3c.dom.Document

import core.defaultConstants._
import core.util._
import core.ABRCertificate._
import core.keystoreException._
import core.secToken._

object ATOTFND {

	private val AGENCY: String = "ato.gov.au"
	private val SERVICE_TYPE: ServiceType = ServiceType.PRELODGE
	private val MESSAGE_TYPE: String = "tfnd.0002.prelodge.request"
	private val SERVICE_ENDPOINT: String = "https://test.sbr.gov.au/services/prelodge.02.service"
	private val DOCUMENT: String = "CONF-ATO-TFND-005_Prelodge_Request.xml"

	//private val MESSAGE_TYPE: String = "message.ping"
	//private val SERVICE_TYPE: ServiceType = ServiceType.LODGE
	//private val SERVICE_TYPE: ServiceType = ServiceType.LIST
	//private val SERVICE_ENDPOINT: String = "https://test.sbr.gov.au/services/list.02.service"
	//private val AGENCY: String = "osr.nsw.gov.au"

	def run() 
  {
		doSBRRequest()
	}

	private def doSBRRequest() {

		if (DefaultConstants.ORGANISATION_NAME == null || 
				DefaultConstants.ORGANISATION_NAME.trim().equals(""))
		{
			System.err.println("Organisation name must be set. Check DefaultConstants.")
		}

    else if (DefaultConstants.SOFTWARE_NAME == null || 
				DefaultConstants.SOFTWARE_NAME.trim().equals(""))
		{
			System.err.println("Software name must be set. Check DefaultConstants.")
			return
		}

		if (DefaultConstants.VERSION_NUMBER == null || 
				DefaultConstants.VERSION_NUMBER.trim().equals(""))
		{
			System.err.println("Version number must be set. Check DefaultConstants.")
			return
		}	

		if(System.getProperty(DefaultConstants.VANGUARD_EP_PROPS) == null) {
			System.setProperty(DefaultConstants.VANGUARD_EP_PROPS, "conf/sts.properties")
		}

		if (System.getProperty(DefaultConstants.STM_PROPS) == null) {
			System.setProperty(DefaultConstants.STM_PROPS, "conf/securitytokenmanager.properties")
		}
		
		var factory: SBRCoreServicesRequestFactory = new SBRCoreServicesRequestFactory(
				DefaultConstants.ORGANISATION_NAME, DefaultConstants.SOFTWARE_NAME,
				DefaultConstants.VERSION_NUMBER)

    try
    {
			val abr: ABRCertificate = new ABRCertificate()
			val certificateChain: Array[Certificate] = abr.getCertificateChain()
			val privateKey: PrivateKey = abr.getPrivateKey()
			
			factory.setBusinessCertificate(certificateChain, privateKey)
      factory.setDefaultLogging(true)
      factory.setDefaultSoftwareSubscriptionId("1111111111");

			var request: RequestInterface = factory.createRequest(SERVICE_TYPE, AGENCY, MESSAGE_TYPE, SERVICE_ENDPOINT)
			request.addBusinessDocument(getDocument().getDocumentElement(), new Date())

			val security: SecToken = new SecToken()

			val token: SecurityToken = 
        security.getSecurityToken(DefaultConstants.CORE_ENDPOINT, DefaultConstants.STS_ENDPOINT, certificateChain, privateKey)

			val details: SecurityTokenDetails = 
        new SecurityTokenDetails(token.getAssertionAsXML(), token.getProofTokenAsString(), token.getExpiryTime())
			request.setSecurityTokenDetails(details)			
			
			val response: ResponseInterface = request.getResponse()
			if (response != null)
			{
				System.out.println(response.getMessageType() + " received from " + response.getSender() + System.getProperty("line.separator"))
				if (response.getMessageEvent() != null && response.getMessageEvent().getMessageEventItems() != null &&
						response.getMessageEvent().getMessageEventItems().getMessageEventItem() != null)
				{
					System.out.println("Message response item details")
					for (meit <- response.getMessageEvent().getMessageEventItems().getMessageEventItem().asScala)
					{
						System.out.println("Code: " + meit.getMessageEventItemErrorCode())
						System.out.println(meit.getMessageEventItemDetailedDescription())
					}
				}
			} else {
				System.out.println("No successful response received from agency.")
      }
    }
    catch {
      case abrkse: ABRKeyStoreException => {}
      case se: STMException => {
        //
        //There are other exceptions thrown by the ABR SecurityTokenManager
        //such as STMFaultException, STMReceiverException, STMSenderException
        //however to keep the example clean, only the superclass of all 
        //STMExceptions is caught here
        //
        se.printStackTrace()
      }
      case sbrce: SBRCoreException => {
        sbrce.printStackTrace()
      }
      case sbrtoe: SBRTimeoutException => {
        sbrtoe.printStackTrace()
      }
      case sbre: SBRException => {
        sbre.printStackTrace()
      }
    }
  }

	private def getDocument(): Document = {
		var xml: String = null
		try
		{
			val f: File = new File(DOCUMENT)
			var fis: FileInputStream = new FileInputStream(f)
			var data: Array[Byte] = new Array[Byte](f.length().toInt)
			fis.read(data)
			fis.close()
			xml = new String(data)
		}
		catch 
    {
      case e:Exception => {
        e.printStackTrace()
      }
    }

		return XMLUtils.loadDocumentFromString(xml, false)
	}
}
