 'use strict';

angular.module('samplecassandraApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-samplecassandraApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-samplecassandraApp-params')});
                }
                return response;
            },
        };
    });