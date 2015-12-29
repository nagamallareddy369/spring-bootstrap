<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>测试</title>

    <!-- Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->



  </head>
  <body>
          <table class="table table-hover">
              <thead>
                <tr>
                  <th>编号 ID</th>
                  <th>描述 DESC</th>
                  <th>状态 STATUS</th>
                  <th>动作 ACTION</th>
                </tr>
              </thead>
              <tbody>
                <tr dev_id="1">
                  <td>1</td>
                  <td>RPi.PCF8574.IO0</td>
                  <td >off</td>
                  <td>
                    <input type="hidden" name="id" value="1"/>
                    <button type="button" class="btn btn-primary btn-xs" >删除</button>
                  </td>
                </tr>
                <tr dev_id="2">
                  <td>2</td>
                  <td>RPi.PCF8574.IO1</td>
                  <td>off</td>
                  <td>
                    <input type="hidden" name="id" value="2"/>
                    <button type="button" class="btn btn-primary btn-xs">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script>
        jQuery(document).ready(function(){
          jQuery('.btn').on("click",function(){
              var idField = $(this).parent().children("input");
              var id = jQuery(idField).val();
              console.log(id);

              jQuery.ajax({
                type:'DELETE',
                url : 'user/delete.do',
                dataType : 'json',
                data : {'id' : id},
                success: function(data) {
                    console.log(data);
                },
                error:function(data){
                    console.log('error : ' + data);
                },
              })

          })
        });
    </script>
  </body>
</html>