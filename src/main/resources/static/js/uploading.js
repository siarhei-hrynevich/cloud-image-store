
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

// function dragenter(e) {
//     e.stopPropagation();
//     e.preventDefault();
// }
//
// function dragover(e) {
//     e.stopPropagation();
//     e.preventDefault();
// }
//
// function drop(e) {
//     e.stopPropagation();
//     e.preventDefault();
//
//     var dt = e.dataTransfer;
//     var files = dt.files;
//     upload(files);
// }
//
// let dropbox = document.querySelector('.dropbox');
//
// document.querySelector('.upload-btn').addEventListener('click', ()=>{
//     upload(dropbox.files);
// });
//
// dropbox.addEventListener("dragenter", dragenter, false);
// dropbox.addEventListener("dragover", dragover, false);
// dropbox.addEventListener("drop", drop, false);