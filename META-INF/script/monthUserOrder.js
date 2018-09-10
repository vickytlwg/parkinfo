var monthUserApp=angular.module("monthUserApp",['ui.bootstrap']);
monthUserApp.controller("monthUserCtrl",['$scope', '$http','$uibModal', 'textModal', '$timeout',
function($scope,$http,$uibModal,textModal,$timeout){
    $scope.users=[];
    $scope.checkedIndex=-1;
    $scope.start=0;
    $scope.count=200;
     $scope.parks=[];
    $scope.getParks=function(){
        $http({
            url:'/parkinfo/getParks?_t=' + (new Date()).getTime(),
            method:'get'
        }).success(function(response){
            if(response.status=1001){
                var body=response.body;
                for(var i=0;i<body.length;i++){
                    
                        $scope.parks.push(body[i]);
                    
                }
                $scope.selectValue=$scope.parks[0];
            }
        });
    };
    $scope.getParks();
    $scope.selectChanged=function(selectedParkId){
          
              var data={};
              data.start=$scope.start;
              data.count=$scope.count;
              data.parkId=selectedParkId;
        $http({
            url:'/parkinfo/monthUser/getByParkIdAndCountOrder',
            method:'post',
            data:angular.toJson(data)
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
    $scope.refreshUser=function(){
        $http({
            url:'/parkinfo/monthUser/getByStartAndCountOrder',
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
    $scope.updateUserPark=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
          $uibModal.open({
            templateUrl: '/parkinfo/views/template/monthUserParkUpdate.html',
            controller: 'userParkCtrl',
            scope:$scope,
            resolve: {
                index: function(){
                       return $scope.checkedIndex;
                }
            }
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
    $scope.deleteUser=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        var id=$scope.users[$scope.checkedIndex].id;
        $http({
            url:'/parkinfo/monthUser/delete/'+id,
            method:'get'
        }).success(function(response){
            if(response.status==1001)
            {
                textModal.open($scope, "成功","操作成功");
                $scope.refreshUser();
            }
            else{
                textModal.open($scope, "失败","操作失败");
            }
        }).error(function(){
             textModal.open($scope, "失败","操作失败");
        });
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
}]);

monthUserApp.controller("monthUserModify",function($scope, textModal,$modalInstance, $http, $timeout, index){
    var url = '/parkinfo/monthUser/insertOrder';
    $scope.tempUser={};
    if(index != undefined){
        $scope.tempUser = $scope.$parent.users[index];
        url = '/parkinfo/monthUser/updateOrder';
    }else{
    $scope.tempUser.starttime=new Date().format("yyyy-MM-dd hh:mm:ss");
    $scope.tempUser.endtime=new Date().format("yyyy-MM-dd hh:mm:ss");
    }   
    $scope.statuses=[{value:0,text:'未支付'},{value:1,text:'已支付'}];
  
    $scope.parks=[];
    $scope.getParks=function(){
        $http({
            url:'/parkinfo/getParks?_t=' + (new Date()).getTime(),
            method:'get'
        }).success(function(response){
            if(response.status=1001){
                var body=response.body;
                for(var i=0;i<body.length;i++){
                    
                        $scope.parks.push(body[i]);
                    
                }
                $scope.selectValue=$scope.parks[0];
            }
        });
    };
    $scope.getParks();
    $scope.showResult=false;
    $scope.showLoader=false;
    $scope.result='';
    $scope.submit = function(){
        $scope.showLoader=true;
        $http({
            url:url,
            method:'post',
            data:$scope.tempUser,
        }).success(function(response){
            if(response.status==1001){
                $scope.result="成功";
                $scope.showResult=true;
                $scope.showLoader=false;
                $timeout(function(){
                      $scope.close('ok');
                },2000);
              
                $scope.$parent.refreshUser();
                
            }
            else
            {
                textModal.open($scope, "失败","操作失败");
            }
        }).error(function(){
            textModal.open($scope, "失败","操作失败");
        });
    };
    $scope.close = function(){

        $modalInstance.close('cancel');
    };

});

monthUserApp.controller("userParkCtrl",function($scope,$http,$modalInstance,getPositionData,textModal,index){
    
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
    $scope.getParkNames();
    $scope.showResult=false;
    $scope.showLoader=false;
    $scope.result='';
    $scope.add=function(){
        if(!$scope.tempUser.id){
            return;
        }
        $http({
            url:"/parkinfo/monthUser/insertPark",
            method:'post',
            data:{'monthuserid':$scope.tempUser.id,'parkid':$scope.parkid}
        }).success(function(response){
            if(response.status==1001){
                $scope.result="成功";
                $scope.showResult=true;
                $scope.showLoader=false;
                $scope.getParkNames();
            }
            else{
                textModal.open($scope, "失败","操作失败");
            }
        });
    };
    $scope.deletePark=function(){
        if(!$scope.tempUser.id){
            return;
        }
        $http({
            url:"/parkinfo/monthUser/deletePark",
            method:'post',
            data:{'monthuserid':$scope.tempUser.id,'parkid':$scope.parkid}
        }).success(function(response){
            if(response.status==1001){
                $scope.result="成功";
                $scope.showResult=true;
                $scope.showLoader=false;
                $scope.getParkNames();
            }
            else{
                textModal.open($scope, "失败","操作失败");
            }
        });
    };
    
    $scope.getZoneCenter=function(){
        getPositionData.getZoneCenter().then(function(result){
            $scope.zoneCenters=result;
        });
    };
    $scope.getZoneCenter();
    $scope.getArea=function(zoneid){
        getPositionData.getArea(zoneid).then(function(result){
            $scope.areas=result;
        });
    };
    $scope.getStreets=function(areaid){
        $scope.streets=getPositionData.getStreetByAreaId(areaid).then(function(result){
            $scope.streets=result;
        });
    };
    $scope.getParks=function(streetId){
        getPositionData.getOutsideParkByStreetId(streetId).then(function(result){
            $scope.parks=result;
        });
    };
    $scope.close = function(){
        $modalInstance.close('cancel');
    };
});
monthUserApp.factory("getPositionData",function($http,$q){
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
monthUserApp.service('textModal',  ['$uibModal', function($uibModal){
    
    this.open = function($scope, header, body){
        $scope.textShowModal = $uibModal.open({
            templateUrl: '/parkinfo/views/template/text-modal.html',
            controller: 'textCtrl',
            scope: $scope,
            resolve:{
                msg:function(){ return {header:header, body:body}; }
            }
        });
    };
    
    this.close = function($scope, header, body){
        $scope.textShowModal.close('cancel');
    };   
}]);


  monthUserApp.controller('textCtrl',  function($scope, $uibModalInstance, $http, msg){
    $scope.text = msg;   
    $scope.close = function(){
        $uibModalInstance.close('cancel');
    };
});
