
currentPage = {
    init: function () {
        hideBanner();
    },
    clone: function () {
        return { init: this.init }
    }
}

function upload(files) {
    var formData = new FormData();
    formData.append('files', files);

    //var xhr = new XMLHttpRequest();

    //xhr.open('POST', '/images/', false);
    //xhr.open('GET', 'https://www.google.com/', false);

    //xhr.send(formData);


    $.ajax({
        url: '/images/',
        data: formData,
        cache: false,
        processData: false,
        contentType: 'multipart/form-data',
        type: 'POST',
        success: function(data){
            alert(data);
        }
    });


    // if (xhr.status != 200) {
    //     alert( xhr.status + ': ' + xhr.statusText );
    // } else {
    //     alert( xhr.responseText );
    // }
}

function dragenter(e) {
    e.stopPropagation();
    e.preventDefault();
}

function dragover(e) {
    e.stopPropagation();
    e.preventDefault();
}

function drop(e) {
    e.stopPropagation();
    e.preventDefault();

    var dt = e.dataTransfer;
    var files = dt.files;
    upload(files);
}

let dropbox = document.querySelector('.dropbox');

document.querySelector('.upload-btn').addEventListener('click', ()=>{
    upload(dropbox.files);
});

dropbox.addEventListener("dragenter", dragenter, false);
dropbox.addEventListener("dragover", dragover, false);
dropbox.addEventListener("drop", drop, false);