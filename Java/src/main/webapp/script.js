let name = document.getElementById('name');
let size = document.getElementById('size');
let format = document.getElementById('format');
let resolution = document.getElementById('resolution');
let inputFile = document.getElementById('inputFile');
let inputImg = document.getElementById('inputImg');
let resultImg = document.getElementById('resultImg');
let pixelize = document.getElementById('pixelize');
let fname = '';
let valid = true;

/**
 * ----------------------------- Load
 */
function upload(target) {
    clear();
    if (target.files &&
        target.files.length > 0 &&
        target.files[0] &&
        target.files[0].size > 0 &&
        inputFile.value !== '') {

        let img = target.files[0];
        let reader = new FileReader();
        reader.onloadend = function () {
            inputImg.src = reader.result;
            inputImg.style.display = 'block';
        };
        reader.readAsDataURL(img);

        name.textContent = `name: ${fname = img.name}`;
        format.textContent = `format: ${img.type}`;
        setSize(target);
        setTimeout(setResolution, 100);
    } else {
        valid = false;
    }
}
function setSize(input) {
    let totalBytes = input.files[0].size;
    let res;

    if (totalBytes < 1000000)
        res = `${(totalBytes / 1000).toFixed(2)} KB`;
    else
        res = `${(totalBytes / 1000000).toFixed(2)} MB`;
    size.textContent = `size: ${res}`;
}

function setResolution() {
    resolution.textContent = `resolution: ${inputImg.naturalWidth} x ${inputImg.naturalHeight}`;
}

/**
 * ----------------------------- Clear
 */
document.getElementById('fileClear').addEventListener('click',  clear);

function clear() {
    fname = '';
    name.textContent = '';
    size.textContent = '';
    format.textContent = '';
    resolution.textContent = '';
    inputImg.src = '#';
    inputImg.style.display = 'none';
    resultImg.src = '#';
    resultImg.style.display = 'none';
}

/**
 * ----------------------------- Range
 */
document.getElementById('pixRange').addEventListener('click', function () {
    document.getElementById('outNumber').textContent =
        'x' + document.getElementById('pixRange').value;
});

/**
 * ----------------------------- Download
 */
document.querySelectorAll('.download').forEach(
    item => item.addEventListener('click', function () {
        download(item.getAttribute('value'));
    })
);
function download(name) {
    if (document.getElementById('resultImg').src !== 'http://localhost:8080/pixelizator/'
    && fname !== '') {
        let tag = document.createElement('a');
        let newName = fname.substr(0, fname.lastIndexOf('.', fname.length))
                    + '_pix.' + name;

        tag.setAttribute('download', newName);
        tag.setAttribute('href', document.getElementById('resultImg').src);
        tag.click();
        tag.remove();
    } else {
        alert('No image to download');
    }
}

/**
 * ----------------------------- Server request
 */
pixelize.addEventListener('click', async function () {
    if (valid) {
        let img = inputFile.files[0];
        let pixRange = document.getElementById('pixRange').value;
        let formData = new FormData();

        if (img !== 0) {
            formData.append('file', img);
            formData.set('fileName', fname);
            formData.set('pixRange', pixRange);
            formData.set('type', fname.substr(fname.lastIndexOf('.', fname.length) + 1));
            formData.set('algorithm', document.querySelector('input[name="algorithms"]:checked').value);
            formData.set('filter', document.querySelector('input[name="filters"]:checked').value);

            let response = await fetch('http://localhost:8080/pixelizator/pix',{
                method: 'POST',
                body: formData,
                enctype: 'multipart/form-data'
            });

            if (response.ok) {
                let blob = await response.blob();
                let reader = new FileReader();
                reader.onloadend = function(event){
                    resultImg.src = event.target.result;
                    resultImg.style.display = 'block';
                };
                reader.readAsDataURL(blob);
            } else {
                alert('Error in server post method');
            }
        } else {
            alert('Invalid image for pixelization');
        }
    } else {
        alert('Choose valid image for pixelization');
    }
});






