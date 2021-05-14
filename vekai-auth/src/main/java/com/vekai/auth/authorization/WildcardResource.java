package com.vekai.auth.authorization;


import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;

import java.util.*;

import static com.vekai.auth.AuthConsts.*;

public class WildcardResource implements PermissionResource {

    protected final PermissionResourceType permissionResourceType;
    protected List<Set<String>> parts;

    public WildcardResource(PermissionResourceType permissionResourceType, String resourceCode) {
        this.permissionResourceType = permissionResourceType;
        setParts(resourceCode);
    }

    protected void setParts(String wildcardString) {
        wildcardString = StringUtils.clean(wildcardString);

        if (wildcardString == null || wildcardString.isEmpty()) {
            throw new IllegalArgumentException("Wildcard string cannot be null or empty. Make sure permission strings are properly formatted.");
        }
        wildcardString = wildcardString.toLowerCase();
        if (wildcardString.endsWith(ROOT_PART_TOKEN))
            wildcardString += WILDCARD_TOKEN;
        if (wildcardString.startsWith(ROOT_PART_TOKEN))
            wildcardString = wildcardString.substring(ROOT_PART_TOKEN.length());
        this.parts = new ArrayList<>();



        List<String> parts = CollectionUtils.asList(wildcardString.split(PART_DIVIDER_TOKEN));


        for (String part : parts) {
            Set<String> subparts = CollectionUtils.asSet(part.split(SUBPART_DIVIDER_TOKEN));

            if (subparts.isEmpty()) {
                throw new IllegalArgumentException("Wildcard string cannot contain parts with only dividers. Make sure permission strings are properly formatted.");
            }
            this.parts.add(subparts);
        }

        if (this.parts.isEmpty()) {
            throw new IllegalArgumentException("Wildcard string cannot contain only dividers. Make sure permission strings are properly formatted.");
        }
    }

    @Override
    public List<Set<String>> getParts() {
        return this.parts;
    }

    @Override
    public PermissionResourceType getPermissionResourceType() {
        return permissionResourceType;
    }

    @Override
    public boolean contains(PermissionResource permissionResource) {
        if (permissionResourceType != permissionResource.getPermissionResourceType())
            return false;
        List<Set<String>> otherParts = permissionResource.getParts();

        int i = 0;
        for (Set<String> otherPart : otherParts) {
            // If this permission has less parts than the other permission, everything after the number of parts contained
            // in this permission is automatically implied, so return true
            if (getParts().size() - 1 < i) {
                return true;
            } else {
                Set<String> part = getParts().get(i);
                if (!part.contains(WILDCARD_TOKEN) && !part.containsAll(otherPart)) {
                    return false;
                }
                i++;
            }
        }

        // If this permission has more parts than the other parts, only imply it if all of the other parts are wildcards
        for (; i < getParts().size(); i++) {
            Set<String> part = getParts().get(i);
            if (!part.contains(WILDCARD_TOKEN)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(permissionResourceType.getScheme()).append(SCHEME_DIVIDER_TOKEN)
                .append(ROOT_PART_TOKEN);
        int index = 0;
        for (Set<String> part : parts) {
            if (index++ > 0) {
                buffer.append(PART_DIVIDER_TOKEN);
            }
            Iterator<String> partIt = part.iterator();
            while (partIt.hasNext()) {
                buffer.append(partIt.next());
                if (partIt.hasNext()) {
                    buffer.append(SUBPART_DIVIDER_TOKEN);
                }
            }
        }
        return buffer.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WildcardResource) {
            WildcardResource wr = (WildcardResource) o;
            return permissionResourceType == wr.permissionResourceType &&
                    parts.equals(wr.parts);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionResourceType, parts);
    }
}
