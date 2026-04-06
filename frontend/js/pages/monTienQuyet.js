let _tqAllMons = [], _tqMonId = null;

async function initTQ() {
  _tqAllMons = await monHocApi.getAll();
  const opts = _tqAllMons.map(m => `<option value="${m.id}">${m.maMon} — ${m.tenMon}</option>`).join('');
  document.getElementById('sel-tq-mon').innerHTML   = '<option value="">-- Chọn môn --</option>' + opts;
  document.getElementById('inp-tq-mon').innerHTML   = '<option value="">-- Môn học --</option>' + opts;
  document.getElementById('inp-tq-montq').innerHTML = '<option value="">-- Môn tiên quyết --</option>' + opts;
}

async function loadTQ(monId) {
  _tqMonId = monId;
  const tbody = document.getElementById('tbody-tq');
  if (!monId) { tbody.innerHTML = ''; return; }

  const data = await monHocApi.getTienQuyet(monId);
  if (!data?.length) {
    tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted py-3">Chưa có môn tiên quyết.</td></tr>';
    return;
  }
  tbody.innerHTML = data.map((d, i) => {
    const mon   = _tqAllMons.find(m => m.id === d.monHocId);
    const monTQ = _tqAllMons.find(m => m.id === d.monTienQuyetId);
    return `<tr>
      <td>${i + 1}</td>
      <td>${mon?.maMon   || '—'} — ${mon?.tenMon   || '—'}</td>
      <td>${monTQ?.maMon || '—'} — ${monTQ?.tenMon || '—'}</td>
      <td><button class="btn btn-sm btn-danger" onclick="deleteTQ(${d.id})">Xóa</button></td>
    </tr>`;
  }).join('');
}

async function saveTQ() {
  const monId   = Number(document.getElementById('inp-tq-mon').value);
  const monTQId = Number(document.getElementById('inp-tq-montq').value);
  if (!monId || !monTQId) { Toast.warning('Vui lòng chọn đủ môn học và môn tiên quyết.'); return; }
  if (monId === monTQId)  { Toast.warning('Môn học và môn tiên quyết không được trùng nhau.'); return; }
  try {
    await monHocApi.addTienQuyet({ monHocId: monId, monTienQuyetId: monTQId });
    Toast.success('Thêm thành công.');
    Modal.close('modal-tq');
    loadTQ(_tqMonId || monId);
  } catch (e) { Toast.error(e.message); }
}

async function deleteTQ(id) {
  if (!await confirmDelete('môn tiên quyết này')) return;
  try { await monHocApi.deleteTienQuyet(id); Toast.success('Xóa thành công.'); loadTQ(_tqMonId); }
  catch (e) { Toast.error(e.message); }
}