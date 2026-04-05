let _mtCtId = null, _mtPlos = [], _mtMons = [], _mtMatrix = {};

async function initMaTran() {
  const cts = await chuongTrinhApi.getAll();
  document.getElementById('sel-mt-ct').innerHTML =
    '<option value="">-- Chọn chương trình --</option>'
    + cts.map(c => `<option value="${c.id}">${c.ten_chuong_trinh}</option>`).join('');
}

async function loadMaTran(ctId) {
  _mtCtId = ctId;
  const wrap = document.getElementById('wrap-matrix');
  if (!ctId) { wrap.innerHTML = '<p class="text-muted mt-3">Chọn chương trình để hiển thị ma trận.</p>'; return; }

  const [plos, khung, mons] = await Promise.all([
    chuanDauRaApi.getByChuongTrinh(ctId),
    khungApi.getByChuongTrinh(ctId),
    monHocApi.getAll(),
  ]);
  _mtPlos = plos;
  _mtMons = khung.map(k => mons.find(m => m.id === k.mon_hoc_id)).filter(Boolean);

  const links = (await Promise.all(_mtMons.map(m => maTranApi.getByMonHoc(m.id)))).flat();
  _mtMatrix = {};
  links.forEach(l => { _mtMatrix[`${l.mon_hoc_id}_${l.chuan_dau_ra_id}`] = { id: l.id, muc_do: l.muc_do }; });

  if (!plos.length || !_mtMons.length) {
    wrap.innerHTML = '<p class="text-muted mt-3">Chưa đủ dữ liệu (PLO hoặc môn học).</p>'; return;
  }

  let html = '<div class="table-responsive"><table class="table table-bordered table-sm"><thead><tr><th>Môn học</th>';
  plos.forEach(p => { html += `<th title="${p.noi_dung}">${p.ma_chuan}</th>`; });
  html += '</tr></thead><tbody>';
  _mtMons.forEach(m => {
    html += `<tr><td class="text-start">${m.ma_mon} — ${m.ten_mon}</td>`;
    plos.forEach(p => {
      const key = `${m.id}_${p.id}`, cur = _mtMatrix[key]?.muc_do || '';
      html += `<td><select class="form-select form-select-sm mx-auto" style="width:68px"
        onchange="onMaTranChange(${m.id},${p.id},this.value)">
        <option value="">—</option>
        ${MUC_DO_CLO.map(v => `<option value="${v}"${cur === v ? ' selected' : ''}>${v}</option>`).join('')}
      </select></td>`;
    });
    html += '</tr>';
  });
  html += '</tbody></table></div>';
  wrap.innerHTML = html;
}

async function onMaTranChange(monId, ploId, mucDo) {
  const key = `${monId}_${ploId}`;
  try {
    if (_mtMatrix[key]) {
      if (!mucDo) { await maTranApi.delete(_mtMatrix[key].id); delete _mtMatrix[key]; }
      else        { await maTranApi.update(_mtMatrix[key].id, { muc_do: mucDo }); _mtMatrix[key].muc_do = mucDo; }
    } else if (mucDo) {
      const r = await maTranApi.create({ mon_hoc_id: monId, chuan_dau_ra_id: ploId, muc_do: mucDo });
      _mtMatrix[key] = { id: r.id, muc_do: mucDo };
    }
    Toast.success('Đã lưu.');
  } catch (e) { Toast.error(e.message); }
}