'use strict';

angular.module('samplecassandraApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
