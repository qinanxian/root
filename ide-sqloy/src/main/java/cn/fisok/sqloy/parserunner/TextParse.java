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
package cn.fisok.sqloy.parserunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 文本内容解析器,根据分割符，解析文本为独立子句
 */
public class TextParse {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 行式的SQL注释（以"--"开始的SQL注释）
     */
    public static final Pattern SQL_COMMENT_LINE_STYLE_PATTERN = Pattern.compile("^\\s*\\-{2}");
    /**
     * 范围式的SQL注释，类似java的类注释
     */
    public static final Pattern SQL_COMMENT_RANGE_STYLE_PATTERN = Pattern.compile("^(\\s*)(\\/\\*)(.*)(\\*\\/)(\\s*)", Pattern.DOTALL);
    /**
     * SQL注释命令，消息展示
     */
    public static final Pattern SQL_COMMENT_SHOW_MESSAGE = Pattern.compile("^(\\s*)(\\/\\*)(SQL@Show\\:)(.*)(\\*\\/)(\\s*)", Pattern.DOTALL);

    protected List<File> sqlFileList = new ArrayList<File>();
    private List<Pattern> passedCommentPattern = new ArrayList<Pattern>();
    /**
     * SQL执行的
     */
    private Pattern sqlExecpattern = null;

    /**
     * Content 解析SQL文本内容，File解析SQL文件
     */
    public enum ParseType {Content, File}

    ;
    private String delimiter;
    private String content;
    private ParseType parseType;
    private TextLineProcess textLineProcess;
    private StringBuilder readerBuffer = null;
    private StringBuilder execSQLBuffer = null;

    /**
     * 定义文本解析器
     *
     * @param delimiter       分割符
     * @param content         解析内容
     * @param parseType       解析模式ParseType.Content或ParseType.File
     * @param textLineProcess 单行文本处理器
     */
    public TextParse(String delimiter, String content, ParseType parseType, TextLineProcess textLineProcess) {
        this.delimiter = delimiter;
        this.content = content;
        this.parseType = parseType;
        this.textLineProcess = textLineProcess;
        String s1 = content.replaceAll("\\s+", "");
        if (parseType == ParseType.File) {
            String[] s2 = s1.split(",");
            for (String s : s2) {
                sqlFileList.add(new File(s));
            }
        }

        sqlExecpattern = Pattern.compile("(.+)(" + delimiter + ")", Pattern.DOTALL);
//        sqlExecpattern = Pattern.compile("(.+\\s*)(" + delimiter + ")(\\s*)", Pattern.DOTALL);
//        sqlExecpattern = Pattern.compile( delimiter, Pattern.DOTALL);
    }

    /**
     * 定义文本解析器
     *
     * @param delimiter       分割符
     * @param files           解析文件列表
     * @param textLineProcess 单行文本处理器
     */
    public TextParse(String delimiter, File[] files, TextLineProcess textLineProcess) {
        this.delimiter = delimiter;
        this.parseType = ParseType.File;
        this.textLineProcess = textLineProcess;
        sqlFileList.addAll(Arrays.asList(files));
        sqlExecpattern = Pattern.compile("(.+\\s*)(" + delimiter + ")(\\s*)", Pattern.DOTALL);
    }

    /**
     * 解析中，需要忽略的注释正则表达式对象
     *
     * @param pattern pattern
     */
    public void addPassedComment(Pattern... pattern) {
        passedCommentPattern.addAll(Arrays.asList(pattern));
    }

    /**
     * 查看当前注释是否为忽略注释
     *
     * @param comment comment
     * @return boolean
     */
    public boolean inPassedComment(CharSequence comment) {
        for (Pattern pattern : passedCommentPattern) {
            if (pattern.matcher(comment).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行解析
     *
     * @throws Exception Exception
     */
    public void parse() throws TextParseException {
        if (parseType == ParseType.File) {
            parseFiles();
        } else if (parseType == ParseType.Content) {
            parseContent();
        }
    }

    /**
     * 解析文件
     *
     * @throws Exception Exception
     */
    protected void parseFiles() throws TextParseException {
        validateFile();
        for (File file : sqlFileList) {
            long beginTime = System.currentTimeMillis();
            parseSingleFile(file);
            long endTime = System.currentTimeMillis();
            try {
                logger.info("文件:" + file.getCanonicalPath() + ",运行时长：[" + DateTimeConverter.longSecond2HMS(endTime - beginTime) + "]");
            } catch (IOException e) {
                throw new TextParseException(e);
            }
        }
    }

    /**
     * 解析文本内容
     *
     * @throws TextParseException TextParseException
     * @throws Exception Exception
     */
    public void parseContent() throws TextParseException {
        long beginTime = System.currentTimeMillis();
        BufferedReader scriptReader = new BufferedReader(new StringReader(content));
        processContent(scriptReader);
        long endTime = System.currentTimeMillis();
        logger.info("运行时长：[" + DateTimeConverter.longSecond2HMS(endTime - beginTime) + "]");

    }

    /**
     * 解析处理单个文件
     *
     * @param file file
     */
    protected void parseSingleFile(File file) throws TextParseException {
        FileInputStream fis = null;
        BufferedReader bufferReader = null;
        try {
            fis = new FileInputStream(file);
            bufferReader = new BufferedReader(new InputStreamReader(fis));
            processContent(bufferReader);
        } catch (FileNotFoundException e) {
            throw new TextParseException("文件未找到", e);
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    throw new TextParseException("关闭bufferReader出错", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new TextParseException("关闭FileInputStream出错", e);
                }
            }
        }
    }

    /**
     * 检查列表中的文件是否存在，是否为一个文件
     *
     * @throws Exception Exception
     */
    private void validateFile() throws TextParseException {
        for (File f : sqlFileList) {
            if (!f.exists()) {
                throw new TextParseException("文件[" + f.getAbsolutePath() + "]不存在！");
            }
            if (!f.isFile()) {
                throw new TextParseException("文件[" + f.getAbsolutePath() + "]不是一个文件，可是一个目录");
            }
        }
    }

    /**
     * 处理文本内容
     *
     * @param scriptReader scriptReader
     */
    protected void processContent(BufferedReader scriptReader) throws TextParseException {
        readerBuffer = new StringBuilder();
        execSQLBuffer = new StringBuilder();
        String line = null;
        try {
            long preTime = System.nanoTime();
            while ((line = scriptReader.readLine()) != null) {
                //忽略空行
                long s = System.nanoTime();
                if (line.matches("^(\\s*)(\\r|\\n)*(\\s*)$")) continue;

                //如果是需要传给处理器处理的注释，则处理一下
                if (processComment(line)) {
                    continue;
                }

                //4.匹配执行标识
                Matcher matcher = sqlExecpattern.matcher(line);
//                Matcher matcher = sqlExecpattern.matcher(execSQLBuffer);

                if (matcher.find()) {
                    boolean skipCurrentError = false;
                    String sqlText = matcher.group(1);
                    execSQLBuffer.append(sqlText).append("\n");

                    String hint = matcher.group(2);
                    String sqlItem = execSQLBuffer.toString();
                    if (execSQLBuffer.indexOf("/*SkipError*/") > 0) {
                        sqlItem = sqlItem.replaceAll("\\/\\*SkipError\\*\\/".toString(), "");
                        skipCurrentError = true;
                    }
                    //兼容以前老的写法"/*SQL@Run,SkipError*/"
                    if (hint.indexOf("SkipError") > 0) {
                        skipCurrentError = true;
                    }
                    textLineProcess.process(sqlItem, skipCurrentError);
                    execSQLBuffer = new StringBuilder();
                    readerBuffer = new StringBuilder();


//                    long newTime = System.nanoTime();
//                    System.out.println(new DecimalFormat("###000,000,000").format(newTime-preTime)+":"+sqlItem);
//                    preTime = newTime;
                }else{
                    execSQLBuffer.append(line).append("\n");
                }

            }
        } catch (IOException e) {
            throw new TextParseException("读取文本流出错", e);
        }
    }

    /**
     * 处理注释部分
     *
     * @param line 当前行
     * @return 如果是注释，返回true,否则返回false
     * @throws TextParseException TextParseException
     */
    private boolean processComment(String line) throws TextParseException {
        readerBuffer.append(line).append("\n");
//        execSQLBuffer.append(line).append("\n");
        //如果是"--“或"/* */"式注释
        if (SQL_COMMENT_LINE_STYLE_PATTERN.matcher(readerBuffer).find()
                || SQL_COMMENT_RANGE_STYLE_PATTERN.matcher(readerBuffer).find()) {
            //如果是忽略的注释，需要单独调用处理器来处理一下
            if (inPassedComment(readerBuffer)) {
                textLineProcess.process(readerBuffer.toString(), true);
            } else {
                // 如果是命令类注释，则需要处理
                Matcher matcher = SQL_COMMENT_SHOW_MESSAGE.matcher(readerBuffer);
                if (matcher.find()) {
                    String message = matcher.group(4);
                    logger.info("\t\t SQL文本单元[" + message + "]");
                }
            }
            //删除SQL内容中的注释部分
//            execSQLBuffer = new StringBuilder(execSQLBuffer.toString().replace(readerBuffer, ""));
            readerBuffer = new StringBuilder();
            return true;
        }
        return false;
    }


    /**
     * 获取分割符
     *
     * @return String
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * 获取需要解析的原始设置内容
     *
     * @return String
     */
    public String getContent() {
        return content;
    }


}
