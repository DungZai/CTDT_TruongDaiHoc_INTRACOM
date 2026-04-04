let allData = [], editingId = null, currentPage = 1;
const PAGE_SIZE = 10;

document.addEventListener('DOMContentLoaded', () => {
  if (!requireAuth()) return;
  renderUserInfo(); loadSidebar(); loadData();
});

async function loadData() {
  try {
    allData = await api.get('/api/mon-hoc') || [];
    renderTable();
  } catch(e) { showToast(e.message,'error'); }
}

function renderTable() {
  const q = document.getElementById('search').value.toLowerCase();
  const filtered = q ? allData.filter(r=>(r.maMon+r.tenMon).toLowerCase().includes(q)) : allData;
  document.getElementById('tbl-count').textContent = `Tổng: ${filtered.length} môn học`;
  const total = Math.ceil(filtered.length/PAGE_SIZE)||1;
  if (currentPage>total) currentPage=1;
  const rows = filtered.slice((currentPage-1)*PAGE_SIZE, currentPage*PAGE_SIZE);
  document.getElementById('tbl-body').innerHTML = rows.length
    ? rows.map(r=>`<tr>
        <td>${r.id}</td>
        <td><strong>${r.maMon}</strong></td>
        <td>${r.tenMon}</td>
        <td style="text-align:center">${r.tinChi}</td>
        <td style="text-align:center">${r.soTietLt??'--'}</td>
        <td style="text-align:center">${r.soTietTh??'--'}</td>
        <td><span class="badge ${r.trangThai?'badge-green':'badge-red'}">${r.trangThai?'Hoạt động':'Ngừng'}</span></td>
        <td><div class="action-btns">
          <button class="btn btn-primary btn-sm" onclick='openEdit(${JSON.stringify(r)})'>✏️ Sửa</button>
          <button class="btn btn-danger btn-sm" onclick="onDelete(${r.id})">🗑️ Xóa</button>
        </div></td>
      </tr>`).join('')
    : '<tr><td colspan="8" class="table-empty">Không có dữ liệu</td></tr>';
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

function formHtml(r) {
  return `
    <div class="form-row">
      <div class="form-group"><label>Mã môn *</label>
        <input id="f-ma" value="${r?.maMon||''}" placeholder="VD: IT001"/></div>
      <div class="form-group"><label>Số tín chỉ *</label>
        <input id="f-tc" type="number" value="${r?.tinChi||3}" min="1"/></div>
    </div>
    <div class="form-group"><label>Tên môn *</label>
      <input id="f-ten" value="${r?.tenMon||''}" placeholder="VD: Lập trình Java"/></div>
    <div class="form-row">
      <div class="form-group"><label>Số tiết lý thuyết</label>
        <input id="f-lt" type="number" value="${r?.soTietLt||30}" min="0"/></div>
      <div class="form-group"><label>Số tiết thực hành</label>
        <input id="f-th" type="number" value="${r?.soTietTh||15}" min="0"/></div>
    </div>
    <div class="form-group"><label>Mô tả</label>
      <textarea id="f-mota">${r?.moTa||''}</textarea></div>
    <div class="form-group"><label>Trạng thái</label>
      ${makeToggle('f-tt', r?.trangThai!==false)}</div>`;
}

function openAdd(){editingId=null;openModal('➕ Thêm môn học',formHtml(null),saveData);}
function openEdit(r){editingId=r.id;openModal('✏️ Cập nhật môn học',formHtml(r),saveData);}

async function saveData(){
  const body={maMon:val('f-ma'),tenMon:val('f-ten'),tinChi:+val('f-tc'),soTietLt:+val('f-lt'),soTietTh:+val('f-th'),moTa:val('f-mota'),trangThai:getToggle('f-tt')};
  if(!body.maMon||!body.tenMon){showToast('Vui lòng nhập đủ thông tin','error');return;}
  try{
    editingId?await api.put(`/api/mon-hoc/${editingId}`,body):await api.post('/api/mon-hoc',body);
    showToast(editingId?'Cập nhật thành công':'Thêm thành công');
    closeModal();loadData();
  }catch(e){showToast(e.message,'error');}
}

async function onDelete(id){
  if(!confirm('Xác nhận xóa môn học này?'))return;
  try{await api.delete(`/api/mon-hoc/${id}`);showToast('Xóa thành công');loadData();}
  catch(e){showToast(e.message,'error');}
}