async function confirmDelete(name = 'mục này') {
  const r = await Swal.fire({
    title: 'Xác nhận xóa',
    text: `Bạn có chắc muốn xóa ${name}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#dc2626',
    cancelButtonText: 'Hủy',
    confirmButtonText: 'Xóa',
  });
  return r.isConfirmed;
}