function renderTable({ tbodyId, columns, data, actions, type }) {
  const tbody = document.getElementById(tbodyId);
  if (!tbody) return;

  const role      = TokenService.getRole();
  const isAdmin   = role === 'ADMIN';
  const isDecuong = type === 'decuong';

  // Có hiện cột thao tác không?
  const showAction = isAdmin || isDecuong;

  // Cập nhật header — thêm/bỏ cột Thao tác
  const thead = tbody.closest('table')?.querySelector('thead tr');
  if (thead) {
    const lastTh = thead.querySelector('th:last-child');
    if (showAction && lastTh?.textContent.trim() === '') {
      lastTh.textContent = 'Thao tác';
    } else if (!showAction && lastTh) {
      lastTh.style.display = 'none';
    }
  }

  if (!data?.length) {
    const colspan = showAction ? columns.length + 2 : columns.length + 1;
    tbody.innerHTML = `<tr><td colspan="${colspan}"
      class="text-center py-4 text-muted">Không có dữ liệu</td></tr>`;
    return;
  }

  const canEdit   = isAdmin || isDecuong;
  const canDelete = isAdmin;

  tbody.innerHTML = data.map((row, i) => {
    const cells = columns.map(c =>
      `<td>${c.render ? c.render(row) : (row[c.key] ?? '—')}</td>`
    ).join('');

    const btnE = (actions?.edit && canEdit)
      ? `<button class="btn btn-sm btn-warning me-1"
           onclick="${actions.edit}(${row.id})">Sửa</button>` : '';
    const btnD = (actions?.delete && canDelete)
      ? `<button class="btn btn-sm btn-danger"
           onclick="${actions.delete}(${row.id})">Xóa</button>` : '';

    const actionTd = showAction ? `<td>${btnE}${btnD}</td>` : '';

    return `<tr><td class="text-muted">${i + 1}</td>${cells}${actionTd}</tr>`;
  }).join('');
}

function filterTable(inputId, tbodyId) {
  const kw = document.getElementById(inputId)?.value.toLowerCase() ?? '';
  document.querySelectorAll(`#${tbodyId} tr`).forEach(tr => {
    tr.style.display = tr.textContent.toLowerCase().includes(kw) ? '' : 'none';
  });
}