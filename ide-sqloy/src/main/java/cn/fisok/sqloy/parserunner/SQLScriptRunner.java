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

import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 包装一条可执行SQL语句
 * @update 2014/02/13
 * 	<ul>
 * 		<h4>升级明细</h4>
 * 		<li>1.支持配置分割符配置</li>
 * 		<li>2.支持整个单元出错忽略</li>
 * 		<li>2.更精细化的SQL执行。更新类显示影响记录条数据，支持选择SQL输出结果</li>
 * 	<ul>
 * @update 2014/02/24 分离文本解析部分，以便校验功能能够复用此模块
 * @update 2018/08/09 重构后，放到vekai-sql模块中
 */
public class SQLScriptRunner {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String content;
    private TextParse.ParseType parseType = TextParse.ParseType.Content;
    private String delimiter = ";";				//SQL语句分割符
    private boolean skipAllError = false;		//是否忽略跳过所有错误
    private long sqlWarmTime = 30000;			//单条SQL执行警告时间
    private Statement stmt = null;

    public SQLScriptRunner(String content,Statement stmt) {
        this.content = content;
        this.stmt = stmt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextParse.ParseType getParseType() {
        return parseType;
    }

    public void setParseType(TextParse.ParseType parseType) {
        this.parseType = parseType;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isSkipAllError() {
        return skipAllError;
    }

    public void setSkipAllError(boolean skipAllError) {
        this.skipAllError = skipAllError;
    }

    public long getSqlWarmTime() {
        return sqlWarmTime;
    }

    public void setSqlWarmTime(long sqlWarmTime) {
        this.sqlWarmTime = sqlWarmTime;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public void exec() throws TextParseException {
        //构造解析器，并且生成一个解析单行原子文本执行器
        TextParse textParse = new TextParse(delimiter,content,parseType,new TextLineProcess(){
            public void process(String lineContent, boolean skipError)
                    throws TextParseException {
//                lineContent = replaceEnvVar(lineContent);
                SQLExecItem execItem = new SQLExecItem(lineContent);
                try {
                    execItem.exec(stmt, skipError||skipAllError, sqlWarmTime);
                    logger.debug("-----------------------------\n");
                } catch (SQLException e) {
//                    logger.error(e.getMessage(),e);
                    throw new TextParseException(MessageFormat.format("执行SQL出错,SQL:{0}", lineContent),e);
                }
            }
        });
        textParse.parse();
    }


}
