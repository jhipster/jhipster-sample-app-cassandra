'use strict';

angular.module('sampleCassandraApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


