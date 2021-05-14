package com.vekai.auth.event;

public interface AuthcCfgModifiedEvent {
    class UserModifiedEvent {
        protected final String userId;

        public UserModifiedEvent(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }
    }

    class UserPwdModifiedEvent extends UserModifiedEvent {
        protected final String userCode;
        public UserPwdModifiedEvent(String userId, String userCode) {
            super(userId);
            this.userCode = userCode;
        }

        public String getUserCode() {
            return userCode;
        }
    }
}
