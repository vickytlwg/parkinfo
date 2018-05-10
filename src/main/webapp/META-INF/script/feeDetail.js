var chargeApp = angular.module("feeDetailApp", ['ui.bootstrap', 'tm.pagination']);

chargeApp.controller("feeDetailCtrl", ['$scope', '$http', '$window', 'textModal', 'textModalTest', '$modal', '$timeout',
function($scope, $http, $window, textModal, textModalTest, $uibModal, $timeout) {

    //define table content
    $scope.detail = {
        items : [],
        loading : false,
        page : {
            hidden : true,
            allCounts : 0,
            size : 100,
            indexRange : [1],
            index : 1
        }
    };

 $scope.searchDate=new Date().format('yyyy-MM-dd hh:mm:ss');
 $scope.startDate=new Date(new Date().getTime()-1000*60*60*24).format('yyyy-MM-dd hh:mm:ss');
 $scope.endDate=new Date().format('yyyy-MM-dd hh:mm:ss');
 $scope.startDate1=new Date(new Date().getTime()-1000*60*60*24).format('yyyy-MM-dd hh:mm:ss');
 $scope.endDate1=new Date().format('yyyy-MM-dd hh:mm:ss');
 $scope.startDate22=new Date(new Date().getTime()-1000*60*60*24).format('yyyy-MM-dd hh:mm:ss');
 $scope.endDate22=new Date().format('yyyy-MM-dd hh:mm:ss');
 $scope.startDate33=new Date(new Date().getTime()-1000*60*60*24).format('yyyy-MM-dd hh:mm:ss');
 $scope.endDate33=new Date().format('yyyy-MM-dd hh:mm:ss');
      var dateInitial=function(){
    	  /*在停车*/
    	  $('#date').datetimepicker({
    		  format: 'YYYY-MM-DD HH:mm:ss', 
    		  locale: moment.locale('zh-cn')
    	   });
    	  $('#date1').datetimepicker({
    		  format: 'YYYY-MM-DD HH:mm:ss', 
    		  locale: moment.locale('zh-cn')
   	   		});
    	  
    	  /*免费*/
    	   $('#date2').datetimepicker({
    		   format: 'YYYY-MM-DD HH:mm:ss', 
     		  locale: moment.locale('zh-cn') 
  	   		});
    	   $('#date3').datetimepicker({
    		   format: 'YYYY-MM-DD HH:mm:ss', 
     		  locale: moment.locale('zh-cn') 
  	   		});
    	   
    	   /*记录时间查*/
    	   $('#date4').datetimepicker({
    		   format: 'YYYY-MM-DD HH:mm:ss', 
     		  	locale: moment.locale('zh-cn')
  	   		});
    	   $('#date5').datetimepicker({
    		   format: 'YYYY-MM-DD HH:mm:ss', 
     		  	locale: moment.locale('zh-cn')
  	   		});
    	   
    	   /*收费记录*/
    	   $('#date6').datetimepicker({
    		   format: 'YYYY-MM-DD HH:mm:ss', 
     		  	locale: moment.locale('zh-cn')
  	   		});
    	   $('#date7').datetimepicker({
    		   format: 'YYYY-MM-DD HH:mm:ss', 
     		  	locale: moment.locale('zh-cn')
  	   		});
    	   
        /*$('.date').datepicker({
            autoClose: true,
            dateFormat: "yyyy-mm-dd",
            days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
            daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
            daysMin: ["日", "一", "二", "三", "四", "五", "六"],
            months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            showMonthAfterYear: true,
            viewStart: 0,
            weekStart: 1,
            yearSuffix: "年",
            isDisabled: function(date){return date.valueOf() > Date.now() ? true : false;}        
        });*/
    };  
    
     $scope.paginationConf = {
            currentPage: 1,
            totalItems: 500,
            itemsPerPage: 30,
            pagesLength: 10,
            perPageOptions: [20,30,40,50],
            rememberPerPage: 'perPageItems',
            onChange: function(){
                getInitail($scope.pagedata);
            }
        };
        $scope.pagedata=[];
       
      var getdiff = function(datediff) {
        var nd = 1000 * 24 * 60 * 60;
        // 一天的毫秒数
        var nh = 1000 * 60 * 60;
        // 一小时的毫秒数
        var nm = 1000 * 60;
        // 一分钟的毫秒数
        var ns = 1000;
        // 一秒钟的毫秒数

        var day = Math.floor(datediff / nd);
        var hour = Math.floor(datediff % nd / nh);
        var minutes = Math.floor(datediff % nd % nh / nm);
        if (day != 0) {
            return day + "天" + hour + '小时' + minutes + '分钟';
        }
        if (day == 0 && hour != 0) {
            return hour + '小时' + minutes + '分钟';
        } else {
            return minutes + '分钟';
        }
    };
    var getInitail = function(data) {
        $scope.pagedata = data;
        $scope.paginationConf.totalItems = data.length;
        var currentData = [];
        var start = ($scope.paginationConf.currentPage - 1) * $scope.paginationConf.itemsPerPage;
        for (var i = 0; i < $scope.paginationConf.itemsPerPage; i++) {
            if (data.length > (start + i)) {
                var tmpdata = data[start + i];
                var enddate = new Date();
                var startdate = new Date(tmpdata["entranceDate"]);

                if (tmpdata["exitDate"] != undefined && tmpdata["exitDate"] != null) {
                    enddate = new Date(tmpdata["exitDate"]);
                }
                tmpdata.range = getdiff(enddate.getTime() - startdate.getTime());
                currentData[i] = data[start + i];
            }
        };
        $scope.detail.items = currentData;
    };
        
    $scope.searchDateTime=function(){
    	$http({
    		url:'/parkinfo/pos/charge/getByParkDatetime',
	    	method:'post',
	    	data:{"startDate":$("#date4").val(),"endDate":$("#date5").val(),"parkId":$('#park-select3').val()}
    	}).success(function(response){
            if(response.status==1001){
                getInitail(response.body);
            }
        });
    };
    
    $scope.searchParkingRecords=function(){
        $http({
            url:'/parkinfo/pos/charge/getParkingData',
            method:'post',
            data:{"startDate":$("#date").val(),"endDate":$("#date1").val(),"parkId":$('#park-select').val()}
        }).success(function(response){
            if(response.status==1001){
                getInitail(response.body);
            }
        });
    };

    $scope.searchFreeRecords=function(){
         $http({
            url:'/parkinfo/pos/charge/getFreeData',
            method:'post',
            data:{"startDate":$("#date2").val(),"endDate":$("#date3").val(),"parkId":$('#park-select2').val()}
        }).success(function(response){
            if(response.status==1001){
                getInitail(response.body);
            }
        });
    };

    $scope.searchChargeMoney=function(){
    	/*alert("开始时间："+$scope.startDate333+"结束时间："+$scope.endDate333);*/
    	$http({
            url:'/parkinfo/pos/charge/getChargeMoneyData',
            method:'post',
            data:{"startDate":$("#date6").val(),"endDate":$("#date7").val(),"parkId":$('#park-select4').val()}
        }).success(function(response){
            if(response.status==1001){
                getInitail(response.body);
            }
        });
    }
   dateInitial();

    $scope.detail.getCount = function() {
        $http.get('count').success(function(response) {
            if (response.status == 1001) {
                $scope.detail.page.hidden = false;
                $scope.detail.page.allCounts = response.body;
                var maxIndex = Math.ceil($scope.detail.page.allCounts / $scope.detail.page.size);
                $scope.detail.page.indexRange = [1];
                for (var i = 2; i <= maxIndex; i++)
                    $scope.detail.page.indexRange.push(i);
            } else
                textModal.open($scope, "错误", "获取计费错误: " + response.status);
        }).error(function(response) {
            textModal.open($scope, "错误", "获取计费错误: " + response.status);

        });
    };

    //previous page
    $scope.detail.previousPage = function() {
        if ($scope.detail.page.index <= 1)
            return;
        $scope.detail.page.index--;
        $scope.detail.getPage();
    };
    $scope.searchText = "";
    $scope.searchByCardnumber = function() {
        if ($scope.searchText == "" || $scope.searchText == undefined) {
            return;
        }
        $http({
            url : 'getByCardnumberAuthority',
            method : 'post',
            data : {
                "cardNumber" : $scope.searchText
            }
        }).success(function(response) {
            if (response.status == 1001) {
                getInitail(response.body);
            }
        });
    };
    $scope.getExcelByDay = function() {
        $window.location.href = "getExcelByDay?date=" + $scope.searchDate;
    };
    $scope.getExcelByDayRange = function() {
        $window.location.href = "getExcelByDayRange?startDate=" + $scope.startDate + "&endDate=" + $scope.endDate;
    };
    $scope.getExcelByParkAndDay = function() {
        $window.location.href = "getExcelByParkAndDay?date=" + $scope.searchDate + "&parkId=" + $('#park-select').val();
    };
    $scope.getExcelByParkAndDayRange = function() {
        $window.location.href = "getExcelByParkAndDayRange?startDate=" + $scope.startDate + "&endDate=" + $scope.endDate + "&parkId=" + $('#park-select2').val();
    };
    $scope.searchByParkName = function() {
        if ($scope.searchParkNameText == "" || $scope.searchParkNameText == undefined) {
            return;
        }
        $http({
            url : 'getByParkName',
            method : 'post',
            data : {
                "parkName" : $scope.searchParkNameText
            }
        }).success(function(response) {
            if (response.status == 1001) {
                getInitail(response.body);
            }
        });
    };
    //first page
    $scope.detail.firstPage = function() {
        if ($scope.detail.page.index <= 1)
            return;
        $scope.detail.page.index = 1;
        $scope.detail.getPage();
    };

    //next page
    $scope.detail.nextPage = function() {
        if ($scope.detail.page.index >= $scope.detail.page.indexRange.length)
            return;
        $scope.detail.page.index++;
        $scope.detail.getPage();
    };

    //last page
    $scope.detail.lastPage = function() {
        if ($scope.detail.page.index >= $scope.detail.page.indexRange.length)
            return;
        $scope.detail.page.index = $scope.detail.page.indexRange.length;
        $scope.detail.getPage();
    };

    //get one page detail
    $scope.detail.getPage = function() {
        if ($scope.detail.page.index > $scope.detail.page.indexRange.length) {
            textModal.open($scope, "错误", "当前页不存在!");
            return;
        }
        $scope.detail.loading = true;

        var pageArgs = {
            page : {
                index : $scope.detail.page.index,
                size : $scope.detail.page.size
            },
            property : ["id", "name", "sex", "phone", "createTime"],
            order : {
                createTime : true
            }
        };

        $http.post('page', {
            low : ($scope.detail.page.index - 1) * $scope.detail.page.size,
            count : $scope.detail.page.size
        }).success(function(response) {
            $scope.detail.loading = false;

            if (response.status == 1001) {
                $scope.detail.items = [];
                $scope.detail.items = response.body;
            } else
                textModal.open($scope, "错误", "获取计费信息错误:" + response.status);

        }).error(function(response) {
            $scope.detail.loading = false;
            textModal.open($scope, "错误", "获取计费信息错误: " + +response.status);

        });
    };

    //init page
    $scope.detail.getCount();
    $scope.detail.refresh = $scope.detail.getPage;
    $scope.detail.getPage();

}]);

var feeDetail = angular.module('feeDetailApp');

feeDetail.service('textModal', ['$uibModal',
function($uibModal) {

    this.open = function($scope, header, body) {
        $scope.textShowModal = $uibModal.open({
            templateUrl : '/park/views/template/text-modal.html',
            controller : 'textCtrl',
            scope : $scope,
            resolve : {
                msg : function() {
                    return {
                        header : header,
                        body : body
                    };
                }
            }
        });
    };

    this.close = function($scope, header, body) {
        $scope.textShowModal.close('cancel');
    };

}]);
feeDetail.service('textModalTest', function($uibModal) {
    var modalInstance;
    var open = function($scope) {
        modalInstance = $uibModal.open({
            templateUrl : 'myModalTest',
            scope : $scope,
            //      controller:'feeDetailCtrl',
            backdrop : 'static'
        });
        modalInstance.opened.then(function() {
            console.log('modalInstance is opened');
        });
        modalInstance.result.then(function(result) {
            console.log(result);
        });
    };

    var close = function(result) {
        modalInstance.close(result);
        return '能得到消息吗';
    };
    return {
        open : open,
        close : close
    };
});
//show message dilog controller
feeDetail.controller('textCtrl', function($scope, $uibModalInstance, $http, msg) {
    $scope.text = msg;
    $scope.close = function() {
        $uibModalInstance.close('cancel');
    };
});

