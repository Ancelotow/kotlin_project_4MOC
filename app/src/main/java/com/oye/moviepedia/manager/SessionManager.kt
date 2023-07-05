object SessionManager {
    private var accountId: String? = null

    fun isLoggedIn(): Boolean {
        return accountId != null
    }

    fun login(accountId: String) {
        this.accountId = accountId
    }

    fun logout() {
        this.accountId = null
    }

    fun getAccountId(): String? {
        return accountId
    }
}
