'use strict';

angular.module('samplecassandraApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


