/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.lang;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/01/10 22:30
 * @desc :
 */
public abstract class TreeNode<T extends TreeNode> {
    protected T parent;
    protected List<T> children;

    @JsonIgnore
    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public void addChild(T value) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(value);
        value.setParent(this);
    }

    public void addChilds(List<T> value) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        value.forEach(this::addChild);
    }
}
