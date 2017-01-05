
var app = angular.module('ListeEnviesTranslation', ['pascalprecht.translate']);

app.config(['$translateProvider', function ($translateProvider) {
    // add translation table
    $translateProvider.useUrlLoader('lang/fr-FR.json');
    $translateProvider.preferredLanguage('fr-FR');
    $translateProvider.useSanitizeValueStrategy('sanitizeParameters');
}]);