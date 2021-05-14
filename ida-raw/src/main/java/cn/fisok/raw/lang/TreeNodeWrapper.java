package cn.fisok.raw.lang;

import java.io.Serializable;

public class TreeNodeWrapper<T> extends TreeNode<TreeNodeWrapper<T>> implements Serializable, Cloneable {

    private static final long serialVersionUID = -1863294836839490555L;

    private T value = null;

    public TreeNodeWrapper() {

    }

    public TreeNodeWrapper(TreeNodeWrapper<T> parent) {
        if (null != parent) {
            this.parent = parent;
            parent.addChild(this);
        }
    }

    public TreeNodeWrapper(TreeNodeWrapper<T> parent, T value) {
        this(parent);
        this.value = value;
    }

    public TreeNodeWrapper(T value) {
        this.value = value;
    }


    public void setParent(TreeNodeWrapper<T> parent) {
        this.parent = parent;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


    public void removeChild(TreeNodeWrapper<T> value) {
        this.children.remove(value);
        value.setParent(null);
    }

    /**
     * 查找一个子树
     *
     * @param v v
     * @return TreeNodeWrapper
     */
    public TreeNodeWrapper<T> downFind(T v) {
        for (int i = 0; i < children.size(); i++) {
            TreeNodeWrapper<T> node = children.get(i);
            if (node.getValue() == v) {
                return node;
            } else if (value.equals(v)) {
                return node;
            }
        }
        for (int i = 0; i < children.size(); i++) {
            TreeNodeWrapper<T> node = children.get(i).downFind(v);
            if (node != null) return node;
        }
        return null;
    }


}

