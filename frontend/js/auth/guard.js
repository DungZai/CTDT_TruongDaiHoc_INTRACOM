(function () {
  if (!TokenService.isValid()) {
    TokenService.clear();
    window.location.href = '/frontend/pages/login.html';
  }
})();