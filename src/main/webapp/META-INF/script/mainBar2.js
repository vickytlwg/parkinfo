function initEchatsJS(){
	require.config({
				paths: {
					echarts: 'js/dist',
					jquery: 'vendor/jquery',
        			bootstrap: 'vendor/bootstrap',
				}
			});
			require(
				[
					'echarts',
					'echarts/chart/bar',
					'echarts/chart/line',
					'echarts/chart/pie'
				],
				function(ec) {
					var mychart = ec.init(document.getElementById('mainBar'));
					var option = {
						tooltip: {
							show: true,
							trigger: 'axis'
						},
						legend: {
							y: '20',
							data: ['停车订单数量', '临时车收入', '月租车充值收入']
						},
						calculable: true,
						toolbox: {
							show: true,
							y: '20',
							x: 720,
							feature: {
								/*dataView:{show:true},*/
								magicType: { show: true, type: ['line', 'bar'] },
								restore: { show: true }
							}
						},
						xAxis: [{
							type: 'category',
							data: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]
						}],
						yAxis: [{
							type: 'value',
							position: 'right'
						}],
						series: [{
								name: "停车订单数量",
								type: "bar",
								data: [3, 1, 1, 0, 0, 2, 1, 2, 27, 59, 73, 82, 90, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

							},
							{
								name: '临时车收入',
								type: 'line',
								data: [0, 1, 0, 0, 0, 3, 0, 0, 0, 19.5, 46.5, 117.5, 210.5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
							},
							{
								name: '月租车充值收入',
								type: 'line',
								data: [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
							},
							{
								name: '收入分析',
								type: 'pie',
								tooltip: {
									trigger: 'item',
									formatter: '{a} <br/>{b} : {c} ({d}%)'
								},
								center: [150, 30],
								radius: [0, 10],
								itemStyle: {
									normal: {
										labelLine: {
											length: 5
										}
									}
								},
								data: [
									{ value: 376.5, name: '月租车总收入' },
									{ value: 0, name: '临时车总收入' }

								]
							}
						]
					}
					mychart.setOption(option);
				}
			);
}



function loadMonthEchatsJS(){
require.config({
				paths: {
					echarts: '../js/dist'
				}
			});
			require(
				[
					'echarts',
					'echarts/chart/bar',
					'echarts/chart/line',
					'echarts/chart/pie'
				],
				function(ec) {
					var mychart = ec.init(document.getElementById('mainBar2'));
					var option = {
						tooltip: {
							show: true,
							trigger: 'axis'
						},
						legend: {
							y: '20',
							data: ['停车订单数量', '临时车收入', '月租车充值收入']
						},
						calculable: true,
						toolbox: {
							show: true,
							y: '20',
							x: 720,
							feature: {
								/*dataView:{show:true},*/
								magicType: { show: true, type: ['line', 'bar'] },
								restore: { show: true }
							}
						},
						xAxis: [{
							type: 'category',
							data: ["2017-11-01","2017-11-02","2017-11-03","2017-11-04",
									"2017-11-05","2017-11-06","2017-11-07","2017-11-08",
									"2017-11-09","2017-11-10","2017-11-11","2017-11-12",
									"2017-11-13","2017-11-14","2017-11-15","2017-11-16",
									"2017-11-17","2017-11-18","2017-11-19","2017-11-20",
									"2017-11-21","2017-11-22","2017-11-23","2017-11-24",
									"2017-11-25","2017-11-26","2017-11-27","2017-11-28",
									"2017-11-29","2017-11-30",]
						}],
						
						yAxis: [{
							type: 'value',
//							boundaryGap : false,
							position: 'right'
//							data:['0','2,000','4,000','6,000','8,000','10,000']
						}],
						series: [{
								name: "停车订单数量",
								type: "bar",
								data: [3, 1, 1, 0, 0, 2, 1, 2, 27, 59, 73, 82, 90, 0, 0, 0, 0, 45, 0, 0, 0, 56, 0, 0,0,0,0,0,0,0,0,0,0,0,]

							},
							{
								name: '临时车收入',
								type: 'line',
								data: [0, 1, 0, 0, 0, 3, 0, 0, 0, 19.5, 46.5, 117.5, 210.5, 0, 0, 0, 0, 0, 23.5, 0, 0, 0, 12.8, 0,0,0,0,0,0,0,0,0,0,0,0]
							},
							{
								name: '月租车充值收入',
								type: 'line',
								data: [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 5, 0,0,6,0,0,0,0,0,0,0,0,0]
							},
							{
								name: '收入分析',
								type: 'pie',
								tooltip: {
									trigger: 'item',
									formatter: '{a} <br/>{b} : {c} ({d}%)'
								},
								center: [150, 30],
								radius: [0, 10],
								itemStyle: {
									normal: {
										labelLine: {
											length: 5
										}
									}
								},
								data: [
									{ value: 376.5, name: '月租车总收入' },
									{ value: 0, name: '临时车总收入' }

								]
							}
						]
					}
					mychart.setOption(option);
				}
			);
}
	

function loadYearEchatsJS(){
	require.config({
				paths: {
					echarts: 'js/dist'
				}
			});
			require(
				[
					'echarts',
					'echarts/chart/bar',
					'echarts/chart/line',
					'echarts/chart/pie'
				],
				function(ec) {
					var mychart = ec.init(document.getElementById('mainBar3'));
					var option = {
						tooltip: {
							show: true,
							trigger: 'axis'
						},
						legend: {
							y: '20',
							data: ['停车订单数量', '临时车收入', '月租车充值收入']
						},
						calculable: true,
						toolbox: {
							show: true,
							y: '20',
							x: 720,
							feature: {
								/*dataView:{show:true},*/
								magicType: { show: true, type: ['line', 'bar'] },
								restore: { show: true }
							}
						},
						xAxis: [{
							type: 'category',
							data: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]
						}],
						yAxis: [{
							type: 'value',
							position: 'right'
						}],
						series: [{
								name: "停车订单数量",
								type: "bar",
								data: [3, 1, 1, 0, 0, 2, 1, 2, 27, 59, 73, 82, 90]

							},
							{
								name: '临时车收入',
								type: 'line',
								data: [0, 1, 0, 0, 0, 3, 0, 0, 0, 19.5, 46.5, 117.5, 210.5]
							},
							{
								name: '月租车充值收入',
								type: 'line',
								data: [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
							},
							{
								name: '收入分析',
								type: 'pie',
								tooltip: {
									trigger: 'item',
									formatter: '{a} <br/>{b} : {c} ({d}%)'
								},
								center: [150, 30],
								radius: [0, 10],
								itemStyle: {
									normal: {
										labelLine: {
											length: 5
										}
									}
								},
								data: [
									{ value: 376.5, name: '月租车总收入' },
									{ value: 0, name: '临时车总收入' }

								]
							}
						]
					}
					mychart.setOption(option);
				}
			);
}


//	require.config({
//				paths:{
//					echarts:'../js/dist'
//				}
//			});
//			require(
//				[
//					'echarts',  
//					'echarts/chart/bar',
//					'echarts/chart/line',
//					'echarts/chart/pie'
//				],
//				function(ec){
//					var mychart2=ec.init(document.getElementById('mainBar2'));
//					var option={
//						tooltip:{
//							show:true,
//							trigger: 'axis'
//						},
//						legend:{
//							y:'20',
//							data:['停车订单数量2','临时车收入2','月租车充值收入2']
//						},
//						calculable : true,
//						toolbox:{
//							show:true,
//							y:'20',
//							x:720,
//							feature:{
//								/*dataView:{show:true},*/
//								magicType:{show:true,type:['line','bar']},
//								restore:{show:true}
//							}
//						},
//						xAxis:[{
//							type:'category',
//							data:["0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"]
//						}],
//						yAxis:[{
//							type:'value',
//							position: 'right'
//						}],
//						series:[{
//							name:"停车订单数量",
//							type:"bar",
//							data:[3,1,1,0,0,2,1,2,27,59,73,82,90,0,0,0,0,0,0,0,0,0,0,0],
//							
//						},
//						{
//							name:'临时车收入',
//							type:'line',
//							data:[0,1,0,0,0,3,0,0,0,19.5,46.5,117.5,210.5,0,0,0,0,0,0,0,0,0,0,0]
//						},
//						{
//							name:'月租车充值收入',
//							type:'line',
//							data:[0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
//						},
//						{
//							name:'收入分析',
//							type:'pie',
//							tooltip:{
//								trigger:'item',
//								formatter:'{a} <br/>{b} : {c} ({d}%)'
//							},
//							center:[150,30],
//							radius:[0,10],
//							itemStyle:{
//								normal:{
//									labelLine:{
//										length:5
//									}
//								}
//							},
//							data:[
//					                {value:376.5,name:'月租车总收入'},
//					                {value:0,name:'临时车总收入'}
//					                
//					            ]
//						}
//						]
//					}
//					mychart2.setOption(option);
//				}
//				)