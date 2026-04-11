let _mtCtId = null, _mtPlos = [], _mtMons = [], _mtMatrix = {}, _mtKhung = [];
let _mtHocKy = 'all'; // học kỳ đang chọn

async function initMaTran() {
  const cts = await chuongTrinhApi.getAllForSelect();
  document.getElementById('sel-mt-ct').innerHTML =
    '<option value="">-- Chọn chương trình --</option>'
    + cts.map(c => `<option value="${c.id}">${c.tenChuongTrinh}</option>`).join('');
}

async function loadMaTran(ctId) {
  _mtCtId = ctId;
  _mtHocKy = 'all';
  const wrap = document.getElementById('wrap-matrix');
  if (!ctId) {
    wrap.innerHTML = '<p class="text-muted mt-3">Chọn chương trình để hiển thị ma trận.</p>';
    return;
  }

  const [plos, khung, mons] = await Promise.all([
    chuanDauRaApi.getByChuongTrinh(ctId),
    khungApi.getByChuongTrinh(ctId),
    monHocApi.getAllForSelect(),
  ]);

  _mtPlos  = plos;
  _mtKhung = khung;

  // Thay { ...mon, hocKy } → Object.assign
  _mtMons = khung
    .map(k => {
      const mon = mons.find(m => m.id === k.monHocId);
      if (!mon) return null;
      return Object.assign({}, mon, { hocKy: k.hocKy });
    })
    .filter(Boolean);

  // Thay .flat() → .reduce concat
  const linkArrays = await Promise.all(_mtMons.map(m => maTranApi.getByMonHoc(m.id)));
  const links = linkArrays.reduce((acc, arr) => acc.concat(arr), []);

  _mtMatrix = {};
  links.forEach(l => {
    _mtMatrix[`${l.monHocId}_${l.chuanDauRaId}`] = { id: l.id, mucDo: l.mucDo };
  });

  if (!plos.length || !_mtMons.length) {
    wrap.innerHTML = '<p class="text-muted mt-3">Chưa đủ dữ liệu (PLO hoặc môn học).</p>';
    return;
  }

  // Thay [...new Set(...)] → filter indexOf
  const hocKys = _mtMons
    .map(m => m.hocKy)
    .filter((v, i, arr) => arr.indexOf(v) === i)
    .sort((a, b) => a - b);

  _renderTabs(hocKys);
  _renderMatrix('all');
}

function _renderTabs(hocKys) {
  const wrap = document.getElementById('wrap-matrix');
  const tabHtml = `
    <div class="mt-tabs mb-3">
      <button class="mt-tab active" data-hk="all" onclick="switchHocKy('all')">Tất cả</button>
      ${hocKys.map(hk =>
        `<button class="mt-tab" data-hk="${hk}" onclick="switchHocKy(${hk})">Học kỳ ${hk}</button>`
      ).join('')}
    </div>
    <div id="wrap-matrix-table"></div>
  `;
  wrap.innerHTML = tabHtml;
}

function switchHocKy(hk) {
  _mtHocKy = hk;
  document.querySelectorAll('.mt-tab').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.hk == hk);
  });
  _renderMatrix(hk);
}

function _renderMatrix(hk) {
  const tableWrap = document.getElementById('wrap-matrix-table');
  const mons = hk === 'all' ? _mtMons : _mtMons.filter(m => m.hocKy == hk);

  if (!mons.length) {
    tableWrap.innerHTML = '<p class="text-muted mt-3">Không có môn học nào.</p>';
    return;
  }

  let html = '<div class="table-responsive"><table class="table table-bordered table-sm"><thead><tr><th>Môn học</th>';
  if (hk === 'all') html += '<th>HK</th>';
  _mtPlos.forEach(p => { html += `<th title="${p.noiDung}">${p.maChuan}</th>`; });
  html += '</tr></thead><tbody>';

  mons.forEach(m => {
    html += `<tr><td class="text-start text-nowrap">${m.maMon} — ${m.tenMon}</td>`;
    if (hk === 'all') html += `<td class="text-center text-muted">${m.hocKy}</td>`;
    _mtPlos.forEach(p => {
      const key = `${m.id}_${p.id}`;
      const cur = _mtMatrix[key] ? _mtMatrix[key].mucDo : '';
      let opts = '<option value="">—</option>';
      MUC_DO_CLO.forEach(v => {
        opts += `<option value="${v}"${cur === v ? ' selected' : ''}>${v}</option>`;
      });
      html += `<td><select class="form-select form-select-sm mx-auto" style="width:68px"
        onchange="onMaTranChange(${m.id},${p.id},this.value)">${opts}</select></td>`;
    });
    html += '</tr>';
  });

  html += '</tbody></table></div>';
  tableWrap.innerHTML = html;
}

async function onMaTranChange(monId, ploId, mucDo) {
  const key = `${monId}_${ploId}`;
  try {
    if (_mtMatrix[key]) {
      if (!mucDo) {
        await maTranApi.delete(_mtMatrix[key].id);
        delete _mtMatrix[key];
      } else {
        await maTranApi.update(_mtMatrix[key].id, { mucDo });
        _mtMatrix[key].mucDo = mucDo;
      }
    } else if (mucDo) {
      const r = await maTranApi.create({ monHocId: monId, chuanDauRaId: ploId, mucDo });
      _mtMatrix[key] = { id: r.id, mucDo };
    }
    Toast.success('Đã lưu.');
  } catch (e) { Toast.error(e.message); }
}