async function loadSidebar() {
  // pages/ nằm trong frontend/ nên đi lên 2 cấp để tới partials/
  const res  = await fetch('/frontend/partials/sidebar.html');
  const html = await res.text();
  const wrap = document.createElement('div');
  wrap.innerHTML = html;
  document.body.insertBefore(wrap, document.body.firstChild);
  _initSidebar();
}

function _initSidebar() {
  const username = TokenService.getUsername();

  document.querySelectorAll('#sidebar-user, #topbar-user')
    .forEach(el => { if (el) el.textContent = username; });

  const av = document.getElementById('topbar-avatar');
  if (av) av.textContent = username.charAt(0).toUpperCase();

  // Active link
  const cur = window.location.pathname;
  document.querySelectorAll('.sidebar-link').forEach(a => {
    const href = a.getAttribute('href') || '';
    if (cur.endsWith(href) || cur.includes(href.replace('.html', '')))
      a.classList.add('active');
  });

  // Topbar title
  const active = document.querySelector('.sidebar-link.active');
  const title  = document.getElementById('topbar-title');
  if (title && active) title.textContent = active.textContent.trim();

  // Phân quyền UI
  Permission.applyUI();

  // Logout
  document.getElementById('btn-logout')
    ?.addEventListener('click', () => authApi.logout());

  // Mobile toggle
  const sb = document.querySelector('.sidebar');
  const ov = document.getElementById('sidebar-overlay');
  document.getElementById('btn-toggle')
    ?.addEventListener('click', () => { sb?.classList.add('open'); ov?.classList.add('show'); });
  ov?.addEventListener('click', () => { sb?.classList.remove('open'); ov?.classList.remove('show'); });
}

document.addEventListener('DOMContentLoaded', loadSidebar);