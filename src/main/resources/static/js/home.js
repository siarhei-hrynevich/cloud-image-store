
currentPage = {
    init: function () {
        showBanner();
    },
    clone: function () {
        return { init: currentPage.init }
    }
}