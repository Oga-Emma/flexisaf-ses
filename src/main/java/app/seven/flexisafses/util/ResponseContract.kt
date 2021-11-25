//package app.seven.flexisafses.util
//
//import java.io.Serializable
//import java.util.*
//
//data class ResponseContract(
//    val error: Boolean,
//    val message: String?,
//    val errors: HashMap<String, String>?,
//    val data: Any?
//) : Serializable {
//    constructor(error: Boolean, message: String?, errors: HashMap<String, String>) : this(error, message, errors, null)
//    constructor(error: Boolean, message: String?, data: Any?) : this(error, message, null, data)
//    constructor(error: Boolean, message: String?) : this(error, message, null)
//}
