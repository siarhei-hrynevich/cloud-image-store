
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