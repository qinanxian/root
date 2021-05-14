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
package cn.fisok.sqloy.loadmd.impl;

import cn.fisok.raw.kit.ResourceKit;
import cn.fisok.sqloy.loadmd.SQLCollecter;
import cn.fisok.sqloy.loadmd.SQLTextLoader;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.FisokException;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/05
 * @desc : SQLDao文本加载器实现
 */
public class SQLTextLoaderImpl implements SQLTextLoader {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean exists(String resource){
        Resource res = ResourceKit.loadResource(resource);
        return res.exists();
    }

    public SQLCollecter parse(String... resource){
        Map<String,String> textMap = new HashMap<String,String>();

        for(String res : resource){
//            if(!res.startsWith("classpath:")
//                    &&!res.startsWith("/")
//                    &&!res.startsWith("file:")
//                    ){
//                res = "classpath:"+res;
//            }
            String text = null;
            InputStream inputStream = null;
            try {
                URL url = ResourceUtils.getURL(res);
                inputStream = url.openStream();
                text = IOKit.toString(inputStream, Charset.defaultCharset());
            } catch (FileNotFoundException e) {
//                logger.warn("加载SQLDao对应的文件出错，文件不存在",e);
            } catch (IOException e) {
                logger.warn("加载SQLDao对应的文件出错",e);
            } catch (Exception e){
                logger.error("加载SQLDao对应的文件出错",e);
            }finally {
                IOKit.close(inputStream);
            }
            if(StringKit.isBlank(text))continue;

            List<SQLTextItem> textList = parseText(text);
            textList.forEach(item->{
                if(textMap.containsKey(item.name)){
                    throw new FisokException("SQL查询项["+item.name+"]在加载的SQL资源文件中重复");
                }
                textMap.put(item.name,item.sql);
            });
        }

        //构建SQL文本集合器
        SQLCollecter collecter = new SQLCollecter(textMap);
        return collecter;
    }
    private List<SQLTextItem> parseText(String text){
        List<SQLTextItem> textList = new ArrayList<SQLTextItem>();

        Parser parser = Parser.builder().build();
        Node node = parser.parse(text);

        Node curNode = node.getFirstChild();
        SQLTextItem sqlTextItem = null;
        while(curNode != null){
            if(sqlTextItem == null) sqlTextItem = new SQLTextItem();

            if(curNode instanceof Heading){                 //标题头部-->SQL查询KEY
                Heading heading = (Heading)curNode;
                SQLTextItem tmpItem = new SQLTextItem();
                heading.accept(new AbstractVisitor() {
                    public void visit(Text text) {
                        tmpItem.name = text.getLiteral();
                    }
                });
                if(StringKit.isBlank(sqlTextItem.name)){
                    sqlTextItem.name = tmpItem.name;
                }
            }else if(curNode instanceof FencedCodeBlock){   //SQL文本内容部
                FencedCodeBlock codeBlock = (FencedCodeBlock)curNode;
                if(StringKit.isBlank(sqlTextItem.sql)){
                    sqlTextItem.sql = codeBlock.getLiteral();
                }
            }else{                                          //其他的说明文字
                List<String> texts = new ArrayList<String>();
                curNode.accept(new AbstractVisitor() {
                    public void visit(Text text) {
                        texts.add(text.getLiteral());
                    }
                });
                sqlTextItem.intro = StringKit.join(texts,",");
            }

            //凑齐了，加到列表中，并且就新创建一个实例
            if(StringKit.isNoneBlank(sqlTextItem.name)&&StringKit.isNoneBlank(sqlTextItem.sql)){
                textList.add(sqlTextItem);
                sqlTextItem = new SQLTextItem();
            }

            curNode = curNode.getNext();
        }



        return textList;
    }
    private class SQLTextItem{
        private String name;
        private String intro;
        private String sql;
    }
}
