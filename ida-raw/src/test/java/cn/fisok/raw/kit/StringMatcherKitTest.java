package cn.fisok.raw.kit;

import org.junit.Test;

import java.util.List;

public class StringMatcherKitTest {

    @Test
    public void test01(){
        FileItem[] fileItems = {
                new FileItem("尽职调查.docx","237.2 K",""),
                new FileItem("投资价值分析报告（可行性论证报告）.docx","20 K",""),
                new FileItem("信用评级报告.pdf","1.2 M",""),
                new FileItem("合规性评估表.xlx","456.1 K",""),
                new FileItem("风险评估报告.docx","20.2 K",""),
                new FileItem("法律意见书.docx","84.2 K",""),
                new FileItem("保险资产管理产品注册通知书.docx","45 K",""),
                new FileItem("项目来源信息.docx","20 K",""),
                new FileItem("关于发起设立的报告（资管发文）.docx","8 K",""),
                new FileItem("产委会签报.docx","35.1 K",""),
                new FileItem("投管会签报.docx","10.2 K",""),
                new FileItem("产委会会议纪要.docx","2.3 M",""),
                new FileItem("时间计划表.xlsx","95.1 K",""),
                new FileItem("尽调提纲.docx","323.2 K",""),
                new FileItem("尽调会议纪要.docx","956.9 K",""),
                new FileItem("前期调查报告.pdf","4.3 K",""),
                new FileItem("投资合同.docx","932.4 K",""),
                new FileItem("保证合同.docx","30 K",""),


        };
        String[] fileList = {
                "尽职调查报告",
                "投资价值分析报告",
                "信用评级报告",
                "合规性评估表",
                "风险评估报告",
                "法律意见书",
                "保险资产管理产品注册通知书",
                "项目来源信息",
                "关于发起设立的报告",
                "产委会签报",
                "投管会签报",
                "产委会会议纪要",
                "时间计划表",
                "尽调提纲",
                "尽调会议纪要",
                "前期调查报告",
                "投资合同",
                "保证合同",
                "抵押合同",
                "质押合同",
                "托管合同",
                "受托合同",
                "监督合同",
                "保管合同",
                "受益人大会章程"
        };

        for(FileItem fileItem:fileItems){
            String name = fileItem.name;
            if(name==null||name.length()<=1)continue;
            //name = name.substring(0,name.lastIndexOf("."));
            List<String> similarList = StringMatcherKit.sortSimilarity(name, 0.01, fileList);
            if(similarList.size()>0){
                fileItem.matched = similarList.get(0);
            }
        }

        for(FileItem fileItem:fileItems){
            System.out.println(fileItem.name+"-->"+fileItem.matched);
        }


    }

    class FileItem {
        public String name;
        public String size;
        public String matched;

        public FileItem(String name, String size, String matched) {
            this.name = name;
            this.size = size;
            this.matched = matched;
        }
    }
}
