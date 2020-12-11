currentPage = {

    popUp: {
        show: function () {
            openModal(document.getElementById('image-modal'));

        },
        hide: function () {
            closeModal(document.getElementById('image-modal'));
        },
        init: function () {
            document.querySelector('[data-close-button]').addEventListener('click', currentPage.popUp.hide);
            document.getElementById('download-link').addEventListener('click', downloadImage);
        }
    },

    images: [],

    initEventHandlers: function () {
        let clearIcon = document.querySelector(".clear-icon");
        let searchBar = document.querySelector(".search");
        searchBar.addEventListener("keyup", () => {
            if (searchBar.value && clearIcon.style.visibility !== "visible") {
                clearIcon.style.visibility = "visible";
            } else if (!searchBar.value) {
                clearIcon.style.visibility = "hidden";
            }
        });

        clearIcon.addEventListener("click", () => {
            searchBar.value = "";
            clearIcon.style.visibility = "hidden";
        });

        document.getElementById('search').addEventListener("click", (e) => {
            let searchField = document.querySelector(".search");
            this.search(searchField.value);
        });
    },

    search: function (desiredValue) {
        let tag = document.getElementById('tag').value;
        if(tag === '')
            query("/api/images/search?name=" + desiredValue, null, 'GET', 'text', this.updateImages);
        else
            query("/api/images/tag_search?name=" + desiredValue + '&tag=' + tag, null, 'GET', 'text', this.updateImages);

    },

    updateImages: function (data) {
        let imagesContainer = document.querySelector('.images');
        imagesContainer.innerHTML = '';
        currentPage.images = data;
        for (let i = 0; i < data.length; i++) {
            let image = convertImageToDOM(data[i]);
            imagesContainer.appendChild(image);
        }
    },

    init: function () {
        this.popUp.init();
        this.popUp.hide();
        document.querySelector('[data-close-button]').addEventListener('click', this.popUp.hide);
        document.getElementById('download-link').addEventListener('click', downloadImage);
        hideBanner();
        this.initEventHandlers();
        query("/api/images/last?count=10", null, 'GET', 'text', this.updateImages);
    },
    clone: function () {
        return {
            popUp: this.popUp,
            images: this.images,
            initEventHandlers: this.initEventHandlers,
            search: this.search,
            updateImages: this.updateImages,
            init: this.init
        }
    }
}
