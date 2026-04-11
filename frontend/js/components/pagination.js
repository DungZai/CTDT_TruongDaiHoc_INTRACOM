// =============================================
// pagination.js — Phân trang tái sử dụng
// =============================================

const Pagination = (() => {
  // Lưu state theo id
  const _state = {};

  /**
   * Khởi tạo pagination container
   * @param {string} id       - id của div chứa pagination
   * @param {function} onLoad - callback(page, size) khi người dùng chuyển trang / đổi size
   */
  function init(id, onLoad) {
    _state[id] = { page: 0, size: 10, totalPages: 0, totalElements: 0, onLoad };
  }

  /**
   * Cập nhật UI phân trang sau khi nhận response từ API
   * @param {string} id
   * @param {{ page, size, totalPages, totalElements, first, last }} res
   */
  function update(id, res) {
    const s = _state[id];
    if (!s) return;

    s.page          = res.page          ?? 0;
    s.size          = res.size          ?? 10;
    s.totalPages    = res.totalPages    ?? 0;
    s.totalElements = res.totalElements ?? 0;
    s.first         = res.first         ?? true;
    s.last          = res.last          ?? true;

    _render(id);
  }

  function getPage(id) { return _state[id]?.page ?? 0; }
  function getSize(id) { return _state[id]?.size ?? 10; }

  // ── Render UI ──────────────────────────────────────────────
  function _render(id) {
    const el = document.getElementById(id);
    if (!el) return;

    const s = _state[id];
    const { page, size, totalPages, totalElements, onLoad } = s;

    if (totalPages === 0) { el.innerHTML = ''; return; }

    const from = totalElements === 0 ? 0 : page * size + 1;
    const to   = Math.min((page + 1) * size, totalElements);

    // Tạo danh sách số trang hiển thị (cửa sổ ±2)
    const pages = _pageWindow(page, totalPages);

    el.innerHTML = `
      <div class="pagination-wrap">
        <div class="pagination-info">
          Hiển thị <strong>${from}–${to}</strong> / <strong>${totalElements}</strong> bản ghi
        </div>

        <div class="pagination-size">
          <label>Hiển thị</label>
          <select id="${id}-size-select">
            ${[5, 10, 20, 50].map(v =>
              `<option value="${v}" ${v === size ? 'selected' : ''}>${v}</option>`
            ).join('')}
          </select>
          <label>/ trang</label>
        </div>

        <nav>
          <ul class="pagination-list">
            <!-- Prev -->
            <li class="page-item ${s.first ? 'disabled' : ''}">
              <button class="page-btn" data-p="${page - 1}" ${s.first ? 'disabled' : ''}>‹</button>
            </li>

            ${pages.map(p =>
              p === '...'
                ? `<li class="page-item"><span class="page-dots">…</span></li>`
                : `<li class="page-item ${p === page ? 'active' : ''}">
                     <button class="page-btn" data-p="${p}">${p + 1}</button>
                   </li>`
            ).join('')}

            <!-- Next -->
            <li class="page-item ${s.last ? 'disabled' : ''}">
              <button class="page-btn" data-p="${page + 1}" ${s.last ? 'disabled' : ''}>›</button>
            </li>
          </ul>
        </nav>
      </div>
    `;

    // Sự kiện chuyển trang
    el.querySelectorAll('.page-btn:not([disabled])').forEach(btn => {
      btn.addEventListener('click', () => {
        const p = Number(btn.dataset.p);
        if (p >= 0 && p < totalPages) {
          _state[id].page = p;
          onLoad(p, _state[id].size);
        }
      });
    });

    // Sự kiện đổi size
    const sizeEl = document.getElementById(`${id}-size-select`);
    sizeEl?.addEventListener('change', () => {
      const newSize = Number(sizeEl.value);
      _state[id].size = newSize;
      _state[id].page = 0;
      onLoad(0, newSize);
    });
  }

  // Tạo mảng số trang với dấu "..." nếu cần
  function _pageWindow(current, total) {
    if (total <= 7) return Array.from({ length: total }, (_, i) => i);

    const pages = [];
    const delta = 2;
    const left  = current - delta;
    const right = current + delta;

    pages.push(0);
    if (left > 1) pages.push('...');

    for (let i = Math.max(1, left); i <= Math.min(total - 2, right); i++) {
      pages.push(i);
    }

    if (right < total - 2) pages.push('...');
    pages.push(total - 1);

    return pages;
  }

  return { init, update, getPage, getSize };
})();