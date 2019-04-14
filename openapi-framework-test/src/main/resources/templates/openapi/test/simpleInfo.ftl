<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="X-CSRF-TOKEN" content="${Request["org.springframework.security.web.csrf.CsrfToken"].token}"/>
    <title>SimpleInfo</title>
</head>
<body>

<div>
    <form action="/openapi/test/simpleInfo/send.html" method="post">
        <input type="hidden" name="_csrf" value="${Request["org.springframework.security.web.csrf.CsrfToken"].token}">
        <div><label>Context:</label> <input type="text" value="🐾一休哥🐾，😁🀀εǚ☏©" size="32" name="context"></div>
        <div>
            <button type="submit">请求</button>
        </div>
    </form>
</div>


</body>
</html>