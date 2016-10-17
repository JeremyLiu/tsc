<%@ page contentType="text/html;charset=UTF-8"%>
<html>
    <head>
        <link rel="shortcut icon" type="image/x-icon" />
        <title>网络管理平台</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <link rel="stylesheet" type="text/css" href="static/css/vendor/bootstrap-theme.css">
        <link rel="stylesheet" type="text/css" href="static/css/vendor/bootstrap.css">
        <link rel="stylesheet" type="text/css" href="static/css/vendor/bootstrap-treeview.min.css">
        <link rel="stylesheet" type="text/css" href="static/css/vendor/jquery-ui-1.10.4.custom.min.css">
        <link rel="stylesheet" type="text/css" href="static/css/vendor/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="static/css/login.css" />
        <script src="static/js/jquery-2.2.3.min.js"></script>
    </head>
    <body>
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <!--<a class="navbar-brand" href="#"><span class="glyphicon glyphicon-briefcase head-navi-icon"></span></a>-->
                </div>
            </div>
        </nav>
        <div class="login-center">
            <div class="login-title">
                欢迎登陆管理系统
            </div>
            <form class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="name" class="col-sm-3 control-label">帐号</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="name"
                               placeholder="请输入您的帐号">
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-sm-3 control-label">密码</label>
                    <div class="col-sm-8">
                        <input type="password" class="form-control" id="password"
                               placeholder="请输入密码">
                    </div>
                </div>
                <div class="form-group">
                    <div class=" col-sm-10">
                        <div class="checkbox">
                            <label>
                                <input id="remember" type="checkbox"> 自动登录
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" id="loginBtn" class="btn btn-default login-btn">登录</button>
                    </div>
                </div>
            </form>
        </div>
        <script src="static/js/login.js"></script>
    </body>
</html>