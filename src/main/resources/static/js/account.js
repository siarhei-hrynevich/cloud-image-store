
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
    init: function () {
        this.popUp.init();
        this.popUp.hide();
        hideBanner();
        query('api/images/user/', null, 'GET', 'text', this.onLoadImages)
    },
    onLoadImages: function (data) {
        currentPage.images = data;
        let imageSection = document.getElementById('images');
        for (let i = 0; i < data.length; i++) {
            imageSection.appendChild(convertImageToDOM(data[i]));
        }
    },
    clone: function () {
        return {
            popUp: this.popUp,
            images: this.images,
            onLoadImages: this.onLoadImages,
            init: this.init,
            clone: this.clone
        }
    }
}