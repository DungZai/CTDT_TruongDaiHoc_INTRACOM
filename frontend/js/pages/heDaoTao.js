let _heEditId = null;

async function loadHe() {
  const data = await heDaoTaoApi.getAll();
  renderTable({
    tbodyId: 'tbody-he',
    columns: [
      { key: 'tenHe' },
      { key: 'thoiGianDaoTao',    render: r => `${r.thoiGianDaoTao} năm` },
      { key: 'tongTinChiMacDinh', render: r => `${r.tongTinChiMacDinh} TC` },
      { key: 'moTa',              render: r => r.moTa || '—' },
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
  document.getElementById('inp-ten-he').value  = d.tenHe;
  document.getElementById('inp-he-tg').value   = d.thoiGianDaoTao;
  document.getElementById('inp-he-tc').value   = d.tongTinChiMacDinh;
  document.getElementById('inp-he-mota').value = d.moTa || '';
  document.getElementById('modal-he-title').textContent = 'Sửa hệ đào tạo';
  Modal.open('modal-he');
}

async function saveHe() {
  const body = {
    tenHe:              document.getElementById('inp-ten-he').value.trim(),
    thoiGianDaoTao:     Number(document.getElementById('inp-he-tg').value),
    tongTinChiMacDinh:  Number(document.getElementById('inp-he-tc').value),
    moTa:               document.getElementById('inp-he-mota').value.trim(),
  };
  if (!Validator.required(body.tenHe, 'Tên hệ')) return;
  if (!Validator.positiveInt(body.thoiGianDaoTao,    'Thời gian đào tạo')) return;
  if (!Validator.positiveInt(body.tongTinChiMacDinh, 'Tổng tín chỉ')) return;
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