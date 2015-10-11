
package core.secToken

import java.security.PrivateKey
import java.security.cert.Certificate
import scala.collection.mutable.ListBuffer
import java.util.ResourceBundle

import au.gov.sbr.core.requester.model.SecurityTokenDetails
import au.gov.abr.securitytokenmanager.SecurityTokenManagerClient
import au.gov.abr.securitytokenmanager.STSClient
import au.gov.abr.securitytokenmanager.VANguardSTSClient
import au.gov.abr.securitytokenmanager.SecurityToken
import au.gov.abr.securitytokenmanager.exceptions.STMException

//import scala.io.Source.fromURL


/**
 * Class used to obtain a token from the STS.
 */
class SecToken {

	/**
	 * Get a security token from Vanguard to use for secure authentication and communication
	 * with the relevant core web service.
	 * @param endpointUrl
	 * @param stsEndpoint
	 * @param certificateChain
	 * @param privateKey
	 * @return SecurityToken
	 * @throws AuthenticationException
	 */
	def getSecurityToken(
    endpointUrl:String , 
    stsEndpoint:String , 
    certificateChain:Array[Certificate] , 
    privateKey:PrivateKey 
  )
  :SecurityToken =

	{
		val im:SecurityTokenManagerClient  = new SecurityTokenManagerClient()
		val sts:STSClient  = im.getImplementingClass()
		if (sts.isInstanceOf[VANguardSTSClient]) {
			var vgClient:VANguardSTSClient  = sts.asInstanceOf[VANguardSTSClient]

			vgClient.setEndpoint(stsEndpoint)
			vgClient.setKeySize("512")
			vgClient.setTTL("30")
    play.Logger.info(stsEndpoint)
		}

		val resources:ResourceBundle  = ResourceBundle.getBundle("SecToken")

		val counter:Int  = 1
		val claimsCount:Int  = Integer.parseInt(resources.getString("claims.count"))
		var claimList  = new ListBuffer[String]()


		for (counter <- 1 to claimsCount) {
			claimList += resources.getString("claim" + counter + ".url") 
		}


		val claims:String  = claimList.mkString(";")
		//for (claim:String <- claimList)
      //claims + claim + ""
    if (im != null) 
      im.getSecurityIdentityToken(endpointUrl, claims, privateKey, certificateChain) 
    else 
      null
	}

	def getSecurityTokenDetails(token:SecurityToken):SecurityTokenDetails = {
		if (token == null)
			throw new IllegalArgumentException("Security Token must not be null.")

		return new SecurityTokenDetails(
      token.getAssertionAsXML(), token.getProofTokenAsString(), token.getExpiryTime())		
	}
}
