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

    <#if urls?has_content>
        <div>
            <p>Urls with content:</p>
            <ul>
        <#list urls as url>
          <li><a href="/${url}">/${url}</a></li>
        </#list>
            </ul>
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