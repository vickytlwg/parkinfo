angular.module("outsideParkStatusApp", ['ui.bootstrap']).controller("outsideParkStatusCtrl", function($scope, $http, $q, getDataService, getPositionData) {
    var dateInitial = function() {
        $('.date').val(new Date().format('yyyy-MM-dd'));
        $('.date').datepicker({
            autoClose : true,
            dateFormat : "yyyy-mm-dd",
            days : ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
            daysShort : ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
            daysMin : ["日", "一", "二", "三", "四", "五", "六"],
            months : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            showMonthAfterYear : true,
            viewStart : 0,
            weekStart : 1,
            yearSuffix : "年",
            isDisabled : function(date) {
                return date.valueOf() > Date.now() ? true : false;
            }
        });
        $scope.date = $('#date').val();
    };

    $scope.getZoneCenter = function() {
        getPositionData.getZoneCenter().then(function(result) {
            $scope.zoneCenters = result;
        });
    };
    $scope.getZoneCenter();
    var selectParks = [];
    var getSelectData = function() {
        var options = $('#park-select').get(0).options;
        for (var i = 0; i < options.length; i++) {
            var item = {
                id : $(options[i]).val(),
                name : $(options[i]).text()
            };
             selectParks.push(item);
        };
        $scope.parks = selectParks;
    };
    getSelectData();
    $scope.getArea = function() {
        getPositionData.getArea($scope.zoneCenterId).then(function(result) {
            $scope.areas = result;
            if($scope.areas!=null&&$scope.areas.length==1){
                $scope.areaId=$scope.areas[0].id;
                $scope.getStreets();
            }
        });
    };
    
    var filtPark=function(orignParks){
        var parks=[];
        for (var i=0; i < orignParks.length; i++) {
         for (var j=0; j < selectParks.length; j++) {
           if(selectParks[j].id==orignParks[i].id){
              parks.push(selectParks[j]); 
           }          
         };
        };
        return parks;
    };
    
    var getpark = function(parkid) {
        var deferred = $q.defer();
        $http({
            url : '/parkinfo/getPark/' + parkid
        }).success(function(result) {
            deferred.resolve(result.body);
        });
        return deferred.promise;
    };
    $scope.getStreets = function() {
         if($scope.areaId==undefined||$scope.areaId==null){
            return;
        }
        getPositionData.getStreetByAreaId($scope.areaId).then(function(result) {
            $scope.streets = result;
            if ($scope.streets!=null&&$scope.streets.length==1) {
                $scope.streetid=$scope.streets[0].id;
                $scope.getParks($scope.streetid);
            };
        });
    };
    var getAreaById = function(areaid) {
        getPositionData.getAreaById(areaid).then(function(result) {
            var selectedArea = result;
            $scope.zoneCenterId = selectedArea.zoneid;
            $scope.getArea();
        });
    };
    var parkToZoneCenter = function() {
        $scope.parkid = selectParks[0].id;       
        if ($scope.parkid == undefined) {
            return;
        }
        getpark($scope.parkid).then(function(result) {
            $scope.streetid = result.streetId;
            getPositionData.getStreetById(result.streetId).then(function(result) {
                $scope.areaId = result.areaid;
                getAreaById($scope.areaId);
                $scope.getStreets();
            });
        });
    };
    parkToZoneCenter();
    
    
    //统计图1
    var dateInitialparkcharge = function() {
        $('#parkMonth').val(new Date().format('yyyy-MM'));
        $('#parkMonth').datepicker({
            autoClose : true,
            dateFormat : "yyyy-mm",
            months : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            showMonthAfterYear : true,
            viewStart : 1,
            weekStart : 1,
            yearSuffix : "年",
            isDisabled : function(date) {
                return date.valueOf() > Date.now() ? true : false;
            }
        });
        $scope.monthDate = $('#parkMonth').val();
    };
    dateInitialparkcharge();
    dateInitial();
    $scope.parkid=4;

    $scope.getParks = function(streetId) {
        if (streetId==undefined||streetId==null) {
            return;
        };
        getPositionData.getOutsideParkByStreetId(streetId).then(function(result) {
            $scope.parks = filtPark(result);       
            if($scope.parks!=undefined&&$scope.parks.length==1){
//            $scope.parkid=$scope.parks[0].id;   
            $scope.parkid=$scope.parks[0].id;
            
    //        $scope.selectPosdataByParkAndRange();
            }
        });
    };
    
    
    //作图1
    var renderChart = function(category, seriesData) {
        var myChart = echarts.init(document.getElementById('chart_park_period_charge'));
        var option = {
            title : {
                text : '停车场每日应收/实收费用对比',
                subtext : '月单位'
            },
            tooltip : {
                trigger : 'axis'
            },
            legend : {
                data : ['应收费用', '实收费用']
            },
            toolbox : {
                show : true,
                feature : {
                    mark : {
                        show : true
                    },
                    saveAsImage : {
                        show : true
                    }
                }
            },
            calculable : true,
            xAxis : [{
                type : 'category',
                boundaryGap : false,
                data : category
            }],
            yAxis : [{
                type : 'value',
                axisLabel : {
                    formatter : '{value} 元'
                }
            }],
            series : seriesData,
        };
        myChart.setOption(option);
    };

    var chartData = function() {
        var series1 = {
            name : '应收费用',
            type : 'line',
            data : $scope.totalMoney
        };
        var series2 = {
            name : '实收费用',
            type : 'line',
            data : $scope.realMoney
        };
        renderChart($scope.catagory, [series1, series2]);
    };
    
    
    //根据日期范围获取停车场收费
    $scope.getParkChargeByRange = function() {
        var dateselect = $('#parkMonth').val();
        var dateStart = new Date(dateselect.substring(0, 7) + '-01');
        var dateEnd = dateStart;
        dateEnd.setMonth(dateStart.getMonth() + 1);
        $scope.show1=true;
        getDataService.getParkChargeByRange($scope.parkid, dateselect.substring(0, 7) + '-01', dateEnd.getFullYear() + '-' + (dateEnd.getMonth()+1) + '-01').then(function(result) {
        $scope.show1=false;
        $scope.catagory = [];
        $scope.totalMoney = [];
        $scope.realMoney = [];
//        getDataService.getParkChargeByRange($scope.parkid, dateselect.substring(0, 7) + '-01', dateEnd.getFullYear() + '-' + (dateEnd.getMonth()+1) + '-01').then(function(result) {
            $.each(result, function(name, value) {
                var date = new Date(parseInt(name));
                var month = date.getMonth() + 1;
                var day = date.getDate();
                $scope.catagory.push(month + '-' + day);
                $scope.totalMoney.push(value['totalMoney']);
                $scope.realMoney.push(value['realMoney']);
            });
            chartData();
        });
    };
    
    
  //统计图2
    var dateInitialparkcharge2 = function() {
        $('#parkMonth2').val(new Date().format('yyyy-MM'));
        $('#parkMonth2').datepicker({
            autoClose : true,
            dateFormat : "yyyy-mm",
            months : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            showMonthAfterYear : true,
            viewStart : 1,
            weekStart : 1,
            yearSuffix : "年",
            isDisabled : function(date) {
                return date.valueOf() > Date.now() ? true : false;
            }
        });
        $scope.monthDate2 = $('#parkMonth2').val();
    };
    dateInitialparkcharge2();
    
       
  //统计图3
    var dateInitialparkcharge3 = function() {
        $('#parkMonth3').val(new Date().format('yyyy-MM'));
        $('#parkMonth3').datepicker({
            autoClose : true,
            dateFormat : "yyyy-mm",
            months : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            showMonthAfterYear : true,
            viewStart : 1,
            weekStart : 1,
            yearSuffix : "年",
            isDisabled : function(date) {
                return date.valueOf() > Date.now() ? true : false;
            }
        });
        $scope.monthDate3 = $('#parkMonth3').val();
    };
    dateInitialparkcharge3();
    
    //作图3
    var renderChart3 = function(category, seriesData) {
        var myChart3 = echarts.init(document.getElementById('chart_park_period_charge3'));
        var option3 = {
            title : {
                text : '停车场每日进出车辆数',
                subtext : '月单位'
            },
            tooltip : {
                trigger : 'axis'
            },
            legend : {
                data : ['次数']
            },
            toolbox : {
                show : true,
                feature : {
                    mark : {
                        show : true
                    },
                    saveAsImage : {
                        show : true
                    }
                }
            },
            calculable : true,
            xAxis : [{
                type : 'category',
                boundaryGap : false,
                data : category
            }],
            yAxis : [{
                type : 'value',
                axisLabel : {
                    formatter : '{value} '
                }
            }],
            series : seriesData,
        };
        myChart3.setOption(option3);
    };

    var chartData3 = function() {
        var series1 = {
            name : '次数',
            type : 'line',
            data : $scope.carnumberCount
        };
     
        renderChart3($scope.catagory3, [series1]);

    };
    
    //统计图2
    var dateInitialparkcharge2 = function() {
        $('#parkMonth2').val(new Date().format('yyyy-MM'));
        $('#parkMonth2').datepicker({
            autoClose : true,
            dateFormat : "yyyy-mm",
            months : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            showMonthAfterYear : true,
            viewStart : 1,
            weekStart : 1,
            yearSuffix : "年",
            isDisabled : function(date) {
                return date.valueOf() > Date.now() ? true : false;
            }
        });
        $scope.monthDate2 = $('#parkMonth2').val();
    };
    dateInitialparkcharge2();
    
    //作图2
    var renderChart2 = function(category, seriesData) {
        var myChart2 = echarts.init(document.getElementById('chart_park_period_charge2'));
        var option2 = {
            title : {
                text : '停车场每日出入量对比',
                subtext : '月单位'
            },
            tooltip : {
                trigger : 'axis'
            },
            legend : {
                data : ['次数']
            },
            toolbox : {
                show : true,
                feature : {
                    mark : {
                        show : true
                    },
                    saveAsImage : {
                        show : true
                    }
                }
            },
            calculable : true,
            xAxis : [{
                type : 'category',
                boundaryGap : false,
                data : category
            }],
            yAxis : [{
                type : 'value',
                axisLabel : {
                    formatter : '{value} '
                }
            }],
            series : seriesData,
        };
        myChart2.setOption(option2);
    };

    var chartData2 = function() {
        var series1 = {
            name : '入场',
            type : 'line',
            data : $scope.incount
        };
         var series2 = {
            name : '出场',
            type : 'line',
            data : $scope.outcount
        };
     
        renderChart2($scope.catagory, [series1,series2]);

    };
    
      $scope.incount = [];
      $scope.outcount = [];
      $scope.show1=false;
      $scope.show2=false;
      $scope.show3=false;
    //根据日期范围获取停车场进出次数
      $scope.getByDayAndDateDiffNoOut = function() {
          var dateselect = $('#parkMonth3').val();
          var dateStart = new Date(dateselect.substring(0, 7) + '-01');
          var dateEnd = dateStart;
          dateEnd.setMonth(dateStart.getMonth() + 1);
            $scope.show3=true;
          getDataService.getByDayAndDateDiffNoOut($scope.parkid, dateselect.substring(0, 7) + '-01', dateEnd.getFullYear() + '-' + (dateEnd.getMonth()+1) + '-01').then(function(result) {
          $scope.catagory3 = [];
         $scope.carnumberCount = [];
          $scope.show3=false;
          $.each(result, function(name, value) {
                  var date = new Date(parseInt(name));
                  var month = date.getMonth() + 1;
                  var day = date.getDate();
                  $scope.catagory3.push(month + '-' + day);
                  $scope.carnumberCount.push(value['count']);
                 
              });
              chartData3();
          });
      };
    
      //根据日期范围获取停车场停车次数
      $scope.getParkRecordsCountByRange = function() {
          var dateselect = $('#parkMonth2').val();
          var dateStart = new Date(dateselect.substring(0, 7) + '-01');
          var dateEnd = dateStart;
          dateEnd.setMonth(dateStart.getMonth() + 1);
          $scope.show2=true;
          getDataService.getParkRecordsCountByRange($scope.parkid, dateselect.substring(0, 7) + '-01', dateEnd.getFullYear() + '-' + (dateEnd.getMonth()+1) + '-01').then(function(result) {
          $scope.show2=false;
          $scope.catagory = [];
          $scope.incount = [];
          $scope.outcount = [];
          $.each(result, function(name, value) {
                  var date = new Date(parseInt(name));
                  var month = date.getMonth() + 1;
                  var day = date.getDate();
                  $scope.catagory.push(month + '-' + day);
                  $scope.incount.push(value['in']);
                  $scope.outcount.push(value['out']);
              });
              chartData2();
          });
      };

    
    //获取posdata 并处理
    $scope.selectPosdataByParkAndRange = function(parkId) {
   //     $scope.parkid=parkid;
        if($scope.parkid==undefined||$scope.parkid==null){
            return;
        }
        $scope.getParkById();
     
        var date = $('#date').val();
        var dateInit = new Date(date);
        dateInit.setDate(dateInit.getDate() + 1);
        var dateEnd = dateInit.getFullYear() + "-" + (dateInit.getMonth() + 1) + "-" + dateInit.getDate();
        // getDataService.selectPosdataByParkAndRange($scope.parkid, date, dateEnd).then(function(data) {
            // $scope.dayChargeTotal = 0;
            // $scope.dayRealReceiveMoney = 0;
            // $scope.dayCarsCount = 0;
            // $scope.carsInCount = 0;
            // $scope.carsOutCount = 0;
            // if (data.status == 1002) {
                // getPosChargeDataByParkAndRange($scope.parkid, date, dateEnd);                               
                // return;
            // }
            // data = data['body'];
            // for (var i = 0; i < data.length; i++) {
                // if (data[i]['mode'] == 1) {
                    // $scope.dayChargeTotal += data[i]['money'];
                    // $scope.dayRealReceiveMoney = $scope.dayRealReceiveMoney + data[i]['giving'] + data[i]['realmoney'] - data[i]['returnmoney'];
                    // $scope.carsOutCount++;
                // }
                // if (data[i]['mode'] == 0) {
                    // $scope.dayCarsCount++;
                    // $scope.carsInCount++;
                // }
            // }
        // });
    };
    
  //获取posdata 并处理
    $scope.selectPosdataByParkAndRange2 = function() {
        if($scope.parkid==undefined||$scope.parkid==null){
            return;
        }
        $scope.getParkById2();
//        $scope.getParkChargeByRange();
//        $scope.getParkRecordsCountByRange();
        var date = $('#date').val();
        var dateInit = new Date(date);
        dateInit.setDate(dateInit.getDate() + 1);
        var dateEnd = dateInit.getFullYear() + "-" + (dateInit.getMonth() + 1) + "-" + dateInit.getDate();
        getDataService.selectPosdataByParkAndRange2($scope.parkid, date, dateEnd).then(function(data) {
            $scope.dayChargeTotal = 0;
            $scope.dayRealReceiveMoney = 0;
            $scope.dayCarsCount = 0;
            $scope.carsInCount = 0;
            $scope.carsOutCount = 0;
            if (data.status == 1002) {
                getPosChargeDataByParkAndRange2($scope.parkid, date, dateEnd);                               
                return;
            }
            data = data['body'];
            for (var i = 0; i < data.length; i++) {
                if (data[i]['mode'] == 1) {
                    $scope.dayChargeTotal += data[i]['money'];
                    $scope.dayRealReceiveMoney = $scope.dayRealReceiveMoney + data[i]['giving'] + data[i]['realmoney'] - data[i]['returnmoney'];
                    $scope.carsOutCount++;
                }
                if (data[i]['mode'] == 0) {
                    $scope.dayCarsCount++;
                    $scope.carsInCount++;
                }
            }
        });
    };
    
    var getPosChargeDataByParkAndRange2 = function(parkid, startDay, endDay) {
        getDataService.getPosChargeDataByParkAndRange2(parkid, startDay, endDay).then(function(data) {
            if (data.status == 1002) {
                return;
            }
            data = data['body'];
            $scope.dayChargeTotal = 0;
            $scope.dayRealReceiveMoney = 0;
            $scope.dayCarsCount = 0;
            $scope.carsInCount = 0;
            $scope.carsOutCount = 0;
            for (var i = 0; i < data.length; i++) {
                $scope.dayChargeTotal += data[i]['chargeMoney'];
                $scope.dayRealReceiveMoney = $scope.dayRealReceiveMoney + data[i]['paidMoney'] + data[i]['givenMoney'] - data[i]['changeMoney'];
                $scope.carsInCount++;
                if (data[i]['exitDate'] != null) {
                    $scope.carsOutCount++;
                    $scope.dayCarsCount++;
                }
            }
        });
    };
    
   
    var getPosChargeDataByParkAndRange = function(parkid, startDay, endDay) {
        getDataService.getPosChargeDataByParkAndRange(parkid, startDay, endDay).then(function(data) {
            if (data.status == 1002) {
                return;
            }
            data = data['body'];
            $scope.dayChargeTotal = 0;
            $scope.dayRealReceiveMoney = 0;
            $scope.dayCarsCount = 0;
            $scope.carsInCount = 0;
            $scope.carsOutCount = 0;
            for (var i = 0; i < data.length; i++) {
                $scope.dayChargeTotal += data[i]['chargeMoney'];
                $scope.dayRealReceiveMoney = $scope.dayRealReceiveMoney + data[i]['paidMoney'] + data[i]['givenMoney'] - data[i]['changeMoney'];
                $scope.carsInCount++;
                if (data[i]['exitDate'] != null) {
                    $scope.carsOutCount++;
                    $scope.dayCarsCount++;
                }
            }
        });
    };
    //获取停车场 得到停车位数据
    $scope.getParkById = function() {
        getDataService.getParkById($scope.parkid).then(function(result) {
            $scope.carportsCount = result.portCount;
            $scope.selectedParkName = result.name;
            $scope.selectedParkNum = result.id;
            $scope.principalName = result.contact;
            $scope.contactNumber = result.number;
        });
    };
 //   $scope.selectPosdataByParkAndRange();
    
  //获取停车场 得到停车位数据
    $scope.getParkById2 = function() {
        getDataService.getParkById2($scope.parkid).then(function(result) {
            $scope.carportsCount = result.portCount;
            $scope.selectedParkName = result.name;
            $scope.selectedParkNum = result.id;
            $scope.principalName = result.contact;
            $scope.contactNumber = result.number;
        });
    };
 //   $scope.selectPosdataByParkAndRange2();

}).service("getDataService", function($http, $q) {
    var getParkChargeByRange = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/pos/charge/getParkChargeByRange',
            method : 'post',
            data : {
                'parkId' : parkId,
                'startDay' : startDay,
                'endDay' : endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return promise;
    };
    
    var getParkRecordsCountByRange = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/pos/charge/getParkRecordsCountByRange',
            method : 'post',
            data : {
                'parkId' : parkId,
                'startDay' : startDay,
                'endDay' : endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return promise;
    };
    
      var getByDayAndDateDiffNoOut = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/pos/charge/getByDayAndDateDiffNoOut',
            method : 'post',
            data : {
                'parkId' : parkId,
                'startDay' : startDay,
                'endDay' : endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return promise;
    };

    var selectPosdataByParkAndRange = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/pos/selectPosdataByParkAndRange',
            method : 'post',
            data : {
                'parkId' : parkId,
                'startDay' : startDay,
                'endDay' : endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return promise;
    };
    
    var selectPosdataByParkAndRange2 = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/pos/selectPosdataByParkAndRange',
            method : 'post',
            data : {
                'parkId' : parkId,
                'startDay' : startDay,
                'endDay' : endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return promise;
    };
    
    var getPosChargeDataByParkAndRange = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        $http({
            url : '/parkinfo/pos/charge/getByParkAndRange',
            method : 'post',
            data : {
                'parkId' : parkId,
                'startDay' : startDay,
                'endDay' : endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return deferred.promise;
    };
    
    var getPosChargeDataByParkAndRange2 = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        $http({
            url : '/parkinfo/pos/charge/getByParkAndRange',
            method : 'post',
            data : {
                'parkId' : parkId,
                'startDay' : startDay,
                'endDay' : endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return deferred.promise;
    };
    
    var getParkById = function(id) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/getPark/' + id,
            method : 'get'
        }).success(function(response) {
            if (response.status = 1001) {
                deferred.resolve(response.body);
            };
        });
        return promise;
    };
    
    var getParkById2 = function(id) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/getPark/' + id,
            method : 'get'
        }).success(function(response) {
            if (response.status = 1001) {
                deferred.resolve(response.body);
            };
        });
        return promise;
    };
    return {
        getParkChargeByRange : getParkChargeByRange,
        getParkRecordsCountByRange : getParkRecordsCountByRange,
        getByDayAndDateDiffNoOut : getByDayAndDateDiffNoOut,
        selectPosdataByParkAndRange : selectPosdataByParkAndRange,
        selectPosdataByParkAndRange2 : selectPosdataByParkAndRange2,
        getParkById : getParkById,
        getParkById2 : getParkById2,
        getPosChargeDataByParkAndRange : getPosChargeDataByParkAndRange,
        getPosChargeDataByParkAndRange2 : getPosChargeDataByParkAndRange2
    };
}).factory("getPositionData", function($http, $q) {
    var getZoneCenter = function() {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/parkinfo/zoneCenter/getByStartAndCount",
            method : 'get'
        }).success(function(response) {
            deferred.resolve(response.body);
        });
        return promise;
    };
    var getAreaById = function(areaid) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        if (!areaid) {
            return;
        }
        $http({
            url : '/parkinfo/area/selectByPrimaryKey/' + areaid,
            method : 'get'
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        });
        return promise;
    };
    var getArea = function(zoneid) {
        if (!zoneid) {
            return;
        }
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/area/getByZoneCenterId/' + zoneid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        });
        return promise;
    };
    var getStreetByAreaId = function(areaid) {
       
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/parkinfo/street/getByAreaid/" + areaid,
            method : 'get'
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        });
        return promise;
    };
    var getOutsideParkByStreetId = function(streetId) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/parkinfo/getOutsideParkByStreetId/" + streetId,
            method : 'get'
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        });
        return promise;
    };
    var getStreetById = function(streetId) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/parkinfo/street/selectByPrimaryKey/" + streetId,
            method : 'get'
        }).success(function(response) {
            deferred.resolve(response.body);
        });
        return promise;
    };
    return {
        getZoneCenter : getZoneCenter,
        getArea : getArea,
        getStreetByAreaId : getStreetByAreaId,
        getAreaById : getAreaById,
        getOutsideParkByStreetId : getOutsideParkByStreetId,
        getStreetById : getStreetById
    };

});
