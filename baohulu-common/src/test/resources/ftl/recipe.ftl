<html>
<head>
    <title></title>
    <meta charset="utf-8"></meta>
    <meta name="description" content=""></meta>
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"></meta>
    <!-- <link rel="stylesheet" href="http://yzued.111.com.cn/FFF/zepto/v0.2/style/base.css"/>  -->
    <style type="text/css">
        * {
            font-family: Arial Unicode MS;
        }

        html, body, p, h4 {
            margin: 0;
            padding: 0;
        }

        img {
            border: 0 none;
            vertical-align: middle;
        }

        body {
            font-size: 14px;
            color: #333;
            line-height: 150%;
        }

        .fl {
            float: left;
        }

        .fr {
            float: right;
        }

        .clearfix {
            clear: both;
        }

        .clearfix:after {
            content: "";
            display: table;
            clear: both;
        }

        .inlineBlock {
            display: inline-block;
            margin-right: 50px;
        }

        .container {
            width: 700px;
            margin: 0 auto;
            padding-top: 10px;
            position: relative;
        }

        h4 {
            font-size: 20px;
            line-height: 61px;
            height: 61px;
            text-align: center;
            padding: 0 0 8px 0;
        }

        .pp_header_desc {
            border-bottom: 1px solid #333;
            padding: 0 5px;
        }

        .pp_header_userInfo {
            padding: 5px 0 5px 5px;
            line-height: 25px;
        }

        .pp_header_userInfo div {
            height: 25px;
            line-height: 25px;
            white-space: nowrap;
        }

        .pp_dialog-recipe-img {
            top: 10px;
            right: 5px;
            position: absolute;
        }

        .pp_body {
            height: 235px;
            border-top: 1px solid #333;
            padding: 5px 0;
        }

        .pp_body table {
            width: 100%;
            table-layout: fixed;
            word-break:break-all;
            text-align: center;
        }

        .pp_body th {
            font-size: 14px;
        }

        .pp_body td {
            padding: 2px 0;
            font-size: 14px;
            height: 42px;
        }
        .pp_body .medicines_rp{
            width: 5%;
        }
        .pp_body .medicines_name {
            width: 30%;
        }

        .pp_body .medicines_spe {
            width: 20%;
        }

        .pp_body .medicines_num {
            width: 10%;
        }

        .pp_body .medicines_use {
            width: 33%;
        }
        .pp_body td p {
            width: 100%;
            word-break:break-all;
            word-wrap : break-word;
        }
        .pp_footer {
            padding: 0 5px;
            padding-top: 20px;
            position: relative;
        }

        .pp_footer .stamp_left {
            width: 160px;
            margin-right: 10px;
        }

        .pp_footer .stamp_right {
            width: 120px;
        }

        .pp_footer img {
            width: 100%;
        }

        .pp_footer .stamp_right_bottom {
            margin-top: 60px;
        }

        .pp_footer .pp_footer_tips {
            width: 379px;
            overflow: hidden;
        }

        .pp_footer .pp_footer_stamp {
            width: 310px;
        }

        .pp_footer_tips p {
            width: 100%;
            font-size: 14px;
            padding-top: 30px;
        }
    </style>
</head>
<body>
<div class="container">
    <h4>${hospitalName!}便民门诊处方</h4>
    <div class="pp_dialog-recipe-img">

    </div>
    <div class="pp_header">
        <div class="pp_header_desc clearfix">
            <div class="fl">处方号：${recipeCode!}</div>
            <div class="fr">开方日期：${recipeTime!}</div>
        </div>
        <div class="pp_header_userInfo">
            <div class="inlineBlock">姓名：<![CDATA[ ${(patientName)!} ]]></div>
            <div class="inlineBlock">性别：${patientSex!}</div>
            <div class="inlineBlock">年龄：${age!}${ageType!}</div>
            <div class="inlineBlock">健康档案号：${archiveId!}</div>
            <div class="inlineBlock">费别：${payType!}</div>
            <div class="inlineBlock">地址：<![CDATA[ ${(address)!} ]]></div>
            <div class="inlineBlock">科别：${departmentName!}</div>
            <div class="inlineBlock">初步诊断：<![CDATA[ ${doctorDesc!}(历史诊断) ]]></div>
        </div>
    </div>
    <div class="pp_body">
        <table>
            <thead>
            <tr>
                <th>R.p.</th>
                <th class="medicines_name">药品名称</th>
                <th class="medicines_spe">规格</th>
                <th class="medicines_num">数量</th>
                <th class="medicines_use">用法及用量</th>
            </tr>
            </thead>
            <tbody>
<#--            <#list recommentDrugs as recommentDrug>-->
<#--            <tr>-->
<#--                <td></td>-->
<#--                <td class="medicines_name"><![CDATA[ ${(recommentDrug.drugName)!} ]]></td>-->
<#--                <td class="medicines_spe"><![CDATA[ ${(recommentDrug.specification)!} ]]></td>-->
<#--                <td class="medicines_num">${(recommentDrug.specificationNum)!}</td>-->
<#--                <td class="medicines_use">${(recommentDrug.usageAndDosage)!}</td>-->
<#--            </tr>-->
<#--            </#list>-->
            </tbody>
        </table>
    </div>

    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>


    <div class="pp_footer clearfix">
        <div class="pp_footer_tips fl">
            <p>${remark}</p>
        </div>
        <div class="pp_footer_stamp fr">
            <div class="stamp_left fl">
                <p>医师</p>
                <div>
<#--                    <img src = "${doctorSeal!}" width="80" height="60"/>-->
                </div>
            </div>
            <div class="stamp_right fr">
                <div class="stamp_right_top">
                    <p>审核药师</p>
                    <div>
                    <#--<img src="${dosagePharmacistSeal!}" />-->
                    </div>
                </div>
                <div class="stamp_right_bottom">
                    <p>核对/发药</p>
                    <div>
                    <#--<img src="${confirmPharmacistSeal!}" />-->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</body>
</html>
