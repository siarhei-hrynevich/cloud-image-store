currentPage = {

    popUp: {
        show: function () {
            openModal(document.getElementById('image-modal'));

        },
        hide: function () {
            closeModal(document.getElementById('image-modal'));
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
        query("/api/images/search?name=" + desiredValue, null, 'GET', 'text', this.updateImages);
    },

    updateImages: function (data) {
        let imagesContainer = document.querySelector('.images');
        imagesContainer.innerHTML = '';
        currentPage.images = data;
        for (let i = 0; i < data.length; i++) {
            let image = convertImageToDOM(data[i]);
            image.addEventListener('click', currentPage.onClickImage);
            imagesContainer.appendChild(image);
        }
    },

    onClickImage: function (e) {
        let targetSection = document.getElementById('target');
        targetSection.innerHTML = '';
        let id = Number(e.toElement.getAttribute('id'));
        let target = currentPage.images.find((image, index, array) => image.id === id);
        if (target !== undefined) {
            let nameElement = document.createElement('span');
            nameElement.innerText = target.name;
            targetSection.appendChild(nameElement);
            targetSection.appendChild(convertImageToDOM(target));
            let downloadBtn = document.getElementById('download-link');
            let file = target.name + target.url.substr(target.url.lastIndexOf('.'));
            downloadBtn.setAttribute('link', target.url);
            downloadBtn.setAttribute('file', file);
            currentPage.popUp.show();
        }

    },

    init: function () {
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
            onClickImage: this.onClickImage,
            init: this.init
        }
    }
}

function downloadImage(e) {
    let element = e.toElement;
    let link = element.getAttribute('link');
    let name = element.getAttribute('file');
    saveAs(link, name);
}

function convertImageToDOM(image) {
    let imageDOM = document.createElement('img');
    imageDOM.className = 'image';
    imageDOM.src = image.url;
    imageDOM.setAttribute('id', image.id);
    return imageDOM;
}