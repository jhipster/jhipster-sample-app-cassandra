 'use strict';

angular.module('sampleCassandraApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-sampleCassandraApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-sampleCassandraApp-params')});
                }
                return response;
            }
        };
    });
