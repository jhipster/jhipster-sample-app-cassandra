(function () {
    'use strict';

    angular
        .module('jhipsterCassandraSampleApplicationApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
