package cn.fisok.raw.kit;

import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

public class PacketFileKitTest {
    @Test
    public void testUnZip(){
//        File zipFile = new File("/Users/asher/Downloads/amarcdp_1.0_doc_20101214.zip");
//        File outDir = new File("/Users/asher/Downloads/amarcdp_1.0_doc_20101214");
//        File zipFile = new File("/Users/asher/Downloads/FlowEngine.zip");
//        File outDir = new File("/Users/asher/Downloads/FlowEngine");
//        File zipFile = new File("/Users/asher/Downloads/CRGet.zip");
//        File outDir = new File("/Users/asher/Downloads/CRGet");
//        File zipFile = new File("/Users/asher/Downloads/CRGet.rar");
//        File outDir = new File("/Users/asher/Downloads/CRGet");
//        File zipFile = new File("/Users/asher/Downloads/软件设计文档模板.rar");
//        File outDir = new File("/Users/asher/Downloads/软件设计文档模板");
        File zipFile = new File("/Users/asher/workspace/ws-rober/vekai-cabin/vekai-runtime/src/test/resources/file/packet-demo.rar");
        File outDir = new File("/Users/asher/workspace/ws-rober/vekai-cabin/vekai-runtime/src/test/resources/file/packet-demo");

//        PacketFileKit.unzip(zipFile,outDir,true, Charset.defaultCharset());
//        PacketFileKit.unpack(zipFile,outDir,true, Charset.defaultCharset());
        PacketFileKit.unpack(zipFile,outDir,true, Charset.defaultCharset());
//        PacketFileKit.unzip(zipFile,outDir,true, Charset.forName("GBK"));
    }
    @Test
    public void test02(){
        System.out.println(FileKit.getFileName("/Users/asher/Downloads/CRGet.rar",true));
        System.out.println(FileKit.getFileName("/Users/asher/Downloads/CRGet.rar",false));
        System.out.println(FileKit.getFileName("/Users/asher/Downloads/CRGet",true));
        System.out.println(FileKit.getFileName("/Users/asher/Downloads/CRGet",false));
    }

    @Test
    public void testMakeZip0(){
        File[] files = new File[]{
                new File("/Users/asher/Downloads/额度管理功能需求说明书V0.3.docx"),
                new File("/Users/asher/Downloads/导入-案例数据.xlsx"),
                new File("/Users/asher/Downloads/creditloan.sql"),
        };
        File zipFile = new File("/Users/asher/Downloads/test-mk-zip1.zip");
        PacketFileKit.makeZip(ListKit.listOf(files),zipFile);
    }
    @Test
    public void testMakeZip1(){
        File[] files = new File[]{
                new File("/Users/asher/Downloads/CRGet"),
                new File("/Users/asher/Downloads/creditloan.sql"),
        };
        File zipFile = new File("/Users/asher/Downloads/CRGet-2.zip");
        PacketFileKit.makeZip(ListKit.listOf(files),zipFile);
    }
}
