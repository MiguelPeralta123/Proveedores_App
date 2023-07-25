package drawable.io.response

import com.learning.proveedoresapp.model.User

data class LoginResponse(
    val success: Boolean,
    val user: User,
    val jwt: String
)