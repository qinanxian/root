package com.vekai.auth.event;


import java.util.List;

public interface AuthzCfgModifiedEvent {

    class RolePermissionModifiedEvent implements AuthzCfgModifiedEvent {
        private final String role;

        public RolePermissionModifiedEvent(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    class UserAttachRoleEvent implements AuthzCfgModifiedEvent {
        private final String userId;
        private final List<String> roles;

        public UserAttachRoleEvent(String userId, List<String> roles) {
            this.userId = userId;
            this.roles = roles;
        }

        public String getUserId() {
            return userId;
        }
        public List<String> getRoles() {
            return roles;
        }
    }

    class UserRelieveRoleEvent implements AuthzCfgModifiedEvent {
        private final String userId;
        private final String role;

        public UserRelieveRoleEvent(String userId, String role) {
            this.userId = userId;
            this.role = role;
        }

        public String getUserId() {
            return userId;
        }
        public String getRole() {
            return role;
        }
    }

}