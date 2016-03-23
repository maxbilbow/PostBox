<!DOCTYPE html>
<head>
    <meta charset="utf-8">

    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript" src="/js/lib/bootstrap.js"></script>

    <#--Styles-->
    <!--Bootstrap-->
    <link rel="stylesheet" href="/css/lib/bootstrap.css">
    <link rel="stylesheet" href="/css/lib/bootstrap-responsive.css">
    <link rel="stylesheet" href="/css/lib/toggle-switch.css">
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
<h1>/${pageTitle!"error"}</h1>

<div id="help">

    <#if urls?has_content>
        <div>
            <p>Urls with content:</p>
            <ul>
        <#list urls as url>
          <li><a href="/data/${url}">/${url}</a></li>
        </#list>
            </ul>
        </div>
    <#else>
    <p>No files received. Try <a href="/">Post Box Help</a>
    </p>
    </#if>
</div>


</body>
</html>