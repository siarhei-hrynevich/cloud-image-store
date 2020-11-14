
currentPage = {
    init: function () {
        hideBanner();
        let clearIcon = document.querySelector(".clear-icon");
        let searchBar = document.querySelector(".search");
        searchBar.addEventListener("keyup", () => {
            if(searchBar.value && clearIcon.style.visibility != "visible"){
                clearIcon.style.visibility = "visible";
            } else if(!searchBar.value) {
                clearIcon.style.visibility = "hidden";
            }
        });

        clearIcon.addEventListener("click", () => {
            searchBar.value = "";
            clearIcon.style.visibility = "hidden";
        });
    },
    clone: function () {
        return { init: this.init }
    }
}