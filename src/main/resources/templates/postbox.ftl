<!DOCTYPE html>
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js"><!--<![endif]-->
<head>
    <meta charset="utf-8">

    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript" src="/js/lib/bootstrap.js"></script>

<#--Styles-->
    <!--Bootstrap-->
    <link rel="stylesheet" href="/css/lib/bootstrap.css">
    <link rel="stylesheet" href="/css/lib/bootstrap-responsive.css">
    <link rel="stylesheet" href="/css/main.css">
    <title>${pageTitle!"?"}</title>

</head>
<body>
<h1>/${pageTitle!"?"}</h1>

<div id="data-received">

    <#if dataList?size != 0>
        <#if RequestParameters.n?has_content>
            <#assign n=RequestParameters.n?number>
        <#else>
            <#assign n=0>
        </#if>
        <h4>${n+1} / ${dataList?size} Received</h4>
        <div>

            <#if n != 0><a href="/${postPath}?n=${n-1}"></#if>
            << last
            <#if n != 0></a></#if>
            ||
            <#if dataList[n+1]?has_content><a href="/${postPath}?n=${n+1}"></#if>
                    next >>
            <#if dataList[n+1]?has_content></a></#if>
        </div>
        <#assign data=dataList[n]>
        <div class="data-received">
        <p>
            FILE RECEIVED: ${data.dateTime.toString("dd/MM/yyyy HH:mm")}
            <br/>FILE NAME: ${data.fileName}
        </p>
        <xmp>${data.content}</xmp>
        </div>
        <div class="message-warn">
            <span id="delete-one" data-pk="${data.pk?c}" data-address="${postPath}" class="btn btn-warning">DELETE THIS ENTRY</span>
            <span id="delete-all-data" class="btn btn-danger" data-address="${postPath}">DELETE ALL FOR PATH</span>
        </div>
    <#else>
    <p>No files received. Direct posts to:
    <br/>http://localhost:${serverPort}/{id)"
       <br/> where {id} is the unique url you want to test";
    </p>
    </#if>
</div>
<script lang="text/javascript" src="/js/custom.js"></script>
</body>
</html>