let _dcMonId = null, _dcEditId = null;

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
  if (!monId) { wrap.innerHTML = '<p class="text-muted mt-3">Chọn môn học để xem đề cương.</p>'; return; }

  const list = await deCuongApi.getByMonHoc(monId);
  const dc   = Array.isArray(list) ? list[0] : list;

  if (!dc) {
    wrap.innerHTML = `<div class="alert alert-warning d-flex justify-content-between align-items-center">
      <span>Môn học này chưa có đề cương.</span>
      <button class="btn btn-primary btn-sm" onclick="openDC()">+ Soạn đề cương</button></div>`;
    return;
  }

  wrap.innerHTML = `<div class="dc-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h6 class="mb-0 fw-semibold">Đề cương chi tiết
        <span class="badge ms-2" style="background:#dbeafe;color:#1e40af">v${dc.version || '1.0'}</span>
      </h6>
      <div>
        <button class="btn btn-warning btn-sm me-1" onclick="openDCEdit(${dc.id})">Sửa</button>
        <button class="btn btn-danger btn-sm" onclick="deleteDC(${dc.id})">Xóa</button>
      </div>
    </div>
    <div class="dc-row"><span class="dc-label">Mục tiêu:</span><span>${dc.mucTieu || '—'}</span></div>
    <div class="dc-row"><span class="dc-label">Nội dung:</span><span>${dc.noiDung || '—'}</span></div>
    <div class="dc-row"><span class="dc-label">PP giảng dạy:</span><span>${dc.phuongPhapDay || '—'}</span></div>
    <div class="dc-row"><span class="dc-label">PP đánh giá:</span><span>${dc.phuongPhapDanhGia || '—'}</span></div>
    <div class="dc-row"><span class="dc-label">Tài liệu:</span><span>${dc.taiLieu || '—'}</span></div>
  </div>`;
}

function openDC() {
  _dcEditId = null;
  Modal.reset('modal-dc');
  document.getElementById('inp-dc-version').value = '1.0';
  Modal.open('modal-dc');
}

async function openDCEdit(id) {
  _dcEditId = id;
  const d = await deCuongApi.getById(id);
  document.getElementById('inp-dc-muctieu').value = d.mucTieu || '';
  document.getElementById('inp-dc-noidung').value = d.noiDung || '';
  document.getElementById('inp-dc-ppday').value   = d.phuongPhapDay || '';
  document.getElementById('inp-dc-ppdg').value    = d.phuongPhapDanhGia || '';
  document.getElementById('inp-dc-tailieu').value = d.taiLieu || '';
  document.getElementById('inp-dc-version').value = d.version || '1.0';
  Modal.open('modal-dc');
}

async function saveDC() {
  const body = {
    monHocId:          Number(_dcMonId),
    mucTieu:           document.getElementById('inp-dc-muctieu').value.trim(),
    noiDung:           document.getElementById('inp-dc-noidung').value.trim(),
    phuongPhapDay:     document.getElementById('inp-dc-ppday').value.trim(),
    phuongPhapDanhGia: document.getElementById('inp-dc-ppdg').value.trim(),
    taiLieu:           document.getElementById('inp-dc-tailieu').value.trim(),
    version:           document.getElementById('inp-dc-version').value.trim() || '1.0',
  };
  if (!Validator.required(body.mucTieu, 'Mục tiêu')) return;
  if (!Validator.required(body.noiDung, 'Nội dung')) return;
  try {
    _dcEditId ? await deCuongApi.update(_dcEditId, body) : await deCuongApi.create(body);
    Toast.success('Lưu đề cương thành công.');
    Modal.close('modal-dc');
    loadDC(_dcMonId);
  } catch (e) { Toast.error(e.message); }
}

async function deleteDC(id) {
  if (!await confirmDelete('đề cương này')) return;
  try { await deCuongApi.delete(id); Toast.success('Xóa thành công.'); loadDC(_dcMonId); }
  catch (e) { Toast.error(e.message); }
}