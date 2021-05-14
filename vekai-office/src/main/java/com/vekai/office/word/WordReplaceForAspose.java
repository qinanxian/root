//package com.vekai.office.word;
//
//import com.aspose.words.Bookmark;
//import com.aspose.words.Document;
//import com.aspose.words.OoxmlSaveOptions;
//import com.vekai.office.word.parameter.TableParameter;
//import com.vekai.office.word.parameter.WordParameter;
//import com.vekai.office.word.parameter.WordParameterSet;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//
//
///**
// * 使用aspose进行文档模板替换
// */
//public class WordReplaceForAspose {
//
//    private InputStream inputStream = null;
//    private Document document;
//
//    /**
//     * 构建一个 WordReplaceForAspose 对象，用于使用一个特定的模板进行替换操作
//     *
//     * @param inputStream 模板对象输入流
//     */
//    public WordReplaceForAspose(InputStream inputStream) {
//        this.inputStream = inputStream;
//    }
//
//    /**
//     * 执行替换
//     *
//     * @param parameterSet
//     * @throws WordReplaceException
//     */
//    public void replace(WordParameterSet parameterSet) throws Exception {
//        if (null == parameterSet || parameterSet.getParameters().isEmpty()) return;
//
//        document = new Document(inputStream);
//        for (WordParameter parameter : parameterSet.getParameters()) {
//            if (parameter instanceof TableParameter) {//表格变量
//
//            } else {//文本变量
//                Bookmark bookmark = document.getRange().getBookmarks().get(parameter.getName());
//                if (bookmark != null) {
//                    bookmark.setText(parameter.getStringValue());
//                }
//            }
//        }
//    }
//
//    /**
//     * 输出内容到输出流中
//     *
//     * @param outputStream
//     */
//    public void write(OutputStream outputStream) throws Exception {
//        document.save(outputStream, new OoxmlSaveOptions());
//    }
//
//}
