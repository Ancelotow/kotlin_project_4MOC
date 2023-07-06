import android.util.Log
import com.oye.moviepedia.data.dto.AuthDto

object SessionManager {
    private var auth: AuthDto = AuthDto(false)

    fun isLoggedIn(): Boolean {
        return this.auth.account_id != null
    }

    fun login(authData: AuthDto) {
        this.auth.account_id = authData.account_id
        this.auth.access_token = authData.access_token
        this.auth.success = authData.success
    }

    fun logout() {
        this.auth.account_id  = null
        this.auth.access_token = null
        this.auth.success = false
    }

    fun getAuth(): AuthDto {
        return auth
    }
}
