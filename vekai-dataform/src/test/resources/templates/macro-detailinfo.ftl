<@body title="详情表单-案例-宏">
    <@detailInfo>
        <div class="btn-toolbar">
            <div class="btn-group" role="group">
                <button type="button" class="btn btn-primary"><i class="fa fa-bolt"></i>暂存</button>
                <button type="button" class="btn btn-success"><i class="fa fa-save"></i>保存</button>
            </div>
        </div>
        {{ message }}
        <@fieldset id="group10" name="基本信息" class="rb-container-col-2">
            <@text id="id" name="个人编号" required="true" class="rb-field-col-2"/>
            <@text id="name" name="姓名" required="true"/>
            <@text id="chnName" name="中文名" />
            <@text id="engName" name="英文名" />
            <@radio id="gender" name="性别">
                <label class="radio-inline">
                    <input type="radio" name="gender" value="男" checked=""/>男
                </label>
                <label class="radio-inline">
                    <input type="radio" name="gender" value="女"/>女
                </label>
                <label class="radio-inline">
                    <input type="radio" name="gender" value="未知"/>未知
                </label>
                <label class="radio-inline">
                    <input type="radio" name="gender" value="禁用" disabled=""/>禁用
                </label>
            </@radio>
            <@date id="birth" name="出生日期" placeholder="yyyy-MM-dd" />
            <@number id="height" name="身高" suffix="CM"/>
            <@number id="height" name="体重" suffix="KG"/>
            <@picker id="nation" name="民族" pickername="选择"/>
            <@select id="political" name="政治面貌">
                <option value=""></option>
                <option value="01">中国党员</option>
                <option value="02" selected="">共青团员</option>
                <option value="03">少先队员</option>
                <option value="04">群众</option>
                <option value="05">其他</option>
            </@select>
            <@select id="marital" name="婚姻状况">
                <option value=""></option>
                <option value="01">已婚</option>
                <option value="02" selected="">离异</option>
                <option value="03">未婚</option>
                <option value="04">二婚</option>
                <option value="05">其他</option>
            </@select>
            <@select id="educationLevel" name="最高学历">
                <option value=""></option>
                <option value="01">博士</option>
                <option value="02" selected="">硕士</option>
                <option value="03">本科</option>
                <option value="04">专科</option>
                <option value="05">高中以下</option>
            </@select>
            <@text id="graduatedFrom" name="毕业院校" class="rb-field-col-2" note="取得最高学历所在院校"/>
            <@field id="domicilePlace" name="籍贯" class="rb-field-col-2" note="此处只是演示联动排版，并未做联动交互">
                <div class="rb-input-inline">
                    <select class="form-control" name="quiz1">
                        <option value="">请选择省</option>
                        <option value="浙江" selected="">浙江省</option>
                        <option value="江西">江西省</option>
                        <option value="福建">福建省</option>
                    </select>
                </div>
                <div class="rb-input-inline">
                    <select class="form-control" name="quiz2">
                        <option value="">请选择市</option>
                        <option value="杭州" selected="">杭州</option>
                        <option value="宁波" disabled="">宁波</option>
                        <option value="温州">温州</option>
                        <option value="温州">台州</option>
                        <option value="温州">绍兴</option>
                    </select>
                </div>
                <div class="rb-input-inline">
                    <select class="form-control" name="quiz3">
                        <option value="">请选择县/区</option>
                        <option value="西湖区" selected="">西湖区</option>
                        <option value="余杭区">余杭区</option>
                        <option value="拱墅区">临安市</option>
                    </select>
                </div>
            </@field>
        </@fieldset>
        <@fieldset id="group20" name="联系信息" class="rb-container-col-4">
            <@text id="domicilePlace_address" name="户籍地址" required="true" class="rb-field-col-2"/>
            <@text id="presentAddress" name="居住地址" required="true" class="rb-field-col-2"/>
            <@text id="mobilePhone" name="手机号" required="true" class="rb-field-col-1"/>
            <@text id="email" name="电子邮箱" required="true" class="rb-field-col-1"/>
        </@fieldset>
        <@fieldset id="group30" name="职业信息" class="rb-container-col-4">
            <@datemonth id="entryDate" name="入职年月" required="true" class="rb-field-col-1"/>
            <@text id="workAs" name="职业" required="true" class="rb-field-col-1"/>
            <@text id="jobTitle" name="职称" required="true" class="rb-field-col-1"/>
            <@text id="companyIndustry" name="单位所属行业" required="true" class="rb-field-col-2"/>
            <@text id="companyAddress" name="单位地址" required="true" class="rb-field-col-4"/>
            <@text id="companyPostcode" name="单位邮编" required="true" class="rb-field-col-1"/>
        </@fieldset>
        <@fieldset id="group40" name="经济状况" class="rb-container-col-2">
            <@money id="monthIncome" name="个人月收入" suffix="元" class="rb-field-col-1"/>
            <@money id="familyMonthIncome" name="家庭月收入" prefix="$" suffix="元" class="rb-field-col-1"/>
            <@money id="familyYearIncome" name="家庭每月固定支出" suffix="元" class="rb-field-col-1"/>
            <@money id="familyMonthCost" name="家庭年收入" suffix="元" class="rb-field-col-1"/>
        </@fieldset>
        <@fieldset id="group50" name="其他信息" class="rb-container-col-2">
            <@field id="hobby" name="爱好" class="rb-field-col-2">
                <label class="checkbox-inline">
                    <input type="checkbox" name="hobby" value="读书" checked=""/>读书
                </label>
                <label class="checkbox-inline">
                    <input type="checkbox" name="hobby" value="看报"/>看报
                </label>
                <label class="checkbox-inline">
                    <input type="checkbox" name="hobby" value="打游戏"/>打游戏
                </label>
                <label class="checkbox-inline">
                    <input type="checkbox" name="hobby" value="睡觉" disabled=""/>睡觉
                </label>
            </@field>
            <@richtext id="remark" name="备注" class="rb-field-col-2"/>
            <@text id="status" name="状态" />
            <@text id="revision" name="修改次数" />
        </@fieldset>
        <@fieldset id="group60" name="操作信息" class="rb-container-col-2">
            <@text id="createdBy" name="创建人" />
            <@text id="createdTime" name="创建时间" />
            <@text id="updatedBy" name="更新人" />
            <@text id="updatedTime" name="更新时间" />
        </@fieldset>
    </@detailInfo>

    <div>



    </div>
</@body>
<script type="text/javascript">
    importModule('dataform','dataform/js/detail-info');

    modulet('dataform',function(dataform){
        var df = dataform.create($("#detailInfo"),{message:'111'});

        df.setValue("message","hello 1");


    });


</script>

