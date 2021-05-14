//package com.vekai.office.word;
//
//import com.vekai.office.word.parameter.WordParameterSet;
//import com.vekai.runtime.kit.ValidateKit;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class WordReplace {
//
//    private InputStream inputStream = null;
//    private WordReplaceForAspose wordReplaceForAspose;
//    private WordReplaceForPoi wordReplaceForPoi;
//    private WordReplaceType type;
//
//    public void use(WordReplaceType type) {
//        this.type = type;
//        if (type == WordReplaceType.A) {
//            wordReplaceForAspose = new WordReplaceForAspose(this.inputStream);
//        } else if (type == WordReplaceType.P) {
//            wordReplaceForPoi = new WordReplaceForPoi(this.inputStream);
//        }
//    }
//
//    public WordReplace(InputStream inputStream) {
//        this.inputStream = inputStream;
//    }
//
//
//    /**
//     * 执行替换
//     *
//     * @param parameterSet
//     * @throws WordReplaceException
//     */
//    public void replace(WordParameterSet parameterSet) throws Exception {
//        ValidateKit.notNull(this.type,"请调用 use() 来选择你进行模板替换的方式！");
//        if (type == WordReplaceType.A) {
//            wordReplaceForAspose.replace(parameterSet);
//        } else if (type == WordReplaceType.P) {
//            wordReplaceForPoi.replace(parameterSet);
//        }
//    }
//
//    /**
//     * 输出内容到输出流中
//     *
//     * @param outputStream
//     */
//    public void write(OutputStream outputStream) throws Exception {
//        ValidateKit.notNull(this.type,"请调用 use() 来选择你进行模板替换的方式！");
//        if (type == WordReplaceType.A) {
//            wordReplaceForAspose.write(outputStream);
//        } else if (type == WordReplaceType.P) {
//            wordReplaceForPoi.write(outputStream);
//        }
//    }
//
//}
