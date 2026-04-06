let _dcMonId = null, _dcEditId = null, _bsModalDC = null;

function _getModalDC() {
  if (!_bsModalDC) {
    const el = document.getElementById('modal-dc');
    _bsModalDC = new bootstrap.Modal(el, { backdrop: 'static' });
    // Dọn dẹp khi modal đóng
    el.addEventListener('hidden.bs.modal', () => {
      document.body.classList.remove('modal-open');
      document.querySelectorAll('.modal-backdrop').forEach(e => e.remove());
    });
  }
  return _bsModalDC;
}

async function initDC() {
  const mons = await monHocApi.getAll();
  document.getElementById('sel-dc-mon').innerHTML =
    '<option value="">-- Chọn môn học --</option>'
    + mons.filter(m => m.trangThai)
          .map(m => `<option value="${m.id}">${m.maMon} — ${m.tenMon}</option>`).join('');
}

async function loadDC(monId) {
  _dcMonId = monId;
  const wrap = document.getElementById('wrap-dc');
  if (!monId) {
    wrap.innerHTML = '<p class="text-muted mt-3">Chọn môn học để xem đề cương.</p>';
    return;
  }

  const list = await deCuongApi.getByMonHoc(monId);
  const dc   = Array.isArray(list) ? list[0] : list;

  if (!dc) {
    wrap.innerHTML = `
      <div class="alert alert-warning d-flex justify-content-between align-items-center">
        <span>Môn học này chưa có đề cương.</span>
        <button class="btn btn-primary btn-sm" onclick="openDC()">+ Soạn đề cương</button>
      </div>`;
    return;
  }

  // Render nút tài liệu
  const isFilePath = dc.taiLieu?.startsWith('/api/files/');
  const taiLieuHtml = !dc.taiLieu
    ? '<span class="text-muted">Chưa có tài liệu</span>'
    : isFilePath
      ? `<button onclick="xemTaiLieu('${dc.taiLieu}')"
           class="btn btn-sm btn-outline-primary">
           📄 Xem tài liệu PDF
         </button>`
      : `<span>${dc.taiLieu}</span>
         <button onclick="xemTaiLieu('${dc.taiLieu}')"
           class="btn btn-sm btn-outline-primary ms-2">
           📄 Xem tài liệu
         </button>`;

  const isAdmin     = TokenService.getRole() === 'ADMIN';
  const isGiangVien = TokenService.getRole() === 'GIANG_VIEN';
  const canEdit     = isAdmin || isGiangVien;

  const actionHtml = canEdit ? `
    <div>
      <button class="btn btn-warning btn-sm me-1" onclick="openDCEdit(${dc.id})">Sửa</button>
      ${isAdmin ? `<button class="btn btn-danger btn-sm" onclick="deleteDC(${dc.id})">Xóa</button>` : ''}
    </div>` : '';

  wrap.innerHTML = `
    <div class="dc-card">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h6 class="mb-0 fw-semibold">Đề cương chi tiết
          <span class="badge ms-2" style="background:#dbeafe;color:#1e40af">
            v${dc.version || '1.0'}
          </span>
        </h6>
        ${actionHtml}
      </div>
      <div class="dc-row"><span class="dc-label">Mục tiêu:</span><span>${dc.mucTieu || '—'}</span></div>
      <div class="dc-row"><span class="dc-label">Nội dung:</span><span>${dc.noiDung || '—'}</span></div>
      <div class="dc-row"><span class="dc-label">PP giảng dạy:</span><span>${dc.phuongPhapDay || '—'}</span></div>
      <div class="dc-row"><span class="dc-label">PP đánh giá:</span><span>${dc.phuongPhapDanhGia || '—'}</span></div>
      <div class="dc-row"><span class="dc-label">Tài liệu:</span><span>${taiLieuHtml}</span></div>
    </div>`;
}

function openDC() {
  _dcEditId = null;
  _resetForm();
  document.getElementById('inp-dc-version').value = '1.0';
  document.getElementById('lbl-dc-modal-title').textContent = 'Soạn đề cương';
  Modal.open('modal-dc');
}

async function openDCEdit(id) {
  _dcEditId = id;
  const d = await deCuongApi.getById(id);
  document.getElementById('inp-dc-muctieu').value = d.mucTieu || '';
  document.getElementById('inp-dc-noidung').value = d.noiDung || '';
  document.getElementById('inp-dc-ppday').value   = d.phuongPhapDay || '';
  document.getElementById('inp-dc-ppdg').value    = d.phuongPhapDanhGia || '';
  document.getElementById('inp-dc-version').value = d.version || '1.0';

  // Hiện link tài liệu cũ nếu có
  const wrapCurrent = document.getElementById('wrap-tailieu-current');
  const lnkCurrent  = document.getElementById('lnk-tailieu-current');
  if (d.taiLieu) {
    lnkCurrent.href      = ENV.BASE_URL + d.taiLieu;
    wrapCurrent.style.display = '';
  } else {
    wrapCurrent.style.display = 'none';
  }

  // Lưu URL cũ vào hidden input
  document.getElementById('inp-dc-tailieu-url').value = d.taiLieu || '';
  document.getElementById('lbl-dc-modal-title').textContent = 'Sửa đề cương';
  Modal.open('modal-dc');
}

async function saveDC() {
  // Upload file nếu có chọn file mới
  let taiLieuUrl = document.getElementById('inp-dc-tailieu-url').value;
  const fileInput = document.getElementById('inp-dc-tailieu-file');

  if (fileInput.files.length > 0) {
    const file = fileInput.files[0];
    if (file.size > 200 * 1024 * 1024) {
      Toast.warning('File không được vượt quá 200MB.'); return;
    }
    if (file.type !== 'application/pdf') {
      Toast.warning('Chỉ chấp nhận file PDF.'); return;
    }

    // Hiện loading
    const btnLuu = document.querySelector('#modal-dc .btn-primary');
    const oldText = btnLuu.textContent;
    btnLuu.disabled = true;
    btnLuu.textContent = '⏳ Đang upload...';

    try {
      const formData = new FormData();
      formData.append('file', file);
      const res = await fetch(`${ENV.BASE_URL}/api/files/upload`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${TokenService.get()}` },
        body: formData,
      });
      const json = await res.json();
      if (!res.ok) throw new Error(json.message || 'Upload thất bại');
      taiLieuUrl = json.data;
    } catch (e) {
      Toast.error('Upload tài liệu thất bại: ' + e.message);
      btnLuu.disabled = false;
      btnLuu.textContent = oldText;
      return;
    } finally {
      btnLuu.disabled = false;
      btnLuu.textContent = oldText;
    }
  }

  const body = {
    monHocId:          Number(_dcMonId),
    mucTieu:           document.getElementById('inp-dc-muctieu').value.trim(),
    noiDung:           document.getElementById('inp-dc-noidung').value.trim(),
    phuongPhapDay:     document.getElementById('inp-dc-ppday').value.trim(),
    phuongPhapDanhGia: document.getElementById('inp-dc-ppdg').value.trim(),
    taiLieu:           taiLieuUrl,
    version:           document.getElementById('inp-dc-version').value.trim() || '1.0',
  };

  if (!Validator.required(body.mucTieu, 'Mục tiêu')) return;
  if (!Validator.required(body.noiDung, 'Nội dung')) return;

  try {
    _dcEditId
      ? await deCuongApi.update(_dcEditId, body)
      : await deCuongApi.create(body);
    Toast.success('Lưu đề cương thành công.');
    const monId = _dcMonId; // Lưu lại trước khi đóng modal
    _getModalDC().hide();
    setTimeout(() => loadDC(monId), 300); // Đợi modal đóng hẳn rồi reload
  } catch (e) { Toast.error(e.message); }
}

async function deleteDC(id) {
  if (!await confirmDelete('đề cương này')) return;
  try {
    await deCuongApi.delete(id);
    Toast.success('Xóa thành công.');
    loadDC(_dcMonId);
  } catch (e) { Toast.error(e.message); }
}

function _resetForm() {
  document.getElementById('inp-dc-muctieu').value      = '';
  document.getElementById('inp-dc-noidung').value      = '';
  document.getElementById('inp-dc-ppday').value        = '';
  document.getElementById('inp-dc-ppdg').value         = '';
  document.getElementById('inp-dc-version').value      = '';
  document.getElementById('inp-dc-tailieu-file').value = '';
  document.getElementById('inp-dc-tailieu-url').value  = '';
  document.getElementById('wrap-tailieu-current').style.display = 'none';
}

async function xemTaiLieu(taiLieuPath) {
  if (!taiLieuPath.startsWith('/api/files/')) {
    window.open(`https://www.google.com/search?q=${encodeURIComponent(taiLieuPath + ' PDF')}`, '_blank');
    return;
  }

  try {
    const url = taiLieuPath.startsWith('/api/')
      ? ENV.BASE_URL + taiLieuPath
      : ENV.BASE_URL + '/api/files/decuong/' + taiLieuPath;

    const res = await fetch(url, {
      headers: { 'Authorization': `Bearer ${TokenService.get()}` }
    });
    if (!res.ok) { Toast.error('Không thể tải tài liệu.'); return; }

    const blob    = await res.blob();
    const blobUrl = URL.createObjectURL(blob);

    const el    = document.getElementById('modal-pdf-viewer');
    const modal = new bootstrap.Modal(el, { backdrop: true });
    document.getElementById('pdf-viewer-frame').src = blobUrl;
    modal.show();

    el.addEventListener('hidden.bs.modal', () => {
      URL.revokeObjectURL(blobUrl);
      document.getElementById('pdf-viewer-frame').src = '';
      document.body.classList.remove('modal-open');
      document.querySelectorAll('.modal-backdrop').forEach(e => e.remove());
    }, { once: true });

  } catch (e) {
    Toast.error('Lỗi khi xem tài liệu: ' + e.message);
  }
}