
function openModal(modal) {
    if (modal == null) return
    modal.classList.add('active')
    let overlay = document.getElementById('overlay');
    if(overlay !== undefined)
        overlay.classList.add('active')
}

function closeModal(modal) {
    if (modal == null) return
    modal.classList.remove('active')
    let overlay = document.getElementById('overlay');
    if(overlay !== undefined)
        overlay.classList.remove('active')
}

function downloadImage(e) {
    let element = e.toElement;
    let name = element.getAttribute('file');
    query('api/images/download-link/' + element.getAttribute('image-id'), null, 'GET', 'text',
        (data)=>{
            saveAs(data, name);
        });
}