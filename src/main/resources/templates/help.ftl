<!DOCTYPE html>
<head>
    <meta charset="utf-8">

    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript" src="/js/lib/bootstrap.js"></script>

    <#--Styles-->
    <!--Bootstrap-->
    <link rel="stylesheet" href="/css/lib/bootstrap.css">
    <link rel="stylesheet" href="/css/lib/bootstrap-responsive.css">

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

<div id="help">

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

<div class="message-warn">
  <span id="delete-all-data" data-address="" class="btn btn-warning">DELETE ALL DATA</span>
</div>
<script lang="text/javascript" src="/js/custom.js"></script>
</body>
</html>