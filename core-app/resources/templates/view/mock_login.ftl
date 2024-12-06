<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>mock Login - qifu3</title>
  </head>
  <body>
        <form id="loginForm" class="login-form" method="post" action="/login">
          <div style="display: none;">
            <input class="form-control" type="text" name="username" id="username" placeholder="Account" maxlength="24" value="${uId}">
          </div>
          <div style="display: none;">
            <input class="form-control" type="password" name="password" id="password" placeholder="Password" maxlength="16" value="${mPw}">
          </div>
          <div>
            <#if errMsg?has_content>
            <span>${errMsg}</span>
            </#if>
          </div>
        </form>
        
    <script src="/js/jquery-3.5.1.js"></script>
    <script>
    $('#loginForm').submit();
    </script>
  </body>
</html>