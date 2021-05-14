package com.vekai.auth.authorization;

public interface AfterAuthorizationListener {
    void afterAuthValidate(AuthValidateEvent event);

    class AuthValidateEvent {
        private boolean authPass;

        public AuthValidateEvent(boolean authPass) {
            this.authPass = authPass;
        }
        public boolean isAuthPass() {
            return authPass;
        }
        public void setAuthPass(boolean authPass) {
            this.authPass = authPass;
        }
    }
}