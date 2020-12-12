/**
 * All pages must to implement methods:
 * {
 *  init(),
 *  clone()
 * }
 * Old pages are stored in variable pages
 * On saving page gets a name (url)
 * When user click to old page, it are found from pages by url and invoked method init()
 *
 * Functions in client.js:
 *  showBanner()
 *  hideBanner()
 *  updateContent(url)
 *  query(url, data, method, contentType, onSuccess) - add auth token to query
 */

let bannerSection = document.getElementById('banner');
let contentSection = document.getElementById('contentSection');
let currentPage;
let pages = [];
let scripts = [];
let mainPage = '/about';
let contentURL;


//'multipart/form-data'
function updateContent(url) {
    if(url.charAt(0) === '/')
        contentURL = url.substring(1);
    else
        contentURL = url;
    query(url, null, 'GET', 'text/html', updateContentSection);
}

function getHeaders() {
    let token = getCookie('jwt');
    if (token === undefined)
        token = '';
    return {
        "partial": "true",
        "Authorization": "Bearer " + token
    };
}

function query(url, data, method, contentType, onSuccess) {
    $.ajax({
        url: url,
        data: data,
        cache: false,
        processData: false,
        contentType: contentType,
        type: method,
        success: onSuccess,
        headers: getHeaders()
    });
}

function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function showBanner() {
    bannerSection.innerHTML = banner;
    query('/api/images/totalImagesCount', null, 'GET', 'text', updateCount);
}

function updateCount(data) {
    document.getElementById('counter').innerHTML = String(data);
}

function hideBanner() {
    bannerSection.innerHTML = noBanner;
}

function updateContentSection(content, status, xhr) {
    contentSection.innerHTML = '';
    let parser = new DOMParser();
    let document = parser.parseFromString(content, 'text/html');
    let body = document.getElementsByTagName('body').item(0);
    contentSection.appendChild(body);
    processScripts(body.getElementsByTagName('script'));
    initPage();
    cancelBtn.onclick();
}

function savePage() {
    if (currentPage !== undefined) {
        let clone = currentPage.clone();
        clone.name = contentURL;
        pages.push(clone);
    }
}

function initPage() {
    let pageInArray = pages.find((page, i, arr) => {
        return page.name.includes(contentURL) || contentURL.includes(page.name);
    });

    if (pageInArray === undefined) {
        waitPage();
    } else {
        currentPage = pageInArray;
        currentPage.init()
    }
}

function waitPage() {
    if(currentPage === undefined || currentPage.name !== undefined) {
        setTimeout(waitPage, 50);
    } else {
        currentPage.name = contentURL;
        savePage();
        currentPage.init();
    }
}

function processScripts(scripts) {
    for (let i = 0; i < scripts.length; i++) {
        processScript(scripts.item(i));
    }
}


function processScript(node) {
    if (scripts.indexOf(node.src) < 0) {
        let tag = document.createElement('script');
        tag.textContent = node.textContent;
        tag.src = node.src;
        contentSection.appendChild(tag);
        scripts.push(tag.src);
    }
}

function navigate(e) {
    e.stopPropagation();
    e.preventDefault();

    let url = $(e.target).attr('href');

    updateContent(url);
    history.pushState({page: url}, '', url);
}

function popState(e) {
    let url = (e.state && e.state.page) || mainPage;
    updateContent(url);
}

$('body').on('click', 'a[data-link="ajax"]', navigate);
window.onpopstate = popState;