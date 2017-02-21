(function() {
    'use strict';

    angular
        .module('jhipsterCassandraSampleApplicationApp')
        .controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = ['Principal', 'User', 'ParseLinks', 'AlertService', 'JhiLanguageService'];

    function UserManagementController(Principal, User, ParseLinks, AlertService, JhiLanguageService) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.currentAccount = null;
        vm.languages = null;
        vm.loadAll = loadAll;
        vm.setActive = setActive;
        vm.users = [];
        

        vm.loadAll();
        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });
        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });

        function setActive (user, isActivated) {
            user.activated = isActivated;
            User.update(user, function () {
                vm.loadAll();
                vm.clear();
            });
        }

        function loadAll () {
            User.query(onSuccess, onError);
        }

        function onSuccess(data, headers) {
            
            vm.users = data;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

    }
})();
