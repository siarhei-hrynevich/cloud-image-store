
function convertImageToDOM(image) {
    let imageDOM = document.createElement('img');
    imageDOM.className = 'image';
    imageDOM.src = changeProtocol(image.url);
    imageDOM.addEventListener('click', onClickImage);
    imageDOM.setAttribute('id', image.id);
    imageDOM.setAttribute('name', image.name);
    imageDOM.setAttribute('downloads', image.downloads);
    imageDOM.setAttribute('user-id', image.userID);
    return imageDOM;
}

function convertImageDOMtoImage(imageDOM) {
    return {
        name: imageDOM.getAttribute('name'),
        url: imageDOM.getAttribute('src'),
        id: Number(imageDOM.id),
        userID: Number(imageDOM.getAttribute('user-id')),
        downloads: imageDOM.getAttribute('downloads')
    };
}

function onClickImage(e) {
    let imageDOM = e.toElement;
    if (imageDOM !== undefined) {
        let image = convertImageDOMtoImage(imageDOM);

        let nameElement = document.getElementById('image-name-popup');
        let userLinkElement = document.getElementById('image-user-popup');
        let imageElement = document.getElementById('image-popup');
        let downloadsElement = document.getElementById('image-downloads-popup');
        nameElement.innerHTML = image.name;
        userLinkElement.href = "/user-account?id=" + image.userID;
        downloadsElement.innerHTML = image.downloads;
        currentPage.targetImage = image;
        imageElement.src = image.url;
        fillButtonsAttributes(image)
        currentPage.popUp.show();
    }
}

function fillButtonsAttributes(image) {
    let buttons = document.getElementsByTagName('button');
    let file = image.name + image.url.substr(image.url.lastIndexOf('.'));
    for (let i = 0; i < buttons.length; i++) {
        buttons.item(i).setAttribute('link', image.url);
        buttons.item(i).setAttribute('file', file);
        buttons.item(i).setAttribute('image-id', image.id);
    }
}