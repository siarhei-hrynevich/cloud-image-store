
currentPage = {

    init: function () {
        hideBanner();
        document.querySelector('.upload-btn').onclick = (e) => {
            upload(document.getElementById('file'), document.getElementById('name'));
        }
    },
    clone: function () {
        return { init: this.init }
    }
}

function upload(file, name) {
    let formData = new FormData();
    formData.append('file', file.files[0]);
    formData.append('name', name.value);
    $.ajax({
        url: 'api/images/',
        data: formData,
        enctype: "multipart/form-file",
        cache: false,
        processData: false,
        contentType: false,
        type: 'POST',
        headers: getHeaders()
    });
}