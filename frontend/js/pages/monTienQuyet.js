let _tqAllMons = [], _tqMonId = null, _bsModalTQ = null;

async function initTQ() {
  _tqAllMons = await monHocApi.getAll();
  const opts = _tqAllMons.map(m =>
    `<option value="${m.id}">${m.maMon} — ${m.tenMon}</option>`).join('');
  document.getElementById('sel-tq-mon').innerHTML   = '<option value="">-- Chọn môn học --</option>' + opts;
  document.getElementById('inp-tq-mon').innerHTML   = '<option value="">-- Môn học chính --</option>' + opts;
  document.getElementById('inp-tq-montq').innerHTML = '<option value="">-- Môn tiên quyết --</option>' + opts;

  // Tự động chọn môn đầu tiên
  if (_tqAllMons.length) {
    const firstId = _tqAllMons[0].id;
    document.getElementById('sel-tq-mon').value = firstId;
    loadTQ(firstId);
  }
}

async function loadTQ(monId) {
  _tqMonId = monId;
  const tbody  = document.getElementById('tbody-tq');
  const lbl    = document.getElementById('lbl-tq-count');

  if (!monId) {
    tbody.innerHTML = `<tr><td colspan="4" class="text-center text-muted py-4">
      Vui lòng chọn môn học để xem danh sách tiên quyết.</td></tr>`;
    lbl.textContent = '';
    return;
  }

  const data = await monHocApi.getTienQuyet(monId);

  lbl.textContent = data?.length ? `${data.length} môn tiên quyết` : '';

  if (!data?.length) {
    tbody.innerHTML = `<tr><td colspan="4" class="text-center text-muted py-4">
      Môn học này chưa có môn tiên quyết.</td></tr>`;
    return;
  }

  tbody.innerHTML = data.map((d, i) => `
    <tr>
      <td class="text-muted">${i + 1}</td>
      <td><strong>${d.maMon}</strong> — ${d.tenMon}</td>
      <td><strong>${d.maMonTienQuyet}</strong> — ${d.tenMonTienQuyet}</td>
      <td>
        <button class="btn btn-sm btn-danger" onclick="deleteTQ(${d.id})">Xóa</button>
      </td>
    </tr>`).join('');
}

function _getModalTQ() {
  if (!_bsModalTQ)
    _bsModalTQ = new bootstrap.Modal(document.getElementById('modal-tq'));
  return _bsModalTQ;
}

function openModalTQ() {
  // Nếu đang chọn môn thì tự điền sẵn
  if (_tqMonId) document.getElementById('inp-tq-mon').value = _tqMonId;
  document.getElementById('inp-tq-montq').value = '';
  _getModalTQ().show();
}

async function saveTQ() {
  const monId   = Number(document.getElementById('inp-tq-mon').value);
  const monTQId = Number(document.getElementById('inp-tq-montq').value);

  if (!monId || !monTQId) {
    Toast.warning('Vui lòng chọn đủ môn học và môn tiên quyết.'); return;
  }
  if (monId === monTQId) {
    Toast.warning('Môn học và môn tiên quyết không được trùng nhau.'); return;
  }

  try {
    await monHocApi.addTienQuyet({ monHocId: monId, monTienQuyetId: monTQId });
    Toast.success('Thêm thành công.');
    _getModalTQ().hide();
    loadTQ(_tqMonId || monId);
    // Cập nhật select nếu chưa chọn môn
    if (!_tqMonId) {
      document.getElementById('sel-tq-mon').value = monId;
    }
  } catch (e) { Toast.error(e.message); }
}

async function deleteTQ(id) {
  if (!await confirmDelete('môn tiên quyết này')) return;
  try {
    await monHocApi.deleteTienQuyet(id);
    Toast.success('Xóa thành công.');
    loadTQ(_tqMonId);
  } catch (e) { Toast.error(e.message); }
}