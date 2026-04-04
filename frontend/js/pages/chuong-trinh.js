let allData=[], editingId=null, currentPage=1, nganhs=[], hes=[];
const PAGE_SIZE=10;

document.addEventListener('DOMContentLoaded', async ()=>{
  if(!requireAuth())return;
  renderUserInfo(); loadSidebar();
  [nganhs, hes] = await Promise.all([
    api.get('/api/nganh').catch(()=>[]),
    api.get('/api/he-dao-tao').catch(()=>[])
  ]);
  loadData();
});

async function loadData(){
  try{ allData=await api.get('/api/chuong-trinh')||[]; renderTable(); }
  catch(e){ showToast(e.message,'error'); }
}

function renderTable(){
  const q=document.getElementById('search').value.toLowerCase();
  const filtered=q?allData.filter(r=>r.tenChuongTrinh.toLowerCase().includes(q)):allData;
  document.getElementById('tbl-count').textContent=`Tổng: ${filtered.length} chương trình`;
  const total=Math.ceil(filtered.length/PAGE_SIZE)||1;
  if(currentPage>total)currentPage=1;
  const rows=filtered.slice((currentPage-1)*PAGE_SIZE,currentPage*PAGE_SIZE);
  document.getElementById('tbl-body').innerHTML=rows.length
    ?rows.map(r=>`<tr>
        <td>${r.id}</td>
        <td><strong>${r.tenChuongTrinh}</strong></td>
        <td>${r.tenNganh||'—'}</td>
        <td>${r.tenHe||'—'}</td>
        <td style="text-align:center">${r.tongTinChi}</td>
        <td style="text-align:center">${r.namPhatHanh}</td>
        <td><span class="badge ${r.trangThai?'badge-green':'badge-red'}">${r.trangThai?'Hoạt động':'Ngừng'}</span></td>
        <td><div class="action-btns">
          <button class="btn btn-primary btn-sm" onclick='openEdit(${JSON.stringify(r)})'>✏️ Sửa</button>
          <button class="btn btn-danger btn-sm" onclick="onDelete(${r.id})">🗑️ Xóa</button>
        </div></td>
      </tr>`).join('')
    :'<tr><td colspan="8" class="table-empty">Không có dữ liệu</td></tr>';
  renderPagination(total);
}

function filterRows(){currentPage=1;renderTable();}
function renderPagination(total){
  const pg=document.getElementById('pagination');
  if(total<=1){pg.innerHTML='';return;}
  let h=`<button ${currentPage===1?'disabled':''} onclick="goPage(${currentPage-1})">‹</button>`;
  for(let i=1;i<=total;i++) h+=`<button class="${i===currentPage?'active':''}" onclick="goPage(${i})">${i}</button>`;
  h+=`<button ${currentPage===total?'disabled':''} onclick="goPage(${currentPage+1})">›</button>`;
  pg.innerHTML=h;
}
function goPage(p){currentPage=p;renderTable();}

function formHtml(r){
  const nganhOpts=nganhs.map(n=>`<option value="${n.id}" ${r?.nganhId==n.id?'selected':''}>${n.tenNganh}</option>`).join('');
  const heOpts=hes.map(h=>`<option value="${h.id}" ${r?.heId==h.id?'selected':''}>${h.tenHe}</option>`).join('');
  return `
    <div class="form-group"><label>Tên chương trình *</label>
      <input id="f-ten" value="${r?.tenChuongTrinh||''}" placeholder="VD: CT CNTT 2024"/></div>
    <div class="form-row">
      <div class="form-group"><label>Ngành *</label>
        <select id="f-nganh">${nganhOpts}</select></div>
      <div class="form-group"><label>Hệ đào tạo *</label>
        <select id="f-he">${heOpts}</select></div>
    </div>
    <div class="form-row">
      <div class="form-group"><label>Tổng tín chỉ</label>
        <input id="f-tc" type="number" value="${r?.tongTinChi||130}" min="1"/></div>
      <div class="form-group"><label>Năm phát hành</label>
        <input id="f-nam" type="number" value="${r?.namPhatHanh||2024}"/></div>
    </div>
    <div class="form-group"><label>Mô tả</label>
      <textarea id="f-mota">${r?.moTa||''}</textarea></div>
    <div class="form-group"><label>Trạng thái</label>
      ${makeToggle('f-tt',r?.trangThai!==false)}</div>`;
}

function openAdd(){editingId=null;openModal('➕ Thêm chương trình',formHtml(null),saveData);}
function openEdit(r){editingId=r.id;openModal('✏️ Cập nhật chương trình',formHtml(r),saveData);}

async function saveData(){
  const body={tenChuongTrinh:val('f-ten'),nganhId:+val('f-nganh'),heId:+val('f-he'),
              tongTinChi:+val('f-tc'),namPhatHanh:+val('f-nam'),moTa:val('f-mota'),trangThai:getToggle('f-tt')};
  if(!body.tenChuongTrinh){showToast('Vui lòng nhập tên chương trình','error');return;}
  try{
    editingId?await api.put(`/api/chuong-trinh/${editingId}`,body):await api.post('/api/chuong-trinh',body);
    showToast(editingId?'Cập nhật thành công':'Thêm thành công');
    closeModal();loadData();
  }catch(e){showToast(e.message,'error');}
}

async function onDelete(id){
  if(!confirm('Xác nhận xóa?'))return;
  try{await api.delete(`/api/chuong-trinh/${id}`);showToast('Xóa thành công');loadData();}
  catch(e){showToast(e.message,'error');}
}