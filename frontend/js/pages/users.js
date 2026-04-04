let allData=[], editingId=null, currentPage=1, roles=[];
const PAGE_SIZE=10;

document.addEventListener('DOMContentLoaded', async ()=>{
  if(!requireAuth())return;
  renderUserInfo(); loadSidebar();
  roles = await api.get('/api/roles').catch(()=>[]);
  loadData();
});

async function loadData(){
  try{ allData=await api.get('/api/users')||[]; renderTable(); }
  catch(e){ showToast(e.message,'error'); }
}

function renderTable(){
  const q=document.getElementById('search').value.toLowerCase();
  const filtered=q?allData.filter(r=>(r.username+r.email).toLowerCase().includes(q)):allData;
  document.getElementById('tbl-count').textContent=`Tổng: ${filtered.length} người dùng`;
  const total=Math.ceil(filtered.length/PAGE_SIZE)||1;
  if(currentPage>total)currentPage=1;
  const rows=filtered.slice((currentPage-1)*PAGE_SIZE,currentPage*PAGE_SIZE);
  document.getElementById('tbl-body').innerHTML=rows.length
    ?rows.map(r=>`<tr>
        <td>${r.id}</td>
        <td><strong>${r.username}</strong></td>
        <td>${r.email||'—'}</td>
        <td><span class="badge badge-blue">${r.roleName||'—'}</span></td>
        <td>${r.createdAt||'—'}</td>
        <td><div class="action-btns">
          <button class="btn btn-danger btn-sm" onclick="onDelete(${r.id})">🗑️ Xóa</button>
        </div></td>
      </tr>`).join('')
    :'<tr><td colspan="6" class="table-empty">Không có dữ liệu</td></tr>';
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

function formHtml(){
  const roleOpts=roles.map(r=>`<option value="${r.id}">${r.roleName}</option>`).join('');
  return `
    <div class="form-group"><label>Tên đăng nhập *</label>
      <input id="f-un" placeholder="Nhập username"/></div>
    <div class="form-group"><label>Mật khẩu *</label>
      <input id="f-pw" type="password" placeholder="Nhập mật khẩu"/></div>
    <div class="form-group"><label>Email *</label>
      <input id="f-em" type="email" placeholder="example@intracom.edu.vn"/></div>
    <div class="form-group"><label>Vai trò</label>
      <select id="f-role">${roleOpts}</select></div>`;
}

function openAdd(){editingId=null;openModal('➕ Thêm người dùng',formHtml(),saveData);}

async function saveData(){
  const body={username:val('f-un'),password:val('f-pw'),email:val('f-em'),roleId:+val('f-role')};
  if(!body.username||!body.password||!body.email){showToast('Vui lòng nhập đủ thông tin','error');return;}
  try{
    await api.postPublic('/api/auth/register',body);
    showToast('Thêm người dùng thành công');
    closeModal();loadData();
  }catch(e){showToast(e.message,'error');}
}

async function onDelete(id){
  if(!confirm('Xác nhận xóa người dùng này?'))return;
  try{await api.delete(`/api/users/${id}`);showToast('Xóa thành công');loadData();}
  catch(e){showToast(e.message,'error');}
}