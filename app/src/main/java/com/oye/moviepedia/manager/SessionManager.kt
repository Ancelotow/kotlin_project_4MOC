object SessionManager {
    private var accountId: Int? = null

    fun isLoggedIn(): Boolean {
        return accountId != null
    }

    fun login(accountId: Int) {
        this.accountId = accountId
    }

    fun logout() {
        this.accountId = null
    }

    fun getAccountId(): Int? {
        return accountId
    }
}
