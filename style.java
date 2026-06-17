// Data koleksi gambar mini galeri untuk setiap item
const miniGalleryData = {
  pantaiCB: [
    '2.jpg','3.jpg','4.jpg','5.jpg','6.jpg','7.jpg'
  ],
  nasiKerabu: [
    'restoran.jpg','masjid gua.jpg','taman negara.jpg','masjid buluh.jpg','warong.jpg'
  ],
  stong: [
    'Lata Kashmir.jpg','Lata Rek.jpg','Lata Berangin.jpg','Lata Janggut.jpg'
  ],
};

let currentGalleryKey = null;
let currentGalleryIndex = 0;

function showMiniGallery(key, index = 0) {
  const images = miniGalleryData[key];
  if (!images) return;
  currentGalleryKey = key;
  currentGalleryIndex = index;
  const img = images[currentGalleryIndex];
  let galeriHTML = `
    <div style="display:flex;flex-direction:column;align-items:center;">
      <img src="${img}" style="max-width:90vw;max-height:70vh;width:100%;border-radius:16px;box-shadow:0 6px 32px #0008;margin-bottom:1rem;" alt="" />
      <div style="margin-top:1rem;display:flex;gap:1.5rem;align-items:center;">
        <button onclick="prevGalleryImage()" style="padding:0.6em 1.2em;font-size:1.5em;border-radius:8px;background:#222;border:none;color:#ffcc00;cursor:pointer;">&#8592;</button>
        <span style="color:#fff;font-weight:bold;">${currentGalleryIndex + 1} / ${images.length}</span>
        <button onclick="nextGalleryImage()" style="padding:0.6em 1.2em;font-size:1.5em;border-radius:8px;background:#222;border:none;color:#ffcc00;cursor:pointer;">&#8594;</button>
      </div>
    </div>
  `;
  openModal(galeriHTML);
}
function nextGalleryImage() {
  const images = miniGalleryData[currentGalleryKey];
  if (!images) return;
  currentGalleryIndex = (currentGalleryIndex + 1) % images.length;
  showMiniGallery(currentGalleryKey, currentGalleryIndex);
}
function prevGalleryImage() {
  const images = miniGalleryData[currentGalleryKey];
  if (!images) return;
  currentGalleryIndex = (currentGalleryIndex - 1 + images.length) % images.length;
  showMiniGallery(currentGalleryKey, currentGalleryIndex);
}
function toggleDropdown(id) {
  const el = document.getElementById(id);
  if (!el) return;
  el.classList.toggle('active');
}
function openModal(contentHTML) {
  const modal = document.getElementById('modalOverlay');
  const modalBody = document.getElementById('modalBody');
  modalBody.innerHTML = contentHTML;
  modal.style.display = 'flex';
  document.getElementById('modalContent').focus();
}
function closeModal() {
  const modal = document.getElementById('modalOverlay');
  const modalBody = document.getElementById('modalBody');
  modal.style.display = 'none';
  modalBody.innerHTML = '';
  const video = modalBody.querySelector('video');
  if (video) {
    video.pause();
    video.src = '';
  }
}
function closeOnOutsideClick(e) {
  if (e.target.id === 'modalOverlay') {
    closeModal();
  }
}
document.getElementById('modalOverlay').addEventListener('click', closeOnOutsideClick);
function showMap(locationURL) {
  const mapHTML = `<iframe src="${locationURL}" width="100%" height="450" style="border:0;" allowfullscreen="" loading="lazy"></iframe>`;
  openModal(mapHTML);
}
function showShopList(title, shops) {
  let listHTML = `<h3 style="color:#ffcc00; margin-bottom:1rem;">${title}</h3><ul style="list-style-type:disc; padding-left:1.5rem;">`;
  shops.forEach(shop => {
    listHTML += `<li style="margin-bottom:0.5rem;"><a href="${shop.mapURL}" target="_blank" rel="noopener" style="color:#ffcc00; text-decoration:underline;">${shop.name}</a></li>`;
  });
  listHTML += '</ul>';
  openModal(listHTML);
}
function showVideo(videoURL) {
  const modal = document.getElementById('modalOverlay');
  const modalContent = document.getElementById('modalContent');
  const modalBody = document.getElementById('modalBody');
  modalBody.innerHTML = `
    <video id="modalVideoTemp" width="100%" height="auto" controls autoplay style="background:#000; border-radius:8px;">
      <source src="${videoURL}" type="video/mp4" />
      Browser anda tidak menyokong video.
    </video>
  `;
  modal.style.display = 'flex';
  const video = document.getElementById('modalVideoTemp');
  video.onloadedmetadata = function () {
    if (video.videoHeight > video.videoWidth) {
      modalContent.classList.add('video-portrait');
    } else {
      modalContent.classList.remove('video-portrait');
    }
  };
  video.play();
}
function showSectionFromHash() {
  const hash = window.location.hash || '#home';
  document.querySelectorAll('section').forEach(section => section.classList.remove('active'));
  document.querySelectorAll('header').forEach(header => header.classList.remove('active'));
  if (hash === '#home') {
    document.getElementById('home').classList.add('active');
  } else {
    const target = document.querySelector(hash);
    if (target) {
      target.classList.add('active');
    } else {
      document.getElementById('home').classList.add('active');
    }
  }
  window.scrollTo(0, 0);
}
document.querySelectorAll('nav ul li a').forEach(link => {
  link.addEventListener('click', e => {
    const href = link.getAttribute('href');
    if (href === '#home') {
      e.preventDefault();
      document.querySelector('#home').scrollIntoView({ behavior: 'smooth' });
      history.pushState(null, null, href);
      showSectionFromHash();
    }
  });
});
window.addEventListener('load', showSectionFromHash);
window.addEventListener('hashchange', showSectionFromHash);
