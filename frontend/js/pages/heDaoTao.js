let _heEditId = null;

async function loadHe() {
  const data = await heDaoTaoApi.getAll();
  renderTable({
    tbodyId: 'tbody-he',
    columns: [
      { key: 'ten_he' },
      { key: 'thoi_gian_dao_tao',     render: r => `${r.thoi_gian_dao_tao} năm` },
      { key: 'tong_tin_chi_mac_dinh', render: r => `${r.tong_tin_chi_mac_dinh} TC` },
      { key: 'mo_ta',                 render: r => r.mo_ta || '—' },
    ],
    data,
    actions: { edit: 'editHe', delete: 'deleteHe' },
  });
}

function openHe() {
  _heEditId = null;
  Modal.reset('modal-he');
  document.getElementById('modal-he-title').textContent = 'Thêm hệ đào tạo';
  Modal.open('modal-he');
}

async function editHe(id) {
  _heEditId = id;
  const d = await heDaoTaoApi.getById(id);
  document.getElementById('inp-ten-he').value  = d.ten_he;
  document.getElementById('inp-he-tg').value   = d.thoi_gian_dao_tao;
  document.getElementById('inp-he-tc').value   = d.tong_tin_chi_mac_dinh;
  document.getElementById('inp-he-mota').value = d.mo_ta || '';
  document.getElementById('modal-he-title').textContent = 'Sửa hệ đào tạo';
  Modal.open('modal-he');
}

async function saveHe() {
  const body = {
    ten_he:                document.getElementById('inp-ten-he').value.trim(),
    thoi_gian_dao_tao:     Number(document.getElementById('inp-he-tg').value),
    tong_tin_chi_mac_dinh: Number(document.getElementById('inp-he-tc').value),
    mo_ta:                 document.getElementById('inp-he-mota').value.trim(),
  };
  if (!Validator.required(body.ten_he, 'Tên hệ')) return;
  if (!Validator.positiveInt(body.thoi_gian_dao_tao, 'Thời gian đào tạo')) return;
  if (!Validator.positiveInt(body.tong_tin_chi_mac_dinh, 'Tổng tín chỉ')) return;
  try {
    _heEditId ? await heDaoTaoApi.update(_heEditId, body) : await heDaoTaoApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-he');
    loadHe();
  } catch (e) { Toast.error(e.message); }
}

async function deleteHe(id) {
  if (!await confirmDelete('hệ đào tạo này')) return;
  try { await heDaoTaoApi.delete(id); Toast.success('Xóa thành công.'); loadHe(); }
  catch (e) { Toast.error(e.message); }
}