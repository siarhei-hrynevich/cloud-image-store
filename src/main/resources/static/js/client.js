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
    contentURL = url;
    query(url, null, 'GET', 'text/html', updateContentSection);
}

function query(url, data, method, contentType, onSuccess) {
    let token = getCookie('jwt');
    if (token === undefined)
        token = '';
    $.ajax({
        url: url,
        data: data,
        cache: false,
        processData: false,
        contentType: contentType,
        type: method,
        success: onSuccess,
        headers: {
            "partial": "true",
            "Authorization": "Bearer " + token
        }
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
    setTimeout(
        () => {
            let pageInArray = pages.find((page, i, arr) => {
                return page.name === contentURL;
            });
            if (pageInArray === undefined) {
                if (currentPage !== undefined) {
                    let clone = currentPage.clone();
                    clone.name = contentURL;
                    pages.push(clone);
                }
            } else {
                currentPage = pageInArray;
            }
            currentPage.init()
        },
        100);
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