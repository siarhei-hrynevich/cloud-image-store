
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
            let deleteButton = document.getElementById('delete-link');
            if(deleteButton !== undefined)
                deleteButton.addEventListener('click', onDeleteClick);
        }
    },
    images: [],
    init: function () {
        this.popUp.init();
        this.popUp.hide();
        hideBanner();
        const urlParams = new URLSearchParams(window.location.search);
        let id = urlParams.get('id');
        if(id === null)
            id = "";
        query('api/images/user/' + id, null, 'GET', 'text', this.onLoadImages)
    },
    onLoadImages: function (data) {
        currentPage.images = data;
        let imageSection = document.getElementById('images');
        imageSection.innerHTML = '';
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

function onDeleteClick() {
    let id = currentPage.targetImage.id;
    query('/api/images/' + id, null, 'DELETE', 'text', onDeleteSuccess);
}
function onDeleteSuccess(data) {
    currentPage.images = currentPage.images.filter(item => { return item.id !== currentPage.targetImage.id; });
    currentPage.onLoadImages(currentPage.images);
    currentPage.popUp.hide();
}