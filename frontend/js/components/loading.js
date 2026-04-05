const Loading = (() => {
  let count = 0, el = null;
  function init() {
    if (el) return;
    el = document.createElement('div');
    el.id = 'global-loading';
    el.innerHTML = `<div class="loading-backdrop"><div class="loading-box">
      <div class="spinner-border" role="status"></div>
      <p>Đang xử lý...</p></div></div>`;
    document.body.appendChild(el);
  }
  return {
    show() { init(); count++; el.style.display = 'flex'; },
    hide() { count = Math.max(0, count - 1); if (!count && el) el.style.display = 'none'; },
  };
})();