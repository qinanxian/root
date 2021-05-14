package com.vekai.auth.tags;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;

import java.util.Map;


public class AuthTags extends SimpleHash {
    public AuthTags() {
        super((ObjectWrapper)null);
        put("user", new UserTag());
    }

    public AuthTags(Map map) {
        super(map,null);
    }


}
