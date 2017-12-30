angular.module("generalDataViewApp", []).controller("generalDataViewCtrl", ['$scope', 'getDataService', 'getDataServicePos',
function($scope, getDataService, getDataServicePos) {
    $scope.parkdatasArray = [];
         $scope.dayAmountMoney = 0;
        $scope.dayRealMoney = 0;
        $scope.dayOutCount = 0;
         $scope.dayOnlineParkCount = 0;
        $scope.dayParkCount = 0;
        $scope.dayCarportCount = 0;
        $scope.dayOnlineCarportCount = 0;
        $scope.dayCarportLeftCount = 0;
    $scope.processData = function(data) {
//        $scope.dayAmountMoney = 0;
//        $scope.dayRealMoney = 0;
        // $scope.dayOutCount = 0;
        // $scope.dayParkCount = 0;
        // $scope.dayCarportCount = 0;
   //     $scope.dayOnlineCarportCount = 0;
        $scope.dayCarportLeftCount = 0;
        angular.forEach(data, function(value) {
            //        $scope.dayAmountMoney += value.amountmoney;
            //         $scope.dayRealMoney += value.realmoney;
            //         $scope.dayParkCount += value.parkcount;
            //         $scope.dayCarportCount += value.carportcount;
      //      $scope.dayOnlineCarportCount += value.onlinecarportcount;
       //     $scope.dayCarportLeftCount += value.carportleftcount;
            //         $scope.dayOutCount += value.outcount;
        });
    };

    var getparks = function() {
        getDataServicePos.getParks().then(function(result) {
            angular.forEach(result, function(value) {
                $scope.parkid = value.id;
                var parkdatas = {};
                $scope.selectPosdataByParkAndRange(parkdatas, value.id);
            });
        });
    };
    getparks();
    //获取停车场 得到停车位数据
    $scope.getParkById = function(parkdatas, parkid,online) {
        getDataServicePos.getParkById(parkid).then(function(result) {
            parkdatas.carportsCount = result.portCount;
            $scope.dayCarportCount += parkdatas.carportsCount||0;
            if(online){
                $scope.dayOnlineCarportCount += parkdatas.carportsCount||0;
                $scope.dayOnlineParkCount+=1;
            }
            console.log(online);
            parkdatas.selectedParkName = result.name;
            parkdatas.selectedParkNum = result.id;
            parkdatas.principalName = result.contact;
            
            parkdatas.contactNumber = result.number;
        });
    };

    //获取posdata 并处理
    $scope.selectPosdataByParkAndRange = function(parkdatas, parkid) {
        if ($scope.parkid == undefined || $scope.parkid == null) {
            return;
        }

        var dateInit = new Date();
        var date = dateInit.getFullYear() + "-" + (dateInit.getMonth() + 1) + "-" + dateInit.getDate();
        dateInit.setDate(dateInit.getDate() + 1);
        var dateEnd = dateInit.getFullYear() + "-" + (dateInit.getMonth() + 1) + "-" + dateInit.getDate();
        getDataServicePos.selectPosdataByParkAndRange(parkid, date, dateEnd).then(function(data) {

            parkdatas.dayChargeTotal = 0;
            parkdatas.dayRealReceiveMoney = 0;
            parkdatas.dayCarsCount = 0;
            parkdatas.carsInCount = 0;
            parkdatas.carsOutCount = 0;
            if (data.status == 1002) {
                getPosChargeDataByParkAndRange(parkid, date, dateEnd, parkdatas);
                return;
            }
            data = data['body'];
            if (data != null) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i]['mode'] == 1) {
                        parkdatas.dayChargeTotal += data[i]['money'];
                        parkdatas.dayRealReceiveMoney = $scope.dayRealReceiveMoney + data[i]['giving'] + data[i]['realmoney'] - data[i]['returnmoney'];
                        parkdatas.carsOutCount++;
                    }
                    if (data[i]['mode'] == 0) {
                        parkdatas.dayCarsCount++;
                        parkdatas.carsInCount++;
                    }
                }
            }
             $scope.getParkById(parkdatas, parkid,true);
            $scope.dayAmountMoney += parkdatas.dayChargeTotal||0;
            $scope.dayRealMoney += parkdatas.dayRealReceiveMoney||0;
            $scope.dayParkCount ++;
            var carportscount=parkdatas.carportsCount||0;           
            $scope.dayCarportLeftCount+=(parkdatas.carportsCount||0+parkdatas.dayCarsCount||0-parkdatas.carsInCount);
            $scope.dayOutCount += parkdatas.carsOutCount||0;
        
            $scope.parkdatasArray.push(parkdatas);
        });
    };

    var getPosChargeDataByParkAndRange = function(parkid, startDay, endDay, parkdatas) {
        getDataServicePos.getPosChargeDataByParkAndRange(parkid, startDay, endDay).then(function(data) {
            if (data.status == 1002) {
                $scope.getParkById(parkdatas, parkid,false);             
                $scope.dayParkCount ++;
                $scope.parkdatasArray.push(parkdatas);
                return;
            }
            data = data['body'];
            parkdatas.dayChargeTotal = 0;
            parkdatas.dayRealReceiveMoney = 0;
            parkdatas.dayCarsCount = 0;
            parkdatas.carsInCount = 0;
            parkdatas.carsOutCount = 0;
            if (data != null) {
                for (var i = 0; i < data.length; i++) {
                    parkdatas.dayChargeTotal += data[i]['chargeMoney'];
                    parkdatas.dayRealReceiveMoney = parkdatas.dayRealReceiveMoney + data[i]['paidMoney'] + data[i]['givenMoney'] - data[i]['changeMoney'];
                    parkdatas.carsInCount++;
                    if (data[i]['exitDate'] != null) {
                        parkdatas.carsOutCount++;
                        parkdatas.dayCarsCount++;
                    }
                }
            }
            $scope.getParkById(parkdatas, parkid,true);
            $scope.dayAmountMoney += parkdatas.dayChargeTotal||0;
            $scope.dayRealMoney += parkdatas.dayRealReceiveMoney||0;
            $scope.dayParkCount ++;
            $scope.dayCarportLeftCount+=(parkdatas.carportsCount||0+parkdatas.dayCarsCount||0-parkdatas.carsInCount||0);           
            
            $scope.dayOutCount += parkdatas.carsOutCount||0;
         
            $scope.parkdatasArray.push(parkdatas);
        });
    };

    $scope.toParkInfo = function() {
        parent.location.href = "/parkinfo/parkingInfo/";
    };
    // getDataService.getZoneCenterInfo().then(function(result) {
        // $scope.zoneCenters = result;
        // $scope.processData(result);
      // //  console.log($scope.dayOnlineCarportCount + "  " + $scope.dayOutCount);
    // });
}]).service('getDataService', ['$http', '$q',
function($http, $q) {
    var getZoneCenter = function() {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/parkinfo/zoneCenter/getByStartAndCount",
            method : 'post',
            params : {
                start : 0,
                count : 100
            }
        }).success(function(response) {
            deferred.resolve(response.body);
        });
        return promise;
    };
    var getZoneCenterInfo = function() {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/parkinfo/outsideParkInfo/zoneCenterInfo",
            method : 'post',
            params : {
                start : 0,
                count : 100
            }
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        });
        return promise;
    };
    var getArea = function(zoneid) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/area/getByZoneCenterId/' + zoneid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function() {
            deferred.reject("error");
        });
        return promise;
    };
    var getAreaInfo = function(zoneid) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/outsideParkInfo/areaInfo/' + zoneid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function() {
            deferred.reject("error");
        });
        return promise;
    };
    var getStreetInfo = function(areaid) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/outsideParkInfo/streetInfo/' + areaid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function() {
            deferred.reject("error");
        });
        return promise;
    };
    var getParkInfo = function(streetid) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/outsideParkInfo/parkInfo/' + streetid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function() {
            deferred.reject("error");
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

    return {
        getZoneCenter : getZoneCenter,
        getZoneCenterInfo : getZoneCenterInfo,
        getAreaInfo : getAreaInfo,
        getStreetInfo : getStreetInfo,
        getParkInfo : getParkInfo,
        getAreaByZoneId : getArea,
        getStreetByAreaId : getStreetByAreaId,
        getOutsideParkByStreetId : getOutsideParkByStreetId
    };
}]).service("getDataServicePos", function($http, $q) {
    var getParkChargeByRange = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/pos/getParkChargeByRange',
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
    var getParks = function() {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/parkinfo/getParks',
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
        selectPosdataByParkAndRange : selectPosdataByParkAndRange,
        getParkById : getParkById,
        getPosChargeDataByParkAndRange : getPosChargeDataByParkAndRange,
        getParks : getParks
    };
});
