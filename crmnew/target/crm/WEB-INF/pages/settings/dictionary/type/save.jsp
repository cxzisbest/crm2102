<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		 $(function(){
		 	 $("#create-code").blur(function(){
		 	 	checkCode(); //验证编码是否重复
			 }); //失去焦点

			//保存按钮
			 $("#saveCreateDicTypeBtn").click(function () {
				//收集参数
				 var code=$.trim($("#create-code").val());
				 var name=$.trim($("#create-name").val());
				 var description=$.trim($("#create-description").val());

				 if(checkCode()){
					 $.ajax({
						 url:'settings/dictionary/type/saveCreateDicType.do',
						 data:{
							 code:code,
							 name:name,
							 description:description
						 },
						 type:'post',
						 dataType:'json',
						 success:function(data){
						 	if(data.code=="0"){
						 		alert(data.message);
							}else{
						 		window.location.href="settings/dictionary/type/index.do"
							}
						 }
					 })
				 }
			 })

		 })


		function checkCode(){
		 	//alert("blur");
			var code=$.trim($("#create-code").val());
			//alert(code);
			if(code==""){
				$("#codeMsg").text("编码为空");
				return false;
			}else{
				$("#codeMsg").text("");
			}

			var ret=false;
			$.ajax({
				url:'settings/dictionary/type/checkCode.do',
				data:{
					code:code
				},
				type:'post',
				dataType:'json',
				async:false,
				success:function(data){
					if(data.code=="0"){
						$("#codeMsg").text(data.message); //code重复
					}else{
						$("#codeMsg").text(""); //没有重复
						ret=true;
					}
				}
			})
			return ret;

		}
	</script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>新增字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveCreateDicTypeBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="create-code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-code" style="width: 200%;">
				<span id="codeMsg" style="color: red"></span>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-name" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-description" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 300px;">
				<textarea class="form-control" rows="3" id="create-description" style="width: 200%;"></textarea>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>