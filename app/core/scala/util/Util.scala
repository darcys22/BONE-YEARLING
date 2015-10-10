package au.gov.sbr.core.test.util

import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.channels.FileChannel

import au.gov.abr.akm.credential.store.ABRCredential
import au.gov.abr.akm.credential.store.ABRKeyStore
import au.gov.abr.akm.credential.store.ABRProperties
import au.gov.abr.akm.exceptions.ABRUnhandledException
import au.gov.abr.akm.exceptions.KeyStoreLoadException
import au.gov.abr.akm.exceptions.NoSuchAliasException
import au.gov.abr.akm.exceptions.NullReferenceException
import au.gov.abr.akm.exceptions.SDKExpiredException

/**
 * Utility class that can copy files and retrieve ABRKeystores and 
 * credentials that are required in order to obtain a security token.
 * @author SBR
 */
class Util {
	private val ORGANISATION_NAME: String = null//"ATO CSOU"
	private val SOFTWARE_NAME: String = null//"SBRCoreServicesTesting"
	private val VERSION_NUMBER: String = null//"12.34.0.56"
	private val DATE: String = null//"21 April 2015"

	/**
	 * Make a copy of a given file, to a given destination file
	 * 
	 * @param fileToCopy the file to be copied
	 * @param destinationFile the file to be written out to
	 */
	private def copyFile(fileToCopy:File , destinationFile:File )
	{
		if (fileToCopy.isDirectory() || destinationFile.isDirectory()) {
			throw new IllegalArgumentException("Cannot copy directories, only files")
		}

		var fromChannel:FileChannel  = null
		var toChannel:FileChannel  = null
		var origFile:FileInputStream  = null
		var copiedFile:FileOutputStream  = null
		try {
			if (!destinationFile.exists()) {
				destinationFile.getParentFile().mkdirs()
				destinationFile.createNewFile()
			}

			origFile = new FileInputStream(fileToCopy)
			copiedFile = new FileOutputStream(destinationFile)

			fromChannel = origFile.getChannel()
			toChannel = copiedFile.getChannel()

			fromChannel.transferTo(0, origFile.available(), toChannel)
		}
		catch 
    {
      case e:Exception => {
        System.out.println("Couldn't create copy of file")
        e.printStackTrace()
      }
    } finally {
			safeClose(fromChannel, toChannel, origFile, copiedFile)
		}
	}

	/**
	 * Load the ABR keystore 
	 * @param ksFilename Keystore filename
	 * @return ABRKeyStore
	 * @throws IOException
	 * @throws NullReferenceException
	 * @throws SDKExpiredException
	 * @throws KeyStoreLoadException
	 */

	//def getKeystore(String ksFilename): ABRKeyStore throws IOException, 
		//NullReferenceException, SDKExpiredException, KeyStoreLoadException 
	def getKeystore(ksFilename:String): ABRKeyStore = {
    
		//Load the keystore at the given location.
		var ksfileOrig:File = new File(ksFilename).getAbsoluteFile()

    if (!ksfileOrig.exists()) {
      throw new FileNotFoundException(ksfileOrig.getCanonicalPath())
    }

    var ksfile:File = new File(ksfileOrig.getParentFile().getParent() + File.separator + "keystore.xml")
    copyFile(ksfileOrig, ksfile)
    if(!ksfile.exists()) {
      throw new FileNotFoundException(ksfileOrig.getCanonicalPath())
    }

    var keyStore:ABRKeyStore = null
    var stream:InputStream = new FileInputStream(ksfile)
    try {
      ABRProperties.setSoftwareInfo(ORGANISATION_NAME, SOFTWARE_NAME, 
          VERSION_NUMBER, DATE)
      keyStore = new ABRKeyStore(stream)
    } finally {
      safeClose(stream)
    }

    return keyStore
	}

	/**
	 * Get a credential from the keystore.
	 * @param keyStore ABRKeyStore where credential is.
	 * @param keyAlias Alias name of the credential.
	 * @param keyPassword Password to open the credential.
	 * @return ABRCredential
	 * @throws NoSuchAliasException
	 * @throws ABRUnhandledException
	 */
	def getCredential(keyStore:ABRKeyStore , keyAlias:String , keyPassword:String ):ABRCredential = {
		if (keyStore == null || keyAlias == null) {
			throw new IllegalArgumentException("Keystore and alias cannot be null value")
		}

		var credential:ABRCredential = keyStore.getCredential(keyAlias)
        if(credential.isReadyForRenewal()) {
        	credential.renew(keyPassword.toCharArray())
        }

		return keyStore.getCredential(keyAlias)
	}

	private def safeClose(cl:Closeable*) = {
		for (c:Closeable <- cl) {
			if (c != null) {
				try {
					c.close()
				} catch 
        {
          case io:IOException => {}
        }
			}
		}
	}
}
