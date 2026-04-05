let _tqAllMons = [], _tqMonId = null;

async function initTQ() {
  _tqAllMons = await monHocApi.getAll();
  const opts = _tqAllMons.map(m => `<option value="${m.id}">${m.ma_mon} — ${m.ten_mon}</option>`).join('');
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
    const mon   = _tqAllMons.find(m => m.id === d.mon_hoc_id);
    const monTQ = _tqAllMons.find(m => m.id === d.mon_tien_quyet_id);
    return `<tr>
      <td>${i + 1}</td>
      <td>${mon?.ma_mon || '—'} — ${mon?.ten_mon || '—'}</td>
      <td>${monTQ?.ma_mon || '—'} — ${monTQ?.ten_mon || '—'}</td>
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
    await monHocApi.addTienQuyet({ mon_hoc_id: monId, mon_tien_quyet_id: monTQId });
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