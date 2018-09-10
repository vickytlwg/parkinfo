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

