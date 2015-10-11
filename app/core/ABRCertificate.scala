
package core.ABRCertificate

import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.security.PrivateKey

import au.gov.abr.akm.credential.store.ABRCredential
import au.gov.abr.akm.credential.store.ABRKeyStore
import core.util.Util
import core.defaultConstants._
import core.keystoreException._

class ABRCertificate {

	var certificateChain: Array[X509Certificate] = _
	var privateKey: PrivateKey = _

  private val ksFilename: String = DefaultConstants.KS_FILENAME
  private val keyAlias: String  = DefaultConstants.KEY_ALIAS
  private val keyPassword: String = DefaultConstants.KEY_PASSWORD

  private val util:Util = new Util()
      
  try 
  {
    val keyStore: ABRKeyStore = util.getKeystore(ksFilename)

    val credential: ABRCredential  = util.getCredential(keyStore, keyAlias, keyPassword)
    certificateChain = credential.getCertificateChain()
    privateKey = credential.getPrivateKey(keyPassword.toCharArray())
  }
  catch 
  {
    case e:Exception => {
      e.printStackTrace()
      throw new ABRKeyStoreException()
    }
  }

	def getCertificateChain(): Array[Certificate] = {
    return certificateChain.toArray
	}

	def getPrivateKey(): PrivateKey = {
		return privateKey
	}
}
