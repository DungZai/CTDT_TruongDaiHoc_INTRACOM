// Load sidebar vào #sidebar-container của mỗi trang
async function loadSidebar() {
  try {
    // Tự động xác định đường dẫn dựa theo vị trí trang hiện tại
  const isRoot = !window.location.pathname.includes('/pages/');
  const path = isRoot ? 'components/sidebar.html' : '../components/sidebar.html';
  const res = await fetch(path);
    const html = await res.text();
    document.getElementById('sidebar-container').innerHTML = html;
  } catch(e) {
    console.error('Không load được sidebar:', e);
  }
}