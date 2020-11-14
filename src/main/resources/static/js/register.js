
currentPage = {
    init: function () {
        hideBanner();
    },
    clone: function () {
        return { init: currentPage.init }
    }
}