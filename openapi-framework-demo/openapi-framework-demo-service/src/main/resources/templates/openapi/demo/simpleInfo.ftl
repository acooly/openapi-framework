<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="X-CSRF-TOKEN" content="${Request["org.springframework.security.web.csrf.CsrfToken"].token}"/>
    <title>SimpleInfo</title>
</head>
<body>

<div>
    <form action="/openapi/test/simpleInfo/send.html" method="post" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="_csrf" value="${Request["org.springframework.security.web.csrf.CsrfToken"].token}">
        <div><label>Context:</label> <input type="text" value="🐾一休哥🐾，😁🀀εǚ☏©" size="32" name="context"></div>
        <div>
            <button type="submit">请求</button>
        </div>
    </form>
</div>

<script src="/js/md5.js"></script>
<script>
    var str = "{\"partnerId\":\"test\",\"version\":\"1.0\",\"requestNo\":\"1555400010651\",\"service\":\"simpleInfo\"," +
            "\"context\":\"🐾一休哥🐾\"}06f7aab08aa2431e6dae6a156fc9e0b4";
    console.info("md5:",hex_md5(str));
</script>
</body>
</html>