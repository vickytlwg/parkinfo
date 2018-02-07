var parkUserApp=angular.module("parkUserApp", ['ui.bootstrap'])
parkUserApp.controller("parkUserCtrl",['$scope', '$http','$window','$modal', 'textModalTest','textModal',
function($scope,$http,$window,$uibModal,textModalTest,textModal){
	$scope.detail = {
	    	users : [],
	        loading : false
	    };
	
	$scope.selectChange=function(index){
        if($scope.users[index].checked){
            $scope.checkedIndex = index;
            return;
        }
        for(var i = 0; i < $scope.users.length; i++){
            var item = $scope.users[i];
            if(item.checked != undefined && item.checked == true){
                $scope.checkedIndex = i;
                return;
            }
        }
        $scope.checkedIndex = -1;
    };
    $scope.refreshUser();
    
	
	$scope.insertUser=function(){
        $uibModal.open({
            templateUrl: 'modifyUser',
            controller: 'monthUserModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return undefined;
                }
            }
        });
    };
    
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
    $scope.updateUser=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        $uibModal.open({
            templateUrl: 'modifyUser',
            controller: 'monthUserModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return $scope.checkedIndex;
                }
            }
        });
    };
	
   
}]);

parkUserApp.controller("parkUserCtrl",function($scope,$http,$modalInstance,getPositionData,textModal,index){
    
    $scope.tempUser = $scope.$parent.users[index];
   
    $scope.getParkNames=function(){
        $scope.parkNames="";
        $http({
            url:'/parkinfo/monthUser/getParkNamesByUserId/'+$scope.tempUser.id,
            method:'get'
        }).success(function(response){
            if(response.status==1001){
              var items=response.body;
             for(var i=0;i<items.length;i++){
                 $scope.parkNames+=items[i].parkname+'\n';
                 
             }
              
            }
            
        });
    };
    
    $scope.parks=[]; 
    $scope.getArea = function() {
      getPositionData.getArea($scope.zoneCenterId).then(function(result) {
          $scope.areas = result;  
           $scope.parks=[];
          angular.forEach(result,function(area){
          getPositionData.getOutsideParkByAreaId(area.id).then(function(result) {
               angular.forEach(result,function(result){
                   $scope.parks.push(result);
               });
      });
          });      
      });
      
  };
   $scope.getZoneCenter = function() {
      getPositionData.getZoneCenter().then(function(result) {
          $scope.zoneCenters = result;
      });
  };
  
  $scope.getZoneCenter();
   $scope.getStreets = function() {
       if($scope.areaId==undefined||$scope.areaId==null){
          return;
      }
      getPositionData.getStreetByAreaId($scope.areaId).then(function(result) {
          $scope.streets = result;
         
      });
      getPositionData.getOutsideParkByAreaId($scope.areaId).then(function(result){
          $scope.parks=result;
      });
  };   
    var getAreaById = function(areaid) {
      getPositionData.getAreaById(areaid).then(function(result) {
          var selectedArea = result;
          $scope.zoneCenterId = selectedArea.zoneid;
          $scope.getArea();
      });
  };
      $scope.getParks = function(streetId) {
      if (streetId==undefined||streetId==null) {
          return;
      };
    
      getPositionData.getOutsideParkByStreetId(streetId).then(function(result) {
          $scope.parks = result;   
          });           
  };
});

parkUserApp.factory("getPositionData",function($scope,$http,$modalInstance,getPositionData,textModal,index){
      var getZoneCenter=function(){
            var deferred=$q.defer();
            var promise=deferred.promise;
            $http({
                url:"/parkinfo/zoneCenter/getByStartAndCount",
                method:'post',
                params:{start:0,count:100}
            }).success(function(response){   
                    deferred.resolve(response.body);           
            });
            return promise;
        };
       var getAreaById=function(areaid){
           var deferred=$q.defer();
           var promise=deferred.promise;
           if (!areaid) {
            return;
            }
            $http({
                url:'/parkinfo/area/selectByPrimaryKey/'+areaid,
                method:'get'
            }).success(function(response){
                if(response.status==1001){
                    deferred.resolve(response.body); 
                }
            });
            return promise;
       };
       var getArea=function(zoneid){
           var deferred=$q.defer();
           var promise=deferred.promise;
           if (!zoneid) {
            return;
        }
            $http({
                url:'/parkinfo/area/getByZoneCenterId/'+zoneid,
                method:'get',
            }).success(function(response){
                if(response.status==1001){
                     deferred.resolve(response.body);  
                }
            });
            return promise;
        };
        var getStreetById=function(areaid){
           var deferred=$q.defer();
           var promise=deferred.promise;           
             $http({
                 url:"/parkinfo/street/getByAreaid/"+areaid,
                 method:'get'
             }).success(function(response){
                 if(response.status==1001){
                    deferred.resolve(response.body); 
                 }
             });
             return promise;
         }; 
         var getOutsideParkByStreetId=function(streetId){
           var deferred=$q.defer();
           var promise=deferred.promise;           
             $http({
                 url:"/parkinfo/getOutsideParkByStreetId/"+streetId,
                 method:'get'
             }).success(function(response){
                 if(response.status==1001){
                    deferred.resolve(response.body); 
                 }
             });
             return promise;
         };
         return {
             getZoneCenter:getZoneCenter,
             getArea:getArea,            
             getStreetByAreaId:getStreetById,
             getAreaById:getAreaById,
             getOutsideParkByStreetId:getOutsideParkByStreetId
         };
          
});
var parkDetail = angular.module('parkUserApp');
parkDetail.service('textModal', ['$uibModal',
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
parkDetail.service('textModalTest', function($uibModal) {
    var modalInstance;
    var open=function($scope){
    modalInstance = $uibModal.open({
        templateUrl:'modifyUser',
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
parkDetail.controller('textCtrl', function($scope, $uibModalInstance, $http, msg) {
    $scope.text = msg;
    $scope.close = function() {
        $uibModalInstance.close('cancel');
    };
    
})

