<!DOCTYPE html>
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js"><!--<![endif]-->
<head>
    <meta charset="utf-8">

    <title>${pageTitle!"?"}</title>

    <style>
        .data-received xmp {
            background-color: black;
            color: white;
            max-height:600px;
            overflow-y: scroll;
        }
    </style>
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

            <#if n != 0><a href="/${pageTitle}?n=${n-1}"></#if>
            << last
            <#if n != 0></a></#if>
            ||
            <#if dataList[n+1]?has_content><a href="/${pageTitle}?n=${n+1}"></#if>
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

    <#else>
    <p>No files received. Direct posts to:
    <br/>http://localhost:${serverPort}/{id)"
       <br/> where {id} is the unique url you want to test";
    </p>
    </#if>
</div>
</body>
</html>