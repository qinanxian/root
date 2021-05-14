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

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:28
 * @desc :
 */
public interface ContentType {
    String DOC  = "application/msword";
    String XLS  = "application/vnd.ms-excel";
    String PPT  = "application/vnd.ms-powerpoint";

    String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    String PDF  = "application/pdf";
    String OCTET= "application/octet-stream";
    String CSV = "application/vnd.ms-excel";
}
