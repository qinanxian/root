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
package cn.fisok.raw.kit;

import cn.fisok.raw.lang.FisokException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/2/3
 * @desc : Resource的工具类
 */
public abstract class ResourceKit {
    private static PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();

    /**
     * 通过Resource读取URI
     *
     * @param resource
     * @return
     */
    public static URI getURI(Resource resource) {
        URI uri = null;
        try {
            uri = resource.getURI();
        } catch (IOException e) {
            throw new FisokException(e, "读取资源URI{0}出错", resource.toString());
        }
        return uri;
    }

    /**
     * 通过Resource读取URL
     *
     * @param resource
     * @return
     */
    public static URL getURL(Resource resource) {
        URL url = null;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            throw new FisokException(e, "读取资源URL{0}出错", resource.toString());
        }
        return url;
    }

    /**
     * 通过Resource打开输入流
     *
     * @param resource
     * @return
     */
    public static InputStream openInputStream(Resource resource) {
        URL url = getURL(resource);
        return openInputStream(url);
    }

    /**
     * 通过Resource打开输入流
     * @param expr
     * @return
     */
    public static InputStream openInputStream(String expr) {
        Resource resource = loadResource(expr);
        ValidateKit.notNull(resource,"资源{0}加载失败",expr);
        URL url = getURL(resource);
        return openInputStream(url);
    }

    /**
     * 通过Resource打开输入流
     *
     * @param url
     * @return
     */
    public static InputStream openInputStream(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new FisokException(e, "读取资源{0}出错", url.getFile());
        }
    }

    /**
     * 从一个资源表达式中读取出所有的资源列表，并且检查是否具有读权限
     * @param paths [classpath:/a/b/c/xxx.xml] 格式
     * @return
     */
    public static List<Resource> loadResources(String[] paths) {
        List<Resource> resourceList = new ArrayList<Resource>();
        for (String path : paths) {
            Resource[] resources = null;
            try {

                resources = resourceLoader.getResources(path);
                String resourceLocation = ResourceKit.getURI(resourceLoader.getResource("")).getPath();
                if(resources == null || resources.length == 0){
                    LogKit.warn("位置[{0}]未找到资源[{1}]",resourceLocation,path);
                }
            } catch (IOException e) {
                LogKit.error("读取资源出错",e);
                throw new FisokException("资源集合[{0}]加载出错", path);
            }
            for (Resource resource : resources) {
                if (!resource.isReadable()) {
                    URL url = ResourceKit.getURL(resource);
                    throw new FisokException("资源{0}无读取权限", url.getFile());
                }
                resourceList.add(resource);
            }
        }
        return resourceList;
    }

    /**
     * 从一个资源表达式中读取出所有的资源列表，并且检查是否具有读权限
     * @param path [classpath:/a/b/c/xxx.xml] 格式
     * @return
     */
    public static List<Resource> loadResources(String path) {
        return loadResources(new String[]{path});
    }

    /**
     * 从一个资源表达式中读取出一个资源列表，并且检查是否具有读权限
     * @param path
     * @return
     */
    public static Resource loadResource(String path){
        List<Resource> resourceList = loadResources(path);
        if(ListKit.isBlank(resourceList)){
            return null;
        }
        return resourceList.stream().findFirst().get();
    }
}
