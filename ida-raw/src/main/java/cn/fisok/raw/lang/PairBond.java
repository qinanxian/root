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

import java.io.Serializable;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:39
 * @desc :
 */
public class PairBond<L, R> implements Comparable<PairBond<L, R>>, Serializable,Cloneable {
    protected L left;
    protected R right;

    public PairBond() {
    }

    public PairBond(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> PairBond<L, R> of(final L left, final R right) {
        return new PairBond<L, R>(left, right);
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    public int compareTo(PairBond<L, R> o) {
        return 0;
    }
}