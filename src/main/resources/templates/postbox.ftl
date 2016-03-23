<!DOCTYPE html>
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js"><!--<![endif]-->
<head>
    <meta charset="utf-8">

    <title>${pageTitle!"?"}</title>

    </head>
<body>
<h1>/${pageTitle!"?"}</h1>
<h3>${dataList?size} Received</h3>
<div id="data-received">
    <#if dataList?size != 0>
        <p>
            LAST FILE RECEIVED: ${dataList[0].fileName}
        </p>
        <xmp>${dataList[0].content}</xmp>
    <#else>
    <p>No files received. Direct posts to:
    <br/>http://localhost:${serverPort}/{id)"
       <br/> where {id} is the unique url you want to test";
    </p>
    </#if>
</div>
</body>
</html>