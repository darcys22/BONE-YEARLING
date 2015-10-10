package au.gov.sbr.core.test

/**
 * Interface to hold constant values required to retrieve a security token
 * @author SBR
 */
object DefaultConstants {

    /** Keystore filename default value */
    val KS_FILENAME:String  = "keystore/KeyStore.xml"

    /** Credential alias name default value */
    val KEY_ALIAS:String  = "ABRP:17088268884_22140140"

    /** Password default value */
    val KEY_PASSWORD:String  = "Password1!"

    /** Organisation name */
    val ORGANISATION_NAME:String  = "Darcy Financial"

    /** Software name */
    val SOFTWARE_NAME:String  = "DF financial suite"

    /** Version number */
    val VERSION_NUMBER:String  = "0.0.0.1"

    /** VANguard Endpoint */
    val STS_ENDPOINT:String  = "https://thirdparty.authentication.business.gov.au/r3.0/vanguard/S007v1.2/Service.svc"

    /** Core Services Endpoint */
    val CORE_ENDPOINT:String  = "https://test.sbr.gov.au/services/"

    /** VANguard default values */
    val VANGUARD_EP_PROPS:String  = "au.gov.abr.securitytokenmanager.VANguardSTSClient.properties"

    /** STM default values */
    val STM_PROPS:String  = "au.gov.abr.securitytokenmanager.properties"    
}
