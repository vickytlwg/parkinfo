var chargeApp = angular.module("feeDetailApp", ['ui.bootstrap']);

chargeApp.controller("feeDetailCtrl", ['$scope', '$http', '$window','textModal', 'textModalTest','$modal', '$timeout',
function($scope, $http,$window, textModal,textModalTest, $uibModal, $timeout) {

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
 $scope.searchDate=new Date().format('yyyy-MM-dd');
 $scope.startDate=new Date().format('yyyy-MM-dd hh:mm:ss');
 $scope.endDate=new Date().format('yyyy-MM-dd hh:mm:ss');
      var dateInitial=function(){
        $('.date').datepicker({
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
        });
    };  
   dateInitial();

    $scope.detail.getCount = function() {
        $http.get('/parkinfo/pos/charge/count').success(function(response) {
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

    $scope.selectedPark={};
    $scope.selectParks = [];
    var getSelectData = function() {
        var options = $('#get_Park').get(0).options;
        for (var i = 0; i < options.length; i++) {
            var item = {
                value : $(options[i]).val(),
                name : $(options[i]).text()
            };
             $scope.selectParks.push(item);
        };
        $scope.selectedPark=$scope.selectParks[0];
    };
    getSelectData();
    
     $scope.$watch("selectedPark",function(){
        $scope.detail.getPage();
    },true);
    //previous page
    $scope.detail.previousPage = function() {
        if ($scope.detail.page.index <= 1)
            return;
        $scope.detail.page.index--;
        $scope.detail.getPage();
    };
    $scope.searchText="";
    $scope.parkId="";
    $scope.searchByCardnumber=function(){
        if($scope.searchText==""||$scope.searchText==undefined){
            return;
        }
        $http({
            url:'/parkinfo/pos/charge/getByCardnumber',
            method:'post',
            data:{"cardNumber":$scope.searchText,"parkId":$scope.parkId}
        }).success(function(response){
            if(response.status==1001){
                $scope.detail.items=response.body;
            }
        });
    };
    $scope.getExcelByDay=function(){  
         $window.location.href="/parkinfo/pos/charge/getExcelByDay?date="+$scope.searchDate;
        };
    $scope.getExcelByDayRange=function(){  
         $window.location.href="/parkinfo/pos/charge/getExcelByDayRange?startDate="+$scope.startDate+"&endDate="+$scope.endDate;
        };
    $scope.getExcelByParkAndDay=function(){
         $window.location.href="/parkinfo/pos/charge/getExcelByParkAndDay?date="+$scope.searchDate+"&parkId="+$('#park-select').val();
     };
     $scope.getExcelByParkAndDayRange=function(){
         $window.location.href="/parkinfo/pos/charge/getExcelByParkAndDayRange?startDate="+$scope.startDate+"&endDate="+$scope.endDate
         +"&parkId="+$('#park-select2').val();
     };
     $scope.searchByParkName=function(){
        if($scope.searchParkNameText==""||$scope.searchParkNameText==undefined){
            return;
        }
        $http({
            url:'/parkinfo/pos/charge/getByParkName',
            method:'post',
            data:{"parkName":$scope.searchParkNameText}
        }).success(function(response){
            if(response.status==1001){
                $scope.detail.items=response.body;
            }
        });
    };
    //刷新
    $scope.refreshUser=function(){
        $http({
            url:'/parkinfo/monthUser/getByStartAndCount',
            method:'post',
            params:{start:$scope.start,count:$scope.count}
        }).success(function(response){
            if(response.status==1001){
                $scope.users=response.body;
            }
            else{
               textModal.open($scope,"错误","数据请求失败");
            }
        }).error(function(){
            textModal.open($scope,"错误","数据请求失败");
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

          $http.post('/parkinfo/pos/charge/pageByParkId', {
            start : ($scope.detail.page.index - 1) * $scope.detail.page.size,
            count : $scope.detail.page.size,
            parkId: parseInt($scope.selectedPark.value)
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
            templateUrl : 'text-modal.html',
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
    var open=function($scope){
    modalInstance = $uibModal.open({
        templateUrl:'myModalTest',
        scope : $scope,
  //      controller:'feeDetailCtrl',
        backdrop:'static'
    });
    modalInstance.opened.then(function(        
    ){
       console.log('modalInstance is opened'); 
    });
    modalInstance.result.then(function(result){
        console.log(result);
    });
};
    
    var close=function(result){
        modalInstance.close(result);
        return '能得到消息吗';
    };
    return{
        open:open,
        close:close
    };
});
//show message dilog controller
feeDetail.controller('textCtrl', function($scope, $uibModalInstance, $http, msg) {
    $scope.text = msg;
    $scope.close = function() {
        $uibModalInstance.close('cancel');
    };
});

