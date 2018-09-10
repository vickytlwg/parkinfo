//设置默认
$.validator.setDefaults({
	ignore: "",
	highlight : function(element,errorClass, validClass) {
    },
    unhighlight : function(element,errorClass, validClass) {
		element=$(element);
		var el;
		if (element.is(':radio') || element.is(':checkbox')) //如果是radio或checkbox 
		  	el=element.parent(); //将错误信息添加当前元素的父结点后面 
		else if(element.is("select") && element.is(".selectpicker"))//对应multiselect控件
		{
			el= element.next();
		}
		else
			el= element;
		$(el).qtip('destroy'); // 清除qtip提示信息
     },
     onchangeout : function(element) {
		 $(element).valid();
	 },
     errorPlacement: function(error, element) { //指定错误信息位置 
    	element=$(element);
    	var el;
     	if (element.is(':radio') || element.is(':checkbox')) //如果是radio或checkbox 
         	el=element.parent(); //将错误信息添加当前元素的父结点后面 
     	else if(element.is("select") && element.is(".selectpicker"))//对应multiselect控件
     	{
     		el= element.next();
     	}
     	else
     		el= element;
     	if (true) { //如果是radio或checkbox 
     		$(el).qtip({
     			content: error.html(),
     			show : {
						event : false,
						effect : true,
						ready : true
					},
					position:{ my: 'center left', at: 'right center' },
     			hide : false,
     			style : {
     				classes : 'qtip-rounded'
     			},
	        		corner: {  
	                    tooltip: 'topMiddle',  
	                    target: 'topRight'  
	                }  
     		});
     	}
     }
});